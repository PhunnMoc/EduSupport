package com.example.edusuport.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edusuport.R;
import com.example.edusuport.adapter.LopHoc_IdGV_Nav_Adapter;
import com.example.edusuport.adapter.MessagesAdapter;
import com.example.edusuport.adapter.TaiLieuHocTapAdapter;
import com.example.edusuport.adapter.ViewHolderClick;
import com.example.edusuport.controllers.LopHocController;
import com.example.edusuport.controllers.MessController;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.MemoryData;
import com.example.edusuport.model.MessageList;
import com.example.edusuport.model.PhuHuynh;
import com.example.edusuport.model.TaiLieuHocTap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Messages extends AppCompatActivity {
    private static GiaoVien giaoVien= Home.giaoVien;
    public  List<MessageList> messageLists = new ArrayList<>();
    private RadioGroup rb_role;String textRole="HS";
    private String phone="0942523074";
    private String name="Trinh thu phuòng";

    public static String idCurUse=giaoVien.getIDGiaoVien();
    private int unseenMsg = 0;
    private String lastMsg = "";
    private String chatKey="";
    private boolean dataSet = false;
    private RecyclerView messageRecyclerView;
    SearchView filter;
    private MessagesAdapter messageAdapter;
    ValueEventListener eventListener;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String IDLop = "";
    LopHoc_IdGV_Nav_Adapter lopHocIdGVNavAdapter;
    ArrayList<LopHoc> listLop=new ArrayList<LopHoc>();
    RecyclerView rv_lophoc;
    ImageView xemthemlophoc;
    LopHocController lopHocController=new LopHocController();

    MessController messController=new MessController();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_message);
        messageRecyclerView = findViewById(R.id.msgRecylerView);
        filter= findViewById(R.id.filter);

//        get intent data from login
        phone = getIntent().getStringExtra("phone");
        name = getIntent().getStringExtra("name");
      //  idCurUse = getIntent().getStringExtra("idCurUse");
        TextView txvTenGV = findViewById(R.id.txvTenGV);
        txvTenGV.setText("Giáo viên: " + giaoVien.getTenGiaoVien());
        CircleImageView imgavt = findViewById(R.id.imgAvatar);
        Picasso.get().load(giaoVien.getUrl()).into(imgavt);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        chonLop();
        filter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<MessageList> temp=new ArrayList<MessageList>();
                for(MessageList file: messageLists){
                    if(file.getName().toLowerCase().contains(newText.toLowerCase()) ||file.getIdpartner().toLowerCase().contains(newText.toLowerCase())){
                        temp.add(file);
                    }
                }
                messageAdapter = new MessagesAdapter(temp,Messages.this);
                messageRecyclerView.setAdapter(messageAdapter);
                messageAdapter.notifyDataSetChanged();
                return false;
            }
        });
        messageRecyclerView.setHasFixedSize(true);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
        //set adapter to recyclerview

        messageAdapter = new MessagesAdapter(messageLists,Messages.this);
        messageRecyclerView.setAdapter(messageAdapter);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();
        messageLists.clear();

        messController.loadPartner_idCuren(idCurUse,messageRecyclerView,Messages.this);
        progressDialog.dismiss();

        messageAdapter.updateData(messageLists);


    }
    public void chonLop(){
        rv_lophoc=(RecyclerView) findViewById(R.id.rv_chonLop);
        xemthemlophoc = (ImageView) findViewById(R.id.xemthem_lophoc);
        rb_role=findViewById(R.id.radioBtn_role);
        rb_role.setVisibility(View.GONE);

        xemthemlophoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_role.setVisibility(View.GONE);
                messageLists.clear();
                messController.loadPartner_idCuren(idCurUse,messageRecyclerView,Messages.this);
            }
        });
        lopHocController.getListLopHoc_idGV(idCurUse, new LopHocController.DataRetrievedCallback_LopHoc() {
            @Override
            public void onDataRetrieved(ArrayList<LopHoc> monHocList) {

                listLop=monHocList;
                lopHocIdGVNavAdapter=new LopHoc_IdGV_Nav_Adapter(listLop);
                rv_lophoc.setAdapter(lopHocIdGVNavAdapter);
                rv_lophoc.setLayoutManager(new LinearLayoutManager(Messages.this, LinearLayoutManager.HORIZONTAL, false));


            }
        });



        rv_lophoc.addOnItemTouchListener(new ViewHolderClick(Messages.this, rv_lophoc, new ViewHolderClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String id) {
                ProgressDialog progressDialog = new ProgressDialog(Messages.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading.....");
                progressDialog.show();
                rb_role.setVisibility(View.VISIBLE);
                IDLop=listLop.get(position).getIdLopHoc();
                messageLists.clear();
                reloadCycleHS(idCurUse,id);
                rb_role.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId==R.id.radioBtn_HS){
                            textRole="HS"; reloadCycleHS(idCurUse,id);
                        } else{
                            textRole="PH";reloadCyclePH(idCurUse,id);
                        }

                    }
                });
                progressDialog.dismiss();


//                reLoadListf();
//                reLoadListGFC();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    private void reloadCycleHS(String idCurUse,String idLopHoc){

        messController.getListUserHS_idGV(idCurUse,idLopHoc, new MessController.DataRetrievedCallback_MessageList() {
            @Override
            public void onDataRetrieved(ArrayList<MessageList> MessageList) {
                messageLists=MessageList;
                messageAdapter = new MessagesAdapter(messageLists,Messages.this);
                messageRecyclerView.setAdapter(messageAdapter);
                Log.d("List mới",String.valueOf(messageLists));
                messageAdapter.updateData(messageLists);
            }
        });


    }
    private void reloadCyclePH(String idCurent,String idLopHoc){

        messController.getListUserPH_idGV(idCurent,idLopHoc, new MessController.DataRetrievedCallback_MessageList() {
            @Override
            public void onDataRetrieved(ArrayList<MessageList> MessageList) {
                messageLists=MessageList;
                messageAdapter = new MessagesAdapter(messageLists,Messages.this);
                messageRecyclerView.setAdapter(messageAdapter);
                Log.d("List mới",String.valueOf(messageLists));
                messageAdapter.updateData(messageLists);
            }
        });


    }
    public void Back(){
        Intent intent = new Intent(Messages.this, Home.class);
        startActivity(intent);
    }
}