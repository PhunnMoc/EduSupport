package com.example.edusuport.model;

public class ThoiKhoaBieu {
    private String IDGiaoVien;
    private String IDLopHoc;
    private String IDMon;
    private String IDThu;
    private long tiet;

    public ThoiKhoaBieu(String IDGiaoVien, String IDLopHoc, String IDMon, String IDThu, long tiet) {
        this.IDGiaoVien = IDGiaoVien;
        this.IDLopHoc = IDLopHoc;
        this.IDMon = IDMon;
        this.IDThu = IDThu;
        this.tiet = tiet;
    }

    public String getIDGiaoVien() {
        return IDGiaoVien;
    }

    public void setIDGiaoVien(String IDGiaoVien) {
        this.IDGiaoVien = IDGiaoVien;
    }

    public String getIDLop() {
        return IDLopHoc;
    }

    public void setIDLop(String IDLop) {
        this.IDLopHoc = IDLop;
    }

    public String getIDMon() {
        return IDMon;
    }

    public void setIDMon(String IDMon) {
        this.IDMon = IDMon;
    }

    public String getIDThu() {
        return IDThu;
    }

    public void setIDThu(String IDThu) {
        this.IDThu = IDThu;
    }

    public long getTiet() {
        return tiet;
    }

    public void setTiet(long tiet) {
        this.tiet = tiet;
    }

    public ThoiKhoaBieu() {
    }
}
