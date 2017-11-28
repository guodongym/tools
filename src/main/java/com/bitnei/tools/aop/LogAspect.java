package com.bitnei.tools.aop;

import com.alibaba.fastjson.JSON;
import com.bitnei.tools.entity.ArcResponse;
import com.bitnei.tools.entity.DateFormatEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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

    private long startTimeMillis;
    private long endTimeMillis;
    private String resultMeta;
    private String url;
    private String method;
    private String uri;
    private String queryString;

    /**
     * 切点，controller包下所有带有RequestMapping注解的方法
     */
    @Pointcut("execution(* com.bitnei..*Controller.*(..))) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void controllerMethodPointcut() {
    }


    @Before("controllerMethodPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 记录方法开始执行的时间
        startTimeMillis = System.currentTimeMillis();
    }

    @After("controllerMethodPointcut()")
    public void doAfter(JoinPoint joinPoint) {
        // 记录方法执行完成的时间
        endTimeMillis = System.currentTimeMillis();
        this.printLog();
    }

    /**
     * 记录访问日志
     */
    @Around("controllerMethodPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        url = request.getRequestURL().toString();
        method = request.getMethod();
        uri = request.getRequestURI();

        if (pjp.getArgs() != null && pjp.getArgs().length > 0) {
            // 只拦截第一个参数信息
            queryString = JSON.toJSONStringWithDateFormat(pjp.getArgs()[0], DateFormatEnum.DATE_TIME.getFormat());
        }

        Object result;
        try {
            result = pjp.proceed();
            ArcResponse response = (ArcResponse) result;
            resultMeta = JSON.toJSONString(response.getMeta());
        } catch (Throwable e) {
            resultMeta = e.toString();
            throw e;
        }

        return result;
    }

    private void printLog() {
        logger.info("executionTime:{}ms , url:{} , method:{} , uri:{} , queryString:{} , resultMeta:{}",
                endTimeMillis - startTimeMillis, url, method, uri, queryString, resultMeta);
    }
}
