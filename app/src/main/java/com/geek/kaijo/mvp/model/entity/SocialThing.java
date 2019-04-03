package com.geek.kaijo.mvp.model.entity;

/**
 * 社会治理物件点位
 * Created by LiuLi on 2019/1/28.
 */

public class SocialThing {

    /**
     * currPage : 1
     * pageSize : 10
     * thingId : 197
     * createTime : 1547636215000
     * createUser : 4
     * modifyTime : null
     * modifyUser : null
     * introUrl : null
     * introTitle : test
     * intro : <p>5345345</p>
     * <p>
     * delFlag : 2
     * lat : 5435
     * lng : 43543
     * name : 54543543
     * remark :
     * iconUrl : filePath/2018/0809/a4759e21d9614f6ea83492fc91ea69e4.jpg
     * streetId : 120
     * communityId : 121
     * gridId : null
     */

    private String thingId;
    private long createTime;
    private String createUser;
    private String introUrl;
    private String introTitle;
    private String intro;
    private int delFlag;
    private String lat;
    private String lng;
    private String name;
    private String remark;
    private String iconUrl;
    private long streetId;
    private long communityId;
    private long gridId;

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

    public String getIntroUrl() {
        return introUrl;
    }

    public void setIntroUrl(String introUrl) {
        this.introUrl = introUrl;
    }

    public String getIntroTitle() {
        return introTitle;
    }

    public void setIntroTitle(String introTitle) {
        this.introTitle = introTitle;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public long getStreetId() {
        return streetId;
    }

    public void setStreetId(long streetId) {
        this.streetId = streetId;
    }

    public long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(long communityId) {
        this.communityId = communityId;
    }

    public long getGridId() {
        return gridId;
    }

    public void setGridId(long gridId) {
        this.gridId = gridId;
    }
}
