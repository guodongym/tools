package com.etiansoft.tools.common;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

public class ExceptionUtil {

	public static String toString(Throwable throwable) {
		if (throwable == null) {
			return "";
		}
		CharArrayWriter caw = new CharArrayWriter(1024);
		PrintWriter writer = new PrintWriter(caw);
		throwable.printStackTrace(writer);
		return String.valueOf(caw.toCharArray());
	}
}
