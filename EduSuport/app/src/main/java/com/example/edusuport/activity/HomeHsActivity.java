package com.example.edusuport.activity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.edusuport.R;
import com.example.edusuport.adapter.ChucNangHomeAdapter;
import com.example.edusuport.model.ChucNang;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.HocSinh;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeHsActivity extends AppCompatActivity {
    //public static HocSinh hocSinh = new HocSinh("21110499", "Lê Quang Lâm", "12B8");
    public static HocSinh hocSinh=null;
    GridView gvChucNang;
    CircleImageView ava;
    ArrayList<ChucNang> ListCN=new ArrayList<>();
    ChucNangHomeAdapter chucNangHomeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_hs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainhs), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView txvTenGV = findViewById(R.id.txvTenHS);
        txvTenGV.setText("Học sinh: " + hocSinh.getTen());
        CircleImageView imgavt = findViewById(R.id.imgAvatar);
        Picasso.get().load(hocSinh.getUrl()).into(imgavt);
        setLocaleFromDatabase(hocSinh.getNgonNgu());
        getForm();
        getData();
        AddEvents();
    }
    private void getData() {
        ListCN.add(new ChucNang("XTKBHS",getResources().getString(R.string.schedule)));
        ListCN.add(new ChucNang("XTBHS",getResources().getString(R.string.seenNoti)));
        ListCN.add(new ChucNang("XTLHS",getResources().getString(R.string.materials)));
        ListCN.add(new ChucNang("XDHS",getResources().getString(R.string.seeScore)));
        ListCN.add(new ChucNang("XNHHS",getResources().getString(R.string.seeReview)));
        chucNangHomeAdapter=new ChucNangHomeAdapter(HomeHsActivity.this,R.layout.icon_tailieu_gv,ListCN);
        gvChucNang.setAdapter(chucNangHomeAdapter);
    }
    public void AddEvents(){

        View includedLayout = findViewById(R.id.navbar_layout); // navbar_layout là ID của layout được include
        ConstraintLayout layoutHome = includedLayout.findViewById(R.id.layoutHome);
        ConstraintLayout layoutMessage = includedLayout.findViewById(R.id.layoutMessage);
        ConstraintLayout layoutEditProfile = includedLayout.findViewById(R.id.layoutEditProfile);
        layoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeHsActivity.this, HomeHsActivity.class);
                startActivity(intent);
            }
        });
        layoutMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeHsActivity.this, Messages_HS.class);
                startActivity(intent);
            }
        });
        layoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeHsActivity.this, EditInformationHS.class);
                startActivity(intent);
            }
        });

        gvChucNang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
    private void getForm() {
        gvChucNang=findViewById(R.id.grid_ChucNang);
        ava= findViewById(R.id.imgAvatar);
//        if(!hocSinh.ge().isEmpty()){
//            Picasso.get().load(giaoVien.getUrl()).into(ava);
//        }
//        else {
//            Picasso.get().load(R.drawable.profile).into(ava);
//        }
    }
    private void setLocaleFromDatabase(String selectedLanguage) {
        Configuration config = getResources().getConfiguration();
        Locale locale = new Locale(selectedLanguage);
        config.setLocale(locale);

        // Tạo một Context mới với Configuration mới
        Context newContext = createConfigurationContext(config);

        // Cập nhật ngôn ngữ cho ứng dụng
        Resources resources = newContext.getResources();
        getResources().updateConfiguration(config, resources.getDisplayMetrics());
    }
}