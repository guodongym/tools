package com.bitnei.tools.entity;

import java.lang.reflect.Field;

public class CellInfo implements Comparable<CellInfo> {

	private String name;
	private String format;
	private Object defaultValue;
	private int order;
	private Field field;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	@Override
	public int compareTo(CellInfo other) {
		return order - other.order;
	}

}
