package com.example.edusuport.model;

public class ChucNang {
    private String idChucNang;
    private String tenChucNang;

    public ChucNang(String idChucNang, String tenChucNang) {
        this.idChucNang = idChucNang;
        this.tenChucNang = tenChucNang;
    }

    public String getIdChucNang() {
        return idChucNang;
    }

    public void setIdChucNang(String idChucNang) {
        this.idChucNang = idChucNang;
    }

    public String getTenChucNang() {
        return tenChucNang;
    }

    public void setTenChucNang(String tenChucNang) {
        this.tenChucNang = tenChucNang;
    }
}
