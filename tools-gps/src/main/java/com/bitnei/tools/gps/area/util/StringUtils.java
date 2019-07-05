package com.bitnei.tools.gps.area.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * 字符串操作工具
 *
 * @author zhaogd
 * @date 2018/11/29
 */
public class StringUtils {

    /**
     * 对集合内容按固定字符拼接
     *
     * @param collection 集合
     * @param delimiter  分隔符
     * @return 拼接后的字符串
     */
    public static String join(Collection collection, CharSequence delimiter) {
        StringBuilder sb = new StringBuilder();
        for (Iterator iterator = collection.iterator(); iterator.hasNext(); sb.append(iterator.next())) {
            if (sb.length() != 0) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }
}
