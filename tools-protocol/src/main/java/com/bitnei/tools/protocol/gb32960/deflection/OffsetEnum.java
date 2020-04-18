package com.bitnei.tools.protocol.gb32960.deflection;

import java.util.EnumSet;

/**
 * 偏移字段、偏移值
 *
 * @author zhaogd
 * @date 2018/12/13
 */
public enum OffsetEnum {

    // 车速 2201 偏移量0，系数0.1 单位：km/h
    ITEM_2201("2201", 0.1D),

    // 里程 2202 偏移量0，系数0.1 单位：km
    ITEM_2202("2202", 0.1D),

    // 燃料电池电压 2110 偏移量0，系数0.1 单位：V
    ITEM_2110("2110", 0.1D),

    // 燃料电池电流 2111 偏移量0，系数0.1 单位：A
    ITEM_2111("2111", 0.1D),

    // 燃料消耗率 2112 偏移量0，系数0.01 单位：kg/100km
    ITEM_2112("2112", 0.01D),

    // 燃料电池温度值 2114 偏移量40℃，单位：1℃
    ITEM_2114("2114", 40, Double.NaN, true),

    // 氢系统中最高温度 2115 偏移量40℃，系数0.1 单位：℃
    ITEM_2115("2115", 400, 0.1D),

    // 氢气最高压力 2119 偏移量0，系数0.1 单位：MPa
    ITEM_2119("2119", 0.1D),

    // 电机控制器温度 2302 偏移量40℃，单位：1℃
    ITEM_2302("2302", 40),

    // 驱动电机转速 2303 偏移量20000r/min，单位1r/min
    ITEM_2303("2303", 20000),

    // 电机温度 2304 偏移量40℃，单位：1℃
    ITEM_2304("2304", 40),

    // 电机控制器输入电压 2305 偏移量0，系数0.1 单位：V
    ITEM_2305("2305", 0.1D),

    // 电机控制器直流母线电流 2306 偏移量1000A，系数0.1 单位：A
    ITEM_2306("2306", 10000, 0.1D),

    // 驱动电机转矩 2311 偏移量2000N*m，系数0.1 单位 N*m
    ITEM_2311("2311", 20000, 0.1D),

    // 燃料消耗率 2413 偏移量0，系数0.01 单位：L/100km
    ITEM_2413("2413", 0.01D),

    // 经度 2502 偏移量0，系数0.000001
    ITEM_2502("2502", 0.000001D),
    // 纬度 2503 偏移量0，系数0.000001
    ITEM_2503("2503", 0.000001D),

    // 电池单体电压最高值 2603 偏移量0，系数0.001 单位：V
    ITEM_2603("2603", 0.001D),

    // 电池单体电压最低值 2606 偏移量0，系数0.001 单位：V
    ITEM_2606("2606", 0.001D),

    // 最高温度值 2609 偏移量40℃，单位：1℃
    ITEM_2609("2609", 40),

    // 最低温度值 2612 偏移量40℃，单位：1℃
    ITEM_2612("2612", 40),

    // 总电压 2613 偏移量0，系数0.1 单位：V
    ITEM_2613("2613", 0.1D),

    // 总电流 2614 偏移量1000A，系数0.1 单位：A
    ITEM_2614("2614", 10000, 0.1D);


    private String key;
    private int offset;
    private double factor;
    private boolean list;

    OffsetEnum(String key, int offset) {
        this.key = key;
        this.offset = offset;
        this.factor = Double.NaN;
        this.list = false;
    }

    OffsetEnum(String key, double factor) {
        this.key = key;
        this.offset = 0;
        this.factor = factor;
        this.list = false;
    }

    OffsetEnum(String key, int offset, double factor) {
        this.key = key;
        this.offset = offset;
        this.factor = factor;
        this.list = false;
    }

    OffsetEnum(String key, int offset, double factor, boolean list) {
        this.key = key;
        this.offset = offset;
        this.factor = factor;
        this.list = list;
    }

    private static EnumSet<OffsetEnum> offsetEnums = EnumSet.allOf(OffsetEnum.class);

    public static EnumSet<OffsetEnum> getAll() {
        return offsetEnums;
    }

    public String getKey() {
        return key;
    }

    public int getOffset() {
        return offset;
    }

    public double getFactor() {
        return factor;
    }

    public boolean isList() {
        return list;
    }
}
