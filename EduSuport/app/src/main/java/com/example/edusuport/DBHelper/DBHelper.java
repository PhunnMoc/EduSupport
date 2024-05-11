package com.example.edusuport.DBHelper;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DBHelper {
    private DatabaseReference databaseRef;
    public final String ColecThoiKhoaBieu = "thoikhoabieu";
    public final String ColecLopHoc = "lophoc";
    public final String ColecDiem = "diem";
    public final String ColecHocSinh = "hocsinh";
    public final String ColecThongBao = "thongbao";
    public final String ColecPhuHuynh = "phuhuynh";
    public final String ColecGiaoVien = "giaovien";
    public final String ColecThuGopY = "thugopy";
    public final String ColecMonHoc = "monhoc";
    public final String ColecNhanXet = "nhanxet";
    public final String ColecDonXinNghiHoc="donxinnghihoc";
    public final String FieldMSHS="mshs";
    public final String FieldMatKhau="matKhau";
    public final String FieldUrlAvt="urlAva";
    public final String FieldSoLuong="soLuong";
    public final String FieldMSPH="msph";
    public final String FieldDanhGia="danhgia";
    public final String FieldHocKy = "hocky";
    public final String FieldDiemToan="idtoan";
    public final String FieldDiemVatLy = "idvatly";
    public final String FieldDiemHoaHoc= "idhoahoc";
    public final String FieldDiemNguVan="idnguvan";
    public final String FieldDiemSinhHoc="idsinhhoc";
    public final String FieldDiemLichSu="idlichsu";
    public final String FieldDiemDiaLy="iddialy";
    public final String FieldDiemGDCD = "idgiaoduccongdan";
    public final String FieldDiemGDTC ="idgiaoducthechat";
    public final String FieldDiemTinHoc = "idtinhoc";
    public final String FieldDiemTiengAnh = "idtienganh";
    public final String FieldLyDo ="lydo";
    public final String FieldThoiGian="thoigian";
    public final String FieldNgayNghi="ngaynghi";
    public final String FieldTrangThai="trangthai";
    public final String FieldTenHS = "ten";
    public final String FieldTenPH = "ten";
    public final String FieldAnDanh = "andanh";
    public final String FieldXem = "xem";
    public final String FieldIDNguoiGui = "idnguoigui";
    public final String FieldIDNguoiNhan = "idnguoinhan";
    public final String FieldIDGiaoVien = "idgiaovien";
    public final String FieldNoiDung = "noidung";
    public final String FieldTieuDe = "tieude";
    public final String FieldIDLopHoc = "idlophoc";
    public final String FieldTenLop = "tenlop";
    public final String FieldTenMon = "tenmon";
    public final String FieldIDMon = "idmon";
    public final String FieldTiet = "tiet";
    public final String FieldThu = "thu";
    public final String ValueTTDaDuyet = "Đã duyệt";
    public final String ValueTTChuaDuyet = "Chưa duyệt";
    public final String ValueTTTuChoi = "Từ chối";
    public final String ValueHocKy1 = "hocky1";
    public final String ValueHocKy2 = "hocky2";

    public DBHelper() {
        // Khởi tạo DatabaseReference
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public void getTenHocSinhByMSHS(String MSHS, final TenHocSinhCallback callback) {
        DatabaseReference hocsinhRef = databaseRef.child(ColecHocSinh).child(MSHS).child(FieldTenHS);
        hocsinhRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tenHocSinh = dataSnapshot.getValue(String.class);
                    callback.onTenHocSinhFetched(tenHocSinh);
                } else {
                    callback.onTenHocSinhFetched(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onTenHocSinhFetched(null);
            }
        });
    }
    public void getTenPhuHuynhByID(String MSPH, final TenHocSinhCallback callback) {
        DatabaseReference hocsinhRef = databaseRef.child(ColecPhuHuynh).child(MSPH).child(FieldTenPH);
        hocsinhRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tenPhuHuynh = dataSnapshot.getValue(String.class);
                    callback.onTenHocSinhFetched(tenPhuHuynh);
                } else {
                    callback.onTenHocSinhFetched(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onTenHocSinhFetched(null);
            }
        });
    }
    public void getTenGiaoVienByID(String MSPH, final TenHocSinhCallback callback) {
        DatabaseReference hocsinhRef = databaseRef.child(ColecGiaoVien).child(MSPH).child(FieldTenPH);
        hocsinhRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tenPhuHuynh = dataSnapshot.getValue(String.class);
                    callback.onTenHocSinhFetched(tenPhuHuynh);
                } else {
                    callback.onTenHocSinhFetched(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onTenHocSinhFetched(null);
            }
        });
    }
    public void getUlrHocSinhByID(String MSPH, final TenHocSinhCallback callback) {
        DatabaseReference hocsinhRef = databaseRef.child(ColecHocSinh).child(MSPH).child("urlAva");
        hocsinhRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String url = dataSnapshot.getValue(String.class);
                    callback.onTenHocSinhFetched(url);
                } else {
                    callback.onTenHocSinhFetched(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onTenHocSinhFetched(null);
            }
        });
    }
    public interface TenHocSinhCallback {
        void onTenHocSinhFetched(String tenHocSinh);
    }


}
