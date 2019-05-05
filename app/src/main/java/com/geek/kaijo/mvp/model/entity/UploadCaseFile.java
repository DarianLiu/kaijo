package com.geek.kaijo.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UploadCaseFile {

    @Id
    private int caseId;
    private int caseProcessRecordId; //0 采集提交 1案件结案
    private int whenType; //1整改前，2整改后，3附件
    private int fileType;// 0照片 1视频  2其它文件
    private int handleType;//直接处理传1 ，非直接处理传2
    private String url;
    private String fileName;
    private int isSuccess; // 1 上传成功

    @Generated(hash = 253611125)
    public UploadCaseFile(int caseId, int caseProcessRecordId, int whenType,
            int fileType, int handleType, String url, String fileName,
            int isSuccess) {
        this.caseId = caseId;
        this.caseProcessRecordId = caseProcessRecordId;
        this.whenType = whenType;
        this.fileType = fileType;
        this.handleType = handleType;
        this.url = url;
        this.fileName = fileName;
        this.isSuccess = isSuccess;
    }

    @Generated(hash = 1732003389)
    public UploadCaseFile() {
    }

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}
