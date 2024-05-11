package com.example.edusuport.model;

import java.io.Serializable;

public class PhuHuynh implements Serializable {
    private String MSPH;
    private String Ten;
    private String IDLopHoc;
    private String MSHS;
    private String Matkhau;
    private String url;
    private String NgonNgu;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNgonNgu() {
        return NgonNgu;
    }

    public void setNgonNgu(String ngonNgu) {
        NgonNgu = ngonNgu;
    }

    public PhuHuynh(String MSPH, String ten, String IDLopHoc, String MSHS, String url, String NgonNgu) {
        this.MSPH = MSPH;
        Ten = ten;
        this.NgonNgu = NgonNgu;
        this.IDLopHoc = IDLopHoc;
        this.MSHS = MSHS;
        this.url = url;
    }

    public String getMSPH() {
        return MSPH;
    }

    public void setMSPH(String MSPH) {
        this.MSPH = MSPH;
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

    public String getMSHS() {
        return MSHS;
    }

    public void setMSHS(String MSHS) {
        this.MSHS = MSHS;
    }
    public String getMatkhau() {
        return Matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.Matkhau = matkhau;
    }

    public PhuHuynh() {
    }
    public PhuHuynh(String MSPH, String ten, String IDLopHoc, String MSHS) {
        this.MSPH = MSPH;
        Ten = ten;
        this.IDLopHoc = IDLopHoc;
        this.MSHS = MSHS;
    }
}
