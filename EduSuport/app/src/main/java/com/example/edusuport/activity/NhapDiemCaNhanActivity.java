package com.example.edusuport.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.adapter.HocSinhAdapter;
import com.example.edusuport.databinding.ActivityNhapDiemCaNhanBinding;
import com.example.edusuport.model.Diem;
import com.example.edusuport.model.DonXinNghiHoc;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.HocSinh;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NhapDiemCaNhanActivity extends AppCompatActivity {
    ActivityNhapDiemCaNhanBinding binding;
    private HocSinh hocSinh;
    private String hocKy;
    private Diem diem;
    DBHelper dbHelper;
    GiaoVien giaoVien = Home.giaoVien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNhapDiemCaNhanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper();
        hocKy = dbHelper.ValueHocKy1;
        Intent intent = getIntent();
        hocSinh = (HocSinh) intent.getSerializableExtra("hocSinh");
        GetDiem(hocSinh.getMSHS());
        AddEvents();
    }
    public void GetDiem(String MSHS){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecDiem);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                diem = new Diem();
                for (DataSnapshot diemSNS : dataSnapshot.getChildren()) {
                    if (diemSNS.child(dbHelper.FieldMSHS).getValue(String.class).equals(MSHS) &&
                            diemSNS.child(dbHelper.FieldHocKy).getValue(String.class).equals(hocKy))
                    {
                        String IDDiem = diemSNS.getKey();
                        double DiemToan = diemSNS.child(dbHelper.FieldDiemToan).getValue(double.class);
                        double DiemVatLy = diemSNS.child(dbHelper.FieldDiemVatLy).getValue(double.class);
                        double DiemHoaHoc = diemSNS.child(dbHelper.FieldDiemHoaHoc).getValue(double.class);
                        double DiemSinhHoc = diemSNS.child(dbHelper.FieldDiemSinhHoc).getValue(double.class);
                        double DiemLichSu = diemSNS.child(dbHelper.FieldDiemLichSu).getValue(double.class);
                        double DiemDiaLy = diemSNS.child(dbHelper.FieldDiemDiaLy).getValue(double.class);
                        double DiemNguVan = diemSNS.child(dbHelper.FieldDiemNguVan).getValue(double.class);
                        double DiemGDCD = diemSNS.child(dbHelper.FieldDiemGDCD).getValue(double.class);
                        double DiemGDTC = diemSNS.child(dbHelper.FieldDiemGDTC).getValue(double.class);
                        double DiemTinHoc = diemSNS.child(dbHelper.FieldDiemTinHoc).getValue(double.class);
                        double DiemTiengAnh = diemSNS.child(dbHelper.FieldDiemTiengAnh).getValue(double.class);
                        diem = new Diem(IDDiem, hocSinh.getMSHS(), hocKy, DiemToan, DiemVatLy, DiemHoaHoc, DiemSinhHoc,
                                DiemLichSu, DiemDiaLy, DiemNguVan, DiemGDCD, DiemGDTC, DiemTinHoc, DiemTiengAnh);
                        Log.d("Dem Ca Nhan", "ID: " + hocSinh.getMSHS() + ", Điểm toán: " + DiemToan);
                    }
                }
                SetData(diem);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void SetData(Diem diem){
        binding.edtToan.setText(String.valueOf(diem.getDiemToan()));
        binding.edtVatLy.setText(String.valueOf(diem.getDiemVatLy()));
        binding.edtHoaHoc.setText(String.valueOf(diem.getDiemHoaHoc()));
        binding.edtNguVan.setText(String.valueOf(diem.getDiemNguVan()));
        binding.edtSinhHoc.setText(String.valueOf(diem.getDiemSinhHoc()));
        binding.edtLichSu.setText(String.valueOf(diem.getDiemLichSu()));
        binding.edtDiaLy.setText(String.valueOf(diem.getDiemDiaLy()));
        binding.edtGDCD.setText(String.valueOf(diem.getDiemGDCD()));
        binding.edtGDTC.setText(String.valueOf(diem.getDiemGDTC()));
        binding.edtTinHoc.setText(String.valueOf(diem.getDiemTinHoc()));
        binding.edtTiengAnh.setText(String.valueOf(diem.getDiemTiengAnh()));

        double DTB = (diem.getDiemToan() + diem.getDiemVatLy() + diem.getDiemHoaHoc() + diem.getDiemNguVan()
                + diem.getDiemSinhHoc() + diem.getDiemLichSu() + diem.getDiemGDCD() + diem.getDiemDiaLy()
                + diem.getDiemGDTC() + diem.getDiemTiengAnh() + diem.getDiemTinHoc()) / 11;
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String roundedDTB = decimalFormat.format(DTB);
        binding.txvDiemTB.setText(String.valueOf(roundedDTB));
        if (DTB >= 8){
            binding.txvXepLoai.setText("Xếp loại: Giỏi");
        } else if (DTB >= 6.5){
            binding.txvXepLoai.setText("Xếp loại: Khá");
        } else if (DTB >= 5) {
            binding.txvXepLoai.setText("Xếp loại: Trung bình");
        } else {
            binding.txvXepLoai.setText("Xếp loại: Yếu");
        }
        binding.getRoot().invalidate();
    }
    public void AddEvents(){
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double DiemToan = Double.parseDouble(binding.edtToan.getText().toString());
                double DiemVatLy = Double.parseDouble(binding.edtVatLy.getText().toString());
                double DiemHoaHoc = Double.parseDouble(binding.edtHoaHoc.getText().toString());
                double DiemSinhHoc = Double.parseDouble(binding.edtSinhHoc.getText().toString());
                double DiemLichSu = Double.parseDouble(binding.edtLichSu.getText().toString());
                double DiemDiaLy = Double.parseDouble(binding.edtDiaLy.getText().toString());
                double DiemNguVan = Double.parseDouble(binding.edtNguVan.getText().toString());
                double DiemGDCD = Double.parseDouble(binding.edtGDCD.getText().toString());
                double DiemGDTC = Double.parseDouble(binding.edtGDTC.getText().toString());
                double DiemTinHoc = Double.parseDouble(binding.edtTinHoc.getText().toString());
                double DiemTiengAnh = Double.parseDouble(binding.edtTiengAnh.getText().toString());

                double[] diems = {
                        DiemToan, DiemVatLy, DiemHoaHoc, DiemSinhHoc, DiemLichSu,
                        DiemDiaLy, DiemNguVan, DiemGDCD, DiemGDTC, DiemTinHoc,
                        DiemTiengAnh
                };
                for (double diem : diems) {
                    if (diem < 0 || diem > 10) {
                        Toast.makeText(getApplicationContext(), "Điểm không hợp lệ!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                String IDDiem = UUID.randomUUID().toString();
                Diem diem = new Diem(IDDiem, hocSinh.getMSHS(), hocKy, DiemToan, DiemVatLy, DiemHoaHoc, DiemSinhHoc, DiemLichSu,
                        DiemDiaLy, DiemNguVan, DiemGDCD, DiemGDTC, DiemTinHoc, DiemTiengAnh);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(dbHelper.ColecDiem);

                Map<String, Object> updates = new HashMap<>();
                String key = "ID" + hocSinh.getMSHS() +"_"+ hocKy;
                updates.put(dbHelper.FieldHocKy, hocKy);
                updates.put(dbHelper.FieldMSHS, hocSinh.getMSHS());
                updates.put(dbHelper.FieldDiemToan, DiemToan);
                updates.put(dbHelper.FieldDiemVatLy, DiemVatLy);
                updates.put(dbHelper.FieldDiemHoaHoc, DiemHoaHoc);
                updates.put(dbHelper.FieldDiemSinhHoc, DiemSinhHoc);
                updates.put(dbHelper.FieldDiemLichSu, DiemLichSu);
                updates.put(dbHelper.FieldDiemDiaLy, DiemDiaLy);
                updates.put(dbHelper.FieldDiemNguVan, DiemNguVan);
                updates.put(dbHelper.FieldDiemGDCD, DiemGDCD);
                updates.put(dbHelper.FieldDiemGDTC, DiemGDTC);
                updates.put(dbHelper.FieldDiemTinHoc, DiemTinHoc);
                updates.put(dbHelper.FieldDiemTiengAnh, DiemTiengAnh);
                myRef.child(key).updateChildren(updates);
                Toast.makeText(getApplicationContext(), "Nhập điểm thành công", Toast.LENGTH_SHORT).show();
                SendNotification(hocSinh.getMSHS(), "Thông báo đã nhập điểm " + hocKy +" cho học sinh");
            }
        });

        binding.spinnerHocKy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = (String) parentView.getItemAtPosition(position);
                Log.d("thien", selectedOption);
                switch (selectedOption) {
                    case "Học kì 1":{
                        hocKy = dbHelper.ValueHocKy1;
                        GetDiem(hocSinh.getMSHS());
                        break;
                    }
                    case "Học kì 2":{
                        hocKy = dbHelper.ValueHocKy2;
                        GetDiem(hocSinh.getMSHS());
                        break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không cần xử lý trong trường hợp này
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
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