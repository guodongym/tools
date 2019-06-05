package com.bitnei.tools.core.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtil {

    private static final BigDecimal NUM_100 = new BigDecimal("100");

    public static BigDecimal toBigDecimal(String number) {
        if (StringUtils.isEmpty(number)) {
            return null;
        }
        return new BigDecimal(number);
    }

    public static String toMoneyStr(BigDecimal number) {
        if (number == null) {
            return "0.00";
        }
        NumberFormat format = new DecimalFormat("0.00");
        return format.format(number);
    }

    public static String toPercent(BigDecimal number) {
        if (number == null) {
            return "0.00";
        }
        NumberFormat format = new DecimalFormat("0.#");
        return format.format(number.multiply(NUM_100)) + "%";
    }

    public static String toString(Number number) {
        if (number == null) {
            return "";
        }
        return String.valueOf(number);
    }

    public static String toPlainString(long number) {
        return new BigDecimal(number).toPlainString();
    }

    public static String toPlainString(double number) {
        if (Double.isInfinite(number) || Double.isNaN(number)) {
            return null;
        }
        return new BigDecimal(number).setScale(6, BigDecimal.ROUND_HALF_UP).toPlainString();
    }
}