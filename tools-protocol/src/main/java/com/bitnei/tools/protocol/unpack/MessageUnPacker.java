package com.bitnei.tools.protocol.unpack;

import com.bitnei.tools.protocol.constant.DataConst;
import com.bitnei.tools.protocol.exception.MessageException;
import com.bitnei.tools.protocol.unpack.bean.*;
import com.bitnei.tools.protocol.unpack.util.DataHandleUtil;
import com.bitnei.tools.protocol.unpack.util.XmlUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jdom2.Attribute;
import org.jdom2.Element;

import java.util.*;

/**
 * @author chenpeng
 * @date 2019-12-26 16:28
 */
public class MessageUnPacker {

    /**
     * 命令单元配置
     */
    private List<DataUnitConfig> dataUnitConfigs = new ArrayList<>();

    /**
     * 获取配置
     * @return
     */
    public List<DataUnitConfig> getDataUnitConfigs(){
        return this.dataUnitConfigs;
    }


    /**
     * 根据命令识别码获取cmdTag配置
     * @param code
     * @return
     */
    public DataUnitConfig getTagDataConfigByTag(final String code){

        for (DataUnitConfig tagDataConfig: dataUnitConfigs){
            if (tagDataConfig.getCode().equalsIgnoreCase(code)){
                return tagDataConfig;
            }
        }
        return null;
    }

    /**
     * 加载xml配置文件， 当前只支持二级di
     * @param xmlPath
     */
    public static MessageUnPacker fromXmlFile(final String xmlPath){

        MessageUnPacker upacker = new MessageUnPacker();
        // 获取Root节点
        Element root = XmlUtil.getRootElement(xmlPath);
        // upstream节点
        Element upstreamEl = root.getChild("upstream");
        // 获取所有的cmdTag子节点
        List<Element> cmdTagEls = upstreamEl.getChildren("cmdTag");
        // 遍历解析所有的coreTag
        upacker.dataUnitConfigs.clear();
        for (Element tagEl: cmdTagEls){
            DataUnitConfig tagDataConfig = upacker.parseCmdTag(tagEl);
            upacker.getDataUnitConfigs().add(tagDataConfig);

        }
        return upacker;
    }

    /**
     * 加载xml配置文件， 当前只支持二级di
     * @param xmlFileName
     */
    public static MessageUnPacker fromClassPathXmlFile(final String xmlFileName){

        MessageUnPacker upacker = new MessageUnPacker();
        // 获取Root节点
        Element root = XmlUtil.getRootElement(MessageUnPacker.class.getClassLoader().getResourceAsStream(xmlFileName));
        // upstream节点
        Element upstreamEl = root.getChild("upstream");
        // 获取所有的cmdTag子节点
        List<Element> cmdTagEls = upstreamEl.getChildren("cmdTag");
        // 遍历解析所有的coreTag
        upacker.dataUnitConfigs.clear();
        for (Element tagEl: cmdTagEls){
            DataUnitConfig tagDataConfig = upacker.parseCmdTag(tagEl);
            upacker.getDataUnitConfigs().add(tagDataConfig);

        }
        return upacker;

    }

