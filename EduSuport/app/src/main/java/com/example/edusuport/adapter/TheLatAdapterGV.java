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

public class TheLatAdapterGV extends ArrayAdapter {
    Activity context;
    private Context mContext;
    int resource;
    ArrayList<TheLat> List= new ArrayList<TheLat>();
    TheLat_HSGVController theLatHsgvController=new TheLat_HSGVController();

    public TheLatAdapterGV(Context context, int resource, ArrayList<TheLat> list) {
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
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(List.get(position).getCauHoi(),List.get(position).getCauTraLoi(),List.get(position).getIdThe(),List.get(position).getIdNhomThe());
            }
        });
        return customView;
    }
    private void showPopUpDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Thẻ: "+ List.get(position).getCauHoi());

        // Thêm các lựa chọn vào danh sách
        String[] options = {"Chỉnh sửa thẻ", "Xóa thẻ" };
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi một lựa chọn được chọn
                String selectedOption = options[which];
                Toast.makeText(mContext, "Bạn đã chọn: " + selectedOption, Toast.LENGTH_SHORT).show();
            }
        });

        // Hiển thị AlertDialog
        builder.create().show();
    }
    public void showBottomSheet(String txtcauhoi,String txtcautraloi, String idTheLat,String idNhomThe){
        final Dialog dialog=new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.botsheet_thelat_gv);

        TableRow sua=dialog.findViewById(R.id.bot_chinhsuathelat);
        TableRow xoa=dialog.findViewById(R.id.bot_xoathelat);
        TextView cauhoi=dialog.findViewById(R.id.bot_cauhoi);
        TextView cautraloi=dialog.findViewById(R.id.bot_cautraloi);

        cauhoi.setText(txtcauhoi);
        cautraloi.setText(txtcautraloi);
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSua(txtcauhoi,txtcautraloi,idTheLat,idNhomThe);
                dialog.dismiss();
            }
        });
        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa không?");

                // Nút xác nhận
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        // Gọi hàm deleteTL() để xóa tài liệu
                        theLatHsgvController.deleteTheLat(idNhomThe, idTheLat, mContext, new DangTaiTaiLieuController.UploadCallback() {
                            @Override
                            public void onUploadComplete() {
                                ((Main_TheLat_GV) mContext).reloadListTL();
                                Toast.makeText(mContext, "Xoa thành coongg ", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onUploadFailed(String errorMessage) {

                            }
                        });
                        dialogmini.dismiss();
                        dialog.dismiss();
                    }
                });

                // Nút hủy
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Đóng hộp thoại xác nhận
                        dialog.dismiss();
                    }
                });

                // Hiển thị hộp thoại xác nhận
                AlertDialog dialog1 = builder.create();
                dialog1.show();
            }

        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations= com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showSua(String txtcauhoi,String txtcautraloi,String idTheLat,String idNhomThe) {
        android.app.AlertDialog.Builder  builder = new AlertDialog.Builder(mContext);
        AlertDialog dialog = builder.create();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        builder.setTitle("Thẻ: ");

        final View alertView = layoutInflater.inflate(R.layout.activity_click_add_card, null);
        builder.setView(alertView);
        EditText edCauHoi=(EditText) alertView.findViewById(R.id.add_cauhoi);
        EditText edCauTraLoi=(EditText) alertView.findViewById(R.id.add_cautraloi);
        edCauHoi.setText(txtcauhoi);
        edCauTraLoi.setText(txtcautraloi);
        builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(!edCauHoi.getText().toString().equals("") && !edCauTraLoi.getText().toString().equals("")){

                    theLatHsgvController.editTheLat(idNhomThe, idTheLat, edCauHoi.getText().toString(), edCauTraLoi.getText().toString(), new DangTaiTaiLieuController.UploadCallback() {
                        @Override
                        public void onUploadComplete() {
                            ((Main_TheLat_GV) mContext).reloadListTL();
                            Toast.makeText(mContext, "Suae thành coongg ", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onUploadFailed(String errorMessage) {
                            Toast.makeText(mContext, "Sua that bai", Toast.LENGTH_SHORT).show();
                        }
                    });

            }
        }});


        builder.create().show();
    }


}
