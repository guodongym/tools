package com.bitnei.tools.annotation.validator;


import com.bitnei.tools.validator.ValueListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义验证注解，验证字段值要在注解要求范围内
 *
 * @author : zhaogd
 * Date: 2017/9/21
 */
@Documented
@Constraint(validatedBy = {ValueListValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface ValueList {

    Class<?>[] beanClasses() default {};

    String[] values() default {};

    String[] exclude() default {};

    String message() default "字段值不在支持范围之内,规则:[beanClasses:{beanClasses},values:{values},exclude:{exclude}]";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
