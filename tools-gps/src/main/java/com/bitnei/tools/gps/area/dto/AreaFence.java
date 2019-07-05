package com.bitnei.tools.gps.area.dto;

import java.io.Serializable;

public class AreaFence implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1210008111L;

	String uuid;
	
	public String areaId;
	public String areaName;
	public String parentAreaId;
	
	public FenceIndex index;

	public AreaFence(String areaId, String areaName, FenceIndex index) {
		super();
		this.areaId = areaId;
		this.areaName = areaName;
		this.index = index;
	}

	public void setParentAreaId(String parentAreaId) {
		this.parentAreaId = parentAreaId;
	}
	
}
