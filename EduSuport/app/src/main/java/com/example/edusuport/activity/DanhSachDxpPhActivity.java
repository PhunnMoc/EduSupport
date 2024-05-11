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
import com.example.edusuport.adapter.DonXinNghiHocAdapter;
import com.example.edusuport.adapter.HopThuGopYPHAdapter;
import com.example.edusuport.databinding.ActivityDanhSachDxpPhBinding;
import com.example.edusuport.databinding.ActivityMainThuGopYphBinding;
import com.example.edusuport.model.DonXinNghiHoc;
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

public class DanhSachDxpPhActivity extends AppCompatActivity {
    ActivityDanhSachDxpPhBinding binding;
    DBHelper dbHelper;
    ArrayList<DonXinNghiHoc> listDon = new ArrayList<>();
    ArrayAdapter<DonXinNghiHoc> adapter;
    Calendar calendar;
    Timestamp selectedTimestamp;
    private PhuHuynh phuHuynh = HomePhActivity.phuHuynh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDanhSachDxpPhBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper();
        calendar = Calendar.getInstance();
        binding.searchView.requestFocus();
        GetDonXinPhep(phuHuynh.getMSHS());
        AddEvents();
    }
    public void GetDonXinPhep(String MSHS){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecDonXinNghiHoc);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listDon = new ArrayList<>();
                for (DataSnapshot donXPSnapshot : dataSnapshot.getChildren()) {
                    if (donXPSnapshot.child(dbHelper.FieldMSHS).getValue(String.class).equals(MSHS))
                    {
                        String idDon = donXPSnapshot.getKey();
                        String mshs = donXPSnapshot.child(dbHelper.FieldMSHS).getValue(String.class);
                        String lydo = donXPSnapshot.child(dbHelper.FieldLyDo).getValue(String.class);
                        long timestampTG = donXPSnapshot.child(dbHelper.FieldThoiGian).getValue(Long.class);
                        long timestampNgayNghi = donXPSnapshot.child(dbHelper.FieldNgayNghi).getValue(Long.class);
                        String trangthai = donXPSnapshot.child(dbHelper.FieldTrangThai).getValue(String.class);
                        Timestamp thoigiangui = new Timestamp(timestampTG);
                        Timestamp ngaynghi = new Timestamp(timestampNgayNghi);
                        DonXinNghiHoc don = new DonXinNghiHoc(idDon, mshs, lydo, ngaynghi, thoigiangui, trangthai);
                        listDon.add(don);
                        Log.d("Don Xin Phep", "ID: " + ngaynghi + ", Lý do: " + lydo);
                    }
                }
                SetData(listDon);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void SetData(ArrayList<DonXinNghiHoc> list){
        adapter = new DonXinNghiHocAdapter(this, R.layout.item_thu_gop_y_layout, list);
        binding.lstDXPPH.setAdapter(adapter);
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
        ArrayList<DonXinNghiHoc> filteredList = new ArrayList<>();
        for (DonXinNghiHoc item : listDon) {
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
                SetData(listDon);
            }
        });
        binding.lstDXPPH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DanhSachDxpPhActivity.this, XemDxpPhActivity.class);
                intent.putExtra("donXinNghiHoc", listDon.get(position));
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
                ArrayList<DonXinNghiHoc> filteredThuGopYList = filter(listDon, newText);
                SetData(filteredThuGopYList);
                Log.d("ListFillert", "ID: " + filteredThuGopYList.size());
                return true;
            }
        });
        binding.btnVietThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachDxpPhActivity.this, Main_DonXinNghiHoc_PH.class);
                intent.putExtra("idPH", phuHuynh.getMSHS());
                startActivity(intent);
            }
        });
    }
    private ArrayList<DonXinNghiHoc> filter(ArrayList<DonXinNghiHoc> thuGopYList, String query) {
        ArrayList<DonXinNghiHoc> filteredList = new ArrayList<>();
        for (DonXinNghiHoc thuGopY : thuGopYList) {
            if (thuGopY.getLyDo().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(thuGopY);
            }
        }
        return filteredList;
    }
    public void Back(){
        Intent intent = new Intent(DanhSachDxpPhActivity.this, HomePhActivity.class);
        startActivity(intent);
    }

}