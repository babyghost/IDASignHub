package com.fpt.ida.idasignhub.data;

public class LocationSignConvertXY {
    float xLocation;
    float yLocation;

    public float getxLocation() {
        return xLocation;
    }

    public void setxLocation(float xLocation) {
        this.xLocation = xLocation;
    }

    public float getyLocation() {
        return yLocation;
    }

    public void setyLocation(float yLocation) {
        this.yLocation = yLocation;
    }

    public LocationSignConvertXY(String locationSign, float heightPage, float widthPage) {
        if(locationSign.equals("tren_trai")) {
            float x = 180; //cach le 20;
            float y = 20; //cach le 20
            this.xLocation = x;
            this.yLocation = y;
        }else if(locationSign.equals("tren_phai")) {
            float x = widthPage - 180; //cach le 20; chua tinh toi width block chu ky
            float y = 20; //cach le 20
            this.xLocation = x;
            this.yLocation = y;
        }else if(locationSign.equals("duoi_trai")) {
            float x = 180; //cach le 20; chua tinh toi width block chu ky
            float y = heightPage - 20; //cach le 20
            this.xLocation = x;
            this.yLocation = y;
        }else if(locationSign.equals("duoi_phai")) {
            float x = widthPage - 180; //cach le 20; chua tinh toi width block chu ky
            float y = heightPage - 20; //cach le 20
            this.xLocation = x;
            this.yLocation = y;
        }
    }
    // danh cho PDF
    public LocationSignConvertXY(String locationSign, float heightPage, float widthPage, float heightObject, float widthObject) {
        if(locationSign.equals("tren_trai")) {
            float x = 10 ; //cach le 20;
            float y = 10; //cach le 20
            this.xLocation = widthObject + x;
            this.yLocation = heightPage - heightObject - y;
        }else if(locationSign.equals("tren_phai")) {
            float x = 10; //cach le 20;
            float y = 10; //cach le 20
            this.xLocation = widthPage - widthObject - x;
            this.yLocation = heightPage - heightObject - y;
        }else if(locationSign.equals("duoi_trai")) {
            float x = 10; //cach le 20; chua tinh toi width block chu ky
            float y = 10; //cach le 20
            this.xLocation = widthObject + x;
            this.yLocation = heightObject + y;
        }else if(locationSign.equals("duoi_phai")) {
            float x = 10; //cach le 20; chua tinh toi width block chu ky
            float y = 10; //cach le 20
            this.xLocation = widthPage - widthObject - x;
            this.yLocation = heightObject + y;
        }
    }
}
