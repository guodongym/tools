package com.bitnei.tools.protocol.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenpeng
 * @date 2020-01-06 13:29
 */
public class DataConst {

    /**
     *  bit长度
     */
    public static final int BIT_LEN = 1;

    /**
     *  byte长度
     */
    public static final int SIZE_OF_BYTE = 1;

    /**
     *  word长度
     */
    public static final int SIZE_OF_WORD = SIZE_OF_BYTE * 2;

    /**
     *  dword长度
     */
    public static final int SIZE_OF_DWORD = SIZE_OF_BYTE * 4;
    /**
     * 时间长度
     */
    public static final int SIZE_OF_TIME = SIZE_OF_BYTE * 6;


    /**
     * BYTE
     */
    public static final String BYTE = "byte";

    /**
     * word
     */
    public static final String WORD = "word";

    /**
     * dword
     */
    public static final String DWORD = "dword";

    /**
     * 时间
     */
    public static final String TIME = "time";
    /**
     * 字符串
     */
    public static final String STRING = "string";
    /**
     * 数组
     */
    public static final String ARRAY = "array";
    /**
     * 二维数组
     */
    public static final String ARRAY2 = "array2";

    /**
     * 位数
     */
    public static final String BITS = "bits";

    /**
     * 源码
     */
    public static final String SOURCE = "source";

    /** sizeof定义 **/
    public static final Map<String, Integer> sizeOfMap = new HashMap<>(10);
    static {
        sizeOfMap.put(BYTE, SIZE_OF_BYTE);
        sizeOfMap.put(WORD, SIZE_OF_WORD);
        sizeOfMap.put(DWORD, SIZE_OF_DWORD);
        sizeOfMap.put(TIME, SIZE_OF_TIME);
    }



    public static final String START_MASK = "##";




}
