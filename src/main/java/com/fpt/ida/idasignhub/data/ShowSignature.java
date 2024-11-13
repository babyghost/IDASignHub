package com.fpt.ida.idasignhub.data;

public class ShowSignature {
    int showType; // 0 = ImageAndInfo, 1 = Image, 2 = Info
    String showInfoExtends; //INFOLABEL_EMAIL_ORGANIZATION_TIMESTAMP

    public String getShowInfoExtends() {
        return showInfoExtends;
    }

    public void setShowInfoExtends(String showInfoExtends) {
        this.showInfoExtends = showInfoExtends;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    @Override
    public String toString() {
        return "ShowSignature{" +
                "showType=" + showType +
                ", showInfoExtends='" + showInfoExtends + '\'' +
                '}';
    }
}
