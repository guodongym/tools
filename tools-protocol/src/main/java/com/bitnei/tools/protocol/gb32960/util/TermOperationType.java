package com.bitnei.tools.protocol.gb32960.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 终端命令类型
 *
 * @author zhaogd
 */
@Slf4j
public enum TermOperationType {

    /**
     * 车辆登入
     */
    LOGIN((byte) 0x01),
    /**
     * 实时数据
     */
    REALINFO((byte) 0x02),
    /**
     * 补发数据
     */
    HISTORY((byte) 0x03),
    /**
     * 车辆登出
     */
    LOGOUT((byte) 0x04),
    /**
     * 心跳
     */
    HEARTBEAT((byte) 0x07);

    private final byte opCode;

    TermOperationType(byte opCode) {
        this.opCode = opCode;
    }

    public byte getOpCode() {
        return opCode;
    }
}
