package com.bitnei.tools.protocol.bean;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenpeng
 * @date 2020-02-21 14:49
 */
@Data
@Builder
public class DataUnitConfig {

    /** 编码 **/
    private String code;
    /** 名称 **/
    private String name;
    /** 数据项 **/
    private List<DataItemConfig> dataItems = new ArrayList<>();
    /** 子Tag **/
    private List<DataUnitConfig> children = new ArrayList<>();

    /**
     * 增加数据项配置
     * @param di
     */
    public void addDataItem(final DataItemConfig di) {
        if (dataItems == null){
            dataItems = new ArrayList<>();
        }
        dataItems.add(di);
    }

    public void addChild(DataUnitConfig tagDataConfig){
        if (children == null){
            children = new ArrayList<>();
        }
        children.add(tagDataConfig);
    }

}
