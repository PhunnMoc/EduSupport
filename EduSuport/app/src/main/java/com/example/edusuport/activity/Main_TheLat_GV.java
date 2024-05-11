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
import com.example.edusuport.controllers.DangTaiTaiLieuController;
import com.example.edusuport.controllers.TheLat_HSGVController;
import com.example.edusuport.model.NhomThe;
import com.example.edusuport.model.TheLat;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class Main_TheLat_GV extends AppCompatActivity {

    TheLatAdapterGV theLatAdapterGV;
    String idNhomThe,idMon,tenNhom;
    ListView lv;
    ImageButton btnAdd1;
    ImageButton imgBack;
    BottomSheetDialog dialog;
    SearchView search;
    TextView tenNhomThe;

    Button add;
    private String txtCauHoi,txtCauTraloi;
    TheLat_HSGVController theLatHsgvController=new TheLat_HSGVController();
    ArrayList<TheLat> listtl=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_thelat_flashcards);
        idNhomThe=getIntent().getStringExtra("idNhomThe");
        tenNhom=getIntent().getStringExtra("tenNhomThe");
        idMon=getIntent().getStringExtra("idMon");
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
                theLatAdapterGV = new TheLatAdapterGV(Main_TheLat_GV.this,R.layout.item_main_flashcard, listtl );
                lv.setAdapter(theLatAdapterGV);
            }
        });



    }
    private void showPopUpDialog() {
        android.app.AlertDialog.Builder  builder = new AlertDialog.Builder(Main_TheLat_GV.this);
        AlertDialog dialog = builder.create();
        LayoutInflater layoutInflater = LayoutInflater.from(Main_TheLat_GV.this);
        builder.setTitle("Thẻ: ");

        final View alertView = layoutInflater.inflate(R.layout.activity_click_add_card, null);
        builder.setView(alertView);
        EditText edCauHoi=(EditText) alertView.findViewById(R.id.add_cauhoi);
        EditText edCauTraLoi=(EditText) alertView.findViewById(R.id.add_cautraloi);
        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("cauhoi caut",edCauHoi.getText().toString()+edCauTraLoi.getText().toString());

                if(!edCauHoi.getText().toString().equals("") && !edCauTraLoi.getText().toString().equals("")){

                    theLatHsgvController.addTheLat(idNhomThe, edCauHoi.getText().toString(), edCauTraLoi.getText().toString(), new TheLat_HSGVController.UploadCallback() {
                        @Override
                        public void onUploadComplete() {
                            reloadListTL();
                            Toast.makeText(Main_TheLat_GV.this, "Thêm thành coongg ", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onUploadFailed(String errorMessage) {
                            Toast.makeText(Main_TheLat_GV.this, "Không thêm ssuowjc ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


           builder.create().show();
    }

    private void getWig(){
        lv=(ListView) findViewById(R.id.lvTheLats);
        tenNhomThe=(TextView)findViewById(R.id.tenNhomThe);
        tenNhomThe.setText("FlashCard: "+ tenNhom);
        btnAdd1=(ImageButton) findViewById(R.id.them1TheMoi);
        imgBack=(ImageButton) findViewById(R.id.back_TaiLieuMain);
        search=(SearchView) findViewById(R.id.filterCard);

    }
    private void  test(){
        dialog.show();
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
                TheLatAdapterGV tl = new TheLatAdapterGV(Main_TheLat_GV.this,R.layout.item_tab_flashcard, temp );
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
        btnAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpDialog();
              //  test();
            }
        });
    }
    private void handAddCard(){

        Toast.makeText(Main_TheLat_GV.this, txtCauHoi,Toast.LENGTH_SHORT).show();

    }
    public void reloadListTL(){
        listtl.clear();
        theLatHsgvController.getListTheLat(idNhomThe, new TheLat_HSGVController.DataRetrievedCallback_TheLat() {
            @Override
            public void onDataRetrieved(ArrayList<TheLat> thelatList) {
                listtl=thelatList;
                theLatAdapterGV.notifyDataSetChanged();
            }
        });
    }
}