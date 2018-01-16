package com.bitnei.tools.validator;


import com.bitnei.tools.annotation.DateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日期格式验证
 *
 * @author zhaogd
 * Date: 2017/9/21
 */
public class DateFormatValidator implements ConstraintValidator<DateFormat, CharSequence> {

    private String[] formats;

    @Override
    public void initialize(DateFormat constraintAnnotation) {
        formats = constraintAnnotation.formats();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        // null values are valid
        if (value == null) {
            return true;
        }

        // 验证格式
        boolean res = true;
        for (String format : formats) {
            try {
                new SimpleDateFormat(format).parse(value.toString());
            } catch (ParseException e) {
                res = false;
                break;
            }
        }
        return res;
    }
}
