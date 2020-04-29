package com.bitnei.tools.protocol.gb32960.util;

import com.bitnei.tools.core.constant.SymbolConstant;
import com.bitnei.tools.core.entity.DateFormatEnum;
import com.bitnei.tools.protocol.constant.DataConst;
import com.bitnei.tools.protocol.exception.MessageException;
import com.bitnei.tools.protocol.gb32960.unpack.bean.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.bitnei.tools.protocol.gb32960.util.RealinfoType.formCode;

/**
 * 报文编解码工具
 *
 * @author zhaogd
 * @date 2020/2/27
 */
public class ProtocolUtil {

    public static byte getCmd(byte[] bytes) {
        return bytes[2];
    }

    public static byte getCmd(ByteBuf byteBuf) {
        return byteBuf.getByte(2);
    }

    public static byte bcc(byte[] buf, int start, int len) {
        byte bccData = 0;
        for (int i = 0; i < len; i++) {
            bccData ^= buf[i + start];
        }
        return bccData;
    }

    public static byte[] readBytes(ByteBuf src, int length) {
        final byte[] bytes = new byte[length];
        src.readBytes(bytes);
        return bytes;
    }

    public static Message readHeader(ByteBuf msg) {
        String startMask = msg.readCharSequence(2, CharsetUtil.UTF_8).toString();
        if (!DataConst.START_MASK.equals(startMask)) {
            throw new MessageException("报文不合法 startMask 期望: 2323 实际: " + startMask);
        }
        byte cmd = msg.readByte();
        byte reply = msg.readByte();
        String vin = msg.readCharSequence(17, CharsetUtil.UTF_8).toString().replaceAll("\\u0000", "");
        byte encryptMode = msg.readByte();

        Message message = new Message();
        message.setStartMask(startMask);
        message.setCmd(cmd);
        message.setReply(reply);
        message.setVin(vin);
        message.setEncryptMode(encryptMode);
        return message;
    }

    public static boolean xorIsCheck(byte[] bytes) {
        final byte xor = ByteUtil.getXor(Arrays.copyOf(bytes, bytes.length - 1));
        return xor == bytes[bytes.length - 1];
    }

    public static byte[] protocolCut(byte[] dataBytes) {
        final ByteBuf src = PooledByteBufAllocator.DEFAULT.buffer();
        final ByteBuf dest = PooledByteBufAllocator.DEFAULT.buffer();
        final ByteBuf body = PooledByteBufAllocator.DEFAULT.buffer();
        try {
            src.writeBytes(dataBytes);
            final Message header = readHeader(src);
            // 如果不是实时数据且不是补发数据原样返回不裁剪
            if (TermOperationType.REALINFO.getOpCode() != header.getCmd() && TermOperationType.HISTORY.getOpCode() != header.getCmd()) {
                return dataBytes;
            }
            int pos = 0;
            final short length = src.readShort();

            final int timeLength = 6;
            final byte[] time = new byte[timeLength];
            src.readBytes(time);
            pos += timeLength;

            dest.writeCharSequence(header.getStartMask(), CharsetUtil.UTF_8);
            dest.writeByte(header.getCmd());
            dest.writeByte(header.getReply());
            dest.writeBytes(ByteUtil.fillZero(header.getVin(), 17));
            dest.writeByte(header.getEncryptMode());

            body.writeBytes(time);

            while (pos < (length - 1)) {
                final byte type = src.readByte();
                pos += 1;
                body.writeByte(type);
                switch (formCode(type)) {
                    case CAR:
                        // 整车数据
                        pos += rebuildCarData(src, body);
                        break;
                    case MOTOR:
                        // 驱动电机数据
                        pos += rebuildMotorData(src, body);
                        break;
                    case BATTERY:
                        // 燃料电池数据
                        pos += rebuildBatteryData(src, body);
                        break;
                    case ENGINE:
                        // 发动机数据
                        pos += rebuildEngineData(src, body);
                        break;
                    case LOCATION:
                        // 车辆位置数据
                        pos += rebuildLocationData(src, body);
                        break;
                    case EXTREMA:
                        // 极值数据
                        pos += rebuildExtremaData(src, body);
                        break;
                    case ALARM:
                        // 报警数据
                        pos += rebuildAlarmData(src, body);
                        break;
                    case VOLTAGE:
                        // 可充电储能装置电压数据
                        pos += rebuildVoltageData(src, body);
                        break;
                    case TEMPERATURE:
                        // 可充电储能装置温度数据
                        pos += rebuildTemperatureData(src, body);
                        break;
                    default:
                        pos += rebuildCustomData(src);
                        // 通过调整写索引移除掉自定义数据项的命令标识
                        body.setIndex(body.readerIndex(), body.writerIndex() - 1);
                        break;
                }
            }

            dest.writeShort(body.readableBytes());
            dest.writeBytes(body);

            final byte xor = ByteUtil.getXor(ByteBufUtil.getBytes(dest));
            dest.writeByte(xor);

            return ByteBufUtil.getBytes(dest);
        } finally {
            ReferenceCountUtil.release(src);
            ReferenceCountUtil.release(dest);
            ReferenceCountUtil.release(body);
        }
    }

