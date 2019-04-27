package com.geek.kaijo.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class UploadFile implements Parcelable{
    public static final int width = 340;
    public static final int height = 600;
    private String fileName;   //文件本地路径
    private String fileRelativePath;
    private String url;
    private String fileDomain;
    private String fileSize;
    private int isSuccess; // 1 上传成功
    public int selectStatus;//是否选中  0未选择 1选择
    public int caseProcessRecordId = 11;  //节点ID
    public int whenType = 1;    // 1整改前  2整改后
    public int fileType = 0;   //0 图片  视频 1

    public UploadFile(){}

    protected UploadFile(Parcel in) {
        fileName = in.readString();
        fileRelativePath = in.readString();
        fileDomain = in.readString();
        fileSize = in.readString();
        isSuccess = in.readInt();
        selectStatus = in.readInt();
    }

    public static final Creator<UploadFile> CREATOR = new Creator<UploadFile>() {
        @Override
        public UploadFile createFromParcel(Parcel in) {
            return new UploadFile(in);
        }

        @Override
        public UploadFile[] newArray(int size) {
            return new UploadFile[size];
        }
    };

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileRelativePath() {
        return fileRelativePath;
    }

    public void setFileRelativePath(String fileRelativePath) {
        this.fileRelativePath = fileRelativePath;
        this.url = fileRelativePath;
    }

    public String getFileDomain() {
        return fileDomain;
    }

    public void setFileDomain(String fileDomain) {
        this.fileDomain = fileDomain;
    }
    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fileName);
        parcel.writeString(fileRelativePath);
        parcel.writeString(fileDomain);
        parcel.writeString(fileSize);
        parcel.writeInt(isSuccess);
        parcel.writeInt(selectStatus);
    }
}
