package com.example.edusuport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.model.ThongBao;
import com.example.edusuport.model.ThuGopY;

import java.util.ArrayList;

public class ThongBaoAdapter extends ArrayAdapter {
    Context context;
    DBHelper dbHelper;
    int resource;
    ArrayList<ThongBao> list= new ArrayList<ThongBao>();
    private ArrayList<String> tenPhuHuynhList = new ArrayList<>();
    public ThongBaoAdapter(@NonNull Context context, int resource, ArrayList<ThongBao> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        dbHelper = new DBHelper();
    }
    private static class ViewHolder {
        TextView txvTenNguoiGui, txvNoiDung;
        TextView txvThoiGian;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.item_thong_bao, parent, false);

        ThongBaoAdapter.ViewHolder viewHolder = new ThongBaoAdapter.ViewHolder();
        viewHolder.txvTenNguoiGui = convertView.findViewById(R.id.txvTenNG);
        viewHolder.txvNoiDung = convertView.findViewById(R.id.txvNoiDungTB);
        viewHolder.txvThoiGian = convertView.findViewById(R.id.txvThoiGianTB);
        ThongBao item = list.get(position);
        viewHolder.txvNoiDung.setText(item.getNoiDung());
        viewHolder.txvThoiGian.setText(item.getThoiGian().toString());
        dbHelper.getTenGiaoVienByID(item.getIDNguoiGui(), new DBHelper.TenHocSinhCallback() {
            @Override
            public void onTenHocSinhFetched(String tenPhuHuynh) {
                if (tenPhuHuynh != null) {
                    viewHolder.txvTenNguoiGui.setText("Người gửi: " + tenPhuHuynh);
                } else {
                }
            }
        });
        return convertView;
    }
}