    private static int rebuildTemperatureData(ByteBuf src, ByteBuf body) {
        int length = 1;
        final byte count = src.readByte();
        body.writeByte(count);

        for (int i = 0; i < count; i++) {
            length += 1;
            body.writeByte(src.readByte());

            length += 2;
            final short batteryCount = src.readShort();
            body.writeShort(batteryCount);

            length += batteryCount;
            body.writeBytes(src, batteryCount);
        }
        return length;
    }

    private static int rebuildVoltageData(ByteBuf src, ByteBuf body) {
        int length = 1;
        final byte count = src.readByte();
        body.writeByte(count);

        for (int i = 0; i < count; i++) {
            length += 9;
            body.writeBytes(src, 9);

            length += 1;
            final byte batteryCount = src.readByte();
            body.writeByte(batteryCount);

            length += batteryCount * 2;
            body.writeBytes(src, batteryCount * 2);
        }
        return length;
    }

    private static int rebuildAlarmData(ByteBuf src, ByteBuf body) {
        int length = 5;
        body.writeBytes(src, 5);

        for (int i = 0; i < 4; i++) {
            length += 1;
            final byte count = src.readByte();
            body.writeByte(count);

            length += count * 4;
            body.writeBytes(src, count * 4);
        }
        return length;
    }

    private static int rebuildExtremaData(ByteBuf src, ByteBuf body) {
        final int length = 14;
        body.writeBytes(src, length);
        return length;
    }

    private static int rebuildLocationData(ByteBuf src, ByteBuf body) {
        final int length = 9;
        body.writeBytes(src, length);
        return length;
    }

    private static int rebuildEngineData(ByteBuf src, ByteBuf body) {
        final int length = 5;
        body.writeBytes(src, length);
        return length;
    }

    private static int rebuildBatteryData(ByteBuf src, ByteBuf body) {
        int length = 6;
        body.writeBytes(src, 6);

        final short count = src.readShort();
        body.writeShort(count);

        length += count + 2;
        body.writeBytes(src, count);

        length += 10;
        body.writeBytes(src, 10);
        return length;
    }

    private static int rebuildMotorData(ByteBuf src, ByteBuf body) {
        final byte count = src.readByte();
        final int length = count * 12;
        body.writeByte(count);
        body.writeBytes(src, length);
        return length + 1;
    }

    private static int rebuildCarData(ByteBuf src, ByteBuf body) {
        final int length = 20;
        body.writeBytes(src, length);
        return length;
    }

    private static int rebuildCustomData(ByteBuf src) {
        final short length = src.readShort();
        final byte[] data = new byte[length];
        src.readBytes(data);
        return length + 2;
    }


    public static void encodeTime(ByteBuf msg, LocalDateTime time) {
        msg.writeByte(time.getYear() - 2000);
        msg.writeByte(time.getMonthValue());
        msg.writeByte(time.getDayOfMonth());
        msg.writeByte(time.getHour());
        msg.writeByte(time.getMinute());
        msg.writeByte(time.getSecond());
    }

    public static LocalDateTime readTime(ByteBuf msg) {
        final byte year = msg.readByte();
        final byte month = msg.readByte();
        final byte day = msg.readByte();
        final byte hour = msg.readByte();
        final byte minute = msg.readByte();
        final byte second = msg.readByte();
        return LocalDateTime.of(year + 2000, month, day, hour, minute, second);
    }

    public static String buildStreamId(String vin, LocalDateTime time, int opCode) {
        return vin + SymbolConstant.UNDER_LINE + formatTimeTrim(time) + SymbolConstant.UNDER_LINE + opCode;
    }

    public static String formatTimeTrim(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DateFormatEnum.DATE_TIME_NO_SEPARATOR.getFormat()));
    }
}
