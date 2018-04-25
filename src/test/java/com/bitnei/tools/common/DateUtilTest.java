package com.bitnei.tools.common;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author zhaogd
 * @Date 2018/1/16
 */
public class DateUtilTest {

    @Test
    public void getMonthBetween() throws ParseException {
        List<String> monthBetween = DateUtil.getMonthBetween("201710", "201802");
        String[] month = {"201710", "201711", "201712", "201801", "201802"};
        Assert.assertArrayEquals(monthBetween.toArray(), month);

        System.out.println(getYearStartByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }


    /**
     * 获取指定日期的起始年月日, e.g: 2014-11-11 10:11:22 -> 2014-01-01 00:00:00
     *
     * @param date   日期
     * @param format 格式化
     * @return 格式化之后字符串
     */
    public static String getYearStartByDate(Date date, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(calendar.getTime());
    }
}