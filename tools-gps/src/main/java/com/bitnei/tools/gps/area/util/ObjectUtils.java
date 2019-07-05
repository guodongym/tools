package com.bitnei.tools.gps.area.util;

import java.util.Collection;
import java.util.Map;

public class ObjectUtils {
	public static boolean isNullOrEmpty(String string){
		if(null == string || "".equals(string))
			return true;
		return "".equals(string.trim());
	}
	public static boolean isNullOrEmpty(Collection collection){
		if (null == collection || collection.size()==0) 
			return true;
		return false;
	}
	public static boolean isNullOrEmpty(Map map){
		if(map == null || map.size()==0)
			return true;
		return false;
	}
	public static boolean isNullOrEmpty(Object object){
		if(null == object)
			return true;
		if (object instanceof String) 
			return isNullOrEmpty((String)object);
		else if (object instanceof Map) 
			return isNullOrEmpty((Map)object);
		else if (object instanceof Collection) 
			return isNullOrEmpty((Collection)object);
		
		return isNullOrEmpty(object.toString());
	}

}
