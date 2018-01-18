package com.bitnei.tools.validator;


import com.bitnei.tools.annotation.validator.DateEnd;
import com.bitnei.tools.annotation.validator.DateFormat;
import com.bitnei.tools.annotation.validator.DateRange;
import com.bitnei.tools.annotation.validator.DateStart;
import com.bitnei.tools.common.DateUtil;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * 关联验证，类属性的两个日期的时间间隔
 *
 * @author zhaogd
 * Date: 2017/9/21
 */
public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

    private long min;
    private long max;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // null values are valid
        if (value == null) {
            return true;
        }

        Map<String, String> dateStartMap = new HashMap<>(16);
        Map<String, String> dateEndMap = new HashMap<>(16);

        Field[] fields = value.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 根据配合注解找到日期字段
            DateStart start = field.getAnnotation(DateStart.class);
            DateFormat format = field.getAnnotation(DateFormat.class);
            // 组装所有开始日期和格式化要求
            if (start != null && format != null) {
                try {
                    String startVal = BeanUtils.getProperty(value, field.getName());
                    dateStartMap.put(start.group(), startVal + "_" + format.format());
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            // 组装所有结束日期和格式化要求
            DateEnd end = field.getAnnotation(DateEnd.class);
            if (end != null && format != null) {
                try {
                    String endVal = BeanUtils.getProperty(value, field.getName());
                    dateEndMap.put(end.group(), endVal + "_" + format.format());
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }

        // 遍历验证
        for (Map.Entry<String, String> entry : dateStartMap.entrySet()) {
            String dateStart = entry.getValue();
            String dateEnd = dateEndMap.get(entry.getKey());
            try {
                // 获取开始和结束日期的秒数
                String[] splitStart = dateStart.split("_");
                long start = DateUtil.convertString2Date(splitStart[0], splitStart[1]).getTime() / 1000;
                String[] splitEnd = dateEnd.split("_");
                long end = DateUtil.convertString2Date(splitEnd[0], splitEnd[1]).getTime() / 1000;

                // 判断日期间隔大于最小值，小于最大值
                if (end - start >= min && end - start <= max) {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
