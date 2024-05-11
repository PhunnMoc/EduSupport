package com.example.edusuport.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edusuport.R;
import com.example.edusuport.adapter.LopHoc_IdGV_Nav_Adapter;
import com.example.edusuport.adapter.NhomTheAdapter;
import com.example.edusuport.adapter.NhomTheAdapterHS;
import com.example.edusuport.adapter.TaiLieuHocTapAdapter;
import com.example.edusuport.adapter.TaiLieuHocTapAdapterHS;
import com.example.edusuport.controllers.DangTaiTaiLieuController;
import com.example.edusuport.controllers.LopHocController;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.NhomThe;
import com.example.edusuport.model.TaiLieuHocTap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class XemTaiLieu_MonHSActivity extends AppCompatActivity {

    TextView textTenMonHoc;
    ListView lvFile;
    GridView gdCard;
    ImageButton imgBack;
    LinearLayout sortList;
    SearchView filterFile;
    TaiLieuHocTapAdapterHS taiLieuHocTapAdapterHS;
    ArrayList<TaiLieuHocTap> listf=new ArrayList<TaiLieuHocTap>();
    ArrayList<NhomThe> listGFC=new ArrayList<NhomThe>();
    NhomTheAdapterHS nhomTheAdapterHS;
    String idMon, tenFile;
    private static HocSinh hocSinh = HomeHsActivity.hocSinh;
    String idLop=hocSinh.getIDLopHoc();
    Intent data=null;

    android.app.AlertDialog.Builder builder;

    private View alertView;

    DangTaiTaiLieuController dangTaiTaiLieuController=new DangTaiTaiLieuController();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_tai_lieu_mon_hsactivity);
        loadTabs();
        doFormWidgetsFile();
        doFormWidgetsFlasCard();
        clickBack();
    }
    private void clickBack(){
        imgBack=(ImageButton) findViewById(R.id.back_TaiLieuMain);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void loadTabs(){
        textTenMonHoc = (TextView) findViewById(R.id.textTenMonHoc);
        idMon=getIntent().getStringExtra("idMon");
        // Toast.makeText(DangTaiTaiLieu_MonActivity.this, idLop.toString(), Toast.LENGTH_SHORT).show();
        dangTaiTaiLieuController.getMonHoc_idmon(idMon, new DangTaiTaiLieuController.DataRetrievedCallback_String() {
            @Override
            public void onDataRetrieved(String tenmon) {
                textTenMonHoc.setText(tenmon);
            }
        });

        //Lấy Tabhost id ra trước /
        final TabHost tab =(TabHost) findViewById(R.id.tabHost_tailieu);
        tab.setup();
        TabHost.TabSpec spec;
        spec= tab.newTabSpec("t1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("File");
        tab.addTab(spec);
        spec= tab.newTabSpec("t2");
        spec.setContent(R.id.tab2);
        spec.setIndicator ("FlashCard");
        tab.addTab(spec);
        tab.setCurrentTab(0);
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {
//                String s = "Tab tag="+arg0+"; index=" + tab.getCurrentTab();
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void doFormWidgetsFile()
    {

        lvFile=(ListView) findViewById(R.id.lv_fileTaiLieu);
        filterFile =(SearchView) findViewById(R.id.filterFile);

        filterFile.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<TaiLieuHocTap> temp=new ArrayList<TaiLieuHocTap>();
                for(TaiLieuHocTap file: listf){
                    if(file.getTenTaiLieu().toLowerCase().contains(newText.toLowerCase())){
                        temp.add(file);
                    }
                }
                taiLieuHocTapAdapterHS = new TaiLieuHocTapAdapterHS(XemTaiLieu_MonHSActivity.this,R.layout.item_tab_listtailieu, temp );
                lvFile.setAdapter(taiLieuHocTapAdapterHS);
                taiLieuHocTapAdapterHS.notifyDataSetChanged();
                return false;
            }
        });
//        sortList=(LinearLayout) findViewById(R.id.sort_listTL);
//        sortList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DangTaiTaiLieu_MonActivity.this, "sort",Toast.LENGTH_SHORT).show();
//
//            }
//        });
        dangTaiTaiLieuController.getListViewTaiLieu(idMon, idLop, new DangTaiTaiLieuController.DataRetrievedCallback_File() {
            @Override
            public void onDataRetrieved(ArrayList<TaiLieuHocTap> FileList) {
                listf=FileList;
                taiLieuHocTapAdapterHS = new TaiLieuHocTapAdapterHS(XemTaiLieu_MonHSActivity.this,R.layout.item_tab_listtailieu, listf );
                lvFile.setAdapter(taiLieuHocTapAdapterHS);
            }

        });


    }
    public void doFormWidgetsFlasCard()
    {
        gdCard=(GridView) findViewById(R.id.grid_flashcard);
        ArrayList<NhomThe> list=new ArrayList<NhomThe>();

        filterFile =(SearchView) findViewById(R.id.filterFC);

        filterFile.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<NhomThe> temp=new ArrayList<NhomThe>();
                for(NhomThe nhomThe: listGFC){
                    if(nhomThe.getTenNhomThe().toLowerCase().contains(newText.toLowerCase())){
                        temp.add(nhomThe);
                    }
                }
                nhomTheAdapterHS = new NhomTheAdapterHS(XemTaiLieu_MonHSActivity.this,R.layout.item_tab_flashcard, temp );
                gdCard.setAdapter(nhomTheAdapterHS);
                nhomTheAdapterHS.notifyDataSetChanged();
                return false;
            }
        });
        dangTaiTaiLieuController.getListGroupFC(idLop, idMon, new DangTaiTaiLieuController.DataRetrievedCallback_GroupFC() {
            @Override
            public void onDataRetrieved(ArrayList<NhomThe> nhomtheList) {
                listGFC=nhomtheList;
                Log.d("heeee",idLop);
                nhomTheAdapterHS = new NhomTheAdapterHS(XemTaiLieu_MonHSActivity.this,R.layout.item_tab_flashcard, listGFC );
                gdCard.setAdapter(nhomTheAdapterHS);
            }
        });



    }
}