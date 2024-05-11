package com.example.edusuport.activity;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.edusuport.R;
import com.example.edusuport.adapter.MonHocAdapter;
import com.example.edusuport.model.MonHoc;
import com.example.edusuport.model.NgayTrongTuan;
import com.example.edusuport.model.TietHoc;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView lvApp;

    MonHocAdapter monHocAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvApp=(GridView) findViewById(R.id.list_view);

        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ArrayList<MonHoc> list=new ArrayList<>();
        list.add(new MonHoc( "1","Toán học"));
        list.add(new MonHoc( "2","Lý học"));
        list.add(new MonHoc( "3","Hóa học"));
        list.add(new MonHoc( "4","Toán học"));
        list.add(new MonHoc( "5","Lý học"));
        list.add(new MonHoc( "6","Hóa học"));


        monHocAdapter=new MonHocAdapter(MainActivity.this,R.layout.icon_tailieu_gv,list);
        lvApp.setAdapter(monHocAdapter);

        addEvents();

        // docDuLieuNgayTrongTuan();
    }
    private  void HandelClick(int position){

    }

    private void docDuLieuNgayTrongTuan() {
        DatabaseReference ngayTrongTuanRef = FirebaseDatabase.getInstance().getReference().child("ngaytrongtuan");

        ngayTrongTuanRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        NgayTrongTuan ngay = snapshot.getValue(NgayTrongTuan.class);
                        if (ngay != null) {
                            String idThu = ngay.getIDThu();
                            String tenThu = ngay.getTenThu();
                            Log.d(TAG, "IDThu: " + idThu + ", TenThu: " + tenThu);
                        }
                    }
                } else {
                    Log.d(TAG, "Data does not exist.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void addEvents() {
        lvApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HandelClick(position);

            }
        });
    }
}