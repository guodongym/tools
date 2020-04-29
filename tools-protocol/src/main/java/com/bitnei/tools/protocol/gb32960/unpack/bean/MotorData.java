package com.bitnei.tools.protocol.gb32960.unpack.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 驱动电机数据
 *
 * @author zhaogd
 * @date 2020/4/29
 */
@Getter
@Setter
public class MotorData {
    /**
     * 驱动电机序号
     */
    private byte v2309;
    /**
     * 驱动电机状态
     */
    private byte v2310;
    /**
     * 驱动电机控制器温度
     */
    private byte v2302;
    /**
     * 驱动电机转速
     */
    private short v2303;
    /**
     * 驱动电机转矩
     */
    private short v2311;
    /**
     * 驱动电机控制器温度
     */
    private byte v2304;
    /**
     * 电机控制器输入电压
     */
    private short v2305;
    /**
     * 电机控制器直流母线电流
     */
    private short v2306;
}
