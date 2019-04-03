package com.geek.kaijo.mvp.model.event;

/**
 * 定位
 * Created by LiuLi on 2018/12/15.
 */

public class LocationEvent {
    private double lng;
    private double lat;

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }
}
