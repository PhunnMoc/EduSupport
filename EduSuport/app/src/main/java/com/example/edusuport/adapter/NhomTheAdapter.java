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
import com.example.edusuport.controllers.DangTaiTaiLieuController;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.NhomThe;

import java.util.ArrayList;

public class NhomTheAdapter extends ArrayAdapter {
    Activity context;
    private Context mContext;
    int resource;
    ArrayList<NhomThe> List= new ArrayList<NhomThe>();
    ArrayList<LopHoc> listLop=new ArrayList<>();
    String idShare;
    DangTaiTaiLieuController dt=new DangTaiTaiLieuController();

    public NhomTheAdapter(Context context, int resource, ArrayList<NhomThe> list) {
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
        moreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showBottomSheet(txtTen.getText().toString(),mota,id);
            }
        });

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, Main_TheLat_GV.class);
                intent.putExtra("idMon",List.get(position).getIdMon());
                intent.putExtra("idGV","1");
                intent.putExtra("tenNhomThe",List.get(position).getTenNhomThe());
                intent.putExtra("idNhomThe",List.get(position).getIdNhomThe());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });
        customView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showBottomSheet(txtTen.getText().toString(),mota,id);
                return false;
            }
        });
        return customView;
    }
    public void showBottomSheet(String tenFC,String mota, String id){
        final Dialog dialog=new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheetgfcard);

        TableRow sua=dialog.findViewById(R.id.bot_chinhsuaGFC);
        TableRow xoa=dialog.findViewById(R.id.bot_xoaGFC);
        TableRow share=dialog.findViewById(R.id.bot_shareGFC);
        TextView ten=dialog.findViewById(R.id.bot_nameGFC);

        ten.setText(tenFC);
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                builder.setTitle("Thay đổi tên tài liệu: ");

                final View alertView = layoutInflater.inflate(R.layout.activity_sua_groupfc, null);
                builder.setView(alertView);

                EditText txtTenGFC=(EditText) alertView.findViewById(R.id.sua_txtTenGFC);
                txtTenGFC.setText(tenFC);

                EditText txtMoTaGFC=(EditText) alertView.findViewById(R.id.sua_txtMoTaGFC);
                txtMoTaGFC.setText(mota);
                txtTenGFC.setTextColor(ContextCompat.getColor(mContext, R.color.black_low_emp));
                txtMoTaGFC.setTextColor(ContextCompat.getColor(mContext, R.color.black_low_emp));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        String tenNew= txtTenGFC.getText().toString();
                        String motaNew= txtMoTaGFC.getText().toString();
                        editTL(id,tenNew,motaNew);

                        dialog.dismiss();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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
                        deleteTL(id);
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
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showBottomSheetShareLop(id);
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations= com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    public void showBottomSheetShareLop(String idGFC){
        final Dialog dialog=new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.botsheet_xemthemlop);


        SearchView filterlop=dialog.findViewById(R.id.filterLop);
        RecyclerView morelophoc=dialog.findViewById(R.id.more_lophoc);

        // LopHoc_IdGV_Nav_Adapter lh =new LopHoc_IdGV_Nav_Adapter(listLop);
        LopHoc_IdGV_Nav_Adapter lopHocIdGVNavAdapter;
        listLop=((DangTaiTaiLieu_MonActivity) mContext).getListLop();
        lopHocIdGVNavAdapter=new LopHoc_IdGV_Nav_Adapter(listLop );
        morelophoc.setAdapter(lopHocIdGVNavAdapter);

        filterlop.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<LopHoc> temp=new ArrayList<LopHoc>();
                for(LopHoc lh: listLop){
                    if(lh.getTenLopHoc().toLowerCase().contains(newText.toLowerCase())){
                        temp.add(lh);

                    }
                }
                LopHoc_IdGV_Nav_Adapter lh =new LopHoc_IdGV_Nav_Adapter(temp);
                morelophoc.setAdapter(lh);
                //morelophoc.setLayoutManager(new LinearLayoutManager(DangTaiTaiLieu_MonActivity.this, LinearLayoutManager.HORIZONTAL, false));
                lh.notifyDataSetChanged();
                return false;
            }
        });

        //lopHocIdGVNavAdapter=new LopHoc_IdGV_Nav_Adapter(listLop);
        morelophoc.addOnItemTouchListener(new ViewHolderClick(mContext, morelophoc, new ViewHolderClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String  id) {
                idShare= id;
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Xác nhận chia sẻ");
                builder.setMessage("Bạn có chắc chắn chia sẻ cho tài liệu này tới lớp "+listLop.get(position).getTenLopHoc());

                // Nút xác nhận
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogmini, int which) {
                        // Gọi hàm deleteTL() để xóa tài liệu
                        dt.shareGFC(idGFC, listLop.get(position).getIdLopHoc(), new DangTaiTaiLieuController.UploadCallback() {
                            @Override
                            public void onUploadComplete() {
                                Toast.makeText(mContext, "Share thành công", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onUploadFailed(String errorMessage) {
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
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
                    }
                });

                // Hiển thị hộp thoại xác nhận
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }


            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations= com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void editTL(String id,String tenNew,String moTaNew){

       // Toast.makeText(mContext, tenNew, Toast.LENGTH_SHORT).show();
        dt.editGFC(id, tenNew,moTaNew, new DangTaiTaiLieuController.UploadCallback() {
            @Override
            public void onUploadComplete() {
                Toast.makeText(mContext, "Đã thay đổi tên tài liệu", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUploadFailed(String errorMessage) {
                Toast.makeText(mContext, "Thay đổi tên thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        ((DangTaiTaiLieu_MonActivity) mContext).reLoadListGFC();
    }
    public void deleteTL(String id){

        dt.deleteGroupFC(id, mContext, new DangTaiTaiLieuController.UploadCallback() {
            @Override
            public void onUploadComplete() {
                Toast.makeText(mContext, "Hoàn tất xóa", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUploadFailed(String errorMessage) {
                Toast.makeText(mContext, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        ((DangTaiTaiLieu_MonActivity) mContext).reLoadListGFC();

    }

}
