package com.fpt.ida.idasignhub.data;

public class SKHCNTrackingData {
    String hopSo;
    String hoSoSo;
    String tieuDeHoSo;
    String ghiChu;
    String thoiGian;
    String tenTep;
    String loai;
    String tep;
    String toSo;
    String soTo;

    public SKHCNTrackingData() {}

    public SKHCNTrackingData(String hopSo, String hoSoSo, String tieuDeHoSo, String ghiChu, String thoiGian, String tenTep, String loai, String tep, String toSo, String soTo) {
        this.hopSo = hopSo;
        this.hoSoSo = hoSoSo;
        this.tieuDeHoSo = tieuDeHoSo;
        this.ghiChu = ghiChu;
        this.thoiGian = thoiGian;
        this.tenTep = tenTep;
        this.loai = loai;
        this.tep = tep;
        this.toSo = toSo;
        this.soTo = soTo;
    }

    public String getHopSo() {
        return hopSo;
    }

    public void setHopSo(String hopSo) {
        this.hopSo = hopSo;
    }

    public String getHoSoSo() {
        return hoSoSo;
    }

    public void setHoSoSo(String hoSoSo) {
        this.hoSoSo = hoSoSo;
    }

    public String getTieuDeHoSo() {
        return tieuDeHoSo;
    }

    public void setTieuDeHoSo(String tieuDeHoSo) {
        this.tieuDeHoSo = tieuDeHoSo;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getTenTep() {
        return tenTep;
    }

    public void setTenTep(String tenTep) {
        this.tenTep = tenTep;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getTep() {
        return tep;
    }

    public void setTep(String tep) {
        this.tep = tep;
    }

    public String getToSo() {
        return toSo;
    }

    public void setToSo(String toSo) {
        this.toSo = toSo;
    }

    public String getSoTo() {
        return soTo;
    }

    public void setSoTo(String soTo) {
        this.soTo = soTo;
    }

    @Override
    public String toString() {
        return "SKHCNTrackingData{" +
                "hopSo='" + hopSo + '\'' +
                ", hoSoSo='" + hoSoSo + '\'' +
                ", tieuDeHoSo='" + tieuDeHoSo + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                ", thoiGian='" + thoiGian + '\'' +
                ", tenTep='" + tenTep + '\'' +
                ", loai='" + loai + '\'' +
                ", tep='" + tep + '\'' +
                ", toSo='" + toSo + '\'' +
                ", soTo='" + soTo + '\'' +
                '}';
    }
}
