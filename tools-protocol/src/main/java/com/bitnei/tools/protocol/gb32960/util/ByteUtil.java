package com.bitnei.tools.protocol.gb32960.util;

import io.netty.util.CharsetUtil;

import java.util.Arrays;

/**
 * 字节操作工具
 *
 * @author zhaogd
 * @date 2020/2/20
 */
public class ByteUtil {
    /**
     * 获取异或校验结果
     *
     * @param bytes 字节数组
     * @return 校验字节
     */
    public static byte getXor(byte[] bytes) {
        byte temp = bytes[0];
        for (int i = 1; i < bytes.length; i++) {
            temp ^= bytes[i];
        }
        return temp;
    }

    /**
     * 对字符串补位，不足的补0x00
     *
     * @param original 原始字符串
     * @param byteSize 字节数组的长度
     * @return 补位后的字节数组
     */
    public static byte[] fillZero(String original, int byteSize) {
        final byte[] originalBytes = original.getBytes(CharsetUtil.UTF_8);
        final byte[] bytes = Arrays.copyOf(originalBytes, byteSize);
        for (int i = originalBytes.length; i < bytes.length; i++) {
            bytes[i] = 0x00;
        }
        return bytes;
    }

    public static String stringFillZero(String original, int byteSize) {
        return new String(fillZero(original, byteSize));
    }


    /**
     * 字节转16进制字符串
     *
     * @param b 字节
     * @return 16进制字符串
     */
    public static String byte2HexString(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return "0x" + hex.toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(byte2HexString((byte) 0x05));

        System.out.println("\\u0000");
    }
}
