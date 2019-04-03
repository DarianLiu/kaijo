package com.geek.kaijo.mvp.model.event;

/**
 * 惠民服务、扶持政策
 * Created by LiuLi on 2019/1/26.
 */

public class ServiceEvent {
    private int categoryId;//文章分类菜单id
    private String key;//搜索关键字

    public ServiceEvent(int categoryId, String key) {
        this.categoryId = categoryId;
        this.key = key;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
