package com.example.edusuport.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class ThuGopY implements Serializable{
    private String IDThuGopY;
    private String IDNguoiGui;
    private String IDGiaoVien;
    private String TieuDe;
    private String NoiDung;
    private Timestamp ThoiGian;
    private boolean AnDanh;
    private boolean Xem;

    public String getIDNguoiGui() {
        return IDNguoiGui;
    }

    public String getIDThuGopY() {
        return IDThuGopY;
    }

    public void setIDThuGopY(String IDThuGopY) {
        this.IDThuGopY = IDThuGopY;
    }

    public void setIDNguoiGui(String IDNguoiGui) {
        this.IDNguoiGui = IDNguoiGui;
    }

    public String getIDGiaoVien() {
        return IDGiaoVien;
    }

    public void setIDGiaoVien(String IDGiaoVien) {
        this.IDGiaoVien = IDGiaoVien;
    }

    public String getTieuDe() {
        return TieuDe;
    }

    public void setTieuDe(String tieuDe) {
        TieuDe = tieuDe;
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

    public boolean isAnDanh() {
        return AnDanh;
    }

    public void setAnDanh(boolean anDanh) {
        AnDanh = anDanh;
    }

    public boolean isXem() {
        return Xem;
    }

    public void setXem(boolean xem) {
        Xem = xem;
    }

    public ThuGopY(String IDThuGopY, String IDNguoiGui, String IDGiaoVien, String tieuDe, String noiDung, Timestamp thoiGian, boolean anDanh, boolean xem) {
        this.IDThuGopY = IDThuGopY;
        this.IDNguoiGui = IDNguoiGui;
        this.IDGiaoVien = IDGiaoVien;
        TieuDe = tieuDe;
        NoiDung = noiDung;
        ThoiGian = thoiGian;
        AnDanh = anDanh;
        Xem = xem;
    }
}
