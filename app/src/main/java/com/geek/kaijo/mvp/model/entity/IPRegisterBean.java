package com.geek.kaijo.mvp.model.entity;

public class IPRegisterBean {
    private String name;
    private String Status;
    private boolean checkes;

    public IPRegisterBean() {
    }

    public IPRegisterBean(String name, String status, boolean checkes) {
        this.name = name;
        Status = status;
        this.checkes = checkes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public boolean getCheckes() {
        return checkes;
    }

    public void setCheckes(boolean checkes) {
        this.checkes = checkes;
    }
}
