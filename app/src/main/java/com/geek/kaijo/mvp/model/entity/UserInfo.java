package com.geek.kaijo.mvp.model.entity;

import java.io.Serializable;

/**
 * 用户网格员信息
 * Created by LiuLi on 2019/1/28.
 */

public class UserInfo implements Serializable {


    private String userId;
    private String username;
    private int streetId;  //街道ID
    private String streetName;
    private int communityId; //社区ID
    private String communityName;   //社区名
    private int gridId;
    private String gridName;  //网格名 第二网格

    private String trueName; //真实姓名
    private String email;
    private String mobile; //手机号
    private String deptId;
    private int areaStreetCommunityId;  //街道ID
    private String phone; //座机号
    private String address;//地址
    private String idcard; //身份证号码
    private String deviceSim; //sim编号
    private String devicePhone; //终端电话号码
    private String deviceSn;  //设备编号
    private String headUrl;  //头像

    public int getAreaStreetCommunityId() {
        return areaStreetCommunityId;
    }

    public void setAreaStreetCommunityId(int areaStreetCommunityId) {
        this.areaStreetCommunityId = areaStreetCommunityId;
        this.streetId = areaStreetCommunityId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getDeviceSim() {
        return deviceSim;
    }

    public void setDeviceSim(String deviceSim) {
        this.deviceSim = deviceSim;
    }

    public String getDevicePhone() {
        return devicePhone;
    }

    public void setDevicePhone(String devicePhone) {
        this.devicePhone = devicePhone;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStreetId() {
        return streetId;
    }

    public void setStreetId(int streetId) {
        this.streetId = streetId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }
}
