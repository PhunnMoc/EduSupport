package com.example.edusuport.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edusuport.R;
import com.example.edusuport.adapter.ChucNangHomeAdapter;
import com.example.edusuport.adapter.LopHocAdapter;
import com.example.edusuport.adapter.ViewHolderClick;
import com.example.edusuport.controllers.LopHocController;
import com.example.edusuport.databinding.ActivityQuanLyLopHocBinding;
import com.example.edusuport.model.ChucNang;
import com.example.edusuport.model.DonXinNghiHoc;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.LopHoc;
import com.squareup.picasso.Picasso;
import com.example.edusuport.model.MonHoc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuanLyLopHocActivity extends AppCompatActivity {
    GridView gvChucNang;
    public static LopHoc lopHoc;
    ArrayList<ChucNang> ListCN=new ArrayList<>();
    ChucNangHomeAdapter chucNangHomeAdapter;
    LopHocController lopHocController=new LopHocController();
    private GiaoVien giaoVien = Home.giaoVien;
    ActivityQuanLyLopHocBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuanLyLopHocBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txvTenGV.setText("Giáo viên: " + giaoVien.getTenGiaoVien());
        Intent intent = getIntent();
        lopHoc = (LopHoc) intent.getSerializableExtra("lopHoc");
        binding.txvSiSo.setText("Sỉ số: "+ lopHoc.getSoLuong());
        binding.txvTenLop.setText(lopHoc.getTenLopHoc());

        binding.txvTenGV.setText("Giáo viên: " + giaoVien.getTenGiaoVien());
        Picasso.get().load(giaoVien.getUrl()).into(binding.imgava);

        getForm();
        getData();
        AddEvents();
    }
    private void getData() {
        ListCN.add(new ChucNang("DSHSGV","Danh sách học sinh"));
        ListCN.add(new ChucNang("DTLGV","Đổi tên lớp"));
        ListCN.add(new ChucNang("XLGV","Xóa lớp"));
        chucNangHomeAdapter=new ChucNangHomeAdapter(QuanLyLopHocActivity.this,R.layout.icon_tailieu_gv,ListCN);
        gvChucNang.setAdapter(chucNangHomeAdapter);


    }

    public void AddEvents(){
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        gvChucNang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

    }
    private void getForm() {
        gvChucNang=findViewById(R.id.grid_ChucNang);
    }
    public void Back(){
        Intent intent = new Intent(QuanLyLopHocActivity.this, Home.class);
        startActivity(intent);
    }
}