package com.example.edusuport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.databinding.ActivityNhanXetCaNhanBinding;
import com.example.edusuport.databinding.ActivityNhanXetChungBinding;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.NhanXet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NhanXetCaNhanActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ActivityNhanXetCaNhanBinding binding;
    private GiaoVien giaoVien = Home.giaoVien;
    private HocSinh hocSinh;
    NhanXet nhanXet;
    String DanhGia ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNhanXetCaNhanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DBHelper();
        Intent intent = getIntent();
        hocSinh = (HocSinh) intent.getSerializableExtra("hocSinh");
        binding.txvTenHS.setText("Học sinh: " + hocSinh.getTen());
        binding.txvMaHS.setText("Mã số học sinh: "+ hocSinh.getMSHS());
        GetNhanXet(hocSinh.getMSHS());
        AddEvents();
    }
    public void GetNhanXet(String mshs){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecNhanXet);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nhanXet = null;
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
                if (nhanXet != null)
                    SetData(nhanXet);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void SetData(NhanXet nx){
        if (nx == null)
            return;
        String danhGia = nx.getDanhGia();
        binding.txvDanhGia.setText(danhGia);
        binding.edtNhanXet.setText(nx.getNoiDung());
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
        binding.edtNhanXet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(R.drawable.edittext_boder_click);
                } else {
                    v.setBackgroundResource(R.drawable.edittext_border);
                }
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LuuNhanXet();
            }
        });
        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) { // Đảm bảo rằng sự kiện chỉ xảy ra khi người dùng thực sự thay đổi đánh giá
                    if (rating == 1.0) {
                        binding.txvDanhGia.setText("Đánh giá: Yếu");
                        DanhGia = "Yếu";
                    } else if (rating == 2.0) {
                        binding.txvDanhGia.setText("Đánh giá: Trung bình");
                        DanhGia = "Trung bình";
                    } else if (rating == 3.0) {
                        binding.txvDanhGia.setText("Đánh giá: Khá");
                        DanhGia = "Khá";
                    } else if (rating == 4.0) {
                        binding.txvDanhGia.setText("Đánh giá: Giỏi");
                        DanhGia = "Giỏi";
                    } else if (rating == 5.0) {
                        binding.txvDanhGia.setText("Đánh giá: Xuất sắc");
                        DanhGia = "Xuất sắc";
                    }
                }
            }
        });

    }

    private void LuuNhanXet() {
        if (DanhGia.equals("")){
            Toast.makeText(NhanXetCaNhanActivity.this, "Vui lòng đánh giá sinh viên!!", Toast.LENGTH_SHORT).show();
            return;
        }
        String NhanXet = binding.edtNhanXet.getText().toString();
        if (NhanXet.equals("")){
            Toast.makeText(NhanXetCaNhanActivity.this, "Vui lòng nhập nhận xét sinh viên!!", Toast.LENGTH_SHORT).show();
            return;
        }
        String IDNhanXet = UUID.randomUUID().toString();
        NhanXet nhanXet = new NhanXet(IDNhanXet, giaoVien.getIDGiaoVien(), hocSinh.getMSHS(), NhanXet, DanhGia);
        Map<String, Object> updates = new HashMap<>();
        updates.put(dbHelper.FieldIDGiaoVien, giaoVien.getIDGiaoVien());
        updates.put(dbHelper.FieldMSHS, hocSinh.getMSHS());
        updates.put(dbHelper.FieldNoiDung, NhanXet);
        updates.put(dbHelper.FieldDanhGia, DanhGia);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference nhanXetRef = database.getReference(dbHelper.ColecNhanXet);
        nhanXetRef.child(IDNhanXet).updateChildren(updates);
        Toast.makeText(NhanXetCaNhanActivity.this, "Đánh giá thành công!!", Toast.LENGTH_SHORT).show();
        SendNotification(hocSinh.getMSHS(), "Thông báo đã đánh giá cho học sinh");
        Back();

    }
    private void SendNotification(String Mshs, String NoiDung) {
        DatabaseReference thongbaoRef = FirebaseDatabase.getInstance().getReference(dbHelper.ColecThongBao);
        // Gui cho học sinh
        Map<String, Object> updates = new HashMap<>();
        String IDThongBao = UUID.randomUUID().toString();
        long thoigian = System.currentTimeMillis();
        updates.put(dbHelper.FieldIDNguoiGui, giaoVien.getIDGiaoVien());
        updates.put(dbHelper.FieldIDNguoiNhan, Mshs);
        updates.put(dbHelper.FieldNoiDung, NoiDung);
        updates.put(dbHelper.FieldThoiGian, thoigian);
        thongbaoRef.child(IDThongBao).updateChildren(updates);
        // Gui cho phụ huynh
        updates = new HashMap<>();
        String MSPH = Mshs + "PH";
        IDThongBao = UUID.randomUUID().toString();
        updates.put(dbHelper.FieldIDNguoiGui, giaoVien.getIDGiaoVien());
        updates.put(dbHelper.FieldIDNguoiNhan,MSPH);
        updates.put(dbHelper.FieldNoiDung, NoiDung);
        updates.put(dbHelper.FieldThoiGian, thoigian);
        thongbaoRef.child(IDThongBao).updateChildren(updates);
        super.onBackPressed();
    }
    public void Back(){
        super.onBackPressed();
    }
}
