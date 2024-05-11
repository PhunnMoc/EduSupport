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
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edusuport.R;
import com.example.edusuport.activity.DangTaiTaiLieu_MonActivity;
import com.example.edusuport.controllers.DangTaiTaiLieuController;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.MonHoc;
import com.example.edusuport.model.TaiLieuHocTap;

import java.util.ArrayList;
import java.util.Objects;

public class TaiLieuHocTapAdapterHS  extends ArrayAdapter {
    Activity context;
    private Context mContext;
    int resource;
    ArrayList<TaiLieuHocTap> List= new ArrayList<TaiLieuHocTap>();
    ArrayList<LopHoc> listLop=new ArrayList<>();
    DangTaiTaiLieuController dt=new DangTaiTaiLieuController();
    String idShare;

    public TaiLieuHocTapAdapterHS(Context context, int resource, ArrayList<TaiLieuHocTap> list) {
        super(context, resource, list);
        List = list;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView= convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.item_tab_listtailieu, null);
        //  ImageView imgHinh = (ImageView)  customView.findViewById(R.id.imgHinh);
        TextView id=(TextView) customView.findViewById(R.id.idTaiLieu);
        ImageView img=(ImageView) customView.findViewById(R.id.ic_fileType);
        TextView txtTen =(TextView) customView.findViewById(R.id.name_tailieu);
        TextView txtDate =(TextView) customView.findViewById(R.id.date_dangtailieu);
        txtTen.setText(List.get(position).getTenTaiLieu());
        id.setText(List.get(position).getIdTaiLieu());
        txtDate.setText("Upload ngày "+List.get(position).getThoiGian());
        String ext= List.get(position).getFileType();


        if(Objects.equals(ext, ".pdf")){
            img.setImageResource(R.drawable.icon_pdf);
        } else if (Objects.equals(ext, ".pptx")) {
            img.setImageResource(R.drawable.icon_ppt);
        }
        else if (Objects.equals(ext, ".xlsx") || Objects.equals(ext, ".xls")) {
            img.setImageResource(R.drawable.icon_excel);
        }
        else if (Objects.equals(ext, ".mp4")) {
            img.setImageResource(R.drawable.icon_mp4);
        }
        else if (Objects.equals(ext, ".doc") || Objects.equals(ext, ".docx")) {
            img.setImageResource(R.drawable.icon_word);
        }
        else if (Objects.equals(ext, ".png") || Objects.equals(ext,".jpeg") || Objects.equals(ext, ".JPG") || Objects.equals(ext, ".gif")) {
            img.setImageResource(R.drawable.icon_imgfile    );
        }
        else{

            img.setImageResource(R.drawable.icon_notefile);
        }
        Log.d("test icon",ext.toString());

        ImageView moreOption= (ImageView) customView.findViewById(R.id.btn_moreOptFile);
        moreOption.setVisibility(View.GONE);

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(id.getText().toString());
            }
        });
        return customView;
    }

    public void downloadFile(String id){

        dt.getTaiLieu_idTaiLieu(id, new DangTaiTaiLieuController.DataRetrievedCallback_String() {
            @Override
            public void onDataRetrieved(String url) {
                Toast.makeText(mContext, url, Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(Intent.ACTION_VIEW);
                intent.setType("*/*");
                intent.setData(Uri.parse(url));
                mContext.startActivity(intent);
            }
        });


    }

}
