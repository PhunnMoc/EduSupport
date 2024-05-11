package com.example.edusuport.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.databinding.ActivityMainDonXinNghiHocPhBinding;
import com.example.edusuport.databinding.ActivityMainThemGopyPhBinding;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.PhuHuynh;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main_DonXinNghiHoc_PH extends AppCompatActivity {

    ActivityMainDonXinNghiHocPhBinding binding;
    PhuHuynh phuHuynh = new PhuHuynh();

    DBHelper dbHelper;
    HocSinh hocSinh;
    long selectedTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDonXinNghiHocPhBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DBHelper();
        phuHuynh = HomePhActivity.phuHuynh;
        AddEvents();
    }
    private void AddEvents(){
        binding.btnchonNgayNghi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Main_DonXinNghiHoc_PH.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        if (selectedDate.before(c)) {
                            binding.txvHienThiNgay.setText("");
                            binding.txvError.setText("Bạn không thể chọn ngày ở quá khứ!!");
                            return;
                        }
                        Calendar futureDate = Calendar.getInstance();
                        futureDate.add(Calendar.DAY_OF_MONTH, 7);
                        if (selectedDate.after(futureDate)) {
                            binding.txvHienThiNgay.setText("");
                            binding.txvError.setText("Bạn chỉ có thể chọn ngày trong vòng 7 ngày từ ngày hiện tại.!!");
                            return;
                        }
                        // Ngày hợp lệ, hiển thị ngày đã chọn trên TextView
                        binding.txvHienThiNgay.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        binding.txvError.setText("");
                        selectedTimestamp = selectedDate.getTimeInMillis();
                    }
                }, year, month, day);
                datePickerDialog.show();

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
                if(Send()){
                    Back();
                }
            }
        });
    }
    public void Back(){
        Intent intent = new Intent(Main_DonXinNghiHoc_PH.this, HomePhActivity.class);
        startActivity(intent);
    }
    public boolean Send(){
        if (binding.txvHienThiNgay.getText().toString().equals(""))
        {
            Toast.makeText(this, "Vui lòng chọn ngày phù hợp", Toast.LENGTH_SHORT).show();
            return false;
        }
        String LyDo = binding.edtLyDo.getText().toString();
        if (LyDo.equals(""))
        {
            Toast.makeText(this, "Vui lòng nhập lý do", Toast.LENGTH_SHORT).show();
            return false;
        }
        long currentTime = System.currentTimeMillis();
        String IDDon = UUID.randomUUID().toString();
        Map<String, Object> donXinPhep = new HashMap<>();
        donXinPhep.put(dbHelper.FieldIDLopHoc, phuHuynh.getIDLopHoc());
        donXinPhep.put(dbHelper.FieldLyDo, LyDo);
        donXinPhep.put(dbHelper.FieldMSHS, phuHuynh.getMSHS());
        donXinPhep.put(dbHelper.FieldThoiGian, currentTime);
        donXinPhep.put(dbHelper.FieldNgayNghi, selectedTimestamp);
        donXinPhep.put(dbHelper.FieldTrangThai, dbHelper.ValueTTChuaDuyet);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference thuGopYRef = database.getReference(dbHelper.ColecDonXinNghiHoc);
        thuGopYRef.child(IDDon).setValue(donXinPhep);
        Toast.makeText(this, "Đã gửi đơn thành công", Toast.LENGTH_SHORT).show();
        return true;
    }

}
