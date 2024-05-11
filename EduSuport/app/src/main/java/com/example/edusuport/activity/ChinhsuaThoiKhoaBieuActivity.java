package com.example.edusuport.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.adapter.LopHoc_IdGV_Nav_Adapter;
import com.example.edusuport.adapter.ViewHolderClick;
import com.example.edusuport.controllers.LopHocController;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.MonHoc;
import com.example.edusuport.model.ThoiKhoaBieu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChinhsuaThoiKhoaBieuActivity extends AppCompatActivity {
    ArrayList<LopHoc> listLop=new ArrayList<LopHoc>();
    LopHoc_IdGV_Nav_Adapter lopHocIdGVNavAdapter;
    RecyclerView rv_lophoc;
    ImageView xemthemlophoc;
    LopHocController lopHocController=new LopHocController();
    private ArrayList<String> listMonHoc = new ArrayList<>();
    private ArrayList<String> listidMonHoc = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    Spinner spinner1, spinner2, spinner3, spinner4, spinner5, spinner6, spinner7, spinner8, spinner9, spinner10;
    ImageButton btnT2, btnT3, btnT4, btnT5, btnT6, btnT7;
    private GiaoVien giaoVien = Home.giaoVien;
    private String IDLopHoc = "";
    String currentThu;
    TabHost tabHost;
    ArrayList<ThoiKhoaBieu> listtkb;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinhsua_thoi_khoa_bieu);
        dbHelper = new DBHelper();
        spinner1 = findViewById(R.id.spiner1);
        spinner2 = findViewById(R.id.spiner2);
        spinner3 = findViewById(R.id.spiner3);
        spinner4 = findViewById(R.id.spiner4);
        spinner5 = findViewById(R.id.spiner5);
        spinner6 = findViewById(R.id.spiner6);
        spinner7 = findViewById(R.id.spiner7);
        spinner8 = findViewById(R.id.spiner8);
        spinner9 = findViewById(R.id.spiner9);
        spinner10 = findViewById(R.id.spiner10);

        btnT2 = findViewById(R.id.btnCST2);
        btnT3 = findViewById(R.id.btnCST3);
        btnT4 = findViewById(R.id.btnCST4);
        btnT5 = findViewById(R.id.btnCST5);
        btnT6 = findViewById(R.id.btnCST6);
        btnT7 = findViewById(R.id.btnCST7);
        chonLop();
        AddEvents();
        GetMonHoc();


    }
    public void GetMonHoc(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecMonHoc);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot monHocSnapshot : dataSnapshot.getChildren()) {
                    String idMonHoc = monHocSnapshot.getKey();
                    String tenMonHoc = monHocSnapshot.child(dbHelper.FieldTenMon).getValue(String.class);
                    MonHoc monHoc = new MonHoc(idMonHoc, tenMonHoc);
                    listMonHoc.add(monHoc.getTenMon());
                    listidMonHoc.add(monHoc.getIdMon());
                    Log.d("Mon Hoc", "ID: " + idMonHoc + ", Ten: " + tenMonHoc);
                }
                SetDataForSpinner();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
            }
        });
    }
    public void GetThoiKhoaBieu(String IDLop, String thu){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecThoiKhoaBieu);
        Query query = myRef.orderByChild(dbHelper.FieldIDLopHoc).equalTo(IDLop);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Kiểm tra thêm điều kiện về 'thu'
                    String currentthu = snapshot.child(dbHelper.FieldThu).getValue(String.class);
                    if (thu != null && thu.equals(currentthu)) {
                        // Dữ liệu thỏa mãn điều kiện, xử lý ở đây
                        String idMon = snapshot.child(dbHelper.FieldIDMon).getValue(String.class);
                        long tiet = snapshot.child(dbHelper.FieldTiet).getValue(Long.class);
                        ThoiKhoaBieu tkb = new ThoiKhoaBieu();
                        tkb.setIDMon(idMon);
                        tkb.setTiet(tiet);
                        listtkb.add(tkb);
                        // Xử lý dữ liệu ở đây, ví dụ: in ra thông tin
                        Log.d(TAG, "idMon: " + idMon + "Tiết " + tiet);
                    }
                }
                SetDefaultDataForSpinner(listtkb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
    public void SetDefaultDataForSpinner(ArrayList<ThoiKhoaBieu> lst){
        int[] result = PreprocessTKB(lst);
        spinner1.setSelection(result[0]);
        spinner10.setSelection(result[1]);
        spinner2.setSelection(result[2]);
        spinner3.setSelection(result[3]);
        spinner4.setSelection(result[4]);
        spinner5.setSelection(result[5]);
        spinner6.setSelection(result[6]);
        spinner7.setSelection(result[7]);
        spinner8.setSelection(result[8]);
        spinner9.setSelection(result[9]);
    }
    public int[] PreprocessTKB(ArrayList<ThoiKhoaBieu> lst){
        int[] result = new int[20];
        for (int i = 0; i < lst.size(); i++)
        {
            switch (lst.get(i).getIDMon()){
                case "iddialy":
                    result[i] = 0;
                    break;
                case "idgiaoduccongdan":
                    result[i] = 1;
                    break;
                case "idgiaoducthechat":
                    result[i] = 2;
                    break;
                case "idhoahoc":
                    result[i] = 3;
                    break;
                case "idlichsu":
                    result[i] = 4;
                    break;
                case "idnguvan":
                    result[i] = 5;
                    break;
                case "idsinhhoc":
                    result[i] = 6;
                    break;
                case "idtienganh":
                    result[i] = 7;
                    break;
                case "idtinhoc":
                    result[i] = 8;
                    break;
                case "idtoan":
                    result[i] = 9;
                    break;
                case "idvatly":
                    result[i] = 10;
                    break;
                case "nghihoc":
                    result[i] = 11;
                    break;
            }
        }
        return result;
    }
    public void addData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("thoikhoabieu");

// Giả sử thông tin cố định
        String idLop = "12B3";
        String idMonHoc = "idsinhhoc";
        String idGiaoVien = "123";
        String thu = "thu4";

// Thêm dữ liệu cho các tiết từ 2 đến 9
        for (int tiet = 1; tiet <= 10; tiet++) {
            // Tạo key cho mỗi tiết
            String key ="ID" + idLop + thu + "_" + tiet;

            // Tạo một Map để lưu thông tin của mỗi tiết
            Map<String, Object> tietData = new HashMap<>();
            tietData.put("idgiaovien", idGiaoVien);
            tietData.put("idlophoc", idLop);
            tietData.put("idmon", idMonHoc);
            tietData.put("thu", thu);
            tietData.put("tiet", tiet);
            // Thêm dữ liệu vào Firebase
            myRef.child(key).setValue(tietData);
        }
    }
    public void SaveThoiKhoaBieu(String IDLop, String thu){

        ArrayList<String> listIDSelected = new ArrayList<>();
        listIDSelected.add(listidMonHoc.get(spinner1.getSelectedItemPosition()));
        listIDSelected.add(listidMonHoc.get(spinner2.getSelectedItemPosition()));
        listIDSelected.add(listidMonHoc.get(spinner3.getSelectedItemPosition()));
        listIDSelected.add(listidMonHoc.get(spinner4.getSelectedItemPosition()));
        listIDSelected.add(listidMonHoc.get(spinner5.getSelectedItemPosition()));
        listIDSelected.add(listidMonHoc.get(spinner6.getSelectedItemPosition()));
        listIDSelected.add(listidMonHoc.get(spinner7.getSelectedItemPosition()));
        listIDSelected.add(listidMonHoc.get(spinner8.getSelectedItemPosition()));
        listIDSelected.add(listidMonHoc.get(spinner9.getSelectedItemPosition()));
        listIDSelected.add(listidMonHoc.get(spinner10.getSelectedItemPosition()));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("thoikhoabieu");

        Map<String, Object> updates = new HashMap<>();
        for (int i = 0; i < 10; i++){
            int tiet = i + 1;

            String key = "ID" + IDLop + thu +"_"+ tiet;
            updates.put(dbHelper.FieldIDMon, listIDSelected.get(i));
            updates.put(dbHelper.FieldIDGiaoVien, giaoVien.getIDGiaoVien());
            updates.put(dbHelper.FieldIDLopHoc, IDLop);
            updates.put(dbHelper.FieldTiet, tiet);
            updates.put(dbHelper.FieldThu, thu);

            myRef.child(key).updateChildren(updates);
        }
        Toast.makeText(this, "Chỉnh sửa thời khóa biểu thành công", Toast.LENGTH_SHORT).show();

    }
    public void SetDataForSpinner(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listMonHoc);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner3.setAdapter(adapter);
        spinner4.setAdapter(adapter);
        spinner5.setAdapter(adapter);
        spinner6.setAdapter(adapter);
        spinner7.setAdapter(adapter);
        spinner8.setAdapter(adapter);
        spinner9.setAdapter(adapter);
        spinner10.setAdapter(adapter);

        adapter.notifyDataSetChanged(); // Cập nhật adapter để hiển thị dữ liệu mới trên spinner

    }
    public void AddEvents(){
        ImageButton btnBack = findViewById(R.id.btnBackTKB);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        btnT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu2");
                currentThu = "thu2";
            }
        });
        btnT3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu3");
                currentThu = "thu3";
            }
        });
        btnT4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu4");
                currentThu = "thu4";
            }
        });
        btnT5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu5");
                currentThu = "thu5";
            }
        });
        btnT6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu6");
                currentThu = "thu6";
            }
        });
        btnT7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu7");
                currentThu = "thu7";
            }
        });
        ImageView btnLuuTKB = findViewById(R.id.btnLuuTKB);
        btnLuuTKB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveThoiKhoaBieu(IDLopHoc, currentThu);
            }
        });
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
                rv_lophoc.setLayoutManager(new LinearLayoutManager(ChinhsuaThoiKhoaBieuActivity.this, LinearLayoutManager.HORIZONTAL, false));


            }
        });

        rv_lophoc.addOnItemTouchListener(new ViewHolderClick(ChinhsuaThoiKhoaBieuActivity.this, rv_lophoc, new ViewHolderClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String id) {
                IDLopHoc=listLop.get(position).getIdLopHoc();
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu2");
                currentThu = "thu2";
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
        morelophoc.addOnItemTouchListener(new ViewHolderClick(ChinhsuaThoiKhoaBieuActivity.this, morelophoc, new ViewHolderClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String  id) {
                IDLopHoc= id;
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu2");
                currentThu = "thu2";

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
        super.onBackPressed();
    }

}