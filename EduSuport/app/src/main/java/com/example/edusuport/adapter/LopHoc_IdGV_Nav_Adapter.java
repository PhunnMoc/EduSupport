package com.example.edusuport.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edusuport.R;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.MonHoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LopHoc_IdGV_Nav_Adapter  extends RecyclerView.Adapter<LopHoc_IdGV_Nav_Adapter.ViewHolder> {
   // Activity context;
    int resource;

    Activity context;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTen;
        public TextView txtId;
        public ViewHolder(View itemView) {

            super(itemView);
            txtTen =(TextView) itemView.findViewById(R.id.ten_navLopHoc);
            txtId =(TextView) itemView.findViewById(R.id.id_navLopHoc);
        }
    }
    ArrayList<LopHoc> List= new ArrayList<LopHoc>();
    public ArrayList<LopHoc> getList() {
        return List;
    }
    public List<Boolean> itemFocusList;

    public LopHoc_IdGV_Nav_Adapter(ArrayList<LopHoc> list) {
            List = list;
            itemFocusList = new ArrayList<>(Collections.nCopies(list.size(), false)); // Khởi tạo danh sách itemFocusList với kích thước bằng với list và giá trị mặc định là false

    }
    public void setItemFocus(int position, boolean isFocused) {
        itemFocusList = new ArrayList<>(Collections.nCopies(List.size(), false));
        itemFocusList.set(position, isFocused);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LopHoc_IdGV_Nav_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_chonlop, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView) ;
        return viewHolder;
        }

    @Override
    public void onBindViewHolder(@NonNull LopHoc_IdGV_Nav_Adapter.ViewHolder viewHolder, int position) {
        LopHoc lopHoc = List.get(position);
        TextView ten = viewHolder.txtTen;
        TextView id = viewHolder.txtId;
        ten.setText(lopHoc.getTenLopHoc());
        id.setText(lopHoc.getIdLopHoc());

        boolean isFocused = itemFocusList.get(position);
        if (isFocused) {
            ten.setBackgroundResource(R.drawable.background_subject_radius_click);
            ten.setTextColor(Color.parseColor("#5F61F0"));

        } else {
            ten.setBackgroundResource(R.drawable.background_subject_radius_lop); // Đặt lại background ban đầu
            ten.setTextColor(Color.parseColor("#686868"));
         //   textView.setTextColor(ContextCompat.getColor(context, R.color.black));
        }



    }
    @Override
    public int getItemCount() {
        return List.size();
    }

}

