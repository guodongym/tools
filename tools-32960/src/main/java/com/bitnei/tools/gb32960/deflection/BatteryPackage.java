package com.bitnei.tools.gb32960.deflection;

import com.bitnei.tools.core.constant.SymbolConstant;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 电池包单体数据
 *
 * @author zhaogd
 * @date 2018/12/13
 */
public class BatteryPackage {

    private String index;
    private List<String> singleBatteryList;

    public BatteryPackage(String index, List<String> singleBatteryList) {
        this.index = index;
        this.singleBatteryList = singleBatteryList;
    }

    @Override
    public String toString() {
        String builder = index +
                SymbolConstant.COLON +
                StringUtils.join(singleBatteryList, SymbolConstant.UNDER_LINE);

        return Base64.encodeBase64String(builder.getBytes());
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<String> getSingleBatteryList() {
        return singleBatteryList;
    }

    public void setSingleBatteryList(List<String> singleBatteryList) {
        this.singleBatteryList = singleBatteryList;
    }
}
