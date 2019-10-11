package com.bitnei.tools.core.util;

import com.google.common.base.CaseFormat;

/**
 * 字符串操作工具类
 *
 * @author zhaogd
 * @date 2019/7/8
 */
public class StringUtil {

    /**
     * 驼峰字段名转换为下划线字段名
     *
     * @param fieldNames 驼峰格式的字段列表
     * @return 转换为下划线格式的字段列表
     */
    public static String[] camelToUnderscore(String[] fieldNames) {
        if (fieldNames == null) {
            return null;
        }

        String[] sourceIncludes = new String[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            sourceIncludes[i] = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldNames[i]);
        }
        return sourceIncludes;
    }
}
