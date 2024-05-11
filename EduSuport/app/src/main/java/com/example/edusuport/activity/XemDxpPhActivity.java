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
import com.example.edusuport.databinding.ActivityXemDxpPhBinding;
import com.example.edusuport.model.DonXinNghiHoc;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class XemDxpPhActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ActivityXemDxpPhBinding binding;
    DonXinNghiHoc donXinNghiHoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemDxpPhBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper();
        Intent intent = getIntent();
        donXinNghiHoc = (DonXinNghiHoc) intent.getSerializableExtra("donXinNghiHoc");

        binding.txvLyDo.setText(donXinNghiHoc.getLyDo());
        binding.txvThoiGian.setText(donXinNghiHoc.getThoiGian().toString());
        binding.txvNgayNghi.setText("Ngày xin nghỉ: " + convertTimestampToString(donXinNghiHoc.getNgayXinNghi()));
        binding.txvTrangThai.setText("Trạng thái: " + donXinNghiHoc.getTrangThai());

        AddEvents();
    }
    private String convertTimestampToString(Timestamp timestamp) {
        // Tạo đối tượng Date từ timestamp
        Date date = new Date(timestamp.getTime());

        // Tạo đối tượng SimpleDateFormat để định dạng ngày tháng
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        // Chuyển đổi Date thành chuỗi và trả về
        return dateFormat.format(date);
    }
    public void AddEvents(){
        binding.btnbackDuyetDon.setOnClickListener(new View.OnClickListener() {
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