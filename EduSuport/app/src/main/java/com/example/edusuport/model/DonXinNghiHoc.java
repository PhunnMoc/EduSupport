package com.example.edusuport.model;
import java.io.Serializable;
import java.sql.Timestamp;
public class DonXinNghiHoc implements Serializable {
    private String IDDon;
    private String MSHS;
    private String LyDo;
    private Timestamp NgayXinNghi;
    private Timestamp ThoiGian;
    private String TrangThai;

    public String getIDDon() {
        return IDDon;
    }

    public void setIDDon(String IDDon) {
        this.IDDon = IDDon;
    }

    public String getMSHS() {
        return MSHS;
    }

    public void setMSHS(String MSHS) {
        this.MSHS = MSHS;
    }

    public String getLyDo() {
        return LyDo;
    }

    public void setLyDo(String lyDo) {
        LyDo = lyDo;
    }

    public Timestamp getNgayXinNghi() {
        return NgayXinNghi;
    }

    public void setNgayXinNghi(Timestamp ngayXinNghi) {
        NgayXinNghi = ngayXinNghi;
    }

    public Timestamp getThoiGian() {
        return ThoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        ThoiGian = thoiGian;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }

    public DonXinNghiHoc(String IDDon, String MSHS, String lyDo, Timestamp ngayXinNghi, Timestamp thoiGian, String trangThai) {
        this.IDDon = IDDon;
        this.MSHS = MSHS;
        LyDo = lyDo;
        NgayXinNghi = ngayXinNghi;
        ThoiGian = thoiGian;
        TrangThai = trangThai;
    }

    public DonXinNghiHoc() {
    }
}
