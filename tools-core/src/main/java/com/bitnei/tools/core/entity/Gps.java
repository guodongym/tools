package com.bitnei.tools.core.entity;

/**
 * Created by IntelliJ IDEA.
 *
 * @author zhaogd
 * Date: 2017/8/3
 */
public class Gps {

    private double wgLat;
    private double wgLon;

    public Gps() {
    }

    public Gps(double wgLat, double wgLon) {
        this.wgLat = wgLat;
        this.wgLon = wgLon;
    }

    public double getWgLat() {
        return wgLat;
    }

    public void setWgLat(double wgLat) {
        this.wgLat = wgLat;
    }

    public double getWgLon() {
        return wgLon;
    }

    public void setWgLon(double wgLon) {
        this.wgLon = wgLon;
    }

    @Override
    public String toString() {
        return wgLat + "," + wgLon;
    }
}
