package com.bitnei.tools.web.interceptor;

import com.bitnei.tools.web.annotation.ResponseResult;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 返回结果增强标识判断拦截器
 *
 * @author zhaogd
 * @date 2020/2/4
 */
public class ResponseResultInterceptor extends HandlerInterceptorAdapter {

    public static final String RESPONSE_RESULT = "RESPONSE_RESULT_MARK";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();

            if (clazz.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(RESPONSE_RESULT, clazz.getAnnotation(ResponseResult.class));
            }
            if (method.isAnnotationPresent(ResponseResult.class)){
                request.setAttribute(RESPONSE_RESULT, method.getAnnotation(ResponseResult.class));
            }
        }
        return true;
    }
}
