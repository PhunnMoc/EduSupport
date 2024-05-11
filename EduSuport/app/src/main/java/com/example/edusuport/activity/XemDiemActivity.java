package com.example.edusuport.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.databinding.ActivityXemDiemBinding;
import com.example.edusuport.model.Diem;
import com.example.edusuport.model.DonXinNghiHoc;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.PhuHuynh;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class XemDiemActivity extends AppCompatActivity {
    ActivityXemDiemBinding binding;
    DBHelper dbHelper;
    Diem diem;
    HocSinh hocSinh;
    PhuHuynh phuHuynh;
    private String hocKy = "học kỳ 1";
    String tenHS;
    String mshs = "";
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemDiemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DBHelper();
        AddEvents();
        hocSinh = HomeHsActivity.hocSinh;
        phuHuynh = HomePhActivity.phuHuynh;
        Intent intent = getIntent();
        String role = (String) intent.getSerializableExtra("role");
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
        GetDiem(mshs);
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
                        diem = new Diem(IDDiem, mshs, hocKy, DiemToan, DiemVatLy, DiemHoaHoc, DiemSinhHoc,
                                DiemLichSu, DiemDiaLy, DiemNguVan, DiemGDCD, DiemGDTC, DiemTinHoc, DiemTiengAnh);
                        Log.d("Dem Ca Nhan", "ID: " +mshs + ", Điểm toán: " + DiemToan);
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
        Picasso.get().load(url).into(binding.ava);
        binding.txvTenHS.setText("Học sinh " + tenHS);

        binding.txvToan.setText(String.valueOf(diem.getDiemToan()));
        binding.txvVatLy.setText(String.valueOf(diem.getDiemVatLy()));
        binding.txvHoaHoc.setText(String.valueOf(diem.getDiemHoaHoc()));
        binding.txvNguVan.setText(String.valueOf(diem.getDiemNguVan()));
        binding.txvSinhHoc.setText(String.valueOf(diem.getDiemSinhHoc()));
        binding.txvLichSu.setText(String.valueOf(diem.getDiemLichSu()));
        binding.txvDiaLy.setText(String.valueOf(diem.getDiemDiaLy()));
        binding.txvGDCD.setText(String.valueOf(diem.getDiemGDCD()));
        binding.txvGDTC.setText(String.valueOf(diem.getDiemGDTC()));
        binding.txvTinHoc.setText(String.valueOf(diem.getDiemTinHoc()));
        binding.txvTiengAnh.setText(String.valueOf(diem.getDiemTiengAnh()));

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

        TextView[] textViews = {
                binding.txvToan, binding.txvVatLy, binding.txvHoaHoc,
                binding.txvNguVan, binding.txvSinhHoc, binding.txvLichSu,
                binding.txvDiaLy, binding.txvGDCD, binding.txvGDTC,
                binding.txvTinHoc, binding.txvTiengAnh
        };

        double[] diemValues = {
                diem.getDiemToan(), diem.getDiemVatLy(), diem.getDiemHoaHoc(),
                diem.getDiemNguVan(), diem.getDiemSinhHoc(), diem.getDiemLichSu(),
                diem.getDiemDiaLy(), diem.getDiemGDCD(), diem.getDiemGDTC(),
                diem.getDiemTinHoc(), diem.getDiemTiengAnh()
        };
        int colorExcellentScore = getResources().getColor(R.color.excellent_score);
        int colorGoodScore = getResources().getColor(R.color.good_score);
        int colorAverageScore = getResources().getColor(R.color.average_score);
        int colorBadScore = getResources().getColor(R.color.bad_score);
        for (int i = 0; i < textViews.length; i++) {
            TextView textView = textViews[i];
            double diemValue = diemValues[i];
            if (diemValue >= 8) {
                textView.setBackgroundResource(R.drawable.background_subject_excellent_score);
                textView.setTextColor(colorExcellentScore);
            } else if (diemValue >= 6.5) {
                textView.setTextColor(colorGoodScore);
                textView.setBackgroundResource(R.drawable.background_subject_good_score);
            } else if (diemValue >= 5){
                textView.setTextColor(colorAverageScore);
                textView.setBackgroundResource(R.drawable.background_subject_average_score);
            }else {
                textView.setTextColor(colorBadScore);
                textView.setBackgroundResource(R.drawable.background_subject_bad_score);
            }
        }

        binding.getRoot().invalidate();
    }
    public void AddEvents(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
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
                        GetDiem(mshs);
                        break;
                    }
                    case "Học kì 2":{
                        hocKy = dbHelper.ValueHocKy2;
                        GetDiem(mshs);
                        break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không cần xử lý trong trường hợp này
            }
        });
    }
    public void Back(){
        super.onBackPressed();
    }
}