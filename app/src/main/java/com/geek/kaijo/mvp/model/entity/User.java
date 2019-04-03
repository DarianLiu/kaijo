package com.geek.kaijo.mvp.model.entity;

import java.io.Serializable;

/**
 * 用户信息
 * Created by LiuLi on 2018/9/4.
 */

public class User implements Serializable{
    private String token;

    private String id;
    /**
     * userId : 4
     * username : admin
     * email : admin@admin.com
     * mobile : 13800000001
     * status : 2
     * deptId : 0
     * createTime : null
     */

    private String userId;
    private String username;
    private String email;
    private String mobile;
    private int status;
    private int deptId;
    private String createTime;

    public void setId(String id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
