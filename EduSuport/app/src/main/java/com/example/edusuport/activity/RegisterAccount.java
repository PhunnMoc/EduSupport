package com.example.edusuport.activity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.edusuport.R;
import com.example.edusuport.controllers.DangKiGV_AuController;

public class RegisterAccount extends AppCompatActivity {

    DangKiGV_AuController dangKiGVAuController=new DangKiGV_AuController();
    TextView login;
    Button btnDK;
    EditText tk, mk,ten;
    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.form_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.form_register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login=findViewById(R.id.Login);
        btnDK=findViewById(R.id.btnDK);
        tk=findViewById(R.id.txtTK);
        mk=findViewById(R.id.txtMK);
        ten=findViewById(R.id.txtName);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked=CheckAllFields();
                if (isAllFieldsChecked){
                    dangKiGVAuController.dangKi_GV(tk.getText().toString(),mk.getText().toString(),ten.getText().toString(),RegisterAccount.this);
                }
            }
        });

    }
    private boolean CheckAllFields() {
        if (tk.length() == 0) {
            tk.setError("This field is required");
            return false;
        }

        if (mk.length() == 0) {
            mk.setError("This field is required");
            return false;
        }

        if (mk.length() < 6) {
            mk.setError("Password must be minimum 6 characters");
            return false;
        }
        if (tk.getText().toString().contains(" ")) {
            tk.setError("Spaces are not allowed");
            return false;
        }

// Kiểm tra xem tk có đúng định dạng email hay không
        if (!Patterns.EMAIL_ADDRESS.matcher(tk.getText().toString()).matches()) {
            tk.setError("Invalid email address");
            return false;
        }
        // after all validation return true.
        return true;
    }
    }
