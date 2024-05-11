package com.example.edusuport.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.adapter.HopThuGopYAdapter;
import com.example.edusuport.adapter.ThongBaoAdapter;
import com.example.edusuport.databinding.ActivityHopThuGopYBinding;
import com.example.edusuport.databinding.ActivityXemThongBaoBinding;
import com.example.edusuport.model.DonXinNghiHoc;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.PhuHuynh;
import com.example.edusuport.model.ThongBao;
import com.example.edusuport.model.ThuGopY;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class XemThongBaoActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ActivityXemThongBaoBinding binding;
    ArrayList<ThongBao> listTB = new ArrayList<>();
    ArrayAdapter<ThongBaoAdapter> adapter;
    Calendar calendar;
    Timestamp selectedTimestamp;
    private HocSinh hocSinh = HomeHsActivity.hocSinh;
    private PhuHuynh phuHuynh = HomePhActivity.phuHuynh;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemThongBaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DBHelper();
        calendar = Calendar.getInstance();
        binding.searchView.requestFocus();
        Intent intent = getIntent();
        role = (String) intent.getSerializableExtra("role");
        String id = "";
        if (role.equals("hocsinh")){
            id = hocSinh.getMSHS();
        }else {
            id = phuHuynh.getMSPH();
        }
        Log.e("idđ",id.toString());

        GetThongBao(id);
        AddEvents();
    }
    public void GetThongBao(String ID){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecThongBao);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listTB = new ArrayList<>();
                for (DataSnapshot thuSnapshot : dataSnapshot.getChildren()) {
                    String idNguoiNhan = thuSnapshot.child(dbHelper.FieldIDNguoiNhan).getValue(String.class);
                    if (idNguoiNhan.equals(ID))
                    {
                        String IDThongBao = thuSnapshot.getKey();
                        String IDNguoiGui = thuSnapshot.child(dbHelper.FieldIDNguoiGui).getValue(String.class);
                        String NoiDung = thuSnapshot.child(dbHelper.FieldNoiDung).getValue(String.class);
                        long timestampLong = thuSnapshot.child(dbHelper.FieldThoiGian).getValue(Long.class);
                        Timestamp thoigian = new Timestamp(timestampLong);
                        ThongBao thongBao = new ThongBao(IDThongBao, IDNguoiGui, idNguoiNhan, NoiDung, thoigian);
                        listTB.add(thongBao);
                        Log.d("ThuGopY", "ID: " + thoigian + ", Lý do: " + NoiDung);
                    }
                }
                SetData(listTB);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void SetData(ArrayList<ThongBao> list){
        adapter = new ThongBaoAdapter(this, R.layout.item_thong_bao, list);
        binding.lstThuGopY.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void showDatePickerDialog(View v) {
        int year, month, day;
        if (selectedTimestamp != null) {
            // Nếu đã có selectedTimestamp, sử dụng ngày từ nó
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(selectedTimestamp.getTime());
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            // Nếu không, sử dụng ngày hiện tại
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        binding.searchView.setQueryHint(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Đặt giờ và phút thành 0
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        selectedTimestamp = new Timestamp(calendar.getTimeInMillis());
                        filterByDate(selectedTimestamp);
                        binding.searchView.requestFocus();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    public void filterByDate(Timestamp selectedDate) {
        ArrayList<ThongBao> filteredList = new ArrayList<>();
        for (ThongBao item : listTB) {
            if (isSameDate(item.getThoiGian(), selectedDate)) {
                filteredList.add(item);
            }
        }
        SetData(filteredList);
        Log.d("ListFillert", "ID: " + filteredList.size());

    }
    private boolean isSameDate(Timestamp timestamp1, Timestamp timestamp2) {
        // Kiểm tra xem hai Timestamp có null không
        Log.d("Date", "ID: " + timestamp1+ " và " + timestamp2);
        if (timestamp1 == null || timestamp2 == null) {
            return false;
        }

        // Lấy ngày tháng năm từ Timestamps
        Date date1 = new Date(timestamp1.getTime());
        Date date2 = new Date(timestamp2.getTime());

        // Lấy ngày tháng năm từ Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString1 = dateFormat.format(date1);
        String dateString2 = dateFormat.format(date2);

        // So sánh ngày giống nhau
        return dateString1.equals(dateString2);
    }
    public void AddEvents(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        binding.btnCancelFillerThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetData(listTB);
            }
        });
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý khi người dùng gửi truy vấn tìm kiếm
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<ThongBao> filteredThuGopYList = filter(listTB, newText);
                SetData(filteredThuGopYList);
                Log.d("ListFillert", "ID: " + filteredThuGopYList.size());
                return true;
            }
        });
    }
    private ArrayList<ThongBao> filter(ArrayList<ThongBao> thuGopYList, String query) {
        ArrayList<ThongBao> filteredList = new ArrayList<>();
        for (ThongBao thongBao : thuGopYList) {
            if (thongBao.getNoiDung().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(thongBao);
            }
        }
        return filteredList;
    }
    public void Back(){
        super.onBackPressed();
    }
}