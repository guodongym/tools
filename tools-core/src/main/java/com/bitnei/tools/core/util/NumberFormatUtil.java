package com.bitnei.tools.core.util;

import java.text.DecimalFormat;

/**
 * 数值格式化
 *
 * @author zhaogd
 * @date 2019/11/2
 */
public class NumberFormatUtil {

    public static String formatMonthOrDay(int monthOrDay){
        return new DecimalFormat("00").format(monthOrDay);
    }
}
