package com.example.edusuport.model;

import java.util.PrimitiveIterator;

public class Diem {
    private String IDDiem;
    private String MSHS;
    private String HocKy;
    private double DiemToan;
    private double DiemVatLy;
    private double DiemHoaHoc;
    private double DiemSinhHoc;
    private double DiemLichSu;
    private double DiemDiaLy;
    private double DiemNguVan;
    private double DiemGDCD;
    private double DiemGDTC;
    private double DiemTinHoc;
    private double DiemTiengAnh;

    public Diem() {
    }

    public String getIDDiem() {
        return IDDiem;
    }

    public void setIDDiem(String IDDiem) {
        this.IDDiem = IDDiem;
    }

    public String getMSHS() {
        return MSHS;
    }

    public void setMSHS(String MSHS) {
        this.MSHS = MSHS;
    }

    public String getHocKy() {
        return HocKy;
    }

    public void setHocKy(String hocKy) {
        HocKy = hocKy;
    }

    public double getDiemToan() {
        return DiemToan;
    }

    public void setDiemToan(double diemToan) {
        DiemToan = diemToan;
    }

    public double getDiemVatLy() {
        return DiemVatLy;
    }

    public void setDiemVatLy(double diemVatLy) {
        DiemVatLy = diemVatLy;
    }

    public double getDiemHoaHoc() {
        return DiemHoaHoc;
    }

    public void setDiemHoaHoc(double diemHoaHoc) {
        DiemHoaHoc = diemHoaHoc;
    }

    public double getDiemSinhHoc() {
        return DiemSinhHoc;
    }

    public void setDiemSinhHoc(double diemSinhHoc) {
        DiemSinhHoc = diemSinhHoc;
    }

    public double getDiemLichSu() {
        return DiemLichSu;
    }

    public void setDiemLichSu(double diemLichSu) {
        DiemLichSu = diemLichSu;
    }

    public double getDiemDiaLy() {
        return DiemDiaLy;
    }

    public void setDiemDiaLy(double diemDiaLy) {
        DiemDiaLy = diemDiaLy;
    }

    public double getDiemNguVan() {
        return DiemNguVan;
    }

    public void setDiemNguVan(double diemNguVan) {
        DiemNguVan = diemNguVan;
    }

    public double getDiemGDCD() {
        return DiemGDCD;
    }

    public void setDiemGDCD(double diemGDCD) {
        DiemGDCD = diemGDCD;
    }

    public double getDiemGDTC() {
        return DiemGDTC;
    }

    public void setDiemGDTC(double diemGDTC) {
        DiemGDTC = diemGDTC;
    }

    public double getDiemTinHoc() {
        return DiemTinHoc;
    }

    public void setDiemTinHoc(double diemTinHoc) {
        DiemTinHoc = diemTinHoc;
    }

    public double getDiemTiengAnh() {
        return DiemTiengAnh;
    }

    public void setDiemTiengAnh(double diemTiengAnh) {
        DiemTiengAnh = diemTiengAnh;
    }

    public Diem(String IDDiem, String MSHS, String hocKy, double diemToan, double diemVatLy, double diemHoaHoc, double diemSinhHoc, double diemLichSu, double diemDiaLy, double diemNguVan, double diemGDCD, double diemGDTC, double diemTinHoc, double diemTiengAnh) {
        this.IDDiem = IDDiem;
        this.MSHS = MSHS;
        HocKy = hocKy;
        DiemToan = diemToan;
        DiemVatLy = diemVatLy;
        DiemHoaHoc = diemHoaHoc;
        DiemSinhHoc = diemSinhHoc;
        DiemLichSu = diemLichSu;
        DiemDiaLy = diemDiaLy;
        DiemNguVan = diemNguVan;
        DiemGDCD = diemGDCD;
        DiemGDTC = diemGDTC;
        DiemTinHoc = diemTinHoc;
        DiemTiengAnh = diemTiengAnh;
    }

}
