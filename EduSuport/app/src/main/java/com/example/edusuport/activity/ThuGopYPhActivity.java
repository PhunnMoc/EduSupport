package com.example.edusuport.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.databinding.ActivityThuGopYBinding;
import com.example.edusuport.databinding.ActivityThuGopYPhBinding;
import com.example.edusuport.model.ThuGopY;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ThuGopYPhActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ActivityThuGopYPhBinding binding;
    ThuGopY thuGopY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThuGopYPhBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper();
        Intent intent = getIntent();
        thuGopY = (ThuGopY) intent.getSerializableExtra("thuGopY");
        binding.txvThoiGian.setText(thuGopY.getThoiGian().toString());
        binding.txvNoiDung.setText(thuGopY.getNoiDung());
        binding.txvTieuDe.setText(thuGopY.getTieuDe());

        dbHelper.getTenGiaoVienByID(thuGopY.getIDGiaoVien(), new DBHelper.TenHocSinhCallback() {
            @Override
            public void onTenHocSinhFetched(String tenHocSinh) {
                if (tenHocSinh != null) {
                    binding.txvGiaoVien.setText("Tới giáo viên: " + tenHocSinh);
                } else {
                    // Xử lý khi không lấy được tên học sinh
                }
            }
        });

        AddEvents();
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