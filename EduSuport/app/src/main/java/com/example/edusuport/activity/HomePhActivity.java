package com.example.edusuport.activity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.edusuport.R;
import com.example.edusuport.adapter.ChucNangHomeAdapter;
import com.example.edusuport.model.ChucNang;
import com.example.edusuport.model.PhuHuynh;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePhActivity extends AppCompatActivity {
   // public static PhuHuynh phuHuynh = new PhuHuynh("21110499PH", "Lê Quang", "12B8", "21110499");
    public static PhuHuynh phuHuynh=null;
    GridView gvChucNang;
    CircleImageView ava;
    ArrayList<ChucNang> ListCN=new ArrayList<>();
    ChucNangHomeAdapter chucNangHomeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_ph);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainph), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView txvTenGV = findViewById(R.id.txvTenPH);
        txvTenGV.setText(phuHuynh.getTen());
        CircleImageView imgavt = findViewById(R.id.imgAvatar);
        Picasso.get().load(phuHuynh.getUrl()).into(imgavt);
        setLocaleFromDatabase(phuHuynh.getNgonNgu());
        getForm();
        getData();
        AddEvents();
    }
    private void getData() {
        ListCN.add(new ChucNang("DXPPH",getResources().getString(R.string.leaveForm)));
        ListCN.add(new ChucNang("GYPH",getResources().getString(R.string.cmtTeacher)));
        ListCN.add(new ChucNang("TBPH",getResources().getString(R.string.seenNoti)));
        ListCN.add(new ChucNang("XNXPH",getResources().getString(R.string.seeReview)));
        ListCN.add(new ChucNang("XDPH",getResources().getString(R.string.seeScore)));
        chucNangHomeAdapter=new ChucNangHomeAdapter(HomePhActivity.this,R.layout.icon_tailieu_gv,ListCN);
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
                Intent intent=new Intent(HomePhActivity.this, HomePhActivity.class);
                startActivity(intent);
            }
        });
        layoutMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePhActivity.this, Messages_PH.class);
                startActivity(intent);
            }
        });
        layoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePhActivity.this, EditInformation_PH.class);
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