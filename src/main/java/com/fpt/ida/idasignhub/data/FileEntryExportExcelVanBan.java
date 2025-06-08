package com.fpt.ida.idasignhub.data;

public class FileEntryExportExcelVanBan {
    private String soTo;
    private String loai;
    private String tenVanBan;
    private String ngayThang;
    private String tacGia;
    private String trichYeu;
    private String toSo;
    private String duongDanMoi;
    private String doMat;
    private String mucDoTinCay;
    private String tinhTrangVatLy;

    public FileEntryExportExcelVanBan() {}
    public FileEntryExportExcelVanBan(String soTo, String loai, String tenVanBan, String ngayThang, String tacGia, String trichYeu, String toSo, String duongDanMoi) {
        this.soTo = soTo;
        this.loai = loai;
        this.tenVanBan = tenVanBan;
        this.ngayThang = ngayThang;
        this.tacGia = tacGia;
        this.trichYeu = trichYeu;
        this.toSo = toSo;
        this.duongDanMoi = duongDanMoi;
    }

    public String getSoTo() {
        return soTo;
    }

    public void setSoTo(String soTo) {
        this.soTo = soTo;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getTenVanBan() {
        return tenVanBan;
    }

    public void setTenVanBan(String tenVanBan) {
        this.tenVanBan = tenVanBan;
    }

    public String getNgayThang() {
        return ngayThang;
    }

    public void setNgayThang(String ngayThang) {
        this.ngayThang = ngayThang;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getTrichYeu() {
        return trichYeu;
    }

    public void setTrichYeu(String trichYeu) {
        this.trichYeu = trichYeu;
    }

    public String getToSo() {
        return toSo;
    }

    public void setToSo(String toSo) {
        this.toSo = toSo;
    }

    public String getDuongDanMoi() {
        return duongDanMoi;
    }

    public void setDuongDanMoi(String duongDanMoi) {
        this.duongDanMoi = duongDanMoi;
    }

    public String getDoMat() {
        return doMat;
    }

    public void setDoMat(String doMat) {
        this.doMat = doMat;
    }

    public String getMucDoTinCay() {
        return mucDoTinCay;
    }

    public void setMucDoTinCay(String mucDoTinCay) {
        this.mucDoTinCay = mucDoTinCay;
    }

    public String getTinhTrangVatLy() {
        return tinhTrangVatLy;
    }

    public void setTinhTrangVatLy(String tinhTrangVatLy) {
        this.tinhTrangVatLy = tinhTrangVatLy;
    }

    @Override
    public String toString() {
        return "FileEntryExportExcelVanBan{" +
                "soTo='" + soTo + '\'' +
                ", loai='" + loai + '\'' +
                ", tenVanBan='" + tenVanBan + '\'' +
                ", ngayThang='" + ngayThang + '\'' +
                ", tacGia='" + tacGia + '\'' +
                ", trichYeu='" + trichYeu + '\'' +
                ", toSo='" + toSo + '\'' +
                ", duongDanMoi='" + duongDanMoi + '\'' +
                ", doMat='" + doMat + '\'' +
                ", mucDoTinCay='" + mucDoTinCay + '\'' +
                ", tinhTrangVatLy='" + tinhTrangVatLy + '\'' +
                '}';
    }
}
