package com.geek.kaijo.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * 案件
 * Created by LiuLi on 2018/9/8.
 */

public class Case implements Parcelable{

    private String caseId;
    private String source;
    private String username;
    private String mobile;
    private String address;
    private String streetName;
    private String communityName;
    private String gridName;
    private String caseAttribute;
    private String casePrimaryCategory;
    private String caseSecondaryCategory;
    private String caseChildCategory;
    private String acceptDate;
    private String description;
    private String caseCode;
    private List<Attach> attachList;

    private String processId;
    private String userId;
    private String firstWorkunit;
    private List<ButtonLabel> buttonList;
    private Double lat;
    private Double lng;

    protected Case(Parcel in) {
        caseId = in.readString();
        source = in.readString();
        username = in.readString();
        mobile = in.readString();
        address = in.readString();
        streetName = in.readString();
        communityName = in.readString();
        gridName = in.readString();
        caseAttribute = in.readString();
        casePrimaryCategory = in.readString();
        caseSecondaryCategory = in.readString();
        caseChildCategory = in.readString();
        acceptDate = in.readString();
        description = in.readString();
        caseCode = in.readString();
        processId = in.readString();
        userId = in.readString();
        firstWorkunit = in.readString();
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            lng = null;
        } else {
            lng = in.readDouble();
        }
    }

    public static final Creator<Case> CREATOR = new Creator<Case>() {
        @Override
        public Case createFromParcel(Parcel in) {
            return new Case(in);
        }

        @Override
        public Case[] newArray(int size) {
            return new Case[size];
        }
    };

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public String getCaseAttribute() {
        return caseAttribute;
    }

    public void setCaseAttribute(String caseAttribute) {
        this.caseAttribute = caseAttribute;
    }

    public String getCasePrimaryCategory() {
        return casePrimaryCategory;
    }

    public void setCasePrimaryCategory(String casePrimaryCategory) {
        this.casePrimaryCategory = casePrimaryCategory;
    }

    public String getCaseSecondaryCategory() {
        return caseSecondaryCategory;
    }

    public void setCaseSecondaryCategory(String caseSecondaryCategory) {
        this.caseSecondaryCategory = caseSecondaryCategory;
    }

    public String getCaseChildCategory() {
        return caseChildCategory;
    }

    public void setCaseChildCategory(String caseChildCategory) {
        this.caseChildCategory = caseChildCategory;
    }

    public String getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(String acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    public void setAttachList(List<Attach> attachList) {
        this.attachList = attachList;
    }

    public List<Attach> getAttachList() {
        return attachList;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstWorkunit() {
        return firstWorkunit;
    }

    public void setFirstWorkunit(String firstWorkunit) {
        this.firstWorkunit = firstWorkunit;
    }

    public List<ButtonLabel> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<ButtonLabel> buttonList) {
        this.buttonList = buttonList;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(caseId);
        parcel.writeString(source);
        parcel.writeString(username);
        parcel.writeString(mobile);
        parcel.writeString(address);
        parcel.writeString(streetName);
        parcel.writeString(communityName);
        parcel.writeString(gridName);
        parcel.writeString(caseAttribute);
        parcel.writeString(casePrimaryCategory);
        parcel.writeString(caseSecondaryCategory);
        parcel.writeString(caseChildCategory);
        parcel.writeString(acceptDate);
        parcel.writeString(description);
        parcel.writeString(caseCode);
        parcel.writeString(processId);
        parcel.writeString(userId);
        parcel.writeString(firstWorkunit);
        if (lat == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(lat);
        }
        if (lng == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(lng);
        }
    }
}
