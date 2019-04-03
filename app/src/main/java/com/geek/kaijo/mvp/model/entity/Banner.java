package com.geek.kaijo.mvp.model.entity;

/**
 * Created by LiuLi on 2018/10/27.
 */

public class Banner {

    /**
     * currPage : 1
     * pageSize : 10
     * bannerId : 4
     * title : xx
     * url : filePath/2018/1017/e742b84e88ec48aeab8d3fbc2023d00a.jpeg
     * bannerType : null
     * createTime : 1539772304000
     * createUser : 4
     * modifyTime : 1539772304000
     * modifyUser : 4
     */

    private int currPage;
    private int pageSize;
    private int bannerId;
    private String title;
    private String url;
    private Object bannerType;
    private long createTime;
    private int createUser;
    private long modifyTime;
    private int modifyUser;

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getBannerType() {
        return bannerType;
    }

    public void setBannerType(Object bannerType) {
        this.bannerType = bannerType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getCreateUser() {
        return createUser;
    }

    public void setCreateUser(int createUser) {
        this.createUser = createUser;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(int modifyUser) {
        this.modifyUser = modifyUser;
    }
}
