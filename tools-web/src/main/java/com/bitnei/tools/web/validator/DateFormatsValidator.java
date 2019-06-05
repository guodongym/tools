package com.bitnei.tools.web.validator;


import com.bitnei.tools.web.validator.annotation.DateFormats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;

/**
 * 日期格式验证
 *
 * @author zhaogd
 * Date: 2017/9/21
 */
public class DateFormatsValidator implements ConstraintValidator<DateFormats, CharSequence> {

    private static final Logger logger = LoggerFactory.getLogger(DateFormatsValidator.class);

    private String[] formats;

    @Override
    public void initialize(DateFormats constraintAnnotation) {
        formats = constraintAnnotation.formats();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        // null values are valid
        if (value == null) {
            return true;
        }

        // 验证格式
        int errCount = 0;
        for (String format : formats) {
            try {
                DateFormatValidator.validateDatePattern(value, format);
            } catch (ParseException e) {
                errCount++;
            }
        }
        if (errCount == formats.length) {
            logger.warn("日期检验不通过, date: \"{}\" formats: \"{}\"", value, formats);
        }
        return errCount < formats.length;
    }
}
