package com.example.edusuport.model;

import java.sql.Timestamp;
import java.util.Map;

public class NhomThe {
    private String idNhomThe;
    private String tenNhomThe;
    private String mota;
    private String idMon;
    private String idLop;
    private Timestamp thoiGian;

    public NhomThe(String idNhomThe, String tenNhomThe, String mota, String idMon, String idLop, Timestamp thoiGian) {
        this.idNhomThe = idNhomThe;
        this.tenNhomThe = tenNhomThe;
        this.mota = mota;
        this.idMon = idMon;
        this.idLop = idLop;
        this.thoiGian = thoiGian;
    }

    public NhomThe(String tenNhomThe, String mota, String idMon, String idLop, Timestamp thoiGian) {
        this.tenNhomThe = tenNhomThe;
        this.mota = mota;
        this.idMon = idMon;
        this.idLop = idLop;
        this.thoiGian = thoiGian;
    }

    public String getIdNhomThe() {
        return idNhomThe;
    }

    public void setIdNhomThe(String idNhomThe) {
        this.idNhomThe = idNhomThe;
    }

    public String getTenNhomThe() {
        return tenNhomThe;
    }

    public void setTenNhomThe(String tenNhomThe) {
        this.tenNhomThe = tenNhomThe;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getIdMon() {
        return idMon;
    }

    public void setIdMon(String idMon) {
        this.idMon = idMon;
    }

    public String getIdLop() {
        return idLop;
    }

    public void setIdLop(String idLop) {
        this.idLop = idLop;
    }

    public Timestamp getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        this.thoiGian = thoiGian;
    }
}
