package com.geek.kaijo.mvp.model.entity;

import java.io.Serializable;

public class InspentionResult implements Serializable{
    private int pathId;
    private int  userId;
    private long startTime;
    private long endTime;
    private int state;
    private long createTime;
//    private int delFlag;


    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
