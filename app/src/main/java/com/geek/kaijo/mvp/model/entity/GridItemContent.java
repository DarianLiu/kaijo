package com.geek.kaijo.mvp.model.entity;

import java.io.Serializable;

public class GridItemContent implements Serializable{
    private int thingId;
    private String name;
    private String iconUrl;

    public int getThingId() {
        return thingId;
    }

    public void setThingId(int thingId) {
        this.thingId = thingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
