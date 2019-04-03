package com.geek.kaijo.mvp.model.entity;

/**
 * 服务（营商、惠民服务基础类）
 * Created by LiuLi on 2019/1/24.
 */

public class ServiceBean {

    private String articleId;
    private String title;
    private String content;
    private String categoryId;
    private long createTime;
    private String modifyTime;
    private String modifyUser;
    private String delFalg;
    private String categoryName;

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setDelFalg(String delFalg) {
        this.delFalg = delFalg;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getContent() {
        return content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getDelFalg() {
        return delFalg;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public String getTitle() {
        return title;
    }
}
