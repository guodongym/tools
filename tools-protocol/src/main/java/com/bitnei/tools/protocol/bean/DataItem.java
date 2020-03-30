package com.bitnei.tools.protocol.bean;


import lombok.Data;

import java.util.List;

/**
 * @author chenpeng
 * @date 2020-01-06 13:11
 */
@Data
public class DataItem<T> {

    /**
     * 数据项序号
     */
    private String seqNo;

    /**
     * 数据项名称
     */
    private String name;
    /**
     * 位置表达式
     */
    private int pos;
    /**
     * 长度
     */
    private int len;
    /**
     * 值
     */
    private T val;

    /**
     * 源码
     */
    private String srcCode;

    /**
     * 下级
     */
    private List<DataItem> children;



}


