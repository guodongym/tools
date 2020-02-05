package com.bitnei.tools.web.annotation;

import java.lang.annotation.*;

/**
 * controller增强注解，增加此注解的类和方法会通过AOP对返回结果进行增强，统一返回数据格式
 *
 * Created on 2020/2/4.
 *
 * @author zhaogd
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ResponseResult {

}
