package com.geek.kaijo.mvp.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 街道社区
 * Created by LiuLi on 2018/9/5.
 */

public class Street implements Serializable{

    /**
     * id : 1
     * parentId : 0
     * name : 和平区
     * childList : [{"id":2,"parentId":1,"name":"八经街道","childList":[{"id":21,"parentId":2,"name":"宝环社区","childList":[]},{"id":22,"parentId":2,"name":"桂林社区","childList":[]},{"id":23,"parentId":2,"name":"和平新村社区","childList":[]},{"id":24,"parentId":2,"name":"红塔社区","childList":[]},{"id":25,"parentId":2,"name":"沈电社区","childList":[]},{"id":26,"parentId":2,"name":"延安里社区","childList":[]}]},{"id":3,"parentId":1,"name":"北市场街道","childList":[{"id":15,"parentId":3,"name":"北市社区","childList":[]},{"id":16,"parentId":3,"name":"大庆路社区","childList":[]},{"id":17,"parentId":3,"name":"皇寺路社区","childList":[]},{"id":18,"parentId":3,"name":"民富社区","childList":[]},{"id":19,"parentId":3,"name":"市府路社区","childList":[]},{"id":20,"parentId":3,"name":"总站路社区","childList":[]}]},{"id":4,"parentId":1,"name":"浑河湾街道","childList":[]},{"id":5,"parentId":1,"name":"浑河站西街道","childList":[]},{"id":6,"parentId":1,"name":"集贤街道","childList":[]},{"id":7,"parentId":1,"name":"马路湾街道","childList":[]},{"id":8,"parentId":1,"name":"南湖街道","childList":[]},{"id":9,"parentId":1,"name":"南市场街道","childList":[]},{"id":10,"parentId":1,"name":"沈水湾街道","childList":[]},{"id":11,"parentId":1,"name":"太原街街道","childList":[]},{"id":12,"parentId":1,"name":"西塔街道","childList":[]},{"id":13,"parentId":1,"name":"新华街道","childList":[]},{"id":14,"parentId":1,"name":"长白街道","childList":[]}]
     */

    private String id;
    private String parentId;
    private String name;
    private List<Street> childList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Street> getChildList() {
        return childList;
    }

    public void setChildList(List<Street> childList) {
        this.childList = childList;
    }

    public String toString() {
        return getName();
    }

}
