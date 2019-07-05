package com.bitnei.tools.gps.area.handle;

import com.bitnei.tools.gps.area.dto.AreaFence;
import com.bitnei.tools.gps.area.dto.Coordinate;
import com.bitnei.tools.gps.area.util.FileSource;

import java.util.*;

public class AreaFenceHandler {
	
	List<AreaFence> areaFences;

	void build(){
		areaFences = FileSource.areaFences;
	}
	{
		build();
	}
	
	/**
	 * 
	 * @param x 经度
	 * @param y 纬度
	 * @return 行政区域 id集合
	 */
	public List<String> areaIds(double x, double y){
		if (0 <= x && x <= 180
				&& 0 <= y && y <= 180) {
			return areaIds(new Coordinate(x, y));
		}
		return null;
	}
	
	/**
	 * 获取 gps 点属于那个行政区域
	 * 
	 * @param coord gps经纬度点
	 * @return 返回行政区域 id 的集合；所属地区 市区 省份
	 */
	public List<String> areaIds(Coordinate coord){
		Set<Integer> idxs = indexs(coord);
		if (null != idxs) {
			List<String> ids = new LinkedList<String>();
			for (int idx : idxs) {
				AreaFence fence = areaFences.get(idx);
				
				if (!ids.contains(fence.areaId)) {
					
					boolean inFence = isPointInPolygon(fence.index.coords, coord);
					if (inFence) {
						ids.add(fence.areaId);
						if (null != fence.parentAreaId
								&& !ids.contains(fence.parentAreaId)) {
							ids.add(fence.parentAreaId);
						}
					}
				} else if (null != fence.parentAreaId
						&& !ids.contains(fence.parentAreaId)) {
					ids.add(fence.parentAreaId);
				}
			}
			int len = ids.size();
			if (len >0) {
				Collections.sort(ids);
				String id = ids.get(0);
				if (!id.endsWith("0000")) {
					String idZero = new String(id.substring(0, 2)+"0000");
					ids.add(0, idZero);
				}
				return ids;
				/*
				List<String> reIds = new ArrayList<String>(len);
				reIds.add("");
				if (len > 1) {
					reIds.add("");
				}
				for (String id : ids) {
					if (id.endsWith("0000")) {
						reIds.set(0, id);
					} else if (id.endsWith("00")
							|| 2 == len) {
						reIds.set(1, id);
					} else {
						reIds.add(id);
					}
				}
				ids = null;
				return reIds;*/
			}
		}
		return null;
	}
	
	/**
	 * 获取 区域围栏的索引，用于缩小搜索范围
	 * @param coord
	 * @return
	 */
	private Set<Integer> indexs(Coordinate coord){
		if (null == areaFences || areaFences.size() ==0) 
			return null;
		
		int len = areaFences.size();
		int st = 0;
		int ed = len-1;
		int idx= (st+ed)/2;
		
		
		while(st < ed){
			
			AreaFence fence = areaFences.get(idx);
			
			if (coord.x < fence.index.xmin ) {
				ed = idx - 1;
			} else if (coord.x > fence.index.xmin) {
				st = idx + 1;
			} else {
				break;
			}
			idx= (st+ed)/2;
		}
		
		Set<Integer> ids = new HashSet<Integer>();
		for(int pos = 0; pos < idx; pos++){
			AreaFence fence = areaFences.get(pos);
			if (coord.x <= fence.index.xmax
					&& coord.y <= fence.index.ymax 
					&& coord.y >= fence.index.ymin) {
				
				if (!ids.contains(pos)) {
					ids.add(pos);
				}
			}
		}
		
		for(int pos = idx; pos <= idx+1; pos++){
			if (pos < len) {
				
				AreaFence fence = areaFences.get(pos);
				if (coord.x <= fence.index.xmax 
						&& coord.x >= fence.index.xmin
						&& coord.y <= fence.index.ymax 
						&& coord.y >= fence.index.ymin) {
					
					if (!ids.contains(pos)) {
						ids.add(pos);
					}
				}
			}
		}
		if (ids.size() > 0) {
			return ids;
		}
		return null;
	}
	
	/**
     * 判断 点是否在多边形的内部
     * @param coords
     * @param coord
     * @return
     */
    private boolean isPointInPolygon(List<Coordinate> coords, Coordinate coord) {  
        int i, j;  
        boolean c = false;  
        for (i = 0, j = coords.size() - 1; i < coords.size(); j = i++) {  
            if ((((coords.get(i).x <= coord.x) && (coord.x < coords  
                    .get(j).x)) || ((coords.get(j).x <= coord.x) && (coord.x < coords  
                    .get(i).x)))  
                    && (coord.y < (coords.get(j).y - coords.get(i).y)  
                            * (coord.x - coords.get(i).x)  
                            / (coords.get(j).x - coords.get(i).x)  
                            + coords.get(i).y)) {  
                c = !c;  
            }  
        }  
        return c;  
    }
    
    public static void main(String[] args) {
    	AreaFenceHandler handler = new AreaFenceHandler();
    	List<String> strings = handler.areaIds(113.527576,22.152782);
    	System.out.println(strings.size());
    	for (String string : strings) {
			System.out.print(string+"|");
		}
//    	long st = System.currentTimeMillis();
//    	Random randomx = new Random(100);
//    	Random randomy = new Random(110);
//    	double xdot = randomx.nextDouble();
//    	double ydot = randomy.nextDouble();
//    	for (int i = 0; i < 100000; i++) {
//    		double x = 118-randomx.nextInt(50)+xdot;
//    		double y = 42-randomx.nextInt(20)+ydot;
//			List<String> strings = handler.areaIds(new Coordinate(x, y));
//			if (null != strings) {
//				
//				//System.out.println(strings.size());
//				if (strings.size() >10) {
//					System.out.println(x+","+y);
//					for (String string : strings) {
//						System.out.print(string+"|");
//					}
//					System.out.println();
//				}
//			} else {
////				System.out.println("x:"+x+",y:"+y);
//			}
//		}
//    	long ed = System.currentTimeMillis();
//    	
//    	System.out.println("time:"+(ed-st));
	}
}
