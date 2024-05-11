package com.example.edusuport.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edusuport.R;
import com.example.edusuport.adapter.ChucNangHomeAdapter;
import com.example.edusuport.adapter.LopHocAdapter;
import com.example.edusuport.adapter.ViewHolderClick;
import com.example.edusuport.adapter.ViewHolderClickLH;
import com.example.edusuport.controllers.LopHocController;
import com.example.edusuport.model.ChucNang;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.LopHoc;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {
    //public static GiaoVien giaoVien = new GiaoVien("123", "Nguyễn Hữu Thoại");
    public static GiaoVien giaoVien=null;
    GridView gvChucNang;
    CircleImageView ava;
    ArrayList<ChucNang> ListCN=new ArrayList<>();
    ArrayList<LopHoc> ListLH=new ArrayList<>();
    ChucNangHomeAdapter chucNangHomeAdapter;
    LopHocController lopHocController=new LopHocController();
    LopHocAdapter adapter;
    RecyclerView rcvLopHoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.form_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView txvTenGV = findViewById(R.id.txvTenGV);
        txvTenGV.setText("Giáo viên: " + giaoVien.getTenGiaoVien());
        CircleImageView imgavt = findViewById(R.id.imgAvatar);
        Picasso.get().load(giaoVien.getUrl()).into(imgavt);
        setLocaleFromDatabase(giaoVien.getNgonNgu());
        getForm();
        getData();
        GetLopHoc();
        AddEvents();
    }
    private void getData() {
        ListCN.add(new ChucNang("TKBGV",getResources().getString(R.string.editSchedule)));
        ListCN.add(new ChucNang("TBGV",getResources().getString(R.string.sendNoti)));
        ListCN.add(new ChucNang("GYGV",getResources().getString(R.string.seeParentOpinion)));
        ListCN.add(new ChucNang("DXNGV",getResources().getString(R.string.seeLeaveform)));
        ListCN.add(new ChucNang("TLHTGV",getResources().getString(R.string.uploadStudyMaterials)));
        ListCN.add(new ChucNang("NDGV",getResources().getString(R.string.inputScore)));
        ListCN.add(new ChucNang("DGHSGV",getResources().getString(R.string.reviewStudent)));
        ListCN.add(new ChucNang("TLHGV",getResources().getString(R.string.addClass)));
        chucNangHomeAdapter=new ChucNangHomeAdapter(Home.this,R.layout.icon_tailieu_gv,ListCN);
        gvChucNang.setAdapter(chucNangHomeAdapter);
    }
    public void AddEvents(){
        View includedLayout = findViewById(R.id.navbar_layout); // navbar_layout là ID của layout được include
        ConstraintLayout layoutHome = includedLayout.findViewById(R.id.layoutHome);
        ConstraintLayout layoutMessage = includedLayout.findViewById(R.id.layoutMessage);
        ConstraintLayout layoutEditProfile = includedLayout.findViewById(R.id.layoutEditProfile);

        layoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this, Home.class);
                startActivity(intent);
            }
        });
        layoutMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this, Messages.class);
                startActivity(intent);
            }
        });
        layoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this, EditInformationActivity.class);
                startActivity(intent);
            }
        });

        gvChucNang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        rcvLopHoc.addOnItemTouchListener(new ViewHolderClickLH(Home.this, rcvLopHoc, new ViewHolderClickLH.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String id) {
                LopHoc lopHoc = ListLH.get(position);
                Intent intent=new Intent(Home.this, QuanLyLopHocActivity.class);
                intent.putExtra("lopHoc", lopHoc);
                startActivity(intent);
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }
    private void getForm() {
        gvChucNang=findViewById(R.id.grid_ChucNang);
        rcvLopHoc = findViewById(R.id.rcvLopHoc);
        ava= findViewById(R.id.imgAvatar);
        if(!giaoVien.getUrl().isEmpty()){
            Picasso.get().load(giaoVien.getUrl()).into(ava);
        }
        else {
            Picasso.get().load(R.drawable.profile).into(ava);
        }
    }
    private void SetDataLopHoc(ArrayList<LopHoc> arrList){
        adapter = new LopHocAdapter(arrList);
        rcvLopHoc.setLayoutManager(new LinearLayoutManager(Home.this, LinearLayoutManager.HORIZONTAL, false));
        rcvLopHoc.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void GetLopHoc(){
        lopHocController.getListLopHoc_idGV(giaoVien.getIDGiaoVien(), new LopHocController.DataRetrievedCallback_LopHoc() {
            @Override
            public void onDataRetrieved(ArrayList<LopHoc> lopHocList) {
                ListLH=lopHocList;
                SetDataLopHoc(ListLH);
            }
        });
    }
    private void setLocaleFromDatabase(String selectedLanguage) {
        Configuration config = getResources().getConfiguration();
        Locale locale = new Locale(selectedLanguage);
        config.setLocale(locale);
        // Tạo một Context mới với Configuration mới
        Context newContext = createConfigurationContext(config);
        // Cập nhật ngôn ngữ cho ứng dụng
        Resources resources = newContext.getResources();
        getResources().updateConfiguration(config, resources.getDisplayMetrics());
    }
}