package com.example.edusuport.model;

public class TheLat {
    private String idThe;
    private String idNhomThe;
    private String cauHoi;
    private String cauTraLoi;

    public TheLat(String idThe, String idNhomThe, String cauHoi, String cauTraLoi) {
        this.idThe = idThe;
        this.idNhomThe = idNhomThe;
        this.cauHoi = cauHoi;
        this.cauTraLoi = cauTraLoi;
    }

    public TheLat(String idNhomThe, String cauHoi, String cauTraLoi) {
        this.idNhomThe = idNhomThe;
        this.cauHoi = cauHoi;
        this.cauTraLoi = cauTraLoi;
    }

    public String getIdThe() {
        return idThe;
    }

    public void setIdThe(String idThe) {
        this.idThe = idThe;
    }

    public String getIdNhomThe() {
        return idNhomThe;
    }

    public void setIdNhomThe(String idNhomThe) {
        this.idNhomThe = idNhomThe;
    }

    public String getCauHoi() {
        return cauHoi;
    }

    public void setCauHoi(String cauHoi) {
        this.cauHoi = cauHoi;
    }

    public String getCauTraLoi() {
        return cauTraLoi;
    }

    public void setCauTraLoi(String cauTraLoi) {
        this.cauTraLoi = cauTraLoi;
    }
}