    /**
     * 解包报文
     * @param message
     * @return
     */
    public DataMessage unpack32960(final String message){

        // 原始报文
        byte[] buf = DataHandleUtil.str2Bytes(message);
        // 报文长度
        int size = message.length()/2;
        // 开始解析报文
        DataMessage dataMessage = new DataMessage();

        // region 报文解析
        int pos = 0;
        // 起始符
        String startMask = DataHandleUtil.byte2String(buf, pos, 2);
        if (!DataConst.START_MASK.equals(startMask)){
            throw new MessageException("报文不合法，必须要以2323开头", message);
        }
        dataMessage.setStartMask(startMask);
        pos += 2;
        // 命令标志
        int cmdFlag = DataHandleUtil.byte2Int(buf, pos, 1);
        dataMessage.setCmdTag(cmdFlag);
        pos++;
        // 响应标志
        int replyFlag = DataHandleUtil.byte2Int(buf, pos, 1);
        dataMessage.setReplyTag(replyFlag);
        pos++;
        // VIN
        String vin = DataHandleUtil.byte2String(buf, pos, 17);
        dataMessage.setVin(vin);
        pos+=17;
        // 加密模式
        int decryptMode = DataHandleUtil.byte2Int(buf, pos, 1);
        dataMessage.setDecryptMode(decryptMode);
        pos++;
        // 数据单元长度
        int dataUnitLength = DataHandleUtil.byte2Int(buf, pos, 2);
        dataMessage.setDataUnitLength(dataUnitLength);
        pos += 2;

        // 命令单元
        byte[] dataUnitBytes = new byte[dataUnitLength];
        System.arraycopy(buf, pos, dataUnitBytes, 0, dataUnitLength);
        // 获取命令单元的定义
        String hexCode = String.format("0x%02x", cmdFlag);
        DataUnitConfig duc = findDataUnitConfig(hexCode, dataUnitConfigs);
        if (duc != null){
            List<DataItem> dataItems = parseDataUnit(dataUnitBytes, 0, dataUnitLength,  duc);
            dataMessage.setDataItemList(dataItems);
        }
        pos += dataUnitLength;
        //判断一下报文长度是否正确
        if (pos != (size-1)){
            throw new MessageException(String.format("报文长度不正确，当前报文长度为%d, 而解析出来的长度要求为%d", size, (pos+1)), message);
        }
        // 检验码
        byte bcc = buf[pos];
        byte messageBcc = DataHandleUtil.getBCC(buf, 0, 24 + dataUnitLength);
        dataMessage.setValidateResult(bcc == messageBcc ? 1 :0);
        dataMessage.setBcc(bcc);
        return dataMessage;
        // endregion 报文解析

    }


    /**
     * 解包报文
     * @param message
     * @return
     */
    public DataMessage unpack17691(final String message){

        // 原始报文
        byte[] buf = DataHandleUtil.str2Bytes(message);
        // 报文长度
        int size = message.length()/2;
        // 开始解析报文
        DataMessage dataMessage = new DataMessage();
        // region 报文解析
        int pos = 0;
        // 起始符
        String startMask = DataHandleUtil.byte2String(buf, pos, 2);
        if (!DataConst.START_MASK.equals(startMask)){
            throw new MessageException("报文不合法，必须要以2323开头", message);
        }
        dataMessage.setStartMask(startMask);
        pos += 2;
        // 命令单元
        int cmdFlag = DataHandleUtil.byte2Int(buf, pos, 1);
        dataMessage.setCmdTag(cmdFlag);
        pos++;
        // VIN
        String vin = DataHandleUtil.byte2String(buf, pos, 17);
        dataMessage.setVin(vin);
        pos+=17;
        // 版本号
        int version = DataHandleUtil.byte2Int(buf, pos, 1);
        dataMessage.setVersion(version);
        pos+=1;
        // 加密模式
        int decryptMode = DataHandleUtil.byte2Int(buf, pos, 1);
        dataMessage.setDecryptMode(decryptMode);
        pos++;
        // 数据单元长度
        int dataUnitLength = DataHandleUtil.byte2Int(buf, pos, 2);
        dataMessage.setDataUnitLength(dataUnitLength);
        pos += 2;

        // 命令单元
        byte[] dataUnitBytes = new byte[dataUnitLength];
        System.arraycopy(buf, pos, dataUnitBytes, 0, dataUnitLength);
        // 获取命令单元的定义
        String hexCode = String.format("0x%02x", cmdFlag);
        DataUnitConfig duc = findDataUnitConfig(hexCode, dataUnitConfigs);
        if (duc != null){
            List<DataItem> dataItems = parseDataUnit(dataUnitBytes, 0, dataUnitLength,  duc);
            dataMessage.setDataItemList(dataItems);
        }
        pos += dataUnitLength;
        //判断一下报文长度是否正确
        if (pos != (size-1)){
            throw new MessageException(String.format("报文长度不正确，当前报文长度为%d, 而解析出来的长度要求为%d", size, (pos+1)), message);
        }
        // 检验码
        byte bcc = buf[pos];
        byte messageBcc = DataHandleUtil.getBCC(buf, 0, 24 + dataUnitLength);
        dataMessage.setValidateResult(bcc == messageBcc ? 1 :0);
        dataMessage.setBcc(bcc);
        return dataMessage;
        // endregion 报文解析

    }

