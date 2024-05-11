package com.example.edusuport.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edusuport.R;
import com.example.edusuport.adapter.MonHocAdapter;
import com.example.edusuport.controllers.DangKiGV_AuController;
import com.example.edusuport.controllers.DangTaiTaiLieuController;
import com.example.edusuport.controllers.LopHocController;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.MonHoc;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DangTaiTaiLieuActivity extends AppCompatActivity {

    GridView gvmon;
    MonHocAdapter monHocAdapter;
    TextView ten;
    CircleImageView imgAva;
    private static GiaoVien giaoVien = Home.giaoVien;
    public  static String idGV = giaoVien.getIDGiaoVien();

    public static final String SHARED_PREFS="sharePrefs";

    DangTaiTaiLieuController dangTaiTaiLieuController =new DangTaiTaiLieuController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tai_tai_lieu);


        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        gvmon=(GridView)findViewById(R.id.grid_monhoc);
        ten=findViewById(R.id.textTen);
        ten.setText("Giáo viên: " + giaoVien.getTenGiaoVien());
        imgAva=findViewById(R.id.imgAvatar);
        if(!giaoVien.getUrl().isEmpty()){
            Picasso.get().load(giaoVien.getUrl()).into(imgAva);
        }
        else {
            Picasso.get().load(R.drawable.profile).into(imgAva);
        }

        dangTaiTaiLieuController.setGridiewMonHoc(new DangTaiTaiLieuController.DataRetrievedCallback_MonHoc() {
            @Override
            public void onDataRetrieved(ArrayList<MonHoc> monHocList) {
                monHocAdapter = new MonHocAdapter(DangTaiTaiLieuActivity.this,R.layout.icon_tailieu_gv, monHocList );
                gvmon.setAdapter(monHocAdapter);
            }
        });


        addEventsClick();

    }





    private void addEventsClick() {

        ImageButton btnBack = findViewById(R.id.btnBackTL);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        gvmon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MonHoc monHoc =(MonHoc)  monHocAdapter.getItem(position);
                Intent intent= new Intent(DangTaiTaiLieuActivity.this,DangTaiTaiLieu_MonActivity.class);
                intent.putExtra("idMon",monHoc.getIdMon());
                // intent.putExtra("idGV","1");
                //LopHocController lopHocController=new LopHocController();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    public void Back(){
        super.onBackPressed();
    }
}