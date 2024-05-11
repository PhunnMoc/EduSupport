package com.example.edusuport.model;

public class NhanXet {
    private String IDNhanXet;
    private String IDGiaoVien;
    private String MSHS;
    private String NoiDung;
    private String DanhGia;

    public NhanXet() {
        // Required empty public constructor
    }

    public String getIDNhanXet() {
        return IDNhanXet;
    }

    public void setIDNhanXet(String IDNhanXet) {
        this.IDNhanXet = IDNhanXet;
    }

    public String getIDGiaoVien() {
        return IDGiaoVien;
    }

    public void setIDGiaoVien(String IDGiaoVien) {
        this.IDGiaoVien = IDGiaoVien;
    }

    public String getMSHS() {
        return MSHS;
    }

    public void setMSHS(String MSHS) {
        this.MSHS = MSHS;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public String getDanhGia() {
        return DanhGia;
    }

    public void setDanhGia(String danhGia) {
        DanhGia = danhGia;
    }

    public NhanXet(String IDNhanXet, String IDGiaoVien, String MSHS, String noiDung, String danhGia) {
        this.IDNhanXet = IDNhanXet;
        this.IDGiaoVien = IDGiaoVien;
        this.MSHS = MSHS;
        NoiDung = noiDung;
        DanhGia = danhGia;
    }
}
