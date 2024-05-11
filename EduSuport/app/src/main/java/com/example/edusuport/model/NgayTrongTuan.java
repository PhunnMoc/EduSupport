package com.example.edusuport.model;

public class NgayTrongTuan {
    private String IDThu;
    private String TenThu;
    public NgayTrongTuan() {
        // Cần phải có constructor rỗng khi sử dụng Firebase
    }

    public NgayTrongTuan(String IDThu, String TenThu) {
        this.IDThu = IDThu;
        this.TenThu = TenThu;
    }

    public String getIDThu() {
        return IDThu;
    }

    public void setIDThu(String IDThu) {
        this.IDThu = IDThu;
    }

    public String getTenThu() {
        return TenThu;
    }

    public void setTenThu(String TenThu) {
        this.TenThu = TenThu;
    }
}
