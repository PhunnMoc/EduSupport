package com.example.edusuport.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.edusuport.R;
import com.example.edusuport.model.MonHoc;

import java.util.ArrayList;

public class MonHocAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<MonHoc> List= new ArrayList<MonHoc>();

    public MonHocAdapter( Context context, int resource, ArrayList<MonHoc> list) {
        super(context, resource, list);
        List = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView= convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.icon_tailieu_gv, null);
       ImageView imgHinh = (ImageView)  customView.findViewById(R.id.imgMonHoc);
        TextView txtTen =(TextView) customView.findViewById(R.id.textTenMonHoc);
        txtTen.setText(List.get(position).getTenMon());
        String id=List.get(position).getIdMon();
        switch (id) {
            case "iddialy": {
                imgHinh.setImageResource(R.drawable.ic_geo);
                break;
            }
            case "idgiaoduccongdan": {
                imgHinh.setImageResource(R.drawable.icon_gdcd);
                break;
            }
            case "idgiaoducthechat": {
                imgHinh.setImageResource(R.drawable.icon_sport);
                break;
            }
            case "idhoahoc": {
                imgHinh.setImageResource(R.drawable.chemistry);
                break;
            }
            case "idlichsu": {
                imgHinh.setImageResource(R.drawable.ic_history);
                break;
            }
            case "idnguvan": {
                imgHinh.setImageResource(R.drawable.ic_vanhoc);
                break;
            }
            case "idsinhhoc": {
                imgHinh.setImageResource(R.drawable.ic_sinh_microscope);
                break;
            }
            case "idtienganh": {
                imgHinh.setImageResource(R.drawable.english);
                break;
            }
            case "idtinhoc": {
                imgHinh.setImageResource(R.drawable.icon_tech);
                break;
            }
            case "idtoan": {
                imgHinh.setImageResource(R.drawable.math);
                break;
            }
            case "idvatly": {
                imgHinh.setImageResource(R.drawable.ic_physical);
                break;
            }
            case "nghihoc": {
                imgHinh.setImageResource(R.drawable.icon_breaktime);
                break;
            }
        }
        return customView;
    }
}
