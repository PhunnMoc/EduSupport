package com.example.edusuport.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edusuport.R;
import com.example.edusuport.adapter.MonHocAdapter;
import com.example.edusuport.adapter.NhomTheAdapter;
import com.example.edusuport.adapter.TheLatAdapterGV;
import com.example.edusuport.adapter.TheLatAdapterHS;
import com.example.edusuport.controllers.DangTaiTaiLieuController;
import com.example.edusuport.controllers.TheLat_HSGVController;
import com.example.edusuport.model.NhomThe;
import com.example.edusuport.model.TheLat;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class Main_TheLat_HS_viewCard extends AppCompatActivity {

    TheLatAdapterHS theLatAdapterHS;
    String idNhomThe,idMon,tenNhom;
    ListView lv;
    ImageButton btnPlay;
    ImageButton imgBack;
    TextView tenNhomThe;

    SearchView search;


    TheLat_HSGVController theLatHsgvController=new TheLat_HSGVController();
    ArrayList<TheLat> listtl=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_the_lat_hs_view_card);
        idNhomThe=getIntent().getStringExtra("idNhomThe");
        idMon=getIntent().getStringExtra("idMon");
        tenNhom=getIntent().getStringExtra("tenNhomThe");
        getWig();
        getData();

        handleAllClick();

        //  dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }
    private void getData(){
        theLatHsgvController.getListTheLat(idNhomThe, new TheLat_HSGVController.DataRetrievedCallback_TheLat() {
            @Override
            public void onDataRetrieved(ArrayList<TheLat> thelatList) {
                listtl=thelatList;
                theLatAdapterHS = new TheLatAdapterHS(Main_TheLat_HS_viewCard.this,R.layout.item_main_flashcard, listtl );
                lv.setAdapter(theLatAdapterHS);
            }
        });



    }
    private void getWig(){
        lv=(ListView) findViewById(R.id.lvTheLats);
        tenNhomThe=(TextView)findViewById(R.id.tenNhomThe);
        tenNhomThe.setText("FlashCard: "+ tenNhom);
        btnPlay=(ImageButton) findViewById(R.id.btn_stydyFC);
        imgBack=(ImageButton) findViewById(R.id.back_TaiLieuMain);
        search=(SearchView) findViewById(R.id.filterCard);

    }

    private void handleAllClick(){

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<TheLat> temp=new ArrayList<TheLat>();
                for(TheLat theLat: listtl){
                    if(theLat.getCauHoi().toLowerCase().contains(newText.toLowerCase()) || theLat.getCauTraLoi().toLowerCase().contains(newText.toLowerCase())){
                        temp.add(theLat);
                    }
                }
                TheLatAdapterHS tl = new TheLatAdapterHS(Main_TheLat_HS_viewCard.this,R.layout.item_tab_flashcard, temp );
                lv.setAdapter(tl);
                tl.notifyDataSetChanged();
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Main_TheLat_HS_viewCard.this,Main_TheLat_HS_Stydy.class);
                intent.putExtra("idNhomThe",idNhomThe);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


}