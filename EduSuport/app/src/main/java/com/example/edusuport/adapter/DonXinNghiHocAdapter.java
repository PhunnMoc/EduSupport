package com.example.edusuport.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.model.DonXinNghiHoc;
import com.example.edusuport.model.MonHoc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class DonXinNghiHocAdapter extends ArrayAdapter {
    Context context;
    DBHelper dbHelper;
    int resource;
    ArrayList<DonXinNghiHoc> list= new ArrayList<DonXinNghiHoc>();
    public DonXinNghiHocAdapter(@NonNull Context context, int resource, ArrayList<DonXinNghiHoc> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        dbHelper = new DBHelper();
    }
    private static class ViewHolder {
        TextView txvTenNguoiGui;
        TextView txvThoiGian;
        TextView txvTrangThai;
        LinearLayout mainLayout;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder viewHolder = new ViewHolder();
        convertView = inflater.inflate(R.layout.item_don_xin_phep_layout, parent, false);


        viewHolder.txvTenNguoiGui = convertView.findViewById(R.id.txvTenHSXP);
        viewHolder.txvThoiGian = convertView.findViewById(R.id.txvTGXP);
        viewHolder.txvTrangThai = convertView.findViewById(R.id.txvTrangThaiXP);
        viewHolder.mainLayout = convertView.findViewById(R.id.layoutXPItem);

        DonXinNghiHoc item = list.get(position);
        Log.d("MSHSsssss", "ID: " + item.getMSHS());
        dbHelper.getTenHocSinhByMSHS(item.getMSHS(), new DBHelper.TenHocSinhCallback() {
            @Override
            public void onTenHocSinhFetched(String tenHocSinh) {
                if (tenHocSinh != null) {
                    viewHolder.txvTenNguoiGui.setText(tenHocSinh);
                } else {
                    // Xử lý khi không lấy được tên học sinh
                }
            }
        });
        viewHolder.txvThoiGian.setText(item.getThoiGian().toString());
        viewHolder.txvTrangThai.setText(item.getTrangThai());
        if (item.getTrangThai().equals(dbHelper.ValueTTChuaDuyet)){
            viewHolder.mainLayout.setBackgroundResource(R.drawable.background_subject_good_score);
        }
        else if (item.getTrangThai().equals(dbHelper.ValueTTDaDuyet)){
            viewHolder.mainLayout.setBackgroundResource(R.drawable.background_subject_excellent_score);
        }
        else {
            viewHolder.mainLayout.setBackgroundResource(R.drawable.background_subject_bad_score);
        }

        return convertView;
    }

}
