package com.example.edusuport.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edusuport.R;
import com.example.edusuport.activity.DangTaiTaiLieuActivity;
import com.example.edusuport.activity.DangTaiTaiLieu_MonActivity;
import com.example.edusuport.activity.Main_TheLat_GV;
import com.example.edusuport.activity.Main_TheLat_HS_viewCard;
import com.example.edusuport.controllers.DangTaiTaiLieuController;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.NhomThe;
import com.example.edusuport.model.TaiLieuHocTap;
import com.example.edusuport.model.TheLat;

import java.util.ArrayList;

public class NhomTheAdapterHS extends ArrayAdapter {
    Activity context;
    private Context mContext;
    int resource;
    ArrayList<NhomThe> List= new ArrayList<NhomThe>();
    ArrayList<LopHoc> listLop=new ArrayList<>();
    String idShare;
    DangTaiTaiLieuController dt=new DangTaiTaiLieuController();

    public NhomTheAdapterHS(Context context, int resource, ArrayList<NhomThe> list) {
        super(context, resource, list);
        List = list;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView= convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.item_tab_flashcard, null);
        //  ImageView imgHinh = (ImageView)  customView.findViewById(R.id.imgHinh);
        TextView txtTen =(TextView) customView.findViewById(R.id.nameNhomThe);
        TextView txtDate =(TextView) customView.findViewById(R.id.soluongThe);
        String mota=List.get(position).getMota();
        txtTen.setText(List.get(position).getTenNhomThe());
        txtDate.setText("Upload ngày "+List.get(position).getThoiGian());
        String id=List.get(position).getIdNhomThe();

        ImageButton moreOption= customView.findViewById(R.id.btn_moreOptFlashCard);
        moreOption.setVisibility(View.GONE);

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, Main_TheLat_HS_viewCard.class);
                intent.putExtra("idMon",List.get(position).getIdMon());
                intent.putExtra("idGV","1");
                intent.putExtra("idNhomThe",List.get(position).getIdNhomThe());
                intent.putExtra("tenNhomThe",List.get(position).getTenNhomThe());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });

        return customView;
    }

}
