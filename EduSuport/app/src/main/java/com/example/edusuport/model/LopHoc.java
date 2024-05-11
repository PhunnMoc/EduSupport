package com.example.edusuport.model;

import java.io.Serializable;

public class LopHoc implements Serializable {
    private String idLopHoc;
    private String idGiaoVien;
    private String tenLopHoc;
    private long SoLuong;
    public String getIdLopHoc() {
        return idLopHoc;
    }
    public void setIdLopHoc(String idLopHoc) {
        this.idLopHoc = idLopHoc;
    }
    public String getIdGiaoVien() {
        return idGiaoVien;
    }

    public void setIdGiaoVien(String idGiaoVien) {
        this.idGiaoVien = idGiaoVien;
    }

    public String getTenLopHoc() {
        return tenLopHoc;
    }

    public void setTenLopHoc(String tenLopHoc) {
        this.tenLopHoc = tenLopHoc;
    }

    public long getSoLuong() {
        return SoLuong;
    }

    public LopHoc(String idLopHoc, String idGiaoVien, String tenLopHoc) {
        this.idLopHoc = idLopHoc;
        this.idGiaoVien = idGiaoVien;
        this.tenLopHoc = tenLopHoc;
    }

    public void setSoLuong(long soLuong) {
        SoLuong = soLuong;
    }

    public LopHoc(String idGiaoVien, String tenLopHoc) {
        this.idGiaoVien = idGiaoVien;
        this.tenLopHoc = tenLopHoc;
    }

    public LopHoc() {
    }

    public LopHoc(String idLopHoc, String idGiaoVien, String tenLopHoc, long soLuong) {
        this.idLopHoc = idLopHoc;
        this.idGiaoVien = idGiaoVien;
        this.tenLopHoc = tenLopHoc;
        SoLuong = soLuong;
    }
}
