package com.example.edusuport.model;

import java.io.Serializable;

public class HocSinh implements Serializable {
    private String MSHS;
    private String Ten;
    private String IDLopHoc;
    private String Matkhau;
    private String NgonNgu;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public HocSinh(String MSSV, String ten, String IDLopHoc) {
        this.MSHS = MSSV;
        Ten = ten;
        this.IDLopHoc = IDLopHoc;
    }

    public String getNgonNgu() {
        return NgonNgu;
    }

    public void setNgonNgu(String ngonNgu) {
        NgonNgu = ngonNgu;
    }

    public HocSinh(String MSHS, String ten, String IDLopHoc, String url, String NgonNgu) {
        this.MSHS = MSHS;
        Ten = ten;
        this.NgonNgu = NgonNgu;
        this.IDLopHoc = IDLopHoc;
        this.url = url;
    }
    public HocSinh(String MSHS, String ten, String IDLopHoc, String url) {
        this.MSHS = MSHS;
        Ten = ten;
        this.IDLopHoc = IDLopHoc;
        this.url = url;
    }

    public String getMSHS() {
        return MSHS;
    }

    public void setMSHS(String MSSV) {
        this.MSHS = MSSV;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getIDLopHoc() {
        return IDLopHoc;
    }

    public void setIDLopHoc(String IDLopHoc) {
        this.IDLopHoc = IDLopHoc;
    }
    public String getMatkhau() {
        return Matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.Matkhau = matkhau;
    }
    public HocSinh() {
    }
}
