package com.bitnei.tools.gps.area.handle;

import com.bitnei.tools.gps.area.util.GpsUtil;
import com.bitnei.tools.gps.area.util.ObjectUtils;
import com.bitnei.tools.gps.area.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取Region入口类
 *
 * @author zhaogd
 * @date 2018/11/28
 */
public class RegionUtil {
    /**
     * 无效区域，如果经纬度无效则赋值为无效区域
     */
    public static final String INVALID_REGION = "INVALID";
    /**
     * 未知区域，在围栏中找不到对应区域赋值为未知区域
     */
    public static final String UNKNOWN_REGION = "UNKNOWN";

    /**
     * 车辆最后里程的缓存
     */
    private static Map<String, Double> lastMileageMap = new ConcurrentHashMap<>();
    /**
     * 车辆最后GPS坐标的缓存
     */
    private static Map<String, double[]> lastGPSMap = new ConcurrentHashMap<>();
    /**
     * 车辆最后行政区域编码的缓存
     */
    private static Map<String, String> lastRegionMap = new ConcurrentHashMap<>();
    private static AreaFenceHandler areaHandler = new AreaFenceHandler();


    /**
     * <tt>根据GPS获取行政区域编码</tt>
     * <p>
     * 如果经纬度无效则返回{@link RegionUtil#INVALID_REGION}的值，无效的判断规则为null、0、""、非数字
     * <p>
     * 如果经纬度有效但是不在围栏范围内则返回{@link RegionUtil#UNKNOWN_REGION}的值，判断规则为0 <= 经纬度 <= 180并且在围栏中可以查找到，目前围栏范围只有中国
     * <p>
     * 其他情况正常返回编码，示例：<code>130000|131000|131081</code>
     *
     * @param vid       车辆唯一标识
     * @param longitude 经度
     * @param latitude  纬度
     * @param currentMileage   里程(km)
     * @return 行政区域编码，多级区域以竖线<code>|</code>隔开
     */
    public static String getRegion(String vid, double longitude, double latitude, Double currentMileage) {
        // 获取缓存的最后区域
        String lastRegion = lastRegionMap.get(vid);
        if (!ObjectUtils.isNullOrEmpty(lastRegion)) {

            double[] gpsArray = lastGPSMap.get(vid);
            Double lastMileage = lastMileageMap.get(vid);
            // 判断经纬度是否变化，与当前经纬度的距离不超过5公里视为无变化
            final boolean gpsIsNotMove = gpsArray != null && GpsUtil.getDistance(longitude, latitude, gpsArray[0], gpsArray[1]) < 5;
            // 判断里程是否变化，与当前里程差值不超过5公里视为无变化
            final boolean mileageIsNotMove = lastMileage != null && 0 != currentMileage && Math.abs(currentMileage - lastMileage) < 5;
            // 同时满足GPS和里程都没有变化，则返回缓存的区域编码不进行计算
            if (gpsIsNotMove && mileageIsNotMove) {
                return lastRegion;
            }
        }

        // 根据GPS获取区域编码
        List<String> areaIds = areaHandler.areaIds(longitude, latitude);
        if (areaIds == null || areaIds.isEmpty()) {
            return UNKNOWN_REGION;
        }

        // 拼接区域编码
        final String areas = StringUtils.join(areaIds, "|");
        // 更新缓存值
        lastRegionMap.put(vid, areas);
        lastGPSMap.put(vid, new double[]{longitude, latitude});
        lastMileageMap.put(vid, currentMileage);
        return areas;
    }
}
