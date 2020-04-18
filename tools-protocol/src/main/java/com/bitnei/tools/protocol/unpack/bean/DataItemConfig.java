package com.bitnei.tools.protocol.unpack.bean;


import com.bitnei.tools.protocol.constant.DataConst;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenpeng
 * @date 2020-01-06 13:11
 */
@Data
@Builder
public class DataItemConfig {

    /**
     * 数据项序号
     */
    private String seqNo;

    /**
     * 数据项名称
     */
    private String name;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 位置表达式
     */
    private String pos;

    /**
     * 下级
     */
    private List<DataItemConfig> children;

    /**
     * 属性map
     */
    private Map<String, String> attributes;

    /**
     * 获取属性
     * @param key
     * @return
     */
    public String get(final String key){
        return attributes.get(key);
    }

    /**
     * 获取属性
     * @param key
     * @return
     */
    public int getInt(final String key , final int defaultValue){
        return attributes.containsKey(key) ? Integer.parseInt(attributes.get(key)) : defaultValue;
    }


    /**
     * 获取属性
     * @param key
     * @return
     */
    public Double getDouble(final String key , final Double defaultValue){
        return attributes.containsKey(key) ? Double.parseDouble(attributes.get(key)) : defaultValue;
    }


    /**
     * 增加子节点
     * @param dataItemConfig
     */
    public void addChild(final DataItemConfig dataItemConfig) {
        if (children == null){
            children = new ArrayList<>();
        }
        children.add(dataItemConfig);
    }

    /**
     * 增加属性
     * @param key
     * @param value
     */
    public void putAttribute(final String key, final String value){
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }

    /**
     *  生成数据项
     * @param type  数据类型
     * @param pos   位置
     * @return
     */
    public DataItem dataItem(final String type, final int pos){

        DataItem<String> dataItem = new DataItem();
        dataItem.setName(this.name);
        dataItem.setSeqNo(this.seqNo);
        if (type.equals(DataConst.TIME)){
            dataItem.setLen(DataConst.SIZE_OF_TIME);
        }
        else if (type.equals(DataConst.BYTE)){
            dataItem.setLen(DataConst.SIZE_OF_BYTE);
        }
        else if (type.equals(DataConst.WORD)){
            dataItem.setLen(DataConst.SIZE_OF_WORD);
        }
        else if (type.equals(DataConst.DWORD)){
            dataItem.setLen(DataConst.SIZE_OF_DWORD);
        }
        dataItem.setSeqNo(dataItem.getSeqNo());
        dataItem.setPos(pos);
        return dataItem;
    }



}


