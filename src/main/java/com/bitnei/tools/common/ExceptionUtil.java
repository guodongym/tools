package com.bitnei.tools.common;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: zhaogd
 * Date: 2017/9/28
 */
public class ExceptionUtil {

    /**
     * 异常堆栈转字符串
     *
     * @param t 异常
     * @return 堆栈字符串
     */
    public static String printStackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }
}
