package com.bitnei.tools.common;


import com.bitnei.tools.entity.Gps;

/**
 * 各地图API坐标系统比较与转换;
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
 * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
 * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。
 * <p>
 * 互联网在线地图使用的坐标系
 * <p>
 * 火星坐标系：
 * iOS 地图（其实是高德）
 * Gogole地图
 * 搜搜、阿里云、高德地图
 * 百度坐标系：
 * 当然只有百度地图
 * WGS84坐标系：
 * 国际标准，谷歌国外地图、osm地图等国外的地图一般都是这个
 *
 * @author zhaogd
 */
public class PositionUtil {

    private static double xpi = 3.14159265358979324 * 3000.0 / 180.0;
    private static double pi = 3.1415926535897932384626;
    private static double a = 6378245.0;
    private static double ee = 0.00669342162296594323;

    /**
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
     */
    public static Gps gps84ToGcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return null;
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new Gps(mgLat, mgLon);
    }

    /**
     * 火星坐标系 (GCJ-02) to 84
     */
    public static Gps gcj02ToGps84(double lat, double lon) {
        Gps gps = transform(lat, lon);
        double lontitude = lon * 2 - gps.getWgLon();
        double latitude = lat * 2 - gps.getWgLat();
        return new Gps(latitude, lontitude);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     */
    public static Gps gcj02ToBd09(double lat, double lon) {
        double z = Math.sqrt(lon * lon + lat * lat) + 0.00002 * Math.sin(lat * xpi);
        double theta = Math.atan2(lat, lon) + 0.000003 * Math.cos(lon * xpi);
        double bdLon = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new Gps(bdLat, bdLon);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法
     * 将 BD-09 坐标转换成GCJ-02 坐标
     */
    public static Gps bd09ToGcj02(double bdLat, double bdLon) {
        double x = bdLon - 0.0065, y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * xpi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * xpi);
        double lon = z * Math.cos(theta);
        double lat = z * Math.sin(theta);
        return new Gps(lat, lon);
    }

    /**
     * (BD-09)-->84
     */
    public static Gps bd09ToGps84(double lat, double lon) {
        Gps gcj02 = PositionUtil.bd09ToGcj02(lat, lon);
        return PositionUtil.gcj02ToGps84(gcj02.getWgLat(), gcj02.getWgLon());
    }

    /**
     * (84)-->BD-09
     */
    public static Gps gps84ToBd09(double lat, double lon) {
        Gps gcj02 = PositionUtil.gps84ToGcj02(lat, lon);
        if (gcj02 == null) {
            return null;
        }
        return PositionUtil.gcj02ToBd09(gcj02.getWgLat(), gcj02.getWgLon());
    }


    public static boolean outOfChina(double lat, double lon) {
        double minLon = 72.004;
        double maxLon = 137.8347;
        double minLat = 0.8293;
        double maxLat = 55.8271;

        return lon < minLon || lon > maxLon || lat < minLat || lat > maxLat;
    }

    public static Gps transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new Gps(lat, lon);
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new Gps(mgLat, mgLon);
    }

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }

    public static void main(String[] args) {

        // 北斗芯片获取的经纬度为WGS84地理坐标 31.164601,121.486063
        Gps gps = new Gps(31.164601, 121.486063);
        System.out.println("wgs84 :" + gps);
        Gps gcj = gps84ToGcj02(gps.getWgLat(), gps.getWgLon());
        System.out.println("gcj02:" + gcj);
        assert gcj != null;
        Gps star = gcj02ToGps84(gcj.getWgLat(), gcj.getWgLon());
        System.out.println("wgs84:" + star);
        Gps bd = gcj02ToBd09(gcj.getWgLat(), gcj.getWgLon());
        System.out.println("bd09:" + bd);
        Gps gcj2 = bd09ToGcj02(bd.getWgLat(), bd.getWgLon());
        System.out.println("gcj02:" + gcj2);
    }
}
