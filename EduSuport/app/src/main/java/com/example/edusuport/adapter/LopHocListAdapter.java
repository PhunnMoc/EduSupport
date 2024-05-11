package com.example.edusuport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.LopHoc;

import java.util.ArrayList;

public class LopHocListAdapter extends ArrayAdapter {

    Context context;
    DBHelper dbHelper;
    int resource;
    ArrayList<LopHoc> list= new ArrayList<LopHoc>();
    public LopHocListAdapter(@NonNull Context context, int resource, ArrayList<LopHoc> list) {
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
        LopHocListAdapter.ViewHolder viewHolder = new LopHocListAdapter.ViewHolder();
        convertView = inflater.inflate(R.layout.item_giao_vien_layout, parent, false);

        viewHolder.txvTenGV = convertView.findViewById(R.id.txvTenGV);
        viewHolder.txvMaGV = convertView.findViewById(R.id.txvMaGV);
        viewHolder.imvAvaGV = convertView.findViewById(R.id.avaGV);
        viewHolder.imvAvaGV.setImageResource(R.drawable.img_list_class);
        LopHoc item = list.get(position);
        viewHolder.txvTenGV.setText(item.getTenLopHoc());

        viewHolder.txvMaGV.setText("Mã lớp: " +item.getIdLopHoc());

        return convertView;
    }

}


