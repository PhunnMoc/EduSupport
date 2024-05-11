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
import com.example.edusuport.adapter.HopThuGopYPHAdapter;
import com.example.edusuport.databinding.ActivityMainThuGopYphBinding;
import com.example.edusuport.model.PhuHuynh;
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

public class Main_ThuGopY_PH extends AppCompatActivity {
    ActivityMainThuGopYphBinding binding;
    DBHelper dbHelper;
    ArrayList<ThuGopY> listThu = new ArrayList<>();
    ArrayAdapter<ThuGopY> adapter;
    Calendar calendar;
    Timestamp selectedTimestamp;
    private PhuHuynh phuHuynh = HomePhActivity.phuHuynh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainThuGopYphBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper();
        calendar = Calendar.getInstance();
        binding.searchView.requestFocus();
        GetThuGopY(phuHuynh.getMSPH());
        AddEvents();
    }
    public void GetThuGopY(String IDPhuHuynh){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecThuGopY);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listThu = new ArrayList<>();
                for (DataSnapshot thuSnapshot : dataSnapshot.getChildren()) {
                    String idNguoiGui = thuSnapshot.child(dbHelper.FieldIDNguoiGui).getValue(String.class);
                    if (idNguoiGui.equals(IDPhuHuynh))
                    {
                        String IDThu = thuSnapshot.getKey();
                        String IDGiaoVien = thuSnapshot.child(dbHelper.FieldIDGiaoVien).getValue(String.class);
                        String TieuDe = thuSnapshot.child(dbHelper.FieldTieuDe).getValue(String.class);
                        String NoiDung = thuSnapshot.child(dbHelper.FieldNoiDung).getValue(String.class);
                        long timestampLong = thuSnapshot.child(dbHelper.FieldThoiGian).getValue(Long.class);
                        boolean AnDanh = thuSnapshot.child(dbHelper.FieldAnDanh).getValue(boolean.class);
                        boolean Xem = thuSnapshot.child(dbHelper.FieldXem).getValue(boolean.class);
                        Timestamp thoigian = new Timestamp(timestampLong);
                        ThuGopY don = new ThuGopY(IDThu, idNguoiGui, IDGiaoVien, TieuDe, NoiDung, thoigian, AnDanh, Xem);
                        listThu.add(don);
                        Log.d("ThuGopY", "ID: " + thoigian + ", Lý do: " + NoiDung);
                    }
                }
                SetData(listThu);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void SetData(ArrayList<ThuGopY> list){
        adapter = new HopThuGopYPHAdapter(this, R.layout.item_thu_gop_y_layout, list);
        binding.lstThuGopYPH.setAdapter(adapter);
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
        ArrayList<ThuGopY> filteredList = new ArrayList<>();
        for (ThuGopY item : listThu) {
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
                SetData(listThu);
            }
        });
        binding.lstThuGopYPH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Main_ThuGopY_PH.this, ThuGopYPhActivity.class);
                intent.putExtra("thuGopY", listThu.get(position));
                startActivity(intent);
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
                ArrayList<ThuGopY> filteredThuGopYList = filter(listThu, newText);
                SetData(filteredThuGopYList);
                Log.d("ListFillert", "ID: " + filteredThuGopYList.size());
                return true;
            }
        });
        binding.btnVietThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_ThuGopY_PH.this, Main_them_gopy_PH.class);
                intent.putExtra("idPH", phuHuynh.getMSHS());
                startActivity(intent);
            }
        });
    }
    private ArrayList<ThuGopY> filter(ArrayList<ThuGopY> thuGopYList, String query) {
        ArrayList<ThuGopY> filteredList = new ArrayList<>();
        for (ThuGopY thuGopY : thuGopYList) {
            if (thuGopY.getTieuDe().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(thuGopY);
            }
        }
        return filteredList;
    }
    public void Back(){
        Intent intent = new Intent(Main_ThuGopY_PH.this, HomePhActivity.class);
        startActivity(intent);
    }
}