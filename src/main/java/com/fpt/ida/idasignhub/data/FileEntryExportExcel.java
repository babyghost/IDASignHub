package com.fpt.ida.idasignhub.data;

public class FileEntryExportExcel {
    private int namHoSo;
    private String hopSo;
    private String hoSoSo;
    private String tieuDeHoSo;
    private String ngayThangBDvaKT;
    private String soTo;
    private String thoiHanBaoQuan;
    private String ghiChu;

    public FileEntryExportExcel() {}

    public FileEntryExportExcel(String hopSo, String hoSoSo, String tieuDeHoSo, String ngayThangBDvaKT, String soTo, String thoiHanBaoQuan, String ghiChu, int namHoSo) {
        this.hopSo = hopSo;
        this.hoSoSo = hoSoSo;
        this.tieuDeHoSo = tieuDeHoSo;
        this.ngayThangBDvaKT = ngayThangBDvaKT;
        this.soTo = soTo;
        this.thoiHanBaoQuan = thoiHanBaoQuan;
        this.ghiChu = ghiChu;
        this.namHoSo = namHoSo;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getThoiHanBaoQuan() {
        return thoiHanBaoQuan;
    }

    public void setThoiHanBaoQuan(String thoiHanBaoQuan) {
        this.thoiHanBaoQuan = thoiHanBaoQuan;
    }

    public String getSoTo() {
        return soTo;
    }

    public void setSoTo(String soTo) {
        this.soTo = soTo;
    }

    public String getNgayThangBDvaKT() {
        return ngayThangBDvaKT;
    }

    public void setNgayThangBDvaKT(String ngayThangBDvaKT) {
        this.ngayThangBDvaKT = ngayThangBDvaKT;
    }

    public String getTieuDeHoSo() {
        return tieuDeHoSo;
    }

    public void setTieuDeHoSo(String tieuDeHoSo) {
        this.tieuDeHoSo = tieuDeHoSo;
    }

    public String getHoSoSo() {
        return hoSoSo;
    }

    public void setHoSoSo(String hoSoSo) {
        this.hoSoSo = hoSoSo;
    }

    public String getHopSo() {
        return hopSo;
    }

    public void setHopSo(String hopSo) {
        this.hopSo = hopSo;
    }

    public int getNamHoSo() {
        return namHoSo;
    }

    public void setNamHoSo(int namHoSo) {
        this.namHoSo = namHoSo;
    }

    @Override
    public String toString() {
        return "FileEntryExportExcel{" +
                "namHoSo=" + namHoSo +
                ", hopSo='" + hopSo + '\'' +
                ", hoSoSo='" + hoSoSo + '\'' +
                ", tieuDeHoSo='" + tieuDeHoSo + '\'' +
                ", ngayThangBDvaKT='" + ngayThangBDvaKT + '\'' +
                ", soTo='" + soTo + '\'' +
                ", thoiHanBaoQuan='" + thoiHanBaoQuan + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }
}
