package com.example.edusuport.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.adapter.HocSinhAdapter;
import com.example.edusuport.adapter.LopHoc_IdGV_Nav_Adapter;
import com.example.edusuport.adapter.ViewHolderClick;
import com.example.edusuport.controllers.DangTaiTaiLieuController;
import com.example.edusuport.controllers.LopHocController;
import com.example.edusuport.databinding.ActivityDanhSachHocSinhBinding;
import com.example.edusuport.databinding.ActivityNhanXetChungBinding;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.LopHoc;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DanhSachHocSinh extends AppCompatActivity {
    DBHelper dbHelper;
    ActivityDanhSachHocSinhBinding binding;
    LopHoc_IdGV_Nav_Adapter lopHocIdGVNavAdapter;
    String idShare="";
    private long SoLuong = 0;
    ArrayAdapter<HocSinhAdapter> adapter;
    ArrayList<HocSinh> listHS = new ArrayList<>();
    public  static GiaoVien giaoVien = Home.giaoVien;
    ArrayList<LopHoc> listLop=new ArrayList<LopHoc>();
    private LopHoc lopHoc = QuanLyLopHocActivity.lopHoc;
    LopHocController lopHocController=new LopHocController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDanhSachHocSinhBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DBHelper();

        GetDSHocSinh(lopHoc.getIdLopHoc());
        AddEvents();
    }
    public void GetDSHocSinh(String IDLopHoc){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecHocSinh);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listHS = new ArrayList<>();
                for (DataSnapshot monHocSnapshot : dataSnapshot.getChildren()) {
                    if (monHocSnapshot.child(dbHelper.FieldIDLopHoc).getValue(String.class).equals(IDLopHoc))
                    {
                        String Mshs = monHocSnapshot.getKey();
                        String Ten = monHocSnapshot.child(dbHelper.FieldTenPH).getValue(String.class);
                        String IDLopHoc = monHocSnapshot.child(dbHelper.FieldIDLopHoc).getValue(String.class);
                        String url = monHocSnapshot.child("urlAva").getValue(String.class);
                        HocSinh hs = new HocSinh(Mshs, Ten, IDLopHoc, url);
                        listHS.add(hs);
                        Log.d("Don Xin Phep", "ID: " + Mshs + ",Tên : " + Ten);
                    }
                }
                SetData(listHS);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void SetData(ArrayList<HocSinh> list){
        adapter = new HocSinhAdapter(this, R.layout.item_giao_vien_layout, list);
        binding.lstHocSinh.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void AddEvents(){
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachHocSinh.this, ThemHocSinhActivity.class);
                startActivity(intent);
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });

        binding.lstHocSinh.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showBottomSheet(listHS.get(position));
