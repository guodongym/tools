package com.bitnei.tools.aop;

import com.bitnei.tools.annotation.AjaxException;
import com.bitnei.tools.exception.JsonException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhaogd
 */
@Aspect
@Configuration
public class AjaxExceptionAspect {

    @Around("execution(* com.bitnei..*Controller.*(..))")
    public Object arount(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        Logger logger = LoggerFactory.getLogger(method.getDeclaringClass());

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return pjp.proceed();
        }

        ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
        if (responseBody == null) {
            return pjp.proceed();
        }

        AjaxException ajaxException = method.getAnnotation(AjaxException.class);
        if (ajaxException == null) {
            try {
                return pjp.proceed();
            } catch (InvocationTargetException e) {
                logger.error(e.getMessage(), e.getCause());
                throw new JsonException(e.getMessage(), e.getCause());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new JsonException(e.getMessage(), e);
            }
        }

        try {
            return pjp.proceed();
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage(), e.getCause());
            throw new JsonException(ajaxException.value(), e.getCause());
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new JsonException(ajaxException.value(), e);
        }
    }
}
