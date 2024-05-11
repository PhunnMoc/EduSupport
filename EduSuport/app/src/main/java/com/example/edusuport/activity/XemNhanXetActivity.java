package com.example.edusuport.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.adapter.ThongBaoAdapter;
import com.example.edusuport.databinding.ActivityHopThuGopYBinding;
import com.example.edusuport.databinding.ActivityXemNhanXetBinding;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.NhanXet;
import com.example.edusuport.model.PhuHuynh;
import com.example.edusuport.model.ThongBao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class XemNhanXetActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ActivityXemNhanXetBinding binding;
    private HocSinh hocSinh = HomeHsActivity.hocSinh;
    private PhuHuynh phuHuynh = HomePhActivity.phuHuynh;
    NhanXet nhanXet = new NhanXet();
    String role;
    String tenHS = "";
    String url;
    String mshs = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemNhanXetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DBHelper();
        Intent intent = getIntent();
        role = (String) intent.getSerializableExtra("role");

        if (role.equals("hocsinh")){
            mshs = hocSinh.getMSHS();
            tenHS = hocSinh.getTen();
            url = hocSinh.getUrl();
        }else {
            mshs = phuHuynh.getMSHS();
            dbHelper.getTenHocSinhByMSHS(mshs, new DBHelper.TenHocSinhCallback() {
                @Override
                public void onTenHocSinhFetched(String tenHocSinh) {
                    if (tenHocSinh != null) {
                        tenHS = tenHocSinh;
                    } else {
                        // Xử lý khi không lấy được tên học sinh
                    }
                }
            });
            dbHelper.getUlrHocSinhByID(mshs, new DBHelper.TenHocSinhCallback() {
                @Override
                public void onTenHocSinhFetched(String urlAva) {
                    if (urlAva != null) {
                        url = urlAva;
                    } else {
                        // Xử lý khi không lấy được tên học sinh
                    }
                }
            });
        }

        GetNhanXet(mshs);
        AddEvents();
    }
    public void GetNhanXet(String mshs){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecNhanXet);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nhanXet = new NhanXet();
                for (DataSnapshot thuSnapshot : dataSnapshot.getChildren()) {
                    String Mshs = thuSnapshot.child(dbHelper.FieldMSHS).getValue(String.class);
                    if (Mshs.equals(mshs))
                    {
                        String IDThongBao = thuSnapshot.getKey();
                        String IDNguoiGui = thuSnapshot.child(dbHelper.FieldIDGiaoVien).getValue(String.class);
                        String NoiDung = thuSnapshot.child(dbHelper.FieldNoiDung).getValue(String.class);
                        String DanhGia = thuSnapshot.child(dbHelper.FieldDanhGia).getValue(String.class);
                        nhanXet = new NhanXet(IDThongBao, IDNguoiGui, mshs, NoiDung, DanhGia);
                    }
                }
                SetData(nhanXet);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void SetData(NhanXet nhanXet){
        Picasso.get().load(url).into(binding.ava);
        String danhGia = nhanXet.getDanhGia();
        binding.txvTenHS.setText("Học sinh: "+ tenHS);
        binding.txvNoiDungNX.setText(nhanXet.getNoiDung());
        dbHelper.getTenGiaoVienByID(nhanXet.getIDGiaoVien(), new DBHelper.TenHocSinhCallback() {
            @Override
            public void onTenHocSinhFetched(String tenHocSinh) {
                binding.txvTenNguoiNX.setText("Giác viên: " + tenHocSinh);
            }
        });
        binding.txvDanhGia.setText(danhGia);
        if (danhGia.equals("Yếu")){
            binding.ratingBar.setRating(1);
        }else if (danhGia.equals("Trung bình")){
            binding.ratingBar.setRating(2);
        }else if (danhGia.equals("Khá")){
            binding.ratingBar.setRating(3);
        }else if (danhGia.equals("Giỏi")){
            binding.ratingBar.setRating(4);
        }else if (danhGia.equals("Xuất sắc")){
            binding.ratingBar.setRating(5);
        }


    }
    public void AddEvents(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });

    }
    public void Back(){
        super.onBackPressed();
    }
}