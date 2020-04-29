package com.bitnei.tools.protocol.gb32960.unpack;

import com.alibaba.fastjson.JSON;
import com.bitnei.tools.protocol.exception.MessageException;
import com.bitnei.tools.protocol.gb32960.unpack.bean.Message;
import com.bitnei.tools.protocol.gb32960.unpack.bean.MotorData;
import com.bitnei.tools.protocol.gb32960.unpack.bean.Realinfo;
import com.bitnei.tools.protocol.gb32960.util.ProtocolUtil;
import com.bitnei.tools.protocol.gb32960.util.Utility;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

import static com.bitnei.tools.protocol.gb32960.util.RealinfoType.formCode;

/**
 * 报文解析工具类
 *
 * @author zhaogd
 * @date 2020/4/29
 */
public class UnPack32960Util {

    public static Message unPack(String hexString) {
        return unPack(ByteBufUtil.decodeHexDump(hexString));
    }

    public static Message unPack(byte[] bytes) {
        final ByteBuf src = PooledByteBufAllocator.DEFAULT.buffer();
        try {
            src.writeBytes(bytes);
            int pos = 0;
            // 读取报文头
            final Message message = ProtocolUtil.readHeader(src);
            pos += 22;

            // 读取数据单元长度
            final short length = src.readShort();
            message.setDataLength(length);
            pos += 2;

            final Realinfo body = readBody(src, length);
            message.setBody(body);
            pos += length;

            //判断报文长度是否正确
            if (pos != (bytes.length - 1) || src.readableBytes() != 1) {
                throw new MessageException(String.format("报文长度不正确，接收到报文长度: %d, 解析出来的长度: %d", bytes.length, (pos + 1)), bytes);
            }

            // 检验码
            byte bcc = src.readByte();
            message.setBcc(bcc);

            // 检验是否成功
            byte messageBcc = ProtocolUtil.bcc(bytes, 0, bytes.length - 1);
            message.setValidateResult(bcc == messageBcc);

            return message;
        } finally {
            ReferenceCountUtil.release(src);
        }
    }

    private static Realinfo readBody(ByteBuf src, short length) {
        int pos = 0;
        final Realinfo realinfo = new Realinfo();
        realinfo.setV2000(ProtocolUtil.formatTimeTrim(ProtocolUtil.readTime(src)));
        pos += 6;

        while (pos < (length - 1)) {
            final byte type = src.readByte();
            pos += 1;
            switch (formCode(type)) {
                case CAR:
                    // 整车数据
                    pos += readCarData(src, realinfo);
                    break;
                case MOTOR:
                    // 驱动电机数据
                    pos += readMotorData(src, realinfo);
                    break;
                case BATTERY:
                    // 燃料电池数据
                    pos += readBatteryData(src, realinfo);
                    break;
                case ENGINE:
                    // 发动机数据
                    pos += readEngineData(src, realinfo);
                    break;
                case LOCATION:
                    // 车辆位置数据
                    pos += readLocationData(src, realinfo);
                    break;
                case EXTREMA:
                    // 极值数据
                    pos += readExtremaData(src, realinfo);
                    break;
                case ALARM:
                    // 报警数据
                    pos += readAlarmData(src, realinfo);
                    break;
                case VOLTAGE:
                    // 可充电储能装置电压数据
                    pos += readVoltageData(src, realinfo);
                    break;
                case TEMPERATURE:
                    // 可充电储能装置温度数据
                    pos += readTemperatureData(src, realinfo);
                    break;
                default:
                    pos += rebuildCustomData(src);
                    break;
            }
        }

        return realinfo;
    }


    private static int readTemperatureData(ByteBuf src, Realinfo body) {
        int length = 1;
        final byte count = src.readByte();
        body.setV4101(count);

        byte[] bytesRes = new byte[0];
        for (int i = 0; i < count; i++) {
            length += 1;
            bytesRes = ArrayUtils.add(bytesRes, src.readByte());

            length += 2;
            final short batteryCount = src.readShort();
            bytesRes = ArrayUtils.addAll(bytesRes, Utility.short2Byte(batteryCount));

            length += batteryCount;
            bytesRes = ArrayUtils.addAll(bytesRes, ProtocolUtil.readBytes(src, batteryCount));
        }
        body.setV4102(ByteBufUtil.hexDump(bytesRes));
        return length;
    }

    private static int readVoltageData(ByteBuf src, Realinfo body) {
        int length = 1;
        final byte count = src.readByte();
        body.setV4001(count);
        byte[] bytesRes = new byte[0];
        for (int i = 0; i < count; i++) {
            length += 9;
            bytesRes = ArrayUtils.addAll(bytesRes, ProtocolUtil.readBytes(src, 9));

            length += 1;
            final byte batteryCount = src.readByte();
            bytesRes = ArrayUtils.add(bytesRes, batteryCount);

            length += batteryCount * 2;
            bytesRes = ArrayUtils.addAll(bytesRes, ProtocolUtil.readBytes(src, batteryCount * 2));
        }
        body.setV4002(ByteBufUtil.hexDump(bytesRes));
        return length;
    }

