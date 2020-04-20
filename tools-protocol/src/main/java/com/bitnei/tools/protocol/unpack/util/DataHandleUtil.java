package com.bitnei.tools.protocol.unpack.util;

import cn.hutool.core.codec.Base64;
import com.bitnei.tools.protocol.constant.DataConst;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * @author chenpeng
 * @date 2019-12-26 16:18
 */
public class DataHandleUtil {



    /**
     * 将byte数组转为字符串
     * @param data      源
     * @param size      长度
     * @return
     */
    public static String byte2String(final byte[] data, final int pos, final int size){

        return byte2String(data, pos, size, "gbk", "");

    }

    /**
     * 将byte数组转为字符串
     * @param data      源
     * @param size      长度
     * @return
     */
    public static String byte2hex(final byte[] data, final int pos, final int size){
        StringBuffer hexBuffer =  new StringBuffer();
        for (int i=0; i<size; i++) {
            hexBuffer.append(String.format("%02X", data[pos+i]));
            if (i < (size-1)) {
                hexBuffer.append(" ");
            }
        }
        return hexBuffer.toString();
    }

    /**
     * 将byte数组转为字符串
     * @param data      源
     * @param size      长度
     * @return
     */
    public static String byte2String(final byte[] data, final int pos, final int size, String encoder){

        return byte2String(data, pos, size, "gbk", encoder);

    }

    /**
     * 将源码转为字符串
     * @param data
     * @param size
     * @param charsetName
     * @return
     */
    public static String byte2String(final byte[] data, final int pos,final int size, final String charsetName, String encoder){

        byte[] desc = new byte[size];
        System.arraycopy(data, pos, desc, 0, size);
        String val = null;
        try {
            val = new String(desc, charsetName);
        }
        catch (UnsupportedEncodingException e) {
        }
        if (StringUtils.isEmpty(val)){
            return "";
        }
        if ("base64".equalsIgnoreCase(encoder)){
            return Base64.encode(val);
        }
        else {
            return val;
        }
    }


    /**
     * 将 byte转为 int
     * @param data
     * @return
     */
    public static int byte2Int(final byte data) {
        return data & 0xFF;
    }


    /**
     *  将bytes数组转int
     * @param data
     * @param pos
     * @param len
     * @return
     */
    public static int byte2Int(final byte[] data, final int pos, final int len){
        int res = 0;
        for (int i = 0; i < len; i++) {
            res += byte2Int(data[pos + i]) << ((len - i - 1) * 8);
        }
        return res;
    }


    /**
     * 将6个byte转为时间
     * @param data
     * @param pos
     * @return
     */
    public static String byte2Time(final byte[] data, final int pos){
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("20%02d-%02d-%02d %02d:%02d:%02d", data[pos+0], data[pos+1], data[pos+2], data[pos+3], data[pos+4], data[pos+5]));
        return sb.toString();
    }

    /**
     * 将字符串转为byte数组
     * @param message
     * @return
     */
    public static byte[] str2Bytes(final String message){

        byte buf[] = new byte[4096];
        int len = 0;
        int currentIndex = 0;
        while (currentIndex < message.length()) {
            String src = message.substring(currentIndex, currentIndex+2);
            buf[len++] = (byte) Integer.parseInt(src.trim(), 16);
            currentIndex +=2 ;
        }
        return buf;

    }


    /**
     * 计算BCC检验码
     * @param buf
     * @param start
     * @param len
     * @return
     */
    public static byte getBCC(byte buf[], int start, int len) {
        byte bccData = 0;
        for (int i = 0; i < len; i++) {
            bccData = (byte) (bccData ^ buf[i + start]);
        }
        return bccData;
    }


    /**
     * sizeOf
     * @param type
     * @return
     */
    public static int sizeOf(final String type){
        return DataConst.sizeOfMap.get(type);
    }



    /**
     * @param intVal
     * @param offset
     * @param factor
     * @return
     */
    public static String getDouble(int intVal, int offset, double factor) {


        double result = (intVal - offset) * factor;
        BigDecimal bd = new BigDecimal(result);

        String temp = String.valueOf(factor);
        int position = 0;
        if (temp.contains(".")) {
            int index = temp.indexOf(".");
            position = temp.length() - 1 - index;
        }
        bd.setScale(position, BigDecimal.ROUND_HALF_UP);
        return String.format("%." + position + "f", bd.doubleValue());
    }

}
