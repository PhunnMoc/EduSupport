package com.example.edusuport.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.edusuport.R;
import com.example.edusuport.activity.ChinhsuaThoiKhoaBieuActivity;
import com.example.edusuport.activity.DangTaiTaiLieuActivity;
import com.example.edusuport.activity.DanhSachDxpPhActivity;
import com.example.edusuport.activity.DanhSachHocSinh;
import com.example.edusuport.activity.DanhSachLopHoc;
import com.example.edusuport.activity.DuyetDonXinNghiHocActivity;
import com.example.edusuport.activity.GuiThongBaoActivity;
import com.example.edusuport.activity.Home;
import com.example.edusuport.activity.HopThuGopYActivity;
import com.example.edusuport.activity.Main_DonXinNghiHoc_PH;
import com.example.edusuport.activity.Main_TheLat_GV;
import com.example.edusuport.activity.Main_ThuGopY_PH;
import com.example.edusuport.activity.NhanXetChungActivity;
import com.example.edusuport.activity.NhapDiemChungActivity;
import com.example.edusuport.activity.QuanLyLopHocActivity;
import com.example.edusuport.activity.ThemLopHocActivity;
import com.example.edusuport.activity.ThuGopYPhActivity;
import com.example.edusuport.activity.XemDiemActivity;
import com.example.edusuport.activity.XemNhanXetActivity;
import com.example.edusuport.activity.XemTaiLieuMon;
import com.example.edusuport.activity.XemTaiLieu_MonHSActivity;
import com.example.edusuport.activity.XemThoiKhoaBieuActivity;
import com.example.edusuport.activity.XemThongBaoActivity;
import com.example.edusuport.controllers.LopHocController;
import com.example.edusuport.model.ChucNang;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.MonHoc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChucNangHomeAdapter extends ArrayAdapter {
    Context context;
    LopHoc lopHoc;
    int resource;
    ArrayList<ChucNang> list= new ArrayList<ChucNang>();
    LopHocController lopHocController=new LopHocController();

    public ChucNangHomeAdapter(Context context, int resource, ArrayList<ChucNang> list) {
        super(context, resource, list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView= convertView;
        lopHoc= QuanLyLopHocActivity.lopHoc;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.icon_tailieu_gv, null);

        ImageView imgHinh = (ImageView)  customView.findViewById(R.id.imgMonHoc);
        TextView txtTen =(TextView) customView.findViewById(R.id.textTenMonHoc);
        txtTen.setText(list.get(position).getTenChucNang());
        String id = list.get(position).getIdChucNang();
        switch (id){
            case "XTKBHS":
            case "TKBGV":{
                imgHinh.setImageResource(R.drawable.ic_calendar);
                break;
            }
            case "XTBHS":
            case "TBGV":{
                imgHinh.setImageResource(R.drawable.icon_thongbao);
                break;
            }
            case "GYPH":
            case "GYGV":{
                imgHinh.setImageResource(R.drawable.icon_feedback);break;
            }
            case "DXNGV":
            case "DXPPH": {
                imgHinh.setImageResource(R.drawable.icon_license);break;
            }
            case "XTLHS":
            case "TLHTGV":{
                imgHinh.setImageResource(R.drawable.icon_document);break;
            }
            case "XDHS":
            case "XDPH":
            case "NDGV":{
                imgHinh.setImageResource(R.drawable.icon_score);break;
            }
            case "XNHHS":
            case "XNXPH":
            case "DGHSGV":{
                imgHinh.setImageResource(R.drawable.icon_dghs);break;
            }
            case "DTLGV": {
                imgHinh.setImageResource(R.drawable.icon_change_name);break;
            }
            case "XLGV": {
                imgHinh.setImageResource(R.drawable.icon_delete_class);break;
            }
            case "DSHSGV": {
                imgHinh.setImageResource(R.drawable.icon_list);break;
            }
        }



        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (id){
                    case "TBPH":{
                        Intent intent = new Intent(context, XemThongBaoActivity.class);
                        intent.putExtra("role", "phuhuynh");
                        context.startActivity(intent);
                        break;
                    }
                    case "TKBGV":{
                        Intent intent=new Intent(context, ChinhsuaThoiKhoaBieuActivity.class);
                        context.startActivity(intent);
                        break;
                    }
                    case "TBGV":{
                        Intent intent=new Intent(context, GuiThongBaoActivity.class);
                        context.startActivity(intent);
                        break;
                    } case "TLGV":{
                        Intent intent=new Intent(context, Main_TheLat_GV.class);
                        context.startActivity(intent);
                        break;
                    }
                    case "GYGV":{
                        Intent intent=new Intent(context, HopThuGopYActivity.class);
                        context.startActivity(intent);
                        break;
                    } case "DXNGV":{
                        Intent intent=new Intent(context, DuyetDonXinNghiHocActivity.class);
                        context.startActivity(intent);
                        break;
                    }
                    case "TLHTGV":{
                        Intent intent=new Intent(context, DangTaiTaiLieuActivity.class);
                        context.startActivity(intent);
                        break;
                    }
                    case "NDGV":{
                        Intent intent=new Intent(context, NhapDiemChungActivity.class);
                        context.startActivity(intent);
                        break;
                    }
                    case "DGHSGV":{
                        Intent intent=new Intent(context, NhanXetChungActivity.class);
                        context.startActivity(intent);
                        break;
                    }
                    case "DXPPH":{
                        Intent intent=new Intent(context, DanhSachDxpPhActivity.class);
                        context.startActivity(intent);
                        break;
                    }
                    case "GYPH":{
                        Intent intent=new Intent(context, Main_ThuGopY_PH.class);
                        context.startActivity(intent);
                        break;
                    }
                    case "XNXPH":{
                        Intent intent=new Intent(context, XemNhanXetActivity.class);
                        intent.putExtra("role", "phuhuynh");
                        context.startActivity(intent);
                        break;
                    }
                    case "XTKBHS":{
                        Intent intent=new Intent(context, XemThoiKhoaBieuActivity.class);
                        context.startActivity(intent);
                        break;
                    }
                    case "XTBHS":{
                        Intent intent=new Intent(context, XemThongBaoActivity.class);
                        intent.putExtra("role", "hocsinh");
                        context.startActivity(intent);
                        break;
                    }
                    case "XTLHS":{
                        Intent intent=new Intent(context, XemTaiLieuMon.class);
                        context.startActivity(intent);
                        break;
                    }
                    case "XDHS":{
                        Intent intent=new Intent(context, XemDiemActivity.class);
                        intent.putExtra("role", "hocsinh");
                        context.startActivity(intent);
                        break;
                    }
                    case "XDPH":{
                        Intent intent=new Intent(context, XemDiemActivity.class);
                        intent.putExtra("role", "phuhuynh");
                        context.startActivity(intent);
                        break;
                    }
                    case "XNHHS":{
                        Intent intent=new Intent(context, XemNhanXetActivity.class);
                        intent.putExtra("role", "hocsinh");
                        context.startActivity(intent);
                        break;
                    }
                    case "DSHSGV":{
                        Intent intent=new Intent(context, DanhSachHocSinh.class);
                        context.startActivity(intent);
                        break;
                    }
                    case "DTLGV":{
                        clickDTLGV();
                        break;
                    }
                    case "XLGV":{
                        clickXLGV();
                        break;
                    } case "TLHGV":{
                        Intent intent=new Intent(context, ThemLopHocActivity.class);
                        context.startActivity(intent);
                        break;
                    }
                    default:{
                        Log.e("2:07","sssss");
                    }
                }
            }
        });
        return customView;
    }
    public void clickXLGV(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa lớp không?");

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogmini, int which) {
                deleteLH(lopHoc.getIdLopHoc());
                dialogmini.dismiss();
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
    public void deleteLH(String idLop){
        lopHocController.deleteLopHoc(idLop, context, new LopHocController.UploadCallback() {
            @Override
            public void onUploadComplete() {
                Toast.makeText(context,"Xóa thành công", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context,Home.class);
                context.startActivity(intent);
            }

            @Override
            public void onUploadFailed(String errorMessage) {
                Toast.makeText(context,"Xóa lớp thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void clickDTLGV(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        builder.setTitle("Chỉnh sửa tên lớp học");

        final View alertView = layoutInflater.inflate(R.layout.layout_sua_hoc_sinh, null);
        builder.setView(alertView);

        TextView tvTitle=(TextView) alertView.findViewById(R.id.tvTitle);
        EditText txtTenHS=(EditText) alertView.findViewById(R.id.sua_txtTenHS);
        txtTenHS.setText(lopHoc.getTenLopHoc());
        tvTitle.setText("Tên lớp học");


        txtTenHS.setTextColor(ContextCompat.getColor(context, R.color.black_low_emp));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogmini, int which) {
                String tenNew= txtTenHS.getText().toString();
                editLH(lopHoc.getIdLopHoc(),tenNew);
                lopHoc.setTenLopHoc(tenNew);
                Intent intent=new Intent(context, QuanLyLopHocActivity.class);
                intent.putExtra("lopHoc", lopHoc);
                context.startActivity(intent);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void editLH(String idlh,String tennew){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("lophoc").child(idlh).child("tenlop").setValue(tennew).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context,"Sửa tên lớp thành công", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
