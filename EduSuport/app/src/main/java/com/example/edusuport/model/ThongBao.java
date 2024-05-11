package com.example.edusuport.model;

import java.sql.Timestamp;

public class ThongBao {
    private String IDThongBao;
    private String IDNguoiGui;
    private String IDNguoiNhan;
    private String NoiDung;
    private Timestamp ThoiGian;

    public ThongBao() {
    }

    public String getIDThongBao() {
        return IDThongBao;
    }

    public void setIDThongBao(String IDThongBao) {
        this.IDThongBao = IDThongBao;
    }

    public String getIDNguoiGui() {
        return IDNguoiGui;
    }

    public void setIDNguoiGui(String IDNguoiGui) {
        this.IDNguoiGui = IDNguoiGui;
    }

    public String getIDNguoiNhan() {
        return IDNguoiNhan;
    }

    public void setIDNguoiNhan(String IDNguoiNhan) {
        this.IDNguoiNhan = IDNguoiNhan;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public Timestamp getThoiGian() {
        return ThoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        ThoiGian = thoiGian;
    }

    public ThongBao(String IDThongBao, String IDNguoiGui, String IDNguoiNhan, String noiDung, Timestamp thoiGian) {
        this.IDThongBao = IDThongBao;
        this.IDNguoiGui = IDNguoiGui;
        this.IDNguoiNhan = IDNguoiNhan;
        NoiDung = noiDung;
        ThoiGian = thoiGian;
    }
}
