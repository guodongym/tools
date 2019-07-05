package com.bitnei.tools.gps.area.util;

public class NumberUtils {
    public static String stringNumber(String str) {
        if (!ObjectUtils.isNullOrEmpty(str) && str.trim().matches("[0-9]*.[0-9]*")) {
            return str.trim();
        }
        return "0";
    }

    public static boolean stringIsNumber(String str) {
        if (!ObjectUtils.isNullOrEmpty(str) && str.trim().matches("[0-9]*.[0-9]*")) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串isNullOrEmpty并且为非零开头的数字
     *
     * @param str 字符串
     * @return 判断结果
     */
    public static boolean stringIsNumberNotZero(String str) {
        final String regex = "^[1-9][0-9]*\\.?[0-9]*$";
        return !ObjectUtils.isNullOrEmpty(str) && str.trim().matches(regex);
    }

}
