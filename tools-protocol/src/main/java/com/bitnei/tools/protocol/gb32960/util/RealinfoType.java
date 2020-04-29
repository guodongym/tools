package com.bitnei.tools.protocol.gb32960.util;

import lombok.extern.slf4j.Slf4j;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 实时和补发数据，报文体信息类型标识
 *
 * @author zhaogd
 */
@Slf4j
public enum RealinfoType {
    /**
     * 整车数据
     */
    CAR((byte) 0x01),
    /**
     * 驱动电机数据
     */
    MOTOR((byte) 0x02),
    /**
     * 燃料电池数据
     */
    BATTERY((byte) 0x03),
    /**
     * 发动机数据
     */
    ENGINE((byte) 0x04),
    /**
     * 车辆位置数据
     */
    LOCATION((byte) 0x05),
    /**
     * 极值数据
     */
    EXTREMA((byte) 0x06),
    /**
     * 报警数据
     */
    ALARM((byte) 0x07),
    /**
     * 可充电储能装置电压数据
     */
    VOLTAGE((byte) 0x08),
    /**
     * 可充电储能装置温度数据
     */
    TEMPERATURE((byte) 0x09),
    /**
     * 其他数据
     */
    DEFAULT((byte) 0x80);

    private static final Map<Byte, RealinfoType> NAME_TO_VALUE_MAP = new HashMap<>();

    static {
        for (RealinfoType value : EnumSet.allOf(RealinfoType.class)) {
            NAME_TO_VALUE_MAP.put(value.opCode, value);
        }
    }

    private final byte opCode;

    RealinfoType(byte opCode) {
        this.opCode = opCode;
    }

    public byte getOpCode() {
        return opCode;
    }

    public static RealinfoType formCode(byte opCode) {
        return NAME_TO_VALUE_MAP.getOrDefault(opCode, RealinfoType.DEFAULT);
    }

    public static boolean existsCode(byte opCode) {
        return NAME_TO_VALUE_MAP.containsKey(opCode);
    }
}
