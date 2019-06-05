package com.bitnei.tools.core.entity;

/**
 * Created by IntelliJ IDEA.
 *
 * @author zhaogd
 * Date: 2017/8/7
 */
public enum DateFormatEnum {

    /**
     * 4位数年格式,yyyy
     */
    YEAR("yyyy"),

    /**
     * 年月格式,yyyy-MM
     */
    MONTH("yyyy-MM"),

    /**
     * 年月格式不包含分隔符,yyyyMM
     */
    MONTH_NO_SEPARATOR("yyyyMM"),

    /**
     * 年月日格式,yyyy-MM-dd
     */
    DATE("yyyy-MM-dd"),

    /**
     * 年月日格式不包含分隔符,yyyyMMdd
     */
    DATE_NO_SEPARATOR("yyyyMMdd"),

    /**
     * 年月日时分秒格式,yyyy-MM-dd HH:mm:ss
     */
    DATE_TIME("yyyy-MM-dd HH:mm:ss"),

    /**
     * 年月日时分秒格式不包含分隔符,yyyyMMddHHmmss
     */
    DATE_TIME_NO_SEPARATOR("yyyyMMddHHmmss");

    private final String format;

    private DateFormatEnum(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
