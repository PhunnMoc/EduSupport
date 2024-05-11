package com.example.edusuport.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.adapter.GiaoVienAdapter;
import com.example.edusuport.adapter.HopThuGopYPHAdapter;
import com.example.edusuport.databinding.ActivityMainThemGopyPhBinding;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.PhuHuynh;
import com.example.edusuport.model.ThuGopY;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main_them_gopy_PH extends AppCompatActivity {
    DBHelper dbHelper;
    ActivityMainThemGopyPhBinding binding;
    ArrayList<GiaoVien> listGiaoVien = new ArrayList<>();
    ArrayList<GiaoVien> listGVDuocChon = new ArrayList<>();
    ArrayAdapter<GiaoVien> adapter;
    private PhuHuynh phuHuynh = HomePhActivity.phuHuynh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainThemGopyPhBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper();

        GetGiaoVien();
        AddEvents();
    }
    public void GetGiaoVien(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecGiaoVien);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listGiaoVien = new ArrayList<>();
                for (DataSnapshot thuSnapshot : dataSnapshot.getChildren()) {
                    String IDGiaoVien = thuSnapshot.getKey();
                    String TenGiaoVien = thuSnapshot.child(dbHelper.FieldTenPH).getValue(String.class);

                    GiaoVien gv = new GiaoVien(IDGiaoVien, TenGiaoVien);
                    listGiaoVien.add(gv);
                    Log.d("ThuGopY", "ID: " + IDGiaoVien + ", Tên: " + TenGiaoVien);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void AddEvents(){
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý khi người dùng gửi truy vấn tìm kiếm
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<GiaoVien> filteredGiaoVienList = filter(listGiaoVien, newText);
                SetData(filteredGiaoVienList);
                Log.d("ListFillert", "ID: " + filteredGiaoVienList.size());
                return true;
            }
        });
        binding.lstGiaoVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.searchView.setQuery("", false);
                listGVDuocChon = new ArrayList<>();
                binding.edtTieuDe.requestFocus();
                listGVDuocChon.add(listGiaoVien.get(position));
                SetData(listGVDuocChon);
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
    private ArrayList<GiaoVien> filter(ArrayList<GiaoVien> GVList, String query) {
        ArrayList<GiaoVien> filteredList = new ArrayList<>();
        for (GiaoVien gv : GVList) {
            if (gv.getTenGiaoVien().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(gv);
            }
        }
        return filteredList;
    }
    public void SetData(ArrayList<GiaoVien> list){
        adapter = new GiaoVienAdapter(this, R.layout.item_giao_vien_layout, list);
        binding.lstGiaoVien.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void Back(){
        Intent intent = new Intent(Main_them_gopy_PH.this, Main_ThuGopY_PH.class);
        startActivity(intent);
    }
    public boolean Send(){
        if (listGVDuocChon.size() < 1)
        {
            Toast.makeText(this, "Vui lòng chọn người nhận", Toast.LENGTH_SHORT).show();
            return false;
        }
        String TieuDe = binding.edtTieuDe.getText().toString();
        String NoiDung = binding.txiNoiDung.getText().toString();
        boolean AnDanh = binding.swAnDanh.isChecked();
        String IDGiaoVien = listGVDuocChon.get(0).getIDGiaoVien();

        if (TieuDe.equals("") || NoiDung.equals("")){
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        String IDNguoiGui = this.phuHuynh.getMSPH();
        String IDThu = UUID.randomUUID().toString();
        boolean Xem = false;
        long thoigian = System.currentTimeMillis();
        Map<String, Object> thuGopYMap = new HashMap<>();
        thuGopYMap.put(dbHelper.FieldIDNguoiGui, IDNguoiGui);
        thuGopYMap.put(dbHelper.FieldIDGiaoVien, IDGiaoVien);
        thuGopYMap.put(dbHelper.FieldTieuDe, TieuDe);
        thuGopYMap.put(dbHelper.FieldNoiDung, NoiDung);
        thuGopYMap.put(dbHelper.FieldThoiGian, thoigian);
        thuGopYMap.put(dbHelper.FieldAnDanh, AnDanh);
        thuGopYMap.put(dbHelper.FieldXem, Xem);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference thuGopYRef = database.getReference(dbHelper.ColecThuGopY);
        thuGopYRef.child(IDThu).setValue(thuGopYMap);
        Toast.makeText(this, "Đã gửi thư góp ý thành công", Toast.LENGTH_SHORT).show();
        return true;
    }

}