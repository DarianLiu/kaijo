package com.geek.kaijo.mvp.model.entity;

/**
 * 巡查项
 * Created by LiuLi on 2018/11/6.
 */
public class Thing {

    /**
     * currPage : 1
     * pageSize : 10
     * thingId : 39
     * createTime : 1540165267000
     * createUser : 4
     * modifyTime : 1540165267000
     * modifyUser : 4
     * delFlag : 2
     * lat : 11.0
     * lng : 11.0
     * name : 2
     * remark : 11
     */


    private String thingId;
    private long createTime;
    private String createUser;
    private long modifyTime;
    private String modifyUser;
    private int delFlag;
    private double lat;
    private double lng;
    private String name;
    private String remark;

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
