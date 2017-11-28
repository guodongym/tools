/**
 * @项目名称: estate-web
 * @文件名称: DateTool.java
 * @author tianlihu
 * @Date: 2015-5-12
 * @Copyright: 2015 www.etiansoft.com Inc. All rights reserved.
 * 注意：本内容仅限于北京逸天科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.bitnei.tools.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTool {

	public final static String YYYYMMDD = "yyyy-MM-dd";
	public final static String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
	public final static String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

	/** 获取当前时间 **/
	public static Date currentTime() {
		return new Date();
	}

	/** 获取当前时间字符串 **/
	public static String currentTimeDisplay() {
		return formatDateTime(currentTime());
	}

	public static Date currentDate() {
		Date currentDate = currentTime();
		currentDate = parseDate(formatDate(currentDate));
		return currentDate;
	}

	/** 格式日期 **/
	public static String formatDate(Date date) {
		return format(date, YYYYMMDD);
	}

	/** 格式日期分钟 **/
	public static String formatDateMinute(Date date) {
		return format(date, YYYYMMDDHHMM);
	}

	/** 格式时间 **/
	public static String formatDateTime(Date time) {
		return format(time, YYYYMMDDHHMMSS);
	}

	/** 格式 **/
	public static String format(Date time, String format) {
		if (time == null) {
			return null;
		}
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(time);
	}

	/** 解析日期 **/
	public static Date parseDate(String date) {
		return parse(date, YYYYMMDD);
	}

	/** 解析日期分钟 **/
	public static Date parseDateMinute(String date) {
		return parse(date, YYYYMMDDHHMM);
	}

	/** 解析时间 **/
	public static Date parseDateTime(String time) {
		return parse(time, YYYYMMDDHHMMSS);
	}

	/** 解析 **/
	public static Date parse(String time, String format) {
		if (time == null) {
			return null;
		}
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.parse(time);
		} catch (ParseException e) {
			return null;
		}
	}
}
