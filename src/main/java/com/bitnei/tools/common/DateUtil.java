package com.bitnei.tools.common;

import com.bitnei.tools.constant.SymbolConstant;
import com.bitnei.tools.entity.DateFormatEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhaogd
 * Date: 2017/8/7
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 昨天
     *
     * @param format 格式化
     * @return 昨天日期字符串
     */
    public static String getYesterday(DateFormatEnum format) {
        Date time = getDefineTime(Calendar.DATE, -1);
        return convertDate2String(time, format);
    }

    /**
     * 获取当前日期上个月
     *
     * @param format 格式化
     * @return 上个月日期
     */
    public static String getLastMonth(DateFormatEnum format) {
        Date time = getDefineTime(Calendar.MONTH, -1);
        return convertDate2String(time, format);
    }

    /**
     * 获取指定日期上个月
     *
     * @param dateString 指定日期字符串
     * @param format     格式化
     * @return 指定日期的上个月
     */
    public static String getLastMonth(String dateString, DateFormatEnum format) throws ParseException {
        Date date = convertString2Date(dateString, format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return convertDate2String(cal.getTime(), format);
    }

    /**
     * 去年
     *
     * @param format 格式化
     * @return 去年日期
     */
    public static String getLastYear(DateFormatEnum format) {
        Date time = getDefineTime(Calendar.YEAR, -1);
        return convertDate2String(time, format);
    }


    /**
     * 字符串转日期类型
     *
     * @param dateStr
     * @param dateFormat
     * @return
     */
    public static Date convertString2Date(String dateStr, DateFormatEnum dateFormat) throws ParseException {
        if (null == dateStr || "".equals(dateStr.trim())) {
            return new Date();
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat.getFormat());
        Date result;
        try {
            result = sdf.parse(dateStr);
        } catch (ParseException e) {
            logger.error(SymbolConstant.POUND_TWENTY + "字符串日期不合法" + SymbolConstant.COLON
                    + dateStr
                    + SymbolConstant.POUND_TWENTY);
            throw new ParseException(e.getMessage(), e.getErrorOffset());
        }
        return result;
    }

    /**
     * 日期类型转字符串
     *
     * @param date       java.util.Date
     * @param dateFormat 格式化格式, 使用自定义的枚举类封装所有格式化类型
     * @return 格式化后日期字符串
     */
    public static String convertDate2String(Date date, DateFormatEnum dateFormat) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat.getFormat());
        String dateStr = sdf.format(date);
        return StringUtils.trimToEmpty(dateStr);
    }

    /**
     * 获取指定的date, 参看calendar api
     */
    public static Date getDefineTime(int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 获取指定月份的起始值, e.g: 2014-11-11 10:11:22 -> 2014-11-01 00:00:00
     *
     * @param dateString 字符串日期
     * @param format     格式化
     * @return 格式化之后字符串，格式化类型DateFormatEnum.DATE_TIME
     */
    public static String getMonthStartByDate(String dateString, DateFormatEnum format) throws ParseException {
        Date date = convertString2Date(dateString, format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return convertDate2String(calendar.getTime(), DateFormatEnum.DATE_TIME);
    }


    /**
     * 获取指定月份的结束值, e.g: 2014-11-11 10:11:22 -> 2014-11-30 23:59:59
     *
     * @param dateString 字符串日期
     * @param format     格式化
     * @return 格式化之后字符串，格式化类型DateFormatEnum.DATE_TIME
     */
    public static String getMonthEndByDate(String dateString, DateFormatEnum format) throws ParseException {
        Date date = convertString2Date(dateString, format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return convertDate2String(calendar.getTime(), DateFormatEnum.DATE_TIME);
    }


    /**
     * 获取指定日期的起始值, e.g: 2014-11-11 10:11:22 -> 2014-11-11 00:00:00
     *
     * @param date
     * @return
     */
    public static Date getDateStartByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的起始值, e.g: 2014-11-11 10:11:22 -> 2014-11-11 00:00:00
     *
     * @param dateString 字符串日期
     * @param srcFormat  源日期格式化
     * @param destFormat 目标日期格式化 DateFormatEnum.DATE_TIME
     *                   DateFormatEnum.DATE_TIME_NO_SEPARATOR
     * @return 格式化之后字符串
     */
    public static String getDateStartByDate(String dateString, DateFormatEnum srcFormat, DateFormatEnum destFormat) throws ParseException {
        Date date = convertString2Date(dateString, srcFormat);
        Date startDate = getDateStartByDate(date);
        return convertDate2String(startDate, destFormat);
    }

    /**
     * 获取指定日期的结束值, e.g: 2014-11-11 10:11:22 -> 2014-11-11 23:59:59
     *
     * @param date
     * @return
     */
    public static Date getDateEndByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的结束值, e.g: 2014-11-11 10:11:22 -> 2014-11-11 23:59:59
     *
     * @param dateString 字符串日期
     * @param srcFormat  源日期格式化
     * @param destFormat 目标日期格式化 DateFormatEnum.DATE_TIME
     *                   DateFormatEnum.DATE_TIME_NO_SEPARATOR
     * @return 格式化之后字符串
     */
    public static String getDateEndByDate(String dateString, DateFormatEnum srcFormat, DateFormatEnum destFormat) throws ParseException {
        Date date = convertString2Date(dateString, srcFormat);
        Date endDate = getDateEndByDate(date);
        return convertDate2String(endDate, destFormat);
    }

    /**
     * 获取30天之前的日期
     *
     * @return 格式化之后日期
     */
    public static String getLateMonthDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }
}
                                                  