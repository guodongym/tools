package com.bitnei.tools.protocol.exception;

/**
 * @author chenpeng
 * @date 2020-02-21 18:16
 */
public class MessageException extends RuntimeException {

    /**
     * 原始报文
     */
    private String protocolMessage;


    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public MessageException(String message, String protocolMessage) {
        super(message);
        this.protocolMessage = protocolMessage;
    }
}
