package com.bitnei.tools.validator;


import com.bitnei.tools.annotation.validator.ValueList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 *
 * @author zhaogd
 * Date: 2017/9/21
 */
public class ValueListValidator implements ConstraintValidator<ValueList, CharSequence> {

    private Class<?>[] aClasses;
    private String[] values;
    private String[] exclude;

    @Override
    public void initialize(ValueList constraintAnnotation) {
        aClasses = constraintAnnotation.beanClasses();
        values = constraintAnnotation.values();
        exclude = constraintAnnotation.exclude();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        // null values are valid
        if (value == null) {
            return true;
        }

        // 排除的值，排除优先，如果在排除中直接返回验证失败
        for (String s : exclude) {
            if (s.equalsIgnoreCase(value.toString())) {
                return false;
            }
        }

        // 包含的值
        for (String s : values) {
            if (s.equalsIgnoreCase(value.toString())) {
                return true;
            }
        }

        // 判断值是否在beanClass的字段名中
        for (Class<?> aClass : aClasses) {
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.getName().equals(value.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
