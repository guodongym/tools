package com.bitnei.tools.protocol.exception;

import io.netty.buffer.ByteBufUtil;

/**
 * @author chenpeng
 * @date 2020-02-21 18:16
 */
public class MessageException extends RuntimeException {

    private static final long serialVersionUID = 1013726929784162719L;

    public MessageException(String message, String protocolMessage) {
        super(message + " protocol: " + protocolMessage);
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, byte[] protocolMessage) {
        super(message + " protocol: " + ByteBufUtil.hexDump(protocolMessage));
    }
}
