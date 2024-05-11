package com.example.edusuport.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.edusuport.R;
import com.example.edusuport.adapter.FlashCardAdapter;
import com.example.edusuport.adapter.TheLatAdapterHS;
import com.example.edusuport.controllers.TheLat_HSGVController;
import com.example.edusuport.model.TheLat;

import java.util.ArrayList;

public class Main_TheLat_HS_Stydy extends AppCompatActivity {


    ImageButton imgBack;
    ViewPager viewPager;
    FlashCardAdapter flashCardAdapter;
    TheLat_HSGVController theLatHsgvController=new TheLat_HSGVController();
    ArrayList<TheLat> listtl=new ArrayList<>();
    String idNhomThe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_the_lat_hs);
        findViews();
        loadData();
      // Flip();
    }
    private void findViews() {
        viewPager=findViewById(R.id.viewpager_FC);
        imgBack=(ImageButton) findViewById(R.id.back_TaiLieuMain);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void loadData() {
        idNhomThe=getIntent().getStringExtra("idNhomThe");
        theLatHsgvController.getListTheLat(idNhomThe, new TheLat_HSGVController.DataRetrievedCallback_TheLat() {
            @Override
            public void onDataRetrieved(ArrayList<TheLat> thelatList) {
                listtl=thelatList;
                listtl.add(new TheLat("","","",""));
                flashCardAdapter = new FlashCardAdapter(Main_TheLat_HS_Stydy.this, listtl,viewPager);
                viewPager.setAdapter(flashCardAdapter);
                viewPager.setCurrentItem(0);
            }
        });
    }



}