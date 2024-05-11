package com.example.edusuport.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.adapter.DonXinNghiHocAdapter;
import com.example.edusuport.adapter.LopHoc_IdGV_Nav_Adapter;
import com.example.edusuport.adapter.ViewHolderClick;
import com.example.edusuport.controllers.LopHocController;
import com.example.edusuport.databinding.ActivityDuyetDonXinNghiHocBinding;
import com.example.edusuport.model.DonXinNghiHoc;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.MonHoc;

import java.sql.Date;
import java.sql.Timestamp;

import com.example.edusuport.model.ThuGopY;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

public class DuyetDonXinNghiHocActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ActivityDuyetDonXinNghiHocBinding binding;
    ArrayList<DonXinNghiHoc> listDon = new ArrayList<>();
    ArrayAdapter<DonXinNghiHoc> adapter;
    androidx.appcompat.widget.SearchView searchView;
    Calendar calendar;
    Timestamp selectedTimestamp;
    String IDLop = "12B3";
    LopHoc_IdGV_Nav_Adapter lopHocIdGVNavAdapter;
    ArrayList<LopHoc> listLop=new ArrayList<LopHoc>();
    LopHocController lopHocController=new LopHocController();
    private GiaoVien giaoVien = Home.giaoVien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDuyetDonXinNghiHocBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chonLop();
        dbHelper = new DBHelper();
        calendar = Calendar.getInstance();
        AddEvents();
    }

    public void showDatePickerDialog(View v) {
        int year, month, day;
        if (selectedTimestamp != null) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(selectedTimestamp.getTime());
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
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
        Log.d("Date", "ID: " + timestamp1+ " và " + timestamp2);
        if (timestamp1 == null || timestamp2 == null) {
            return false;
        }
        Date date1 = new Date(timestamp1.getTime());
        Date date2 = new Date(timestamp2.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString1 = dateFormat.format(date1);
        String dateString2 = dateFormat.format(date2);
        return dateString1.equals(dateString2);
    }
    public void GetDonXinhPhep(String IDLopHoc){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecDonXinNghiHoc);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listDon = new ArrayList<>();
                for (DataSnapshot donXPSnapshot : dataSnapshot.getChildren()) {
                    if (donXPSnapshot.child(dbHelper.FieldIDLopHoc).getValue(String.class).equals(IDLopHoc))
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
        adapter = new DonXinNghiHocAdapter(this, R.layout.list_don_xin_phep_item, list);
        binding.lsvDonXinPhep.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void AddEvents(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        binding.btnCancelFillerDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetData(listDon);
            }
        });
        binding.lsvDonXinPhep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DuyetDonXinNghiHocActivity.this, DonXinPhepNghiHocActivity.class);
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
    }
    private ArrayList<DonXinNghiHoc> filter(ArrayList<DonXinNghiHoc> thuGopYList, String query) {
        ArrayList<DonXinNghiHoc> filteredList = new ArrayList<>();
        for (DonXinNghiHoc donXinNghiHoc : thuGopYList) {
            if (donXinNghiHoc.getMSHS().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(donXinNghiHoc);
            }
        }
        return filteredList;
    }
    public void chonLop(){

        binding.xemthemLophoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetMoreLop();
            }
        });
        lopHocController.getListLopHoc_idGV(giaoVien.getIDGiaoVien(), new LopHocController.DataRetrievedCallback_LopHoc() {
            @Override
            public void onDataRetrieved(ArrayList<LopHoc> monHocList) {
                listLop=monHocList;
                lopHocIdGVNavAdapter=new LopHoc_IdGV_Nav_Adapter(listLop);
                binding.rvChonLop.setAdapter(lopHocIdGVNavAdapter);
                binding.rvChonLop.setLayoutManager(new LinearLayoutManager(DuyetDonXinNghiHocActivity.this, LinearLayoutManager.HORIZONTAL, false));
            }
        });

        binding.rvChonLop.addOnItemTouchListener(new ViewHolderClick(DuyetDonXinNghiHocActivity.this, binding.rvChonLop, new ViewHolderClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String id) {
                IDLop=listLop.get(position).getIdLopHoc();
                GetDonXinhPhep(IDLop);
            }
            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
        binding.xemthemLophoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetMoreLop();
            }
        });
    }
    public void showBottomSheetMoreLop(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.botsheet_xemthemlop);
        SearchView filterlop=dialog.findViewById(R.id.filterLop);
        RecyclerView morelophoc=dialog.findViewById(R.id.more_lophoc);

        LopHoc_IdGV_Nav_Adapter lh;
        lopHocIdGVNavAdapter=new LopHoc_IdGV_Nav_Adapter(listLop);
        morelophoc.setAdapter(lopHocIdGVNavAdapter);
        filterlop.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<LopHoc> temp=new ArrayList<LopHoc>();
                for(LopHoc lh: listLop){
                    if(lh.getTenLopHoc().toLowerCase().contains(newText.toLowerCase())){
                        temp.add(lh);
                    }
                }
                LopHoc_IdGV_Nav_Adapter lh =new LopHoc_IdGV_Nav_Adapter(temp);
                morelophoc.setAdapter(lh);
                lh.notifyDataSetChanged();
                return false;
            }
        });

        //lopHocIdGVNavAdapter=new LopHoc_IdGV_Nav_Adapter(listLop);
        morelophoc.addOnItemTouchListener(new ViewHolderClick(DuyetDonXinNghiHocActivity.this, morelophoc, new ViewHolderClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String  id) {
                IDLop= id;
                GetDonXinhPhep(IDLop);
            }
            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations= com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    public void Back(){
        Intent intent = new Intent(DuyetDonXinNghiHocActivity.this, Home.class);
        startActivity(intent);
    }
}