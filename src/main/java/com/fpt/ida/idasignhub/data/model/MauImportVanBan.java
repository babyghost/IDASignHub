package com.fpt.ida.idasignhub.data.model;

import java.io.Serializable;

public class MauImportVanBan implements Serializable {
    private static final long serialVersionUID = 1L;
    String soKyHieuVanBan;
    String ngayThangNamVanBan;
    String trichYeu;
    String tacGiaVanBan;
    String tuTo;
    String denTo;
    String loai;
    String duongDan;
    String doMat;
    String mucDoTinCay;
    String tinhTrangVatLy;
    String soTrang;

    public String getSoKyHieuVanBan() {
        return soKyHieuVanBan;
    }

    public void setSoKyHieuVanBan(String soKyHieuVanBan) {
        this.soKyHieuVanBan = soKyHieuVanBan;
    }

    public String getNgayThangNamVanBan() {
        return ngayThangNamVanBan;
    }

    public void setNgayThangNamVanBan(String ngayThangNamVanBan) {
        this.ngayThangNamVanBan = ngayThangNamVanBan;
    }

    public String getTrichYeu() {
        return trichYeu;
    }

    public void setTrichYeu(String trichYeu) {
        this.trichYeu = trichYeu;
    }

    public String getTacGiaVanBan() {
        return tacGiaVanBan;
    }

    public void setTacGiaVanBan(String tachGiaVanBan) {
        this.tacGiaVanBan = tachGiaVanBan;
    }

    public String getTuTo() {
        return tuTo;
    }

    public void setTuTo(String tuTo) {
        this.tuTo = tuTo;
    }

    public String getDenTo() {
        return denTo;
    }

    public void setDenTo(String denTo) {
        this.denTo = denTo;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getDuongDan() {
        return duongDan;
    }

    public void setDuongDan(String duongDan) {
        this.duongDan = duongDan;
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

    public String getSoTrang() {
        return soTrang;
    }

    public void setSoTrang(String soTrang) {
        this.soTrang = soTrang;
    }

    public MauImportVanBan() {}

    public MauImportVanBan(String soKyHieuVanBan, String ngayThangNamVanBan, String trichYeu, String tachGiaVanBan, String tuTo, String denTo, String loai, String duongDan, String doMat, String mucDoTinCay, String tinhTrangVatLy, String soTrang) {
        this.soKyHieuVanBan = soKyHieuVanBan;
        this.ngayThangNamVanBan = ngayThangNamVanBan;
        this.trichYeu = trichYeu;
        this.tacGiaVanBan = tachGiaVanBan;
        this.tuTo = tuTo;
        this.denTo = denTo;
        this.loai = loai;
        this.duongDan = duongDan;
        this.doMat = doMat;
        this.mucDoTinCay = mucDoTinCay;
        this.tinhTrangVatLy = tinhTrangVatLy;
        this.soTrang = soTrang;
    }

    @Override
    public String toString() {
        return "MauImportVanBan{" +
                "soKyHieuVanBan='" + soKyHieuVanBan + '\'' +
                ", ngayThangNamVanBan='" + ngayThangNamVanBan + '\'' +
                ", trichYeu='" + trichYeu + '\'' +
                ", tacGiaVanBan='" + tacGiaVanBan + '\'' +
                ", tuTo='" + tuTo + '\'' +
                ", denTo='" + denTo + '\'' +
                ", loai='" + loai + '\'' +
                ", duongDan='" + duongDan + '\'' +
                ", doMat='" + doMat + '\'' +
                ", mucDoTinCay='" + mucDoTinCay + '\'' +
                ", tinhTrangVatLy='" + tinhTrangVatLy + '\'' +
                ", soTrang='" + soTrang + '\'' +
                '}';
    }
}
