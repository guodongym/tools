package com.bitnei.tools.web.global;

import com.bitnei.tools.entity.ArcExceptionResponse;
import com.bitnei.tools.exception.InternalServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * GlobalExceptionHandler : 全局异常处理, 通用的异常在此进行处理, 例如: http method不匹配(405)
 * 所有异常均向上抛, 统一在controller层进行处理
 *
 * @author zhaogd
 * @since 2017-04-16 16:21
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有未识别的异常
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ArcExceptionResponse handleException(HttpServletRequest req, Throwable ex) {
        logger.error("系统内部错误==>" + getString(req), ex);
        return ArcExceptionResponse.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    /**
     * 处理404
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ArcExceptionResponse handleHttp404Exception(HttpServletRequest req, NoHandlerFoundException ex) {
        logger.warn("NoHandlerFound==>{}{}", getString(req), ex.getMessage());
        return ArcExceptionResponse.create(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    /**
     * 处理Http method使用不正确的错误, 例如: 新增应该使用POST, 但实际使用了GET
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ArcExceptionResponse handleHttp405Exception(HttpServletRequest req, HttpRequestMethodNotSupportedException ex) {
        logger.warn("不支持的Method==>{}{}", getString(req), ex.getMessage());
        return ArcExceptionResponse.create(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage());
    }

    /**
     * 处理validation框架参数校验异常(bean验证)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ArcExceptionResponse handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException ex) {
        logger.warn("参数校验异常==>{}{}", getString(req), ex.getMessage());
        return ArcExceptionResponse.create(HttpStatus.BAD_REQUEST.value(), ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 处理validation框架参数校验异常(属性认证)
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ArcExceptionResponse handleBindException(HttpServletRequest req, BindException ex) {
        logger.warn("参数校验异常==>{}{}", getString(req), ex.getMessage());
        return ArcExceptionResponse.create(HttpStatus.BAD_REQUEST.value(), ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 处理validation框架参数校验异常(方法验证)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ArcExceptionResponse handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException ex) {
        logger.warn("参数校验异常==>{}{}", getString(req), ex.getMessage());
        return ArcExceptionResponse.create(HttpStatus.BAD_REQUEST.value(), extractMessage(ex.getConstraintViolations()));
    }

    /**
     * 接口参数反序列化异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ArcExceptionResponse handleHttpMessageNotReadableException(HttpServletRequest req, HttpMessageNotReadableException ex) {
        logger.warn("接口参数反序列化异常==>{}{}", getString(req), ex.getMessage());
        return ArcExceptionResponse.create(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * 内部处理出错异常
     */
    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ArcExceptionResponse handleInternalServerException(HttpServletRequest req, InternalServerException ex) {
        logger.error("内部处理出错==>{}{}", getString(req), ex.getMessage());
        return ArcExceptionResponse.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }


    private String getString(HttpServletRequest req) {
        return " URI:" + req.getRequestURI() + " , Method:" + req.getMethod() + " , Message:";
    }

    /**
     * 提取ConstraintViolationException失败信息
     *
     * @param constraintViolations 信息列表
     * @return 格式化后的信息
     */
    private String extractMessage(Set<? extends ConstraintViolation> constraintViolations) {
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation violation : constraintViolations) {
            builder.append(violation.getMessage()).append(",").append(violation.getInvalidValue()).append(";");
        }
        return builder.toString();
    }
}
