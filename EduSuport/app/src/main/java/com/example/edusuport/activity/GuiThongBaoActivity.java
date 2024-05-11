package com.example.edusuport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.adapter.GiaoVienAdapter;
import com.example.edusuport.adapter.LopHocListAdapter;
import com.example.edusuport.databinding.ActivityGuiThongBaoBinding;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.ThongBao;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.example.edusuport.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class GuiThongBaoActivity extends AppCompatActivity {
    ArrayList<HocSinh> listHS = new ArrayList<>();
    ArrayList<LopHoc> listLH = new ArrayList<>();
    ArrayList<LopHoc> listLHDuocChon = new ArrayList<>();
    ActivityGuiThongBaoBinding binding;
    DatabaseReference lophocRef, thongbaoRef;
    private GiaoVien giaoVien = Home.giaoVien;
    DBHelper dbHelper = new DBHelper();
    private LopHocListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuiThongBaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        thongbaoRef = FirebaseDatabase.getInstance().getReference(dbHelper.ColecThongBao);
        binding.txvTenGiaoVien.setText(getResources().getString(R.string.teacher) + giaoVien.getTenGiaoVien());
        GetLopHoc();
        AddEvents();
    }
    private void AddEvents(){
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý khi người dùng gửi truy vấn tìm kiếm
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<LopHoc> filteredGiaoVienList = filter(listLH, newText);
                SetData(filteredGiaoVienList);
                Log.d("ListFillert", "ID: " + filteredGiaoVienList.size());
                return true;
            }
        });
        binding.lstLopHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.searchView.setQuery("", false);
                listLHDuocChon = new ArrayList<>();
                binding.txtNoiDung.requestFocus();
                listLHDuocChon.add(listLH.get(position));
                SetData(listLHDuocChon);
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        binding.btnXacnhangui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các EditText
                String NoiDung = binding.txtNoiDung.getText().toString();
                if (listLHDuocChon.size() < 1)
                {
                    Toast.makeText(GuiThongBaoActivity.this, "Vui lòng chọn người nhận", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (NoiDung.isEmpty()) {
                    Toast.makeText(GuiThongBaoActivity.this, "Vui lòng nhập nội dung thông báo", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(dbHelper.ColecHocSinh);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listHS = new ArrayList<>();
                            for (DataSnapshot monHocSnapshot : dataSnapshot.getChildren()) {
                                if (monHocSnapshot.child(dbHelper.FieldIDLopHoc).getValue(String.class).equals(listLHDuocChon.get(0).getIdLopHoc()))
                                {
                                    String Mshs = monHocSnapshot.getKey();
                                    String Ten = monHocSnapshot.child(dbHelper.FieldTenPH).getValue(String.class);
                                    String IDLopHoc = monHocSnapshot.child(dbHelper.FieldIDLopHoc).getValue(String.class);

                                    HocSinh hs = new HocSinh(Mshs, Ten, IDLopHoc);
                                    listHS.add(hs);
                                    Log.d("Don Xin Phep", "ID: " + Mshs + ",Tên : " + Ten);
                                }
                            }
                            sendNotificationToClass(listHS, NoiDung);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }
    private void sendNotificationToClass(ArrayList<HocSinh> arrayList, String NoiDung) {
        for (int i = 0; i < arrayList.size(); i++){

            // Gui cho học sinh
            Map<String, Object> updates = new HashMap<>();
            String IDThongBao = UUID.randomUUID().toString();
            long thoigian = System.currentTimeMillis();
            updates.put(dbHelper.FieldIDNguoiGui, giaoVien.getIDGiaoVien());
            updates.put(dbHelper.FieldIDNguoiNhan, arrayList.get(i).getMSHS());
            updates.put(dbHelper.FieldNoiDung, NoiDung);
            updates.put(dbHelper.FieldThoiGian, thoigian);
            thongbaoRef.child(IDThongBao).updateChildren(updates);

            // Gui cho phụ huynh
            updates = new HashMap<>();
            String MSPH = arrayList.get(i).getMSHS() + "PH";
            IDThongBao = UUID.randomUUID().toString();
            updates.put(dbHelper.FieldIDNguoiGui, giaoVien.getIDGiaoVien());
            updates.put(dbHelper.FieldIDNguoiNhan,MSPH);
            updates.put(dbHelper.FieldNoiDung, NoiDung);
            updates.put(dbHelper.FieldThoiGian, thoigian);
            thongbaoRef.child(IDThongBao).updateChildren(updates);
        }
        Toast.makeText(GuiThongBaoActivity.this, "Gửi thành công", Toast.LENGTH_SHORT).show();
        super.onBackPressed();

    }
    public void GetLopHoc(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecLopHoc);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listLH = new ArrayList<>();
                for (DataSnapshot thuSnapshot : dataSnapshot.getChildren()) {
                    String IDGiaoVien = thuSnapshot.child(dbHelper.FieldIDGiaoVien).getValue(String.class);
                    if (IDGiaoVien.equals(giaoVien.getIDGiaoVien())){
                        String IDLopHoc = thuSnapshot.getKey();
                        String TenLop = thuSnapshot.child(dbHelper.FieldTenLop).getValue(String.class);
                        LopHoc lh = new LopHoc(IDLopHoc, IDGiaoVien, TenLop);
                        listLH.add(lh);
                    }
                }
                SetData(listLH);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void SetData(ArrayList<LopHoc> list){
        adapter = new LopHocListAdapter(this, R.layout.item_giao_vien_layout, list);
        binding.lstLopHoc.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private ArrayList<LopHoc> filter(ArrayList<LopHoc> GVList, String query) {
        ArrayList<LopHoc> filteredList = new ArrayList<>();
        for (LopHoc gv : GVList) {
            if (gv.getTenLopHoc().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(gv);
            }
        }
        return filteredList;
    }
    public void Back(){
        Intent intent = new Intent(GuiThongBaoActivity.this, Home.class);
        startActivity(intent);
    }
}