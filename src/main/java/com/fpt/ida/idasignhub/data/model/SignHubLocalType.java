package com.fpt.ida.idasignhub.data.model;

import java.io.Serializable;

public class SignHubLocalType implements Serializable {
    private static final long serialVersionUID = 1L;
    String nameSignHubLocalType;
    String pathImageSignHubLocalType;
    String showInfoSignHubLocalType;
    int pageNumberSignHubLocalType;
    String locationSignHubLocalType;
    Boolean trangThaiHoatDong;

    public Boolean getTrangThaiHoatDong() {
        return trangThaiHoatDong;
    }

    public void setTrangThaiHoatDong(Boolean trangThaiHoatDong) {
        this.trangThaiHoatDong = trangThaiHoatDong;
    }

    public String getNameSignHubLocalType() {
        return nameSignHubLocalType;
    }

    public void setNameSignHubLocalType(String nameSignHubLocalType) {
        this.nameSignHubLocalType = nameSignHubLocalType;
    }

    public String getPathImageSignHubLocalType() {
        return pathImageSignHubLocalType;
    }

    public void setPathImageSignHubLocalType(String pathImageSignHubLocalType) {
        this.pathImageSignHubLocalType = pathImageSignHubLocalType;
    }

    public String getShowInfoSignHubLocalType() {
        return showInfoSignHubLocalType;
    }

    public void setShowInfoSignHubLocalType(String showInfoSignHubLocalType) {
        this.showInfoSignHubLocalType = showInfoSignHubLocalType;
    }

    public int getPageNumberSignHubLocalType() {
        return pageNumberSignHubLocalType;
    }

    public void setPageNumberSignHubLocalType(int pageNumberSignHubLocalType) {
        this.pageNumberSignHubLocalType = pageNumberSignHubLocalType;
    }

    public String getLocationSignHubLocalType() {
        return locationSignHubLocalType;
    }

    public void setLocationSignHubLocalType(String locationSignHubLocalType) {
        this.locationSignHubLocalType = locationSignHubLocalType;
    }

    @Override
    public String toString() {
        return "SignHubLocalType{" +
                "nameSignHubLocalType='" + nameSignHubLocalType + '\'' +
                ", pathImageSignHubLocalType='" + pathImageSignHubLocalType + '\'' +
                ", showInfoSignHubLocalType='" + showInfoSignHubLocalType + '\'' +
                ", pageNumberSignHubLocalType=" + pageNumberSignHubLocalType +
                ", locationSignHubLocalType='" + locationSignHubLocalType + '\'' +
                '}';
    }
}
