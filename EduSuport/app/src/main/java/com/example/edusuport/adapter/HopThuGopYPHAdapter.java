package com.example.edusuport.adapter;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
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
import com.example.edusuport.model.ThuGopY;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class HopThuGopYPHAdapter extends ArrayAdapter {
    Context context;
    DBHelper dbHelper;
    int resource;
    ArrayList<ThuGopY> list= new ArrayList<ThuGopY>();
    public HopThuGopYPHAdapter(@NonNull Context context, int resource, ArrayList<ThuGopY> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        dbHelper = new DBHelper();
    }
    private static class ViewHolder {
        TextView txvTieuDe;
        TextView txvThoiGian;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.item_thu_gop_yph, parent, false);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txvTieuDe = convertView.findViewById(R.id.txvTieuDe);
        viewHolder.txvThoiGian = convertView.findViewById(R.id.txvthoigian);

        ThuGopY item = list.get(position);

        viewHolder.txvTieuDe.setText(item.getTieuDe());

        viewHolder.txvThoiGian.setText("Thời gian gửi: " + item.getThoiGian().toString());

        return convertView;
    }

}
