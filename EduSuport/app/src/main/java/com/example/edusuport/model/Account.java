package com.example.edusuport.model;

import android.net.Uri;

public class Account {
    private String idTK;
    private String ten;
    private String urlAva;
    private String sdt;
    private String role;
    private String ngonNgu;
    private String themeDL;
    private String trangThai;

    public Account(String ten, String urlAva, String phoneNum, String role, String ngonNgu, String themeDL, String trangThai) {
        this.ten = ten;
        this.urlAva = urlAva;
        this.sdt = phoneNum;
        this.role = role;
        this.ngonNgu = ngonNgu;
        this.themeDL = themeDL;
        this.trangThai = trangThai;
    }

    public Account(String idTK, String ten, String urlAva, String phoneNum, String role, String ngonNgu, String themeDL, String trangThai) {
        this.idTK = idTK;
        this.ten = ten;
        this.urlAva = urlAva;
        this.sdt = phoneNum;
        this.role = role;
        this.ngonNgu = ngonNgu;
        this.themeDL = themeDL;
        this.trangThai = trangThai;
    }

    public String getIdTK() {
        return idTK;
    }

    public void setIdTK(String idTK) {
        this.idTK = idTK;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getUrlAva() {
        return urlAva;
    }

    public void setUrlAva(String urlAva) {
        this.urlAva = urlAva;
    }

    public String getPhoneNum() {
        return sdt;
    }

    public void setPhoneNum(String phoneNum) {
        this.sdt = phoneNum;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNgonNgu() {
        return ngonNgu;
    }

    public void setNgonNgu(String ngonNgu) {
        this.ngonNgu = ngonNgu;
    }

    public String getThemeDL() {
        return themeDL;
    }

    public void setThemeDL(String themeDL) {
        this.themeDL = themeDL;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}