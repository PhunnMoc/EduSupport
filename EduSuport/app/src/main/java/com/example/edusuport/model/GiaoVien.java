package com.example.edusuport.model;

public class GiaoVien {
    private String IDGiaoVien;
    private String TenGiaoVien;
    private String url;
    private String NgonNgu;

    public String getNgonNgu() {
        return NgonNgu;
    }

    public void setNgonNgu(String ngonNgu) {
        NgonNgu = ngonNgu;
    }

    public GiaoVien(String IDGiaoVien, String tenGiaoVien, String url, String ngonNgu) {
        this.IDGiaoVien = IDGiaoVien;
        TenGiaoVien = tenGiaoVien;
        this.url = url;
        NgonNgu = ngonNgu;
    }

    public GiaoVien(String IDGiaoVien, String tenGiaoVien, String url) {
        this.IDGiaoVien = IDGiaoVien;
        TenGiaoVien = tenGiaoVien;
        this.url = url;
    }

    public String getIDGiaoVien() {
        return IDGiaoVien;
    }

    public void setIDGiaoVien(String IDGiaoVien) {
        this.IDGiaoVien = IDGiaoVien;
    }

    public String getTenGiaoVien() {
        return TenGiaoVien;
    }

    public void setTenGiaoVien(String tenGiaoVien) {
        TenGiaoVien = tenGiaoVien;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public GiaoVien() {
    }

    public GiaoVien(String IDGiaoVien, String tenGiaoVien) {
        this.IDGiaoVien = IDGiaoVien;
        TenGiaoVien = tenGiaoVien;
    }
}
