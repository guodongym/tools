package com.bitnei.tools.gps.area.dto;

import java.util.List;

public class FenceIndex {

	public double xmin;
	public double xmax;
	
	public double ymin;
	public double ymax;
	
	/**
	 * 里面的点非空
	 */
	public List<Coordinate> coords;//按照顺序需要画的点
	
	public FenceIndex(List<Coordinate> coords) {
		super();
		this.coords = coords;
		bulidIndex();
	}

	void bulidIndex(){
		if (null != coords && coords.size() >0) {
			Coordinate st = coords.get(0);
			xmin= st.x;
			xmax= st.x;
			ymin= st.y;
			ymax= st.y;
			for (Coordinate coord : coords) {
				if (xmin > coord.x) 
					xmin = coord.x;
				else if (xmax < coord.x) 
					xmax = coord.x;
				
				if (ymin > coord.y) 
					ymin = coord.y;
				else if (ymax < coord.y)
					ymax = coord.y;
				
			}
		}
	}

	@Override
	public String toString() {
		return "FenceIndex [xmin=" + xmin + ", xmax=" + xmax + ", ymin=" + ymin + ", ymax=" + ymax + "]";
	}
	
}
                                                  