    /**
     *  查找命令单元配置
     * @param code
     * @param dataUnitConfigList
     * @return
     */
    private DataUnitConfig findDataUnitConfig(final String code, List<DataUnitConfig> dataUnitConfigList){
        Optional<DataUnitConfig> optional= dataUnitConfigList.stream().filter(obj -> obj.getCode().equalsIgnoreCase(code)).findFirst();
        if (optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /**
     * 解析命令单元
     * @param buf
     * @param pos
     * @param duc
     * @return
     */
    private List<DataItem> parseDataUnit(byte[] buf, int pos, int size, final DataUnitConfig duc){


        // region 解析数据项
        Map<String, Object> params = new HashMap<>(10);

        // 当前源码位置
        List<DataItem> dataItems = new ArrayList<>();
        for (DataItemConfig dic: duc.getDataItems()) {

            // 先计算出当前的pos， 如果当前数据项配置没有配置pos，那么就自动默认为当前的pos变量值，否则将调用表达式来计算出实际的pos
            if (StringUtils.isNotEmpty(dic.getPos())){
                pos = expressionToInt(dic.getPos(), params);
            }
            pos = parseDataItem(buf, pos,  dic, dataItems, params);


        }
        // endregion 解析数据项

        // region 解析子数据单元
        if (duc.getChildren() != null ){
            while (pos < (size-1)) {

                byte dataFlag = buf[pos++];
                String hexCode = String.format("0x%02x", dataFlag);
                DataUnitConfig subDuc = findDataUnitConfig(hexCode, duc.getChildren() );
                if (subDuc == null) {
                    System.out.println(String.format("%s 找不到定义，则跳过剩余解析", hexCode));
                    break;
                }
                // 先重新拷贝一个数据，从pos开始
                List<DataItem> subDataItems = parseDataUnit(buf, pos, size, subDuc);
                dataItems.addAll(subDataItems);

                // 获取最后一个last的pos
                DataItem lastItem = dataItems.get(dataItems.size()-1);
                pos = (lastItem.getPos() + lastItem.getLen());
            }
        }


        // endregion 解析子数据单元

        return dataItems;

    }


    /**
     * 解析数据项
     * @param buf
     * @param pos
     * @param dic
     * @return  最后的pos
     */
    private int parseDataItem(byte[] buf, int pos, DataItemConfig dic, List<DataItem> dataItems, Map<String, Object> params) {

        int byteSize = 0;

        if (DataConst.TIME.equals(dic.getDataType())) {
            DataItem<String> di = dic.dataItem(dic.getDataType(), pos);
            byteSize = DataHandleUtil.sizeOf(dic.getDataType());
            String time =  DataHandleUtil.byte2Time(buf, pos);
            di.setVal(time);
            di.setSrcCode(DataHandleUtil.byte2hex(buf, pos, byteSize));
            dataItems.add(di);
            params.put("d"+ dic.getSeqNo(), di);
        }
        else if (DataConst.BYTE.equals(dic.getDataType()) || DataConst.WORD.equals(dic.getDataType()) || DataConst.DWORD.equals(dic.getDataType())) {


            DataItem di = dic.dataItem(dic.getDataType(), pos);

            byteSize = DataHandleUtil.sizeOf(dic.getDataType());

            int val = DataHandleUtil.byte2Int(buf, pos, byteSize);
            // 判断有无factor，如果有，则代表实际值为浮点数
            if (dic.getAttributes().containsKey("factor")){
                int offset = dic.getInt("offset", 0);
                double factor = dic.getDouble("factor", 1.0d);
                //实际值
                String doubleVal = DataHandleUtil.getDouble(val, offset, factor);
                di.setVal(doubleVal);
            }
            else if (dic.getAttributes().containsKey("offset")){
                int offset = dic.getInt("offset", 0);
                int realVal = val-offset;
                di.setVal(realVal);
            }
            else {
                di.setVal(val);
            }

            // 判断是否存在bits的子数据项
            if (dic.getChildren() != null) {
                BitGroup bitGroup = new BitGroup(val);
                int bitPos = 0;
                List<DataItem> bitLists = new ArrayList<>(dic.getChildren().size());
                for(DataItemConfig bitDi: dic.getChildren()){
                    if (bitDi.getAttributes().containsKey("bitPos")) {
                        bitPos = bitDi.getInt("bitPos", 0);
                    }
                    int bitCount = 1;
                    if (bitDi.getAttributes().containsKey("bitCount")) {
                        bitCount = bitDi.getInt("bitCount", 1);
                    }
                    DataItem bitData = bitDi.dataItem(bitDi.getDataType(), pos);
                    int bitValue = bitGroup.value(bitPos, bitCount);
                    bitData.setVal(bitValue);
                    bitPos += bitCount;
                    bitLists.add(bitData);

                }
                di.setChildren(bitLists);
            }
            di.setSrcCode(DataHandleUtil.byte2hex(buf, pos, byteSize));
            dataItems.add(di);
            params.put("d"+ dic.getSeqNo(), di);

        }
        else if (DataConst.STRING.equals(dic.getDataType())) {
            DataItem<String> di = dic.dataItem(dic.getDataType(), pos);
            byteSize = expressionToInt(dic.getAttributes().get("byteSize"), params);
            String val = DataHandleUtil.byte2String(buf, pos, byteSize);
            di.setVal(val);
            di.setLen(byteSize);
            di.setSrcCode(DataHandleUtil.byte2hex(buf, pos, byteSize));
            dataItems.add(di);
            params.put("d"+ dic.getSeqNo(), di);
        }
        else if (DataConst.ARRAY.equals(dic.getDataType())) {
            DataItem<List<DataItem>> di = dic.dataItem(dic.getDataType(), pos);
            byteSize = expressionToInt(dic.getAttributes().get("byteSize"), params);
            di.setLen(byteSize);
            // 先加到参数对
            params.put("d"+ dic.getSeqNo(), di);
            // 解析数组
            List<DataItem> children = new ArrayList<>();
            int subPos = pos;
            int listCount = 0;
            String countString = dic.getAttributes().get("listCount");
            if (StringUtils.isNotEmpty(countString)){
                listCount = expressionToInt(countString, params);
            }
            else {
                listCount = Integer.parseInt(dataItems.get(dataItems.size()-1).getVal().toString());
            }
            DataItemConfig subDic = dic.getChildren().get(0);
            for (int i = 0; i < listCount; i++) {
                // 增加数组索引
                params.put("d" + dic.getSeqNo() + "_index", i);
                // 先计算出当前的pos， 如果当前数据项配置没有配置pos，那么就自动默认为当前的pos变量值，否则将调用表达式来计算出实际的pos
                if (StringUtils.isNotEmpty(subDic.getPos())){
                    subPos = expressionToInt(subDic.getPos(), params);
                }
                subPos = parseDataItem(buf, subPos, subDic, children, params);
            }
            di.setVal(children);
            di.setSrcCode(DataHandleUtil.byte2hex(buf, pos, byteSize));
            dataItems.add(di);
            params.put("d"+ dic.getSeqNo(), di);
        }
        else if (DataConst.ARRAY2.equals(dic.getDataType())) {
            DataItem<List<List<DataItem>>> di = dic.dataItem(dic.getDataType(), pos);
            // 先加到参数对
            params.put("d"+ dic.getSeqNo(), di);
            // 解析数组
            List<List<DataItem>> children = new ArrayList<>();
            int subPos = pos;
            // 如果listCount属性不为空，取listCount表达式，否则直接取上一个数据项
            int listCount = 0;
            String countString = dic.getAttributes().get("listCount");
            if (StringUtils.isNotEmpty(countString)){
                listCount = expressionToInt(countString, params);
            }
            else {
                listCount = Integer.parseInt(dataItems.get(dataItems.size()-1).getVal().toString());
            }
            for (int j = 0; j < listCount; j++) {
                // 增加数组索引
                params.put("d" + dic.getSeqNo() + "_index", j);
                List<DataItem> subChildren = new ArrayList<>();
                for (int i = 0; i < dic.getChildren().size(); i++) {
                    DataItemConfig subDic = dic.getChildren().get(i);

                    // 先计算出当前的pos， 如果当前数据项配置没有配置pos，那么就自动默认为当前的pos变量值，否则将调用表达式来计算出实际的pos
                    if (StringUtils.isNotEmpty(subDic.getPos())){
                        subPos = expressionToInt(subDic.getPos(), params);
                    }
                    subPos = parseDataItem(buf, subPos, subDic, subChildren, params);
                }
                children.add(subChildren);
            }

            // 计算当前的子数据项的所有size
            if (dic.getAttributes().containsKey("byteSize")){
                byteSize = expressionToInt(dic.getAttributes().get("byteSize"), params);
            }
            else {
                //计算所有的szie
                for (List<DataItem> list: children){
                    for (DataItem tmp: list){
                        byteSize += tmp.getLen();
                    }
                }
            }

            di.setLen(byteSize);
            di.setVal(children);
            di.setSrcCode(DataHandleUtil.byte2hex(buf, pos, byteSize));
            dataItems.add(di);
            params.put("d"+ dic.getSeqNo(), di);
        }
        else if (DataConst.SOURCE.equals(dic.getDataType())) {
            DataItem<String> di = dic.dataItem(dic.getDataType(), pos);
            byteSize = expressionToInt(dic.getAttributes().get("byteSize"), params);
            String hexCode = DataHandleUtil.byte2hex(buf, pos, byteSize);
            di.setLen(byteSize);
            di.setVal(hexCode);
            di.setSrcCode(hexCode);
            dataItems.add(di);
            params.put("d"+ dic.getSeqNo(), di);


        }
        return pos + byteSize;

    }


    /**
     * 获取当前表达式位置
     * @param expression        表达式
     * @param params            参数Map
     * @return
     */
    private int expressionToInt(final String expression, Map<String, Object> params){

        if (NumberUtils.isDigits(expression)){
            return Integer.parseInt(expression);
        }
        // 编译表达式
        Expression compiledExp = AviatorEvaluator.compile(expression);
        // 执行表达式
        Object result = compiledExp.execute(params);
        // 返回结果
        return Integer.parseInt(result.toString());

    }




    /**
     * 递归解析Tag
     * @param tagEl
     * @return
     */
    private DataUnitConfig parseCmdTag(Element tagEl){

        String code = tagEl.getAttributeValue("code");
        String name = tagEl.getAttributeValue("name");
        DataUnitConfig rootTagData = DataUnitConfig.builder().code(code).name(name).build();

        // 获取节点下的所有di
        List<Element> diEls = tagEl.getChildren("di");
        for (Element diEl: diEls){
            // 将元素转为数据项配置实体
            DataItemConfig dataItemConfig = parseDiTag(diEl);
            rootTagData.addDataItem(dataItemConfig);
        }

        // 获取节点下的子cmdTag
        List<Element> subCmdTagEls = tagEl.getChildren("cmdTag");
        for (Element subCmdTagEl: subCmdTagEls){
            rootTagData.addChild(parseCmdTag(subCmdTagEl));
        }

        return rootTagData;
    }


    /**
     * 解析Di
     * @param diEl
     * @return
     */
    private DataItemConfig parseDiTag(Element diEl){
        // 将元素转为数据项配置实体
        DataItemConfig dataItemConfig = element2DataItemConfig(diEl);
        // 判断一下，看是否有子节点
        List<Element> subDiEls = diEl.getChildren("di");
        for (Element subEl:  subDiEls){
            DataItemConfig sub = parseDiTag(subEl);
            dataItemConfig.addChild(sub);
        }
        return dataItemConfig;
    }


    /**
     *  将element转为数据项配置
     * @param diEl
     * @return
     */
    private DataItemConfig element2DataItemConfig(Element diEl){
        // 获取所有属性
        String dName = diEl.getAttributeValue("name");
        String seqNo = diEl.getAttributeValue("seqNo");
        String dataType = diEl.getAttributeValue("dataType");
        String pos = diEl.getAttributeValue("pos");

        DataItemConfig dataItemConfig = DataItemConfig.builder().name(dName).seqNo(seqNo).dataType(dataType).pos(pos).build();

        List<Attribute> attributes = diEl.getAttributes();
        for (Attribute attribute: attributes){
            String k = attribute.getName();
            String v = attribute.getValue();
            if ((!"name".equals(k)) && (!"seqNo".equals(k)) && (!"dataType".equals(k)) || (!"pos".equals(k))){
                dataItemConfig.putAttribute(k, v);
            }
        }
        return dataItemConfig;
    }

}
