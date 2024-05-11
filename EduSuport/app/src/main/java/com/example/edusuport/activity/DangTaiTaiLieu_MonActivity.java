package com.example.edusuport.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edusuport.R;
import com.example.edusuport.adapter.LopHoc_IdGV_Nav_Adapter;
import com.example.edusuport.adapter.NhomTheAdapter;
import com.example.edusuport.adapter.TaiLieuHocTapAdapter;
import com.example.edusuport.adapter.ViewHolderClick;
import com.example.edusuport.controllers.DangTaiTaiLieuController;
import com.example.edusuport.controllers.LopHocController;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.NhomThe;
import com.example.edusuport.model.TaiLieuHocTap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ServerValue;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DangTaiTaiLieu_MonActivity extends AppCompatActivity {

    TextView textTenMonHoc;
    ListView lvFile;
    RecyclerView rv_lophoc;
    GridView gdCard;
    ImageButton imgBack;
    ImageView xemthemlophoc;
    FloatingActionButton btnuploadfileTL,btnAddFlashCard;
    LinearLayout sortList;
    SearchView filterFile;
    TaiLieuHocTapAdapter taiLieuHocTapAdapter;
    ArrayList<TaiLieuHocTap> listf=new ArrayList<TaiLieuHocTap>();
    ArrayList<NhomThe> listGFC=new ArrayList<NhomThe>();
    NhomTheAdapter nhomTheAdapter;
    String idMon,idGV=DangTaiTaiLieuActivity.idGV, tenFile,idLop=null;
    Intent data=null;

    android.app.AlertDialog.Builder builder;

    private View alertView;
    ArrayList<LopHoc> listLop=new ArrayList<LopHoc>();
    DangTaiTaiLieuController dangTaiTaiLieuController=new DangTaiTaiLieuController();;
    LopHocController lopHocController=new LopHocController();
    LopHoc_IdGV_Nav_Adapter lopHocIdGVNavAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tai_tai_lieu_mon);
        chonLop();
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
    public void chonLop(){
        rv_lophoc=(RecyclerView) findViewById(R.id.rv_chonLop);
        lopHocController.getListLopHoc_idGV(idGV, new LopHocController.DataRetrievedCallback_LopHoc() {
            @Override
            public void onDataRetrieved(ArrayList<LopHoc> monHocList) {

                listLop=monHocList;
                lopHocIdGVNavAdapter=new LopHoc_IdGV_Nav_Adapter(listLop);
                rv_lophoc.setAdapter(lopHocIdGVNavAdapter);
                rv_lophoc.setLayoutManager(new LinearLayoutManager(DangTaiTaiLieu_MonActivity.this, LinearLayoutManager.HORIZONTAL, false));


            }
        });

        rv_lophoc.addOnItemTouchListener(new ViewHolderClick(DangTaiTaiLieu_MonActivity.this, rv_lophoc, new ViewHolderClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String id) {
                idLop=listLop.get(position).getIdLopHoc();
                // lopHocIdGVNavAdapter.setItemFocus(position, true);
                reLoadListf();
                reLoadListGFC();

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
    public ArrayList<LopHoc> getListLop(){
        return listLop;
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
        morelophoc.addOnItemTouchListener(new ViewHolderClick(DangTaiTaiLieu_MonActivity.this, morelophoc, new ViewHolderClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String  id) {
                idLop= id;
                reLoadListf();
                reLoadListGFC();

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
///(1)/////////////////////////////////////////////////////////////////////////////////////////

    public void doFormWidgetsFile()
    {

        lvFile=(ListView) findViewById(R.id.lv_fileTaiLieu);
        btnuploadfileTL=(FloatingActionButton) findViewById(R.id.btn_uploadfileTL);
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
                taiLieuHocTapAdapter = new TaiLieuHocTapAdapter(DangTaiTaiLieu_MonActivity.this,R.layout.item_tab_listtailieu, temp );
                lvFile.setAdapter(taiLieuHocTapAdapter);
                taiLieuHocTapAdapter.notifyDataSetChanged();
                return false;
            }
        });
        sortList=(LinearLayout) findViewById(R.id.sort_listTL);
        sortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DangTaiTaiLieu_MonActivity.this, "sort",Toast.LENGTH_SHORT).show();

            }
        });
        dangTaiTaiLieuController.getListViewTaiLieu(idMon, idLop, new DangTaiTaiLieuController.DataRetrievedCallback_File() {
            @Override
            public void onDataRetrieved(ArrayList<TaiLieuHocTap> FileList) {
                listf=FileList;
                taiLieuHocTapAdapter = new TaiLieuHocTapAdapter(DangTaiTaiLieu_MonActivity.this,R.layout.item_tab_listtailieu, listf );
                lvFile.setAdapter(taiLieuHocTapAdapter);
            }

        });

        btnuploadfileTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(DangTaiTaiLieu_MonActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(DangTaiTaiLieu_MonActivity.this);
                builder.setTitle("Thêm tài liệu: ");

                alertView = layoutInflater.inflate(R.layout.activity_click_upfile_gv, null);
                builder.setView(alertView);

                EditText txtTenFile=(EditText) alertView.findViewById(R.id.txtTenFile);
                LinearLayout upFile=(LinearLayout) alertView.findViewById(R.id.btn_upNewfile);
                tenFile=txtTenFile.getText().toString();


                builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(data!=null && !txtTenFile.getText().toString().equals("")){
                            String ext="."+getFileExtension(data.getData());

                            ProgressDialog progressDialog = new ProgressDialog(DangTaiTaiLieu_MonActivity.this);
                            progressDialog.setMessage("Uploading...");
                            progressDialog.show();
                            dangTaiTaiLieuController.createNewFileTaiLieu_idmon(idMon, txtTenFile.getText().toString(), data.getData(), ext, idLop, new DangTaiTaiLieuController.UploadCallback() {
                                @Override
                                public void onUploadComplete() {
                                    reLoadListf();
                                    Toast.makeText(DangTaiTaiLieu_MonActivity.this, "Đăng thành công", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onUploadFailed(String errorMessage) {
                                    Toast.makeText(DangTaiTaiLieu_MonActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            });
                            data=null;
                            // Gọi callback trực tiếp khi việc thêm đã hoàn tất

                        }
                        else {
                            Toast.makeText(DangTaiTaiLieu_MonActivity.this, "Nhập chưa đầy đủ", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                upFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent();
                        intent.setType("*/*");
                        intent.setAction(intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);
                    }

                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }
    public void reLoadListf(){
        listf.clear();
        dangTaiTaiLieuController.getListViewTaiLieu(idMon, idLop, new DangTaiTaiLieuController.DataRetrievedCallback_File() {

            @Override
            public void onDataRetrieved(ArrayList<TaiLieuHocTap> FileList) {

                listf = FileList;
                taiLieuHocTapAdapter.notifyDataSetChanged();
            }
        });
    }
    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri,null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String filename =  cursor.getString(nameIndex);
        return filename;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeType = contentResolver.getType(uri);
        String fileExtension = null;
        if (mimeType != null) {
            fileExtension = mimeTypeMap.getExtensionFromMimeType(mimeType);
        }
        return fileExtension;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null  ){
            this.data=data;
            EditText txtTenFile=(EditText) alertView.findViewById(R.id.txtTenFile);
            String tenfile=getFileName(data.getData());
            txtTenFile.setText(tenfile);
        }


    }
    ///(1)/////////////////////////////////////////////////////////////////////////////////////////
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
                nhomTheAdapter = new NhomTheAdapter(DangTaiTaiLieu_MonActivity.this,R.layout.item_tab_flashcard, temp );
                gdCard.setAdapter(nhomTheAdapter);
                nhomTheAdapter.notifyDataSetChanged();
                return false;
            }
        });
        dangTaiTaiLieuController.getListGroupFC(idLop, idMon, new DangTaiTaiLieuController.DataRetrievedCallback_GroupFC() {
            @Override
            public void onDataRetrieved(ArrayList<NhomThe> nhomtheList) {
                listGFC=nhomtheList;
                nhomTheAdapter = new NhomTheAdapter(DangTaiTaiLieu_MonActivity.this,R.layout.item_tab_flashcard, listGFC );
                gdCard.setAdapter(nhomTheAdapter);
            }
        });



        btnAddFlashCard=(FloatingActionButton) findViewById(R.id.btn_addFlashCard) ;
        btnAddFlashCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(DangTaiTaiLieu_MonActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(DangTaiTaiLieu_MonActivity.this);
                builder.setTitle("Thêm FlashCard mới : ");

                alertView = layoutInflater.inflate(R.layout.activity_click_add_groupfc, null);
                builder.setView(alertView);

                EditText txtTenGroupFC=(EditText) alertView.findViewById(R.id.tenGroupFC);
                EditText txtMoTaGroupFC=(EditText) alertView.findViewById(R.id.motaGroupFC);
                builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!txtTenGroupFC.getText().toString().equals("") && !txtMoTaGroupFC.getText().toString().equals("") ){
                            dangTaiTaiLieuController.addNewGroupFC(txtTenGroupFC.getText().toString(), txtMoTaGroupFC.getText().toString(), idLop, idMon, new DangTaiTaiLieuController.UploadCallback() {
                                @Override
                                public void onUploadComplete() {
                                    reLoadListGFC();
                                    Toast.makeText(DangTaiTaiLieu_MonActivity.this, "NGON LÀNH", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onUploadFailed(String errorMessage) {
                                    Toast.makeText(DangTaiTaiLieu_MonActivity.this, "Nô nô", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                        else {
                            Toast.makeText(DangTaiTaiLieu_MonActivity.this, "Nhập chưa đầy đủ", Toast.LENGTH_LONG).show();
                        }

                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



    }
    public void reLoadListGFC(){
        listGFC.clear();
        dangTaiTaiLieuController.getListGroupFC(idLop, idMon, new DangTaiTaiLieuController.DataRetrievedCallback_GroupFC() {
            @Override
            public void onDataRetrieved(ArrayList<NhomThe> nhomtheList) {
                listGFC=nhomtheList;
                nhomTheAdapter.notifyDataSetChanged();
            }
        });
    }

}