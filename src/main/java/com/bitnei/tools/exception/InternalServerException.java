package com.bitnei.tools.exception;

/**
 * 内部处理出错异常
 *
 * @author zhaogd
 * Date: 2017/9/28
 */
public class InternalServerException extends Exception {

    public InternalServerException(String message) {
        super(message);
    }
}
