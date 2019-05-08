package com.geek.kaijo.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

@Entity
public class IPRegisterBean{
    private String name;
    private int Status;
    @Id
    private long thingPositionId;
    private int thingId;
    private int createUser;
    private double lat;
    private double lng;
    private int streetId;
    private int communityId;
    private int gridId;


    @Generated(hash = 681453242)
    public IPRegisterBean(String name, int Status, long thingPositionId,
            int thingId, int createUser, double lat, double lng, int streetId,
            int communityId, int gridId) {
        this.name = name;
        this.Status = Status;
        this.thingPositionId = thingPositionId;
        this.thingId = thingId;
        this.createUser = createUser;
        this.lat = lat;
        this.lng = lng;
        this.streetId = streetId;
        this.communityId = communityId;
        this.gridId = gridId;
    }

    @Generated(hash = 1953884383)
    public IPRegisterBean() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public long getThingPositionId() {
        return thingPositionId;
    }

    public void setThingPositionId(long thingPositionId) {
        this.thingPositionId = thingPositionId;
    }

    public int getThingId() {
        return thingId;
    }

    public void setThingId(int thingId) {
        this.thingId = thingId;
    }

    public int getCreateUser() {
        return createUser;
    }

    public void setCreateUser(int createUser) {
        this.createUser = createUser;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getStreetId() {
        return streetId;
    }

    public void setStreetId(int streetId) {
        this.streetId = streetId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }
}
