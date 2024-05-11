package com.example.edusuport.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.databinding.ActivityXemThoiKhoaBieuBinding;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.MonHoc;
import com.example.edusuport.model.ThoiKhoaBieu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class XemThoiKhoaBieuActivity extends AppCompatActivity {
    ActivityXemThoiKhoaBieuBinding binding;
    DBHelper dbHelper;
    String currentThu;
    String IDLopHoc = "";
    private HocSinh hocSinh = HomeHsActivity.hocSinh;
    ArrayList<ThoiKhoaBieu> listtkb;
    ArrayList<MonHoc> listMonHoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemThoiKhoaBieuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txvTenHS.setText("Học sinh: " + hocSinh.getTen());
        Picasso.get().load(hocSinh.getUrl()).into(binding.imgAva);

        listtkb = new ArrayList<>();
        dbHelper = new DBHelper();
        IDLopHoc = hocSinh.getIDLopHoc();
        GetMonHoc();
        AddEvents();
    }
    public void GetThoiKhoaBieu(String IDLop, String thu){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecThoiKhoaBieu);
        Query query = myRef.orderByChild(dbHelper.FieldIDLopHoc).equalTo(IDLop);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Kiểm tra thêm điều kiện về 'thu'
                    String currentthu = snapshot.child(dbHelper.FieldThu).getValue(String.class);
                    if (thu != null && thu.equals(currentthu)) {
                        // Dữ liệu thỏa mãn điều kiện, xử lý ở đây
                        String idMon = snapshot.child(dbHelper.FieldIDMon).getValue(String.class);
                        long tiet = snapshot.child(dbHelper.FieldTiet).getValue(Long.class);
                        ThoiKhoaBieu tkb = new ThoiKhoaBieu();
                        tkb.setIDMon(idMon);
                        tkb.setTiet(tiet);
                        listtkb.add(tkb);
                        // Xử lý dữ liệu ở đây, ví dụ: in ra thông tin
                        Log.d(TAG, "idMon: " + idMon + "Tiết " + tiet);
                    }
                }
                SetDefaultData(listtkb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    public void SetDefaultData(ArrayList<ThoiKhoaBieu> lst){
        String[] result = PreprocessTKB(lst);
        Log.d(TAG, "idMon: " + result[1]);
        binding.txvT1.setText(result[0]);
        binding.txvT10.setText(result[1]);
        binding.txvT2.setText(result[2]);
        binding.txvT3.setText(result[3]);
        binding.txvT4.setText(result[4]);
        binding.txvT5.setText(result[5]);
        binding.txvT6.setText(result[6]);
        binding.txvT7.setText(result[7]);
        binding.txvT8.setText(result[8]);
        binding.txvT9.setText(result[9]);
    }
    public String[] PreprocessTKB(ArrayList<ThoiKhoaBieu> lst){
        String[] result = new String[20];
        for (int i = 0; i < lst.size(); i++)
        {
            for (int j = 0; j < listMonHoc.size(); j++){
                if (lst.get(i).getIDMon().equals(listMonHoc.get(j).getIdMon())){
                    result[i] = listMonHoc.get(j).getTenMon();
                    break;
                }
            }
        }
        return result;
    }
    public void GetMonHoc(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecMonHoc);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot monHocSnapshot : dataSnapshot.getChildren()) {
                    String idMonHoc = monHocSnapshot.getKey();
                    String tenMonHoc = monHocSnapshot.child(dbHelper.FieldTenMon).getValue(String.class);
                    MonHoc monHoc = new MonHoc(idMonHoc, tenMonHoc);
                    listMonHoc.add(monHoc);
                }
                GetThoiKhoaBieu(IDLopHoc, "thu2");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void AddEvents(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        binding.btnT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu2");
                currentThu = "thu2";
            }
        });
        binding.btnT3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu3");
                currentThu = "thu3";
            }
        });
        binding.btnT4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu4");
                currentThu = "thu4";
            }
        });
        binding.btnT5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu5");
                currentThu = "thu5";
            }
        });
        binding.btnT6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu6");
                currentThu = "thu6";
            }
        });
        binding.btnT7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtkb = new ArrayList<>();
                GetThoiKhoaBieu(IDLopHoc, "thu7");
                currentThu = "thu7";
            }
        });
    }
    public void Back(){
        super.onBackPressed();
    }
}
