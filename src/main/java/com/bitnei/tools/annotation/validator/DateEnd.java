package com.bitnei.tools.annotation.validator;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义验证注解，关联验证，类属性的两个日期的时间间隔,与@{@link DateRange}配合使用
 *
 * @author : zhaogd
 * Date: 2017/9/21
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface DateEnd {

    /**
     * 日期组，每组包含开始和结束两个日期
     */
    String group() default "1";
}
