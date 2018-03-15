package com.bitnei.tools.aop;

import com.alibaba.fastjson.JSON;
import com.bitnei.tools.constant.EmptyObjectConstant;
import com.bitnei.tools.entity.ArcResponse;
import com.bitnei.tools.entity.DateFormatEnum;
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

    /**
     * 切点，controller包下所有带有RequestMapping注解的方法
     */
    @Pointcut("execution(* com.bitnei..*Controller.*(..))) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void controllerMethodPointcut() {
    }

    /**
     * 记录访问日志
     */
    @Around("controllerMethodPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 记录方法开始执行的时间
        long startTimeMillis = System.currentTimeMillis();

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String url = request.getRequestURL().toString();
        String method = request.getMethod();

        String params = EmptyObjectConstant.EMPTY_STRING;
        if (pjp.getArgs() != null && pjp.getArgs().length > 0) {
            // 只拦截第一个参数信息
            params = JSON.toJSONStringWithDateFormat(pjp.getArgs()[0], DateFormatEnum.DATE_TIME.getFormat());
        }

        Object result;
        String resultMeta = EmptyObjectConstant.EMPTY_STRING;
        try {
            result = pjp.proceed();
            ArcResponse response = (ArcResponse) result;
            resultMeta = JSON.toJSONString(response.getMeta());
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
