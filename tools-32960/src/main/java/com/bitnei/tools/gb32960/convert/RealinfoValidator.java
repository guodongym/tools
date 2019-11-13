package com.bitnei.tools.gb32960.convert;

import com.bitnei.tools.gb32960.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 数据正确性验证工具类
 *
 * @author zhaogd
 * @date 2018/11/13
 */
public class RealinfoValidator {

    private static Logger logger = LoggerFactory.getLogger(RealinfoValidator.class);

    private static final int DATA_ARRAY_LENGTH = 5;

    /**
     * 判断是否是无效数据
     *
     * @param fields               切分后的数组
     * @param allowMessageTypes    允许的消息类型
     * @param notAllowMessageTypes 不允许的消息类型
     * @return 判断结果布尔值，true表示无效
     */
    public static boolean isInvalid(String[] fields, List<String> allowMessageTypes, List<String> notAllowMessageTypes) {
        if (isInvalid(fields)) {
            return true;
        }

        // 规则没有设置直接放行
        if (allowMessageTypes.isEmpty() && notAllowMessageTypes.isEmpty()) {
            return false;
        }

        // 判断消息类型
        final String messageType = fields[3];
        final boolean isAllow;
        if (allowMessageTypes.isEmpty()) {
            isAllow = !notAllowMessageTypes.contains(messageType);
        } else {
            isAllow = allowMessageTypes.contains(messageType);
        }

        if (!isAllow) {
            logger.warn("数据无效, 消息类型不正确, 允许: {}, 不允许: {}, 实际: {}, fields: {}", allowMessageTypes, notAllowMessageTypes, messageType, fields);
            return true;
        }

        return false;
    }

    private static boolean isInvalid(String[] fields) {
        // 判断数据格式
        if (fields.length != DATA_ARRAY_LENGTH) {
            logger.warn("数据无效, 切分后长度不正确, 期望: {}, 实际: {}, fields: {}", DATA_ARRAY_LENGTH, fields.length, fields);
            return true;
        }

        // 判断指令类型，非主动发送过滤
        final String directiveType = fields[0];
        if (!Constant.SUBMIT.equalsIgnoreCase(directiveType)) {
            logger.warn("数据无效, 指令类型不正确, 期望: {}, 实际: {}, fields: {}", Constant.SUBMIT, directiveType, fields);
            return true;
        }
        return false;
    }

    /**
     * 判断是否无效的vid
     *
     * @param dataMap 解析后key,value消息
     * @return 判断结果，true表示无效
     */
    public static boolean isInvalidVid(Map<String, String> dataMap) {
        return StringUtils.isBlank(dataMap.get(Constant.VID));
    }
}
