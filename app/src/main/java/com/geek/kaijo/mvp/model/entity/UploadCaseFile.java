package com.geek.kaijo.mvp.model.entity;

public class UploadCaseFile {

    private int caseId;
    private int caseProcessRecordId; //0 采集提交 1案件结案
    private int whenType; //1整改前，2整改后，3附件
    private int fileType;// 0照片 1视频  2其它文件
    private int handleType;////直接处理传1 ，非直接处理传2
    private String url;

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int getCaseProcessRecordId() {
        return caseProcessRecordId;
    }

    public void setCaseProcessRecordId(int caseProcessRecordId) {
        this.caseProcessRecordId = caseProcessRecordId;
    }

    public int getWhenType() {
        return whenType;
    }

    public void setWhenType(int whenType) {
        this.whenType = whenType;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public void setHandleType(int handleType) {
        this.handleType = handleType;
    }

    public int getHandleType() {
        return handleType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
