package com.bitnei.tools.protocol.unpack.bean;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenpeng
 * @date 2020-02-21 17:32
 */
@Data
public class DataMessage {

    /** 起始符 **/
    private String startMask;
    /** 命令标识**/
    private int cmdTag;
    /** 应答标志 **/
    private int replyTag;
    /** 唯一识别码 **/
    private String vin;
    /** 数据单元加密方式**/
    private int decryptMode;
    /** 数据单元长度 **/
    private int dataUnitLength;
    /** 数据项 **/
    List<DataItem> dataItemList = new ArrayList<>();
    /** 校验码 **/
    private int bcc;
    /** 终端程序版本号 **/
    private int version;
    /** 是否检验成功 **/
    private int validateResult = 0;

    /**
     * json上下文字
     */
    private ReadContext context;

    public void initJsonContext() {
        if (context == null){
            Gson gson = new Gson();
            this.context = JsonPath.parse(gson.toJson(this));
        }
    }


    /**
     * 通过数据项编码读取数据项列表
     * @param codes
     * @return
     */
    public List<DataItem> read(final String... codes){
        initJsonContext();
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<codes.length; i++) {
            sb.append("@.seqNo == '" + codes[i] + "'");
            if (i != (codes.length-1)) {
                sb.append(" || ");
            }
        }
        final String jsonPath = "$..[?(" + sb.toString() + ")]";
        List<Object> objects = context.read(jsonPath, List.class);
        return objects2items(objects);
    }


    /**
     * 将list对象转为list<DataItem>
     * @param objects
     * @return
     */
    private List<DataItem> objects2items(List<Object> objects){
        if (objects == null) {
            return new ArrayList<>(0);
        }
        List<DataItem> dataItems = new ArrayList<>(objects.size());
        objects.stream().forEach(o -> {
            Gson gson = new Gson();
            String objectJson = gson.toJson(o);
            DataItem di = gson.fromJson(objectJson, DataItem.class);
            dataItems.add(di);
        });

        return dataItems;
    }

    /**
     * 通过数据项序号查找数据项
     * @param code
     * @return
     */
    public DataItem readFirst(final String code){

        List<DataItem> dataItems = readList(code);
        if (dataItems != null && dataItems.isEmpty()){
            return null;
        }
        else {
            return dataItems.get(0);
        }
    }


    /**
     * 通过数据项序号查找数据项
     * @param code
     * @return
     */
    public List<DataItem> readList(final String code){

        initJsonContext();
        final String jsonPath = "$..[?(@.seqNo == '" + code + "')]";
        List<Object> objects = context.read(jsonPath, List.class);
        return objects2items(objects);

    }
}
