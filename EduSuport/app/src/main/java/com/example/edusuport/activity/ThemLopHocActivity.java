package com.example.edusuport.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.LopHoc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThemLopHocActivity extends AppCompatActivity {

    EditText edtTenLop;

    Button btnXacNhan;
    ImageButton btnBackLopHoc;
    CircleImageView imgAva;
    DatabaseReference databaseReference;
    TextView txvTenGV; // thay đổi tên GV ở trên ô Xin chào
    private GiaoVien giaoVien = Home.giaoVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_lop_hoc);
        databaseReference = FirebaseDatabase.getInstance().getReference("lophoc");

        // Ánh xạ các thành phần từ layout
        edtTenLop = findViewById(R.id.edtTenLop);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        btnBackLopHoc = findViewById(R.id.btnBackLopHoc);
        txvTenGV= findViewById(R.id.txvTenGV);
        imgAva=findViewById(R.id.imgAvatar);
        if(!giaoVien.getUrl().isEmpty()){
            Picasso.get().load(giaoVien.getUrl()).into(imgAva);
        }
        else {
            Picasso.get().load(R.drawable.profile).into(imgAva);
        }

        txvTenGV.setText("Giao viên " +giaoVien.getTenGiaoVien().toString());
        // Xử lý sự kiện khi nhấn nút Xác nhận
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các EditText
                String tenLop = edtTenLop.getText().toString().trim();


                // Kiểm tra xem các trường thông tin có được nhập đầy đủ không
                if ( tenLop.isEmpty()) {
                    Toast.makeText(ThemLopHocActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {

                    DBHelper dbHelper = new DBHelper();
                    String maLop = UUID.randomUUID().toString();
                    Map<String, Object> updates = new HashMap<>();
                    updates.put(dbHelper.FieldIDLopHoc, maLop);
                    updates.put(dbHelper.FieldIDGiaoVien, giaoVien.getIDGiaoVien());
                    updates.put(dbHelper.FieldTenLop, tenLop);
                    updates.put(dbHelper.FieldSoLuong, 0);
                    databaseReference.orderByChild("tenLopHoc").equalTo(tenLop).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Tên lớp đã tồn tại trong Firebase
                                Toast.makeText(ThemLopHocActivity.this, "Tên lớp đã tồn tại", Toast.LENGTH_SHORT).show();
                            } else {
                                new AlertDialog.Builder(ThemLopHocActivity.this)
                                        .setTitle("Xác nhận")
                                        .setMessage("Bạn có chắc chắn muốn thêm lớp học này không?")
                                        .setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Thêm lớp học vào Firebase Database
                                                databaseReference.child(maLop).setValue(updates, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@NonNull DatabaseError error, @NonNull DatabaseReference ref) {
                                                        if (error != null) {
                                                            Toast.makeText(ThemLopHocActivity.this, "Thêm lớp học thất bại: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(ThemLopHocActivity.this, "Thêm lớp học thành công", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(ThemLopHocActivity.this, Home.class);
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
                            // Xử lý khi truy vấn bị hủy
                            Toast.makeText(ThemLopHocActivity.this, "Lỗi khi kiểm tra tên lớp: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        // Xử lý sự kiện khi nhấn nút Back
        btnBackLopHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackLopHoc();
            }
        });
    }
    public void BackLopHoc(){
        Intent intent = new Intent(ThemLopHocActivity.this, Home.class);
        startActivity(intent);
    }
}