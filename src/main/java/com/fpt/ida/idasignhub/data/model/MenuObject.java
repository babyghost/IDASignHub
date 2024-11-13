package com.fpt.ida.idasignhub.data.model;

public class MenuObject {
    private String url; // trang-chu, path config in controller
    private String title; // Trang chủ, title menu or title action
    private String role;
    private int active;  //true = menu active, false = menu non active
    private int treeview;
    private int selecter;
    // List<MenuObject> child; // list child menu

    public int getSelecter() {
        return this.selecter;
    }

    public void setSelecter(int selecter) {
        this.selecter = selecter;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int isActive() {
        return this.active;
    }
    public int isTreeview() {
        return this.treeview;
    }

    public int getActive() {
        return this.active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getTreeview() {
        return this.treeview;
    }

    public void setTreeview(int treeview) {
        this.treeview = treeview;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "{" +
            ", url='" + getUrl() + "'" +
            ", title='" + getTitle() + "'" +
            ", active='" + isActive() + "'" +
            ", treeview='" + isTreeview() + "'" +
            "}";
    }

}
