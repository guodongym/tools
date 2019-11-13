package com.bitnei.tools.gb32960.convert;

import com.bitnei.tools.gb32960.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 明细数据格式校验、转换
 *
 * @author zhaogd
 * @date 2019/11/12
 */
@Slf4j
public class RealinfoValidateAndConvertUtil {
    private RealinfoValidateAndConvertUtil() {
    }

    public static Map<String, String> convertToMap(String origBody, List<String> allowMessageTypes, List<String> notAllowMessageTypes) {
        // 判断数据格式是否有误
        String[] fields = origBody.split(Constant.BLANK_SPACE);
        if (RealinfoValidator.isInvalid(fields, allowMessageTypes, notAllowMessageTypes)) {
            return null;
        }

        String content = fields[4];
        final Map<String, String> dataMap = processMessageToMap(content);
        dataMap.put(Constant.VIN, fields[2]);
        dataMap.put(Constant.MESSAGETYPE, fields[3]);

        // 判断是否包含VID
        if (RealinfoValidator.isInvalidVid(dataMap)) {
            log.warn("数据无效, VID为空 dataMap: {}", dataMap);
            return null;
        }
        return dataMap;
    }


    private static Map<String, String> processMessageToMap(String message) {
        //把{}去掉
        if (message.startsWith("{")) {
            message = message.substring(1);
        }
        if (message.endsWith("}")) {
            message = message.substring(0, message.length() - 1);
        }

        Map<String, String> map = new HashMap<>();
        String[] keyValues = message.split(",");
        for (String kv : keyValues) {
            String[] fields = kv.split(":");
            if (fields.length == 1) {
                map.put(StringUtils.trim(fields[0]).replaceAll("\"", ""), "");
            } else {
                map.put(StringUtils.trim(fields[0]).replaceAll("\"", ""), StringUtils.trim(fields[1]).replaceAll("\"", ""));
            }
        }
        return map;
    }

}
