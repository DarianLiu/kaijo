package com.geek.kaijo.mvp.model.entity;

/**
 * 用户信息
 * Created by LiuLi on 2018/9/4.
 */

public class UploadTest {
   private String token;

    private String id;

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
}
