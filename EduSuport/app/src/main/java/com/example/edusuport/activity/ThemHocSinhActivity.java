package com.example.edusuport.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.model.DonXinNghiHoc;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.PhuHuynh;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThemHocSinhActivity extends AppCompatActivity {

    EditText edtHoTen, edtmssv;
    Button btnXacNhan;
    DatabaseReference hocsinhRef, phuhuynhRef;
    DBHelper dbHelper = new DBHelper();
    ImageButton btnBackHocSinh;
    TextView txvTenGV;
    private GiaoVien giaoVien = Home.giaoVien;
    private long SoLuong = 0;
    private LopHoc lopHoc = QuanLyLopHocActivity.lopHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_hoc_sinh);

        hocsinhRef = FirebaseDatabase.getInstance().getReference("hocsinh");
        phuhuynhRef = FirebaseDatabase.getInstance().getReference("phuhuynh");

        // Ánh xạ các thành phần từ layout
        edtHoTen = findViewById(R.id.add_hocsinh);
        edtmssv = findViewById(R.id.add_id);
        btnXacNhan = findViewById(R.id.btnXacNhanHs);
        btnBackHocSinh = findViewById(R.id.btnBackHocSinh);

        // Xử lý sự kiện khi nhấn nút Xác nhận
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoTen = edtHoTen.getText().toString().trim();
                String mssv = edtmssv.getText().toString().trim();
                String idlophoc = lopHoc.getIdLopHoc();// gán id lop hoc, cần sửa lại
                String matkhau = "123456";

                if (hoTen.isEmpty() || mssv.isEmpty()) {
                    Toast.makeText(ThemHocSinhActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Truy vấn Firebase Realtime Database để kiểm tra mã số đã tồn tại hay chưa
                    hocsinhRef.child(mssv).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Mã số đã tồn tại, hiển thị thông báo
                                Toast.makeText(ThemHocSinhActivity.this, "Mã số đã tồn tại", Toast.LENGTH_SHORT).show();
                            } else {
                                new AlertDialog.Builder(ThemHocSinhActivity.this)
                                        .setTitle("Xác nhận")
                                        .setMessage("Bạn có chắc chắn muốn thêm học sinh này không?")
                                        .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                GetSoLuong();
                                                HocSinh hocSinh = new HocSinh(hoTen, mssv, idlophoc, matkhau);
                                                String urlAva = "https://avatar.iran.liara.run/public/boy?username=Ash";

                                                Map<String, Object> updates = new HashMap<>();
                                                    updates.put(dbHelper.FieldTenHS, hoTen);
                                                    updates.put(dbHelper.FieldIDLopHoc, idlophoc);
                                                    updates.put(dbHelper.FieldMatKhau, matkhau);
                                                    updates.put(dbHelper.FieldUrlAvt, urlAva);
                                                hocsinhRef.child(mssv).setValue(updates, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                        if (error != null) {
                                                            Toast.makeText(ThemHocSinhActivity.this, "Thêm học sinh thất bại: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            // Tạo một đối tượng PhuHuynh mới
                                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                            DatabaseReference lophocRef = database.getReference(dbHelper.ColecLopHoc).child(lopHoc.getIdLopHoc());
                                                            SoLuong++;
                                                            Map<String, Object> updates = new HashMap<>();
                                                            updates.put(dbHelper.FieldSoLuong, SoLuong);
                                                            lophocRef.updateChildren(updates);

                                                            PhuHuynh phuHuynh = new PhuHuynh(mssv, "Phụ huynh của " + hoTen, idlophoc, matkhau);
                                                            updates = new HashMap<>();
                                                            updates.put(dbHelper.FieldTenHS, "Phụ huynh của " + hoTen);
                                                            updates.put(dbHelper.FieldIDLopHoc, idlophoc);
                                                            updates.put(dbHelper.FieldMSHS, mssv);
                                                            updates.put(dbHelper.FieldMatKhau, matkhau);
                                                            updates.put(dbHelper.FieldUrlAvt, urlAva);
                                                            // Thêm phụ huynh vào Firebase Database
                                                            phuhuynhRef.child(mssv + "PH").setValue(updates);
                                                            Toast.makeText(ThemHocSinhActivity.this, "Thêm học sinh thành công", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(ThemHocSinhActivity.this, DanhSachHocSinh.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });
                                            }
                                        })
                                        .setPositiveButton("Hủy bỏ", null)
                                        .show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ThemHocSinhActivity.this, "Đã xảy ra lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        btnBackHocSinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackHocSinh();
            }
        });

    }
    public void BackHocSinh(){
        Intent intent = new Intent(ThemHocSinhActivity.this, DanhSachHocSinh.class);
        startActivity(intent);
    }
    public void GetSoLuong(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecLopHoc).child(lopHoc.getIdLopHoc());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot fieldSoLuongSnapshot = dataSnapshot.child(dbHelper.FieldSoLuong);
                    if (fieldSoLuongSnapshot.exists()) {
                        SoLuong = fieldSoLuongSnapshot.getValue(Long.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}