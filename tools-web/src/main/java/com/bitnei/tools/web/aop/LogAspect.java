package com.bitnei.tools.web.aop;

import com.alibaba.fastjson.JSON;
import com.bitnei.tools.core.constant.EmptyObjectConstant;
import com.bitnei.tools.core.entity.DateFormatEnum;
import com.bitnei.tools.web.entity.GlobalResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author zhaogd
 * Date: 2017/9/11
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class LogAspect {

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void requestMappingMethodPointcut() {
    }

    @Pointcut(value = "@within(org.springframework.web.bind.annotation.RestController)")
    public void responseBodyMethodPointcut() {
    }

    @Pointcut(value = "execution(* com.bitnei..*Controller.*(..)))")
    public void controllerMethodPointcut() {
    }

    /**
     * 切点，controller包下所有带有RequestMapping注解的方法
     */
    @Pointcut(value = "controllerMethodPointcut() && responseBodyMethodPointcut() && requestMappingMethodPointcut()")
    public void restControllerMethodPointcut() {
    }

    /**
     * 记录访问日志
     */
    @Around("restControllerMethodPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        log.info("=====================================Method  start====================================");
        long startTimeMillis = System.currentTimeMillis();

        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        HttpServletRequest request = sra.getRequest();

        Object result;
        String resultMeta = EmptyObjectConstant.EMPTY_STRING;
        String resultData = EmptyObjectConstant.EMPTY_STRING;
        try {
            result = pjp.proceed();
            if (result instanceof GlobalResponse) {
                GlobalResponse<?> response = (GlobalResponse<?>) result;
                resultMeta = JSON.toJSONString(response.getMeta());
                resultData = JSON.toJSONStringWithDateFormat(response.getData(), DateFormatEnum.DATE_TIME.getFormat());
            } else {
                resultData = JSON.toJSONStringWithDateFormat(result, DateFormatEnum.DATE_TIME.getFormat());
            }
        } catch (Throwable e) {
            resultMeta = e.toString();
            throw e;
        } finally {
            long endTimeMillis = System.currentTimeMillis();
            String params = JSON.toJSONStringWithDateFormat(pjp.getArgs(), DateFormatEnum.DATE_TIME.getFormat());

            log.info("请求地址: {}", request.getRequestURI());
            log.info("HTTP method: {}", request.getMethod());
            log.info("请求IP: {}", request.getRemoteAddr());
            log.info("CLASS_METHOD: {}", pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
            log.info("参数: {}", params);
            log.info("返回值: {}", resultData);
            if (StringUtils.isNotBlank(resultMeta)) {
                log.info("异常信息: {}", resultMeta);
            }
            log.info("执行时间: {} ms", endTimeMillis - startTimeMillis);
        }
        log.info("=====================================Method  End====================================");
        return result;
    }
}
