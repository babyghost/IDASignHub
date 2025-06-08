package com.fpt.ida.idasignhub.data.model;

import java.util.List;

public class DataResult {

    Integer stt;
    String hoSoId;
    String hoSo;
    String tenVanBan;
    String path;
    Integer soTrang;
    Integer soToA0;
    Integer soToA1;
    Integer soToA2;
    Integer soToA3;
    Integer soToA4;
    Integer soToA5;
    List<DataVanBan> dataVanBans;
    public DataResult() {}

    public DataResult(Integer stt, String hoSoId, String hoSo, String tenVanBan, String path, Integer soTrang, Integer soToA0, Integer soToA1, Integer soToA2, Integer soToA3, Integer soToA4, Integer soToA5) {
        this.stt = stt;
        this.hoSoId = hoSoId;
        this.hoSo = hoSo;
        this.tenVanBan = tenVanBan;
        this.path = path;
        this.soTrang = soTrang;
        this.soToA0 = soToA0;
        this.soToA1 = soToA1;
        this.soToA2 = soToA2;
        this.soToA3 = soToA3;
        this.soToA4 = soToA4;
        this.soToA5 = soToA5;
    }

    public List<DataVanBan> getDataVanBans() {
        return dataVanBans;
    }

    public void setDataVanBans(List<DataVanBan> dataVanBans) {
        this.dataVanBans = dataVanBans;
    }

    public Integer getStt() {
        return stt;
    }

    public void setStt(Integer stt) {
        this.stt = stt;
    }

    public String getHoSoId() {
        return hoSoId;
    }

    public void setHoSoId(String hoSoId) {
        this.hoSoId = hoSoId;
    }

    public String getHoSo() {
        return hoSo;
    }

    public void setHoSo(String hoSo) {
        this.hoSo = hoSo;
    }

    public String getTenVanBan() {
        return tenVanBan;
    }

    public void setTenVanBan(String tenVanBan) {
        this.tenVanBan = tenVanBan;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSoTrang() {
        return soTrang;
    }

    public void setSoTrang(Integer soTrang) {
        this.soTrang = soTrang;
    }

    public Integer getSoToA0() {
        return soToA0;
    }

    public void setSoToA0(Integer soToA0) {
        this.soToA0 = soToA0;
    }

    public Integer getSoToA1() {
        return soToA1;
    }

    public void setSoToA1(Integer soToA1) {
        this.soToA1 = soToA1;
    }

    public Integer getSoToA2() {
        return soToA2;
    }

    public void setSoToA2(Integer soToA2) {
        this.soToA2 = soToA2;
    }

    public Integer getSoToA3() {
        return soToA3;
    }

    public void setSoToA3(Integer soToA3) {
        this.soToA3 = soToA3;
    }

    public Integer getSoToA4() {
        return soToA4;
    }

    public void setSoToA4(Integer soToA4) {
        this.soToA4 = soToA4;
    }

    public Integer getSoToA5() {
        return soToA5;
    }

    public void setSoToA5(Integer soToA5) {
        this.soToA5 = soToA5;
    }
}
