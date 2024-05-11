package com.example.edusuport.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import com.example.edusuport.R;

public class ChangePassword extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ProgressDialog dialog;
    Button btnSave;
    EditText edtOldPass, edtNewPass, edtReNewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.form_changepassword);

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(ChangePassword.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading....");
        btnSave = findViewById(R.id.btnSave);
        edtOldPass = findViewById(R.id.txtoldPass);
        edtNewPass = findViewById(R.id.txtnewPassword);
        edtReNewPass = findViewById(R.id.txtreNewPassword);

        btnSave.setOnClickListener(v -> {
            changePassword();
        });
    }

    private void changePassword() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        String oldPassword = edtOldPass.getText().toString().trim();
        String newPassword = edtNewPass.getText().toString().trim();
        String reNewPassword = edtReNewPass.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword)) {
            Toast.makeText(this, "Nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "Nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(reNewPassword)) {
            Toast.makeText(this, "Nhập lại mật khẩu mới", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        id = "21110928";
        Query checkPassword = databaseReference.child(id).orderByChild("password");

        String finalId = id;
        checkPassword.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //so mật khẩu vừa nhập với mật khẩu lưu trong firebase
                    String passFormDB = snapshot.child("users").child(finalId).child("password").getValue(String.class);
                    if (!Objects.equals(passFormDB, edtOldPass)) {
                        if (!TextUtils.equals(newPassword, reNewPassword)) {
                            Toast.makeText(ChangePassword.this, "Mật khẩu mới không khớp. Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child(finalId).child("password").setValue(edtNewPass.getText().toString());
                            Toast.makeText(ChangePassword.this, "Đổi mật khẩu thành công.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ChangePassword.this, "Mật khẩu cũ không khớp.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}