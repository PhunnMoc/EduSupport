package com.example.edusuport.adapter;

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
import com.example.edusuport.model.GiaoVien;

import java.util.ArrayList;

public class GiaoVienAdapter extends ArrayAdapter {
    Context context;
    DBHelper dbHelper;
    int resource;
    ArrayList<GiaoVien> list= new ArrayList<GiaoVien>();
    public GiaoVienAdapter(@NonNull Context context, int resource, ArrayList<GiaoVien> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        dbHelper = new DBHelper();
    }
    private static class ViewHolder {
        de.hdodenhof.circleimageview.CircleImageView imvAvaGV;
        TextView txvTenGV;
        TextView txvMaGV;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder viewHolder = new ViewHolder();
        convertView = inflater.inflate(R.layout.item_giao_vien_layout, parent, false);

        viewHolder.txvTenGV = convertView.findViewById(R.id.txvTenGV);
        viewHolder.txvMaGV = convertView.findViewById(R.id.txvMaGV);
        viewHolder.imvAvaGV = convertView.findViewById(R.id.avaGV);

        GiaoVien item = list.get(position);
        dbHelper.getTenGiaoVienByID(item.getIDGiaoVien(), new DBHelper.TenHocSinhCallback() {
            @Override
            public void onTenHocSinhFetched(String tenHocSinh) {
                if (tenHocSinh != null) {
                    viewHolder.txvTenGV.setText(tenHocSinh);
                } else {
                    // Xử lý khi không lấy được tên học sinh
                }
            }
        });
        viewHolder.txvMaGV.setText("Mã giáo viên: " +item.getIDGiaoVien());

        return convertView;
    }

}

