package com.bitnei.tools.annotation.validator;


import com.bitnei.tools.validator.DateRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义验证注解，关联验证，类属性的两个日期的时间间隔
 *
 * @author : zhaogd
 * Date: 2017/9/21
 */
@Documented
@Constraint(validatedBy = {DateRangeValidator.class})
@Target({TYPE})
@Retention(RUNTIME)
public @interface DateRange {

    /**
     * 秒数，默认0
     */
    long min() default 0;

    /**
     * 秒数，默认一天
     */
    long max() default 60 * 60 * 24;

    String message() default "日期间隔验证不通过,规则:[min:{min}s,max:{max}s]";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
