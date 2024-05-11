package com.example.edusuport.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.databinding.ActivityDonXinPhepNghiHocBinding;
import com.example.edusuport.databinding.ActivityThuGopYBinding;
import com.example.edusuport.model.DonXinNghiHoc;
import com.example.edusuport.model.ThuGopY;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ThuGopYActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ActivityThuGopYBinding binding;
    ThuGopY thuGopY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThuGopYBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper();
        Intent intent = getIntent();
        thuGopY = (ThuGopY) intent.getSerializableExtra("thuGopY");
        binding.txvThoiGian.setText(thuGopY.getThoiGian().toString());
        binding.txvNoiDung.setText(thuGopY.getNoiDung());
        binding.txvTieuDe.setText(thuGopY.getTieuDe());
        if (thuGopY.isAnDanh()){
            binding.txvTenNguoiGui.setText("Người gửi ẩn danh");
        }else {
            dbHelper.getTenPhuHuynhByID(thuGopY.getIDNguoiGui(), new DBHelper.TenHocSinhCallback() {
                @Override
                public void onTenHocSinhFetched(String tenHocSinh) {
                    if (tenHocSinh != null) {
                        binding.txvTenNguoiGui.setText(tenHocSinh);
                    } else {
                        // Xử lý khi không lấy được tên học sinh
                    }
                }
            });
        }

        updateXemThu(thuGopY.getIDThuGopY());
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
    public void updateXemThu(String thuId) {
        DatabaseReference donRef = FirebaseDatabase.getInstance().getReference(dbHelper.ColecThuGopY).child(thuId);
        Map<String, Object> updateData = new HashMap<>();
        updateData.put(dbHelper.FieldXem, true);
        donRef.updateChildren(updateData);

    }
    public void Back(){
        Intent intent = new Intent(ThuGopYActivity.this, HopThuGopYActivity.class);
        startActivity(intent);
    }
}