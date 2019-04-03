package com.geek.kaijo.mvp.model.entity;

import java.util.List;

/**
 * 案件属性
 * Created by LiuLi on 2018/9/5.
 */

public class CaseAttribute {

    private String categoryId;
    private String pCategoryId;
    private String text;
    private List<CaseAttribute> childList;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPCategoryId() {
        return pCategoryId;
    }

    public void setPCategoryId(String pCategoryId) {
        this.pCategoryId = pCategoryId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<CaseAttribute> getChildList() {
        return childList;
    }

    public void setChildList(List<CaseAttribute> childList) {
        this.childList = childList;
    }

    public String toString() {
        return getText();
    }
}