//                Intent intent = new Intent(DanhSachHocSinh.this, ThemHocSinhActivity.class);
//                intent.putExtra("hocSinh", listHS.get(position));
//                startActivity(intent);
                return false;
            }
        });
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<HocSinh> filteredThuGopYList = filter(listHS, newText);
                SetData(filteredThuGopYList);
                Log.d("ListFillert", "ID: " + filteredThuGopYList.size());
                return true;
            }
        });
    }
    private ArrayList<HocSinh> filter(ArrayList<HocSinh> thuGopYList, String query) {
        ArrayList<HocSinh> filteredList = new ArrayList<>();
        for (HocSinh hocSinh : thuGopYList) {
            if (hocSinh.getMSHS().toLowerCase().contains(query.toLowerCase())
                    || hocSinh.getTen().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(hocSinh);
            }
        }
        return filteredList;
    }
    public void showBottomSheet(HocSinh HS){
        final Dialog dialog=new Dialog(DanhSachHocSinh.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_hocsinh);

        TableRow sua=dialog.findViewById(R.id.bot_chinhsuaGFC);
        TableRow xoa=dialog.findViewById(R.id.bot_xoaGFC);
        TableRow chuyenlop=dialog.findViewById(R.id.bot_shareGFC);
        TextView ten=dialog.findViewById(R.id.txvTenHSBT);
        TextView mahs = dialog.findViewById(R.id.txvMaHSBT);
        ten.setText(HS.getTen());
        mahs.setText(HS.getMSHS());
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachHocSinh.this);
                LayoutInflater layoutInflater = LayoutInflater.from(DanhSachHocSinh.this);
                builder.setTitle("Chỉnh sửa tên học sinh");

                final View alertView = layoutInflater.inflate(R.layout.layout_sua_hoc_sinh, null);
                builder.setView(alertView);

                EditText txtTenHS=(EditText) alertView.findViewById(R.id.sua_txtTenHS);
                txtTenHS.setText(HS.getTen());
                txtTenHS.setTextColor(ContextCompat.getColor(DanhSachHocSinh.this, R.color.black_low_emp));

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        String tenNew= txtTenHS.getText().toString();

                        editHS(HS.getMSHS(),tenNew);
                        dialog.dismiss();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachHocSinh.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa không?");

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        GetSoLuong(HS);

                        dialogmini.dismiss();
                        dialog.dismiss();
                    }
                });

                // Nút hủy
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Hiển thị hộp thoại xác nhận
                AlertDialog dialog1 = builder.create();
                dialog1.show();
            }

        });
        chuyenlop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showBottomSheetShareLop(HS);
            }

        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations= com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    public void showBottomSheetShareLop(HocSinh hs){
        final Dialog dialog=new Dialog(DanhSachHocSinh.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.botsheet_xemthemlop);


        SearchView filterlop=dialog.findViewById(R.id.filterLop);
        RecyclerView morelophoc=dialog.findViewById(R.id.more_lophoc);

        // LopHoc_IdGV_Nav_Adapter lh =new LopHoc_IdGV_Nav_Adapter(listLop);


        lopHocController.getListLopHoc_idGV(giaoVien.getIDGiaoVien(), new LopHocController.DataRetrievedCallback_LopHoc() {
            @Override
            public void onDataRetrieved(ArrayList<LopHoc> monHocList) {

                listLop=monHocList;
                lopHocIdGVNavAdapter=new LopHoc_IdGV_Nav_Adapter(listLop );
                morelophoc.setAdapter(lopHocIdGVNavAdapter);
                Log.d("lít lóppspsp",morelophoc.toString());

            }
        });


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
        morelophoc.addOnItemTouchListener(new ViewHolderClick(DanhSachHocSinh.this, morelophoc, new ViewHolderClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String  id) {
                idShare= id;
                AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachHocSinh.this);
                builder.setTitle("Xác nhận chia sẻ");
                builder.setMessage("Bạn có chắc chuyển hs này tới "+listLop.get(position).getTenLopHoc());

                // Nút xác nhận
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        // Gọi hàm deleteTL() để xóa tài liệu
                        if(!hs.getIDLopHoc().equals(idShare)){
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference();
                            myRef.child("hocsinh").child(hs.getMSHS()).child("idlophoc").setValue(idShare);
                            myRef.child("phuhuynh").child(hs.getMSHS()+"PH").child("idlophoc").setValue(idShare);
                        }
                        else {
                            Toast.makeText(DanhSachHocSinh.this, "Lớp bị trùng lăpj, chọn lớp khác", Toast.LENGTH_SHORT).show();
                        }
                        dialogmini.dismiss();
                        dialog.dismiss();
                    }
                });

                // Nút hủy
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Đóng hộp thoại xác nhận
                    }
                });

                // Hiển thị hộp thoại xác nhận
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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

    public void editHS(String mshs,String tennew){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("hocsinh").child(mshs).child("ten").setValue(tennew);
    }
    public void deleteHS(String mshs){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        lopHocController.XoaHocSinh(mshs);
        Toast.makeText(this, "Xóa học sinh thành công", Toast.LENGTH_SHORT).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference lophocRef = database.getReference(dbHelper.ColecLopHoc).child(lopHoc.getIdLopHoc());

        Map<String, Object> updates = new HashMap<>();
        updates.put(dbHelper.FieldSoLuong, SoLuong);
        lophocRef.updateChildren(updates);
        GetDSHocSinh(lopHoc.getIdLopHoc());
    }
    public void Back(){
        Intent intent = new Intent(DanhSachHocSinh.this, QuanLyLopHocActivity.class);
        intent.putExtra("lopHoc", lopHoc);
        startActivity(intent);
    }
    public void GetSoLuong(HocSinh Hs){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecLopHoc).child(lopHoc.getIdLopHoc());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot fieldSoLuongSnapshot = dataSnapshot.child(dbHelper.FieldSoLuong);
                    if (fieldSoLuongSnapshot.exists()) {
                        SoLuong = fieldSoLuongSnapshot.getValue(Long.class) - 1;
                    }
                }
                deleteHS(Hs.getMSHS());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}