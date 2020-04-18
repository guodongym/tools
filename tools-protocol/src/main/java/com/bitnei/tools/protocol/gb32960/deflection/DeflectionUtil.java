package com.bitnei.tools.protocol.gb32960.deflection;

import com.bitnei.tools.core.constant.SymbolConstant;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GB_32960数据项偏移、系数处理
 *
 * @author zhaogd
 * @date 2019/10/11
 */
public class DeflectionUtil {
    private DeflectionUtil() {
    }

    /**
     * 对GB_32960数据项偏移、系数处理
     *
     * @param realinfoMap 数据, map中的value是object类型的string
     * @return 偏移后的结果
     */
    public static Map<String, String> doDeflectionWithObjectMap(Map<String, Object> realinfoMap) {
        Map<String, String> dataMap = Maps.newHashMap();
        realinfoMap.forEach((k, v) -> {
            dataMap.put(k, (String) v);
        });

        doDeflection(dataMap);
        return dataMap;
    }

    /**
     * 对GB_32960数据项偏移、系数处理
     *
     * @param dataMap 数据
     */
    public static void doDeflection(Map<String, String> dataMap) {
        // 对需要偏移的数据项做偏移处理
        for (OffsetEnum offsetEnum : OffsetEnum.getAll()) {
            final String value = dataMap.get(offsetEnum.getKey());
            final String newValue;
            if (offsetEnum.isList()) {
                newValue = arrayDeflect(value, offsetEnum.getOffset(), offsetEnum.getFactor());
            } else {
                newValue = singleDeflect(value, offsetEnum.getOffset(), offsetEnum.getFactor());
            }

            // 如果原始数据格式不正确则不处理
            if (newValue != null) {
                dataMap.put(offsetEnum.getKey(), newValue);
            }
        }

        // 单体电池电压值列表 2003 系数0.001，单位：V
        // 此数据项是base64编码的多个值的集合，所以单独逻辑处理
        final String singleVoltageList = singleListDeflect(dataMap.get("2003"), 0, 0.001D);
        if (singleVoltageList != null) {
            dataMap.put("2003", singleVoltageList);
        }

        // 单体电池温度值列表 2103 偏移量40℃，单位：1℃
        // 此数据项是base64编码的多个值的集合，所以单独逻辑处理
        final String newValue = singleListDeflect(dataMap.get("2103"), 40, Double.NaN);
        if (newValue != null) {
            dataMap.put("2103", newValue);
        }
    }

    /**
     * 对列表值做偏移、系数处理
     *
     * @param value  原始值
     * @param offset 偏移量
     * @param factor 系数
     * @return 处理后的值，如果原始值为空则返回null
     */
    private static String arrayDeflect(String value, int offset, double factor) {
        // 如果为空返回null
        if (StringUtils.isBlank(value)) {
            return null;
        }

        final String[] singleValues = value.split(SymbolConstant.VERTICAL_LINE_REG);
        final ArrayList<String> list = Lists.newArrayList();
        for (String s : singleValues) {
            final String newValue = singleDeflect(s, offset, factor);
            if (newValue == null) {
                continue;
            }
            list.add(newValue);
        }
        return StringUtils.join(list, SymbolConstant.VERTICAL_LINE);
    }

    /**
     * 对单个值做偏移、系数处理
     *
     * @param value  原始值
     * @param offset 偏移量
     * @param factor 系数
     * @return 处理后的值，如果原始值为空或者非数字则返回null
     */
    private static String singleDeflect(String value, int offset, double factor) {
        // 如果为空或者不是数字返回null
        if (!NumberUtils.isDigits(value)) {
            return null;
        }
        if (Double.isNaN(factor)) {
            return String.valueOf(NumberUtils.toInt(value) - offset);
        }
        final double newValue = (NumberUtils.toInt(value) - offset) * factor;
        return new DecimalFormat(getFormatPattern(factor)).format(newValue);
    }

    /**
     * 对单体电压、温度列表偏移、系数处理之后重新Base64编码
     *
     * @param itemValue 原始值
     * @return 处理后的值
     */
    private static String singleListDeflect(String itemValue, int offset, double factor) {
        if (StringUtils.isBlank(itemValue)) {
            return null;
        }

        List<BatteryPackage> resultList = new ArrayList<>();

        // 多个电池包会以竖线分割
        String[] batteryPackages = itemValue.split(SymbolConstant.VERTICAL_LINE_REG);
        for (String batteryPackage : batteryPackages) {
            if (StringUtils.isBlank(batteryPackage)) {
                continue;
            }

            // 每个电池包数据需要Base64解码
            final String battery = new String(Base64.decodeBase64(batteryPackage));
            final String[] indexAndValues = battery.split(SymbolConstant.COLON);
            if (indexAndValues.length != 2) {
                continue;
            }

            // 解码后格式：1:58_58_58_58_58_58_58_58_58_58_58_58_58_58_58
            // 冒号之前为电池包序号，冒号之后为具体单体电池温度
            final String index = indexAndValues[0];
            final String values = indexAndValues[1];
            if (StringUtils.isBlank(index) || StringUtils.isBlank(values)) {
                continue;
            }

            List<String> singleBatteryResults = new ArrayList<>();
            // 对每个单体温度进行偏移操作
            final String[] singleBatteryArray = values.split(SymbolConstant.UNDER_LINE);
            for (String single : singleBatteryArray) {
                if (StringUtils.isBlank(single)) {
                    singleBatteryResults.add("");
                } else {
                    singleBatteryResults.add(singleDeflect(single, offset, factor));
                }
            }

            // 每个电池包数据由BatteryPackage类封装，并重写toString方法
            resultList.add(new BatteryPackage(index, singleBatteryResults));
        }

        // 拼装多个电池包
        return StringUtils.join(resultList, SymbolConstant.VERTICAL_LINE);
    }


    private static String getFormatPattern(double factor) {
        if (Double.isNaN(factor)) {
            return "#";
        }
        final String s = new DecimalFormat("#.######").format(factor);
        return s.replaceAll("[0-9]", "#");
    }
}
