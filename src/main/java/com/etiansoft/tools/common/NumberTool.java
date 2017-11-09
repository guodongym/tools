package com.etiansoft.tools.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;

public class NumberTool {

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

	public static Integer parseInt(String string) {
		if (StringUtils.isEmpty(string)) {
			return null;
		}
		try {
			return Integer.valueOf(string);
		} catch (Exception e) {
			return null;
		}
	}

	public static String toString(Number number) {
		if (number == null) {
			return "";
		}
		return String.valueOf(number);
	}
}