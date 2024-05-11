package com.example.edusuport.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.edusuport.R;
import com.example.edusuport.activity.DangTaiTaiLieu_MonActivity;
import com.example.edusuport.activity.Main_TheLat_GV;
import com.example.edusuport.controllers.DangTaiTaiLieuController;
import com.example.edusuport.controllers.TheLat_HSGVController;
import com.example.edusuport.model.TheLat;

import java.util.ArrayList;

public class TheLatAdapterHS extends ArrayAdapter {
    Activity context;
    private Context mContext;
    ArrayList<TheLat> List= new ArrayList<TheLat>();
    TheLat_HSGVController theLatHsgvController=new TheLat_HSGVController();

    public TheLatAdapterHS(Context context, int resource, ArrayList<TheLat> list) {
        super(context, resource, list);
        List = list;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView= convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.item_main_flashcard, null);
        //  ImageView imgHinh = (ImageView)  customView.findViewById(R.id.imgHinh);
        TextView cauhoi =(TextView) customView.findViewById(R.id.cauhoi_fc);
        TextView traloi =(TextView) customView.findViewById(R.id.traloi_fc);
        cauhoi.setText(List.get(position).getCauHoi());
        traloi.setText(List.get(position).getCauTraLoi());



        ImageButton btn= customView.findViewById(R.id.btn_moreOptFC);
        btn.setVisibility(View.GONE);

        return customView;
    }

}
