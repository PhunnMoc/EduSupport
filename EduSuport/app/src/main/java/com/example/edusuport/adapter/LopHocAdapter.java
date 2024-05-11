package com.example.edusuport.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edusuport.R;
import com.example.edusuport.model.LopHoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LopHocAdapter extends RecyclerView.Adapter<LopHocAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTenLop;
        public ImageView imgAvt;
        public TextView txtSiSo;
        public ViewHolder(View itemView) {

            super(itemView);
            imgAvt = itemView.findViewById(R.id.imageView);
            txtTenLop =(TextView) itemView.findViewById(R.id.txvTenLop);
            txtSiSo =(TextView) itemView.findViewById(R.id.txvSiSo);
        }
    }
    ArrayList<LopHoc> List= new ArrayList<LopHoc>();
    public ArrayList<LopHoc> getList() {
        return List;
    }
    public List<Boolean> itemFocusList;

    public LopHocAdapter(ArrayList<LopHoc> list) {
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
    public LopHocAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_lop_hoc, parent, false);
        LopHocAdapter.ViewHolder viewHolder = new LopHocAdapter.ViewHolder(contactView) ;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LopHocAdapter.ViewHolder viewHolder, int position) {
        LopHoc lopHoc = List.get(position);
        viewHolder.txtTenLop.setText(lopHoc.getTenLopHoc());
        viewHolder.txtSiSo.setText("Sỉ số: " + lopHoc.getSoLuong());
        boolean isFocused = itemFocusList.get(position);
    }
    @Override
    public int getItemCount() {
        return List.size();
    }

}
