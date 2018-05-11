package com.bitnei.tools.entity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author zhaogd
 * @Date 2017/12/18
 */
public class CustomSheet {

    private List<?> records;

    private Class<?> clazz;

    private String sheetName;

    public List<?> getRecords() {
        return records;
    }

    public void setRecords(List<?> records) {
        this.records = records;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
