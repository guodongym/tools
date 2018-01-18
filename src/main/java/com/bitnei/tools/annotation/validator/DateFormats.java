package com.bitnei.tools.annotation.validator;


import com.bitnei.tools.validator.DateFormatsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义验证注解，验证支持多日期格式
 *
 * @author : zhaogd
 * Date: 2017/9/21
 */
@Documented
@Constraint(validatedBy = {DateFormatsValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface DateFormats {

    String[] formats() default {"yyyy-MM-dd"};

    String message() default "日期格式不正确,规则:[formats:{formats}]";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
