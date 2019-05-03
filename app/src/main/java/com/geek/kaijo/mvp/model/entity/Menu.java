package com.geek.kaijo.mvp.model.entity;

import java.io.Serializable;
import java.util.List;

public class Menu implements Serializable{
    private int menuId;
    private String menuName;
    private List<GridItemContent> menus;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public List<GridItemContent> getMenus() {
        return menus;
    }

    public void setMenus(List<GridItemContent> menus) {
        this.menus = menus;
    }

}
