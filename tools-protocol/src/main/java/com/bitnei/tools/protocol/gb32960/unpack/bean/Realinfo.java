package com.bitnei.tools.protocol.gb32960.unpack.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 明细
 *
 * @author zhaogd
 */
@Getter
@Setter
public class Realinfo implements Serializable {
    private static final long serialVersionUID = 7626153696765933852L;

    /**
     * 整车数据: 报文时间
     */
    private String v2000;

    /**
     * 整车数据: 车辆状态
     */
    private byte v3201;

    /**
     * 整车数据: 充电状态
     */
    private byte v2301;

    /**
     * 整车数据: 运行模式
     */
    private byte v2213;

    /**
     * 整车数据: 车速
     */
    private short v2201;

    /**
     * 整车数据: 里程
     */
    private int v2202;

    /**
     * 整车数据: 总电压
     */
    private short v2613;

    /**
     * 整车数据: 总电流
     */
    private short v2614;

    /**
     * 整车数据: SOC-国标
     */
    private byte v7615;

    /**
     * 整车数据: DC-DC状态
     */
    private byte v2214;

    /**
     * 整车数据: 挡位
     */
    private byte v2203;

    /**
     * 整车数据: 绝缘电阻
     */
    private short v2617;

    /**
     * 整车数据: 加速踏板行程值
     */
    private byte v2208;

    /**
     * 整车数据: 制动踏板行程值
     */
    private byte v2209;

    /**
     * 驱动电机数据: 驱动电机个数
     */
    private byte v2307;

    /**
     * 驱动电机数据: 驱动电机列表[列表]
     */
    private String v2308;

    /**
     * 燃料电池数据: 燃料电池电压
     */
    private short v2110;

    /**
     * 燃料电池数据: 燃料电池电流
     */
    private short v2111;

    /**
     * 燃料电池数据: 燃料消耗率
     */
    private short v2112;

    /**
     * 燃料电池数据: 燃料电池温度探针总数
     */
    private short v2113;

    /**
     * 燃料电池数据: 燃料电池温度值列表[列表]
     */
    private String v2114;

    /**
     * 燃料电池数据: 氢系统中最高温度
     */
    private short v2115;

    /**
     * 燃料电池数据: 氢系统中最高温度探针号
     */
    private byte v2116;

    /**
     * 燃料电池数据: 氢气最高浓度
     */
    private short v2117;

    /**
     * 燃料电池数据: 氢气最高浓度传感器代号
     */
    private byte v2118;

    /**
     * 燃料电池数据: 氢气最高压力
     */
    private short v2119;

    /**
     * 燃料电池数据: 氢气最高压力传感器代号
     */
    private byte v2120;

    /**
     * 燃料电池数据: 高压DC-DC状态
     */
    private byte v2121;

    /**
     * 发动机数据: 发动机状态
     */
    private byte v2401;

    /**
     * 发动机数据: 曲轴转速
     */
    private short v2411;

    /**
     * 发动机数据: 燃料消耗率
     */
    private short v2413;

    /**
     * 车辆位置数据: 定位状态
     */
    private byte v2501;

    /**
     * 车辆位置数据: 经度
     */
    private int v2502;

    /**
     * 车辆位置数据: 纬度
     */
    private int v2503;

    /**
     * 极值数据: 最高电压电池子系统号
     */
    private byte v2601;

    /**
     * 极值数据: 最高电压电池单体代号
     */
    private byte v2602;

    /**
     * 极值数据: 电池单体电压最高值
     */
    private short v2603;

    /**
     * 极值数据: 最低电压电池子系统号
     */
    private byte v2604;

    /**
     * 极值数据: 最低电压电池单体代号
     */
    private byte v2605;

    /**
     * 极值数据: 电池单体电压最低值
     */
    private short v2606;

    /**
     * 极值数据: 最高温度子系统号
     */
    private byte v2607;

    /**
     * 极值数据: 最高温度探针号
     */
    private byte v2608;

    /**
     * 极值数据: 最高温度值
     */
    private byte v2609;

    /**
     * 极值数据: 最低温度子系统号
     */
    private byte v2610;

    /**
     * 极值数据: 最低温度探针号
     */
    private byte v2611;

    /**
     * 极值数据: 最低温度值
     */
    private byte v2612;

    /**
     * 报警数据: 最高报警等级
     */
    private byte v2920;

    /**
     * 报警数据: 通用报警标志
     */
    private int v3801;

    /**
     * 报警数据: 可充电储能装置故障总数N1
     */
    private byte v2921;

    /**
     * 报警数据: 可充电储能装置故障代码列表[列表]
     */
    private String v2922;

    /**
     * 报警数据: 驱动电机故障总数N2
     */
    private byte v2804;

    /**
     * 报警数据: 驱动电机故障代码列表[列表]
     */
    private String v2805;

    /**
     * 报警数据: 发动机故障总数N3
     */
    private byte v2923;

    /**
     * 报警数据: 发动机故障代码列表[列表]
     */
    private String v2924;

    /**
     * 报警数据: 其他故障总数N4
     */
    private byte v2808;

    /**
     * 报警数据: 其他故障代码列表[列表]
     */
    private String v2809;

    /**
     * 可充电储能装置电压: 可充电储能子系统个数
     */
    private byte v4001;

    /**
     * 可充电储能装置电压: 可充电储能子系统电压信息[列表]
     */
    private String v4002;

    /**
     * 可充电储能装置温度: 可充电储能子系统个数
     */
    private byte v4101;

    /**
     * 可充电储能装置温度: 可充电储能子系统温度信息[列表]
     */
    private String v4102;

    /**
     * 自定义数据附加项
     */
    private String extra;
}