    private static int readAlarmData(ByteBuf src, Realinfo body) {
        int length = 5;
        body.setV2920(src.readByte());
        body.setV3801(src.readInt());

        final byte count2921 = src.readByte();
        body.setV2921(count2921);
        length += 1;

        body.setV2922(ByteBufUtil.hexDump(ProtocolUtil.readBytes(src, count2921 * 4)));
        length += count2921 * 4;


        final byte count2804 = src.readByte();
        body.setV2804(count2804);
        length += 1;

        body.setV2805(ByteBufUtil.hexDump(ProtocolUtil.readBytes(src, count2804 * 4)));
        length += count2804 * 4;


        final byte count2923 = src.readByte();
        body.setV2923(count2923);
        length += 1;

        body.setV2924(ByteBufUtil.hexDump(ProtocolUtil.readBytes(src, count2923 * 4)));
        length += count2923 * 4;


        final byte count2808 = src.readByte();
        body.setV2808(count2808);
        length += 1;

        body.setV2809(ByteBufUtil.hexDump(ProtocolUtil.readBytes(src, count2808 * 4)));
        length += count2808 * 4;

        return length;
    }

    private static int readExtremaData(ByteBuf src, Realinfo body) {
        final int length = 14;
        body.setV2601(src.readByte());
        body.setV2602(src.readByte());
        body.setV2603(src.readShort());
        body.setV2604(src.readByte());
        body.setV2605(src.readByte());
        body.setV2606(src.readShort());
        body.setV2607(src.readByte());
        body.setV2608(src.readByte());
        body.setV2609(src.readByte());
        body.setV2610(src.readByte());
        body.setV2611(src.readByte());
        body.setV2612(src.readByte());
        return length;
    }

    private static int readLocationData(ByteBuf src, Realinfo body) {
        final int length = 9;
        body.setV2501(src.readByte());
        body.setV2502(src.readInt());
        body.setV2503(src.readInt());
        return length;
    }

    private static int readEngineData(ByteBuf src, Realinfo body) {
        final int length = 5;
        body.setV2401(src.readByte());
        body.setV2411(src.readShort());
        body.setV2413(src.readShort());
        return length;
    }

    private static int readBatteryData(ByteBuf src, Realinfo body) {
        int length = 6;
        body.setV2110(src.readShort());
        body.setV2111(src.readShort());
        body.setV2112(src.readShort());

        final short count = src.readShort();
        List<Byte> list = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            list.add(src.readByte());
        }
        body.setV2113(count);
        body.setV2114(JSON.toJSONString(list));
        length += count + 2;

        body.setV2115(src.readShort());
        body.setV2116(src.readByte());
        body.setV2117(src.readShort());
        body.setV2118(src.readByte());
        body.setV2119(src.readShort());
        body.setV2120(src.readByte());
        body.setV2121(src.readByte());
        length += 10;

        return length;
    }

    private static int readMotorData(ByteBuf src, Realinfo body) {
        final byte count = src.readByte();
        final int length = count * 12;
        List<MotorData> motorList = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            final MotorData motorData = new MotorData();
            motorData.setV2309(src.readByte());
            motorData.setV2310(src.readByte());
            motorData.setV2302(src.readByte());
            motorData.setV2303(src.readShort());
            motorData.setV2311(src.readShort());
            motorData.setV2304(src.readByte());
            motorData.setV2305(src.readShort());
            motorData.setV2306(src.readShort());
            motorList.add(motorData);
        }
        body.setV2307(count);
        body.setV2308(JSON.toJSONString(motorList));
        return length + 1;
    }

    private static int readCarData(ByteBuf src, Realinfo body) {
        final int length = 20;
        body.setV3201(src.readByte());
        body.setV2301(src.readByte());
        body.setV2213(src.readByte());
        body.setV2201(src.readShort());
        body.setV2202(src.readInt());
        body.setV2613(src.readShort());
        body.setV2614(src.readShort());
        body.setV7615(src.readByte());
        body.setV2214(src.readByte());
        body.setV2203(src.readByte());
        body.setV2617(src.readShort());
        body.setV2208(src.readByte());
        body.setV2209(src.readByte());
        return length;
    }

    private static int rebuildCustomData(ByteBuf src) {
        final short length = src.readShort();
        final byte[] data = new byte[length];
        src.readBytes(data);
        return length + 2;
    }
}
