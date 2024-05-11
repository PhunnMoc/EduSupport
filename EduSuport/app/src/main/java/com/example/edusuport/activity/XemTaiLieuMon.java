package com.example.edusuport.activity;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.edusuport.R;
import com.example.edusuport.adapter.MonHocAdapter;
import com.example.edusuport.controllers.DangKiGV_AuController;
import com.example.edusuport.controllers.DangTaiTaiLieuController;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.MonHoc;

import java.util.ArrayList;

public class XemTaiLieuMon extends AppCompatActivity {
    GridView gvmon;
    MonHocAdapter monHocAdapter;

    private static HocSinh hocSinh = HomeHsActivity.hocSinh;


    DangTaiTaiLieuController dangTaiTaiLieuController =new DangTaiTaiLieuController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tai_tai_lieu);
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        gvmon=(GridView)findViewById(R.id.grid_monhoc);

        dangTaiTaiLieuController.setGridiewMonHoc(new DangTaiTaiLieuController.DataRetrievedCallback_MonHoc() {
            @Override
            public void onDataRetrieved(ArrayList<MonHoc> monHocList) {
                monHocAdapter = new MonHocAdapter(XemTaiLieuMon.this,R.layout.icon_tailieu_gv, monHocList );
                gvmon.setAdapter(monHocAdapter);
            }
        });


        addEventsClick();

    }



    private void HandelClick(int position){
        Toast.makeText(XemTaiLieuMon.this,"position"+position,Toast.LENGTH_SHORT).show();
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
                Intent intent= new Intent(XemTaiLieuMon.this,XemTaiLieu_MonHSActivity.class);
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