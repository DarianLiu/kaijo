package com.geek.kaijo.mvp.model.entity;

import java.io.Serializable;

/**
 * 用户网格员信息
 * Created by LiuLi on 2019/1/28.
 */

public class UserInfo implements Serializable {

    /**
     * userId : 19
     * username : nsc_wgy1
     * streetId : 120
     * streetName : 老虎滩街道
     * communityId : 123
     * communityName : 中兴社区
     * gridId : 523
     * gridName : 第二网格
     */

    private String userId;
    private String username;
    private int streetId;
    private String streetName;
    private int communityId;
    private String communityName;
    private int gridId;
    private String gridName;

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
