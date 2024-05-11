package com.example.edusuport.model;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Map;

public class TaiLieuHocTap {
    private String idTaiLieu;
    private String idmon;
    private String tenTaiLieu;
    private String urlfile;
    private String fileType;
    private Timestamp thoiGian;

    private String idLop;

    public TaiLieuHocTap(String idTaiLieu, String idmon, String tenTaiLieu, String urlfile, String fileType, Timestamp thoiGian, String idLop) {
        this.idTaiLieu = idTaiLieu;
        this.idmon = idmon;
        this.tenTaiLieu = tenTaiLieu;
        this.urlfile = urlfile;
        this.fileType = fileType;
        this.thoiGian = thoiGian;
        this.idLop = idLop;
    }

    public TaiLieuHocTap(String idmon, String tenTaiLieu, String urlfile, String fileType, Timestamp thoiGian, String idLop) {
        this.idmon = idmon;
        this.tenTaiLieu = tenTaiLieu;
        this.urlfile = urlfile;
        this.fileType = fileType;
        this.thoiGian = thoiGian;
        this.idLop = idLop;
    }

    public String getIdTaiLieu() {
        return idTaiLieu;
    }

    public void setIdTaiLieu(String idTaiLieu) {
        this.idTaiLieu = idTaiLieu;
    }

    public String getIdmon() {
        return idmon;
    }

    public void setIdmon(String idmon) {
        this.idmon = idmon;
    }

    public String getTenTaiLieu() {
        return tenTaiLieu;
    }

    public void setTenTaiLieu(String tenTaiLieu) {
        this.tenTaiLieu = tenTaiLieu;
    }

    public String getUrlfile() {
        return urlfile;
    }

    public void setUrlfile(String urlfile) {
        this.urlfile = urlfile;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Timestamp getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getIdLop() {
        return idLop;
    }

    public void setIdLop(String idLop) {
        this.idLop = idLop;
    }
}
