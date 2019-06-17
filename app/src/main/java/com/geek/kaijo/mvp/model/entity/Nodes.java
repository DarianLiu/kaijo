package com.geek.kaijo.mvp.model.entity;


import java.io.Serializable;

/**
 * 笔记本
 */
public class Nodes implements Serializable {

    private long notepadId;
    private String content;
    private long createTime;
    private long modifyTime;
    private String userId;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getNotepadId() {
        return notepadId;
    }

    public void setNotepadId(long notepadId) {
        this.notepadId = notepadId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
