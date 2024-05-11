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

public class HopThuGopYAdapter extends ArrayAdapter {
    Context context;
    DBHelper dbHelper;
    int resource;
    ArrayList<ThuGopY> list= new ArrayList<ThuGopY>();
    private ArrayList<String> tenPhuHuynhList = new ArrayList<>();
    public HopThuGopYAdapter(@NonNull Context context, int resource, ArrayList<ThuGopY> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        dbHelper = new DBHelper();
    }
    private static class ViewHolder {
        TextView txvTenNguoiGui, txvTieuDe;
        TextView txvThoiGian;
        TextView txvTrangThai;
        LinearLayout mainLayout;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.item_thu_gop_y_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txvTenNguoiGui = convertView.findViewById(R.id.txvTenNguoiGui);
        viewHolder.txvTieuDe = convertView.findViewById(R.id.txvTieuDe);
        viewHolder.txvThoiGian = convertView.findViewById(R.id.txvTGThu);
        viewHolder.txvTrangThai = convertView.findViewById(R.id.txvTrangThaiThu);
        viewHolder.mainLayout = convertView.findViewById(R.id.layoutThu);

        ThuGopY item = list.get(position);

        // Thiết lập giá trị cho TextViews từ dữ liệu của dòng hiện tại
        viewHolder.txvTieuDe.setText(item.getTieuDe());
        viewHolder.txvThoiGian.setText(item.getThoiGian().toString());

        if (item.isXem()) {
            viewHolder.txvTrangThai.setText("Đã xem");
            viewHolder.mainLayout.setBackgroundResource(R.drawable.background_subject_excellent_score);
        } else {
            viewHolder.txvTrangThai.setText("Chưa xem");
            viewHolder.mainLayout.setBackgroundResource(R.drawable.background_subject_good_score);
        }

        // Kiểm tra xem item có ẩn danh không và gọi callback tương ứng
        if (!item.isAnDanh()) {
            dbHelper.getTenPhuHuynhByID(item.getIDNguoiGui(), new DBHelper.TenHocSinhCallback() {
                @Override
                public void onTenHocSinhFetched(String tenPhuHuynh) {
                    if (tenPhuHuynh != null) {
                        viewHolder.txvTenNguoiGui.setText(tenPhuHuynh);
                    } else {
                        // Xử lý khi không lấy được tên học sinh
                    }
                }
            });
        } else {
            viewHolder.txvTenNguoiGui.setText("Người gửi ẩn danh");
        }

        return convertView;
    }
}
