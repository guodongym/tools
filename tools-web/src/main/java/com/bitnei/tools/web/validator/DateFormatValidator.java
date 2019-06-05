package com.bitnei.tools.web.validator;


import com.bitnei.tools.web.validator.annotation.DateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式验证
 *
 * @author zhaogd
 * Date: 2017/9/21
 */
public class DateFormatValidator implements ConstraintValidator<DateFormat, CharSequence> {

    private static final Logger logger = LoggerFactory.getLogger(DateFormatValidator.class);

    private String format;

    @Override
    public void initialize(DateFormat constraintAnnotation) {
        format = constraintAnnotation.format();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        // null values are valid
        if (value == null) {
            return true;
        }

        // 验证格式
        try {
            validateDatePattern(value, format);
        } catch (ParseException e) {
            logger.warn("日期检验不通过, {} format: \"{}\"", e.getMessage(), format);
            return false;
        }
        return true;
    }

    static void validateDatePattern(CharSequence value, String format) throws ParseException {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setLenient(false);
        final String valueString = value.toString();
        final Date date = simpleDateFormat.parse(valueString);

        if (!simpleDateFormat.format(date).equals(valueString)) {
            throw new ParseException("Unparseable date: \"" + valueString + "\"", -1);
        }
    }
}
