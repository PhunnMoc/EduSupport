package com.example.edusuport.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.adapter.HocSinhAdapter;
import com.example.edusuport.adapter.LopHoc_IdGV_Nav_Adapter;
import com.example.edusuport.adapter.ViewHolderClick;
import com.example.edusuport.controllers.LopHocController;
import com.example.edusuport.databinding.ActivityNhapDiemChungBinding;
import com.example.edusuport.model.DonXinNghiHoc;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.LopHoc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NhapDiemChungActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ActivityNhapDiemChungBinding binding;
    ArrayAdapter<HocSinhAdapter> adapter;
    ArrayList<HocSinh> listHS = new ArrayList<>();
    public  static GiaoVien giaoVien = Home.giaoVien;
    String IDLH = "";
    RecyclerView rv_lophoc;
    ImageView xemthemlophoc;
    ArrayList<LopHoc> listLop=new ArrayList<LopHoc>();
    LopHoc_IdGV_Nav_Adapter lopHocIdGVNavAdapter;

    LopHocController lopHocController=new LopHocController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNhapDiemChungBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chonLop();
        dbHelper = new DBHelper();
        AddEvents();
    }
    public void GetDonXinhPhep(String IDLopHoc){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecHocSinh);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listHS = new ArrayList<>();
                for (DataSnapshot monHocSnapshot : dataSnapshot.getChildren()) {
                    if (monHocSnapshot.child(dbHelper.FieldIDLopHoc).getValue(String.class).equals(IDLopHoc))
                    {
                        String Mshs = monHocSnapshot.getKey();
                        String Ten = monHocSnapshot.child(dbHelper.FieldTenPH).getValue(String.class);
                        String IDLopHoc = monHocSnapshot.child(dbHelper.FieldIDLopHoc).getValue(String.class);
                        String url = monHocSnapshot.child("urlAva").getValue(String.class);
                        HocSinh don = new HocSinh(Mshs, Ten, IDLopHoc, url);
                        listHS.add(don);
                        Log.d("Don Xin Phep", "ID: " + Mshs + ",Tên : " + Ten);
                    }
                }
                SetData(listHS);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void SetData(ArrayList<HocSinh> list){
        adapter = new HocSinhAdapter(this, R.layout.item_giao_vien_layout, list);
        binding.lstHocSinh.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void AddEvents(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        binding.lstHocSinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NhapDiemChungActivity.this, NhapDiemCaNhanActivity.class);
                intent.putExtra("hocSinh", listHS.get(position));
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
                ArrayList<HocSinh> filteredThuGopYList = filter(listHS, newText);
                SetData(filteredThuGopYList);
                Log.d("ListFillert", "ID: " + filteredThuGopYList.size());
                return true;
            }
        });
    }
    private ArrayList<HocSinh> filter(ArrayList<HocSinh> thuGopYList, String query) {
        ArrayList<HocSinh> filteredList = new ArrayList<>();
        for (HocSinh hocSinh : thuGopYList) {
            if (hocSinh.getMSHS().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(hocSinh);
            }
        }
        return filteredList;
    }
    public void chonLop(){
        rv_lophoc=(RecyclerView) findViewById(R.id.rv_chonLop);
        xemthemlophoc = (ImageView) findViewById(R.id.xemthem_lophoc);
        xemthemlophoc.setOnClickListener(new View.OnClickListener() {
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
                rv_lophoc.setAdapter(lopHocIdGVNavAdapter);
                rv_lophoc.setLayoutManager(new LinearLayoutManager(NhapDiemChungActivity.this, LinearLayoutManager.HORIZONTAL, false));
            }
        });

        rv_lophoc.addOnItemTouchListener(new ViewHolderClick(NhapDiemChungActivity.this, rv_lophoc, new ViewHolderClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String id) {
                IDLH=listLop.get(position).getIdLopHoc();
                GetDonXinhPhep(IDLH);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        xemthemlophoc = (ImageView) findViewById(R.id.xemthem_lophoc);
        xemthemlophoc.setOnClickListener(new View.OnClickListener() {
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

        // LopHoc_IdGV_Nav_Adapter lh =new LopHoc_IdGV_Nav_Adapter(listLop);
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
                //morelophoc.setLayoutManager(new LinearLayoutManager(DangTaiTaiLieu_MonActivity.this, LinearLayoutManager.HORIZONTAL, false));
                lh.notifyDataSetChanged();
                return false;
            }
        });

        //lopHocIdGVNavAdapter=new LopHoc_IdGV_Nav_Adapter(listLop);
        morelophoc.addOnItemTouchListener(new ViewHolderClick(NhapDiemChungActivity.this, morelophoc, new ViewHolderClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String  id) {
                IDLH= id;
                GetDonXinhPhep(id);

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
        Intent intent = new Intent(NhapDiemChungActivity.this, Home.class);
        startActivity(intent);
    }
}