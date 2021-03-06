package com.bitnei.tools.web.validator.annotation;


import com.bitnei.tools.web.validator.DateFormatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义验证注解，验证日期格式
 *
 * @author : zhaogd
 * Date: 2017/9/21
 */
@Documented
@Constraint(validatedBy = {DateFormatValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface DateFormat {

    String format() default "yyyy-MM-dd";

    String message() default "日期格式不正确,规则:[format:{format}]";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
