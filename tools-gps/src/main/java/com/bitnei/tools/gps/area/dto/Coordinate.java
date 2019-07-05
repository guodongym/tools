package com.bitnei.tools.gps.area.dto;

import java.io.Serializable;

public class Coordinate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1655500001L;

	public double x = -999;//经度
	public double y = -999;//维度
	public Coordinate(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
}
                                                  