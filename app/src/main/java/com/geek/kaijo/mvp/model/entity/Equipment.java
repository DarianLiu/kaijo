package com.geek.kaijo.mvp.model.entity;

public class Equipment {
    private String category;
    private String name;
    private String registerCode;
    private String checkIsValid;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public String getCheckIsValid() {
        return checkIsValid;
    }

    public void setCheckIsValid(String checkIsValid) {
        this.checkIsValid = checkIsValid;
    }
}
