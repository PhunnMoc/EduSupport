package com.example.edusuport.model;

public class TietHoc {
    private String IdTiet;
    private String TenTiet;

    public TietHoc() {
    }

    public String getIdTiet() {
        return IdTiet;
    }

    public void setIdTiet(String idTiet) {
        IdTiet = idTiet;
    }

    public String getTenTiet() {
        return TenTiet;
    }

    public void setTenTiet(String tenTiet) {
        TenTiet = tenTiet;
    }

    public TietHoc(String idTiet, String tenTiet) {
        IdTiet = idTiet;
        TenTiet = tenTiet;
    }
}
