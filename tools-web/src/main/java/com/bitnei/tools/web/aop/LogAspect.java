package com.bitnei.tools.web.aop;

import com.alibaba.fastjson.JSON;
import com.bitnei.tools.core.constant.EmptyObjectConstant;
import com.bitnei.tools.core.entity.DateFormatEnum;
import com.bitnei.tools.web.entity.GlobalResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
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
@Configuration
public class LogAspect {

    private static Logger logger = LoggerFactory.getLogger(LogAspect.class);

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
        // 记录方法开始执行的时间
        long startTimeMillis = System.currentTimeMillis();

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String url = request.getRequestURL().toString();
        String method = request.getMethod();

        String params = JSON.toJSONStringWithDateFormat(pjp.getArgs(), DateFormatEnum.DATE_TIME.getFormat());

        Object result;
        String resultMeta = EmptyObjectConstant.EMPTY_STRING;
        try {
            result = pjp.proceed();
            if (result instanceof GlobalResponse) {
                GlobalResponse response = (GlobalResponse) result;
                resultMeta = JSON.toJSONString(response.getMeta());
            }
        } catch (Throwable e) {
            resultMeta = e.toString();
            throw e;
        } finally {
            // 记录方法执行完成的时间
            long endTimeMillis = System.currentTimeMillis();

            logger.info("executionTime:{}MS, url:{}, method:{}, params:{}, resultMeta:{}",
                    endTimeMillis - startTimeMillis, url, method, params, resultMeta);
        }
        return result;
    }
}
