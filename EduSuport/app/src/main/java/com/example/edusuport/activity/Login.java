package com.example.edusuport.activity;

import static android.view.View.GONE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.R;
import com.example.edusuport.controllers.DangKiGV_AuController;
import com.example.edusuport.controllers.DangTaiTaiLieuController;
import com.example.edusuport.model.Account;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.HocSinh;
import com.example.edusuport.model.PhuHuynh;
import com.example.edusuport.model.ThongBao;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

public class Login extends AppCompatActivity {
    RadioGroup rb_role;
    Button btn_dk,btn_dn;
    LinearLayout btn_dkgg;
    TextView txtQuenMK;
    EditText tk,mk;
    TextInputLayout tkhint;
    DBHelper dbHelper = new DBHelper();

    String role="GV";
    boolean isAllFieldsChecked = false;
    DangKiGV_AuController dangKiGVAuController=new DangKiGV_AuController();
    public static final String SHARED_PREFS="sharePrefs";
    public boolean validate = false;
    int RC_SIGNIN_GG =40;
    GoogleSignInClient mGoogleInClient;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.form_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.form_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("name","");
        editor.apply();
        getComponet();
        handelClick();
        checkBoxRemeber();

    }

    private void getComponet(){
        rb_role=(RadioGroup) findViewById(R.id.radioBtn_role);
        btn_dk=(Button) findViewById(R.id.btn_dkGmail);
        btn_dn=(Button) findViewById(R.id.btn_dn);
        txtQuenMK=findViewById(R.id.txtQuenMK);
        tk=findViewById(R.id.txtTK);
        mk=findViewById(R.id.txtMK);
        tkhint=findViewById(R.id.txtLogTKHint);
        btn_dkgg=findViewById(R.id.btn_dkGG);

        tk.setText("");
        mk.setText("");
    }
    private void handelClick(){

        rb_role.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tk.setText("");
                mk.setText("");
                if(checkedId==R.id.radioBtn_GV){
                    role="GV";
                    btn_dk.setVisibility(View.VISIBLE);
                    btn_dkgg.setVisibility(View.VISIBLE);
                    tkhint.setHint(getResources().getString(R.string.mailTeacher));
                    tk.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                } else if (checkedId==R.id.radioBtn_PH) {
                    role="PH";
                    btn_dk.setVisibility(View.GONE);
                    btn_dkgg.setVisibility(View.GONE);
                    tkhint.setHint(getResources().getString(R.string.idParents));
                }
                else
                {
                    role="HS";
                    btn_dk.setVisibility(View.GONE);
                    btn_dkgg.setVisibility(View.GONE);
                    tkhint.setHint(getResources().getString(R.string.idStd));
                }
            }
        });

        btn_dk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this,RegisterAccount.class);
                //intent.putExtra("idMon",monHoc.getIdMon());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btn_dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {
                    if(Objects.equals(role, "GV")){
                        dangKiGVAuController.checkLogin_GV(tk.getText().toString(), mk.getText().toString(), Login.this, new DangKiGV_AuController.UploadCallback() {
                            @Override
                            public void onUploadComplete() {
                                SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                                SharedPreferences.Editor editor= sharedPreferences.edit();
                                editor.putString("name","true");
                                editor.apply();

                            }

                            @Override
                            public void onUploadFailed(String errorMessage) {

                            }
                        });

                    } else if (Objects.equals(role, "PH")) {
                        ValidatePH();
                    }
                    else {
                        ValidateHS();
                    }
                }

            }
        });
        txtQuenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuenMKGV();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1061030003947-m39q1a9866ot9n6npd2adj4lb3bs3ghf.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleInClient=GoogleSignIn.getClient(Login.this,gso);

        btn_dkgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString("name","true");
                editor.apply();
                Intent intent= mGoogleInClient.getSignInIntent();
                startActivityForResult(intent,RC_SIGNIN_GG);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGNIN_GG) {
            if (data != null) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {

                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference();
                                FirebaseUser current= firebaseAuth.getCurrentUser();
                                dangKiGVAuController.getGVbyID(current.getUid(), Login.this);

                                myRef.child("giaovien").orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (!dataSnapshot.exists()) {
                                                    Account gv=new Account(current.getDisplayName(),current.getPhotoUrl().toString(),current.getPhoneNumber(),"giaovien",null,null,"valid");
                                                    myRef.child("giaovien").child(firebaseAuth.getCurrentUser().getUid()).setValue(gv);
                                                    Toast.makeText(Login.this,current.getUid().toString(),Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(Login.this,"má",Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                             }
                        }
                    });
                } catch (ApiException e) {
                    e.printStackTrace();
                    // Handle ApiException properly here
                }
            } else {
                Toast.makeText(Login.this,"má",Toast.LENGTH_SHORT).show();

            }
        }
    }


    private void showQuenMKGV() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        LayoutInflater layoutInflater = LayoutInflater.from(Login.this);
        builder.setTitle("Lấy lại mật khẩu: ");

        final View alertView = layoutInflater.inflate(R.layout.activity_click_quen_mkgv, null);
        builder.setView(alertView);

        EditText edEmailGV=(EditText) alertView.findViewById(R.id.emailGV);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogmini, int which) {
                if (!Patterns.EMAIL_ADDRESS.matcher(edEmailGV.getText().toString()).matches() ) {

                    Toast.makeText(Login.this,"Đề nghị nhập một email đã đăng kí",Toast.LENGTH_SHORT).show();
                }
                else {
                    dangKiGVAuController.quenMK_GV(edEmailGV.getText().toString(),Login.this);
                }

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void checkBoxRemeber(){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String check= sharedPreferences.getString("name","");
        if(check.equals("true")){
            Toast.makeText(Login.this,check,Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(Login.this, Home.class);
            startActivity(intent);
            finish();
        }
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

        if (!Patterns.EMAIL_ADDRESS.matcher(tk.getText().toString()).matches() && Objects.equals(role, "GV")) {
            tk.setError("Invalid email address");
            return false;
        }
        // after all validation return true.
        return true;
    }

    private void ValidatePH(){
        String TK = tk.getText().toString().trim();
        String MK = mk.getText().toString().trim();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecPhuHuynh);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot PHSnapshot : dataSnapshot.getChildren()) {
                    String Msph = PHSnapshot.getKey().toString();
                    String matKhau = PHSnapshot.child(dbHelper.FieldMatKhau).getValue(String.class);
                    boolean check = Msph.equals(TK) && matKhau.equals(MK);
                    if (check)
                    {
                            String Ten = PHSnapshot.child(dbHelper.FieldTenPH).getValue(String.class);
                            String IDLopHoc = PHSnapshot.child(dbHelper.FieldIDLopHoc).getValue(String.class);
                            String mshs = PHSnapshot.child(dbHelper.FieldMSHS).getValue(String.class);
                            String url = PHSnapshot.child("urlAva").getValue(String.class);

                            DataSnapshot ngonNguSnapshot = PHSnapshot.child("ngonNgu");
                            String NgonNgu;

                            if (ngonNguSnapshot.exists()) {
                                NgonNgu = ngonNguSnapshot.getValue(String.class);
                            } else {
                                NgonNgu = "vi";
                            }
                            PhuHuynh phuHuynh = new PhuHuynh(Msph, Ten, IDLopHoc, mshs, url, NgonNgu);
                            HomePhActivity.phuHuynh = phuHuynh;
                            validate = true;
                    }
                }
                handleValidationResult(validate);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void handleValidationResult(boolean isValid) {
        if (isValid){
            Intent intent=new Intent(Login.this, HomePhActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(Login.this, "MSPH hoặc mật khẩu sai, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }

    }
    private void ValidateHS(){
        validate = false;
        String TK = tk.getText().toString().trim();
        String MK = mk.getText().toString().trim();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(dbHelper.ColecHocSinh);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot PHSnapshot : dataSnapshot.getChildren()) {
                    String Mssh = PHSnapshot.getKey().toString();
                    String matKhau = PHSnapshot.child(dbHelper.FieldMatKhau).getValue(String.class);
                    boolean check = Mssh.equals(TK) && matKhau.equals(MK);
                    if (check)
                    {
                        String Ten = PHSnapshot.child(dbHelper.FieldTenPH).getValue(String.class);
                        String IDLopHoc = PHSnapshot.child(dbHelper.FieldIDLopHoc).getValue(String.class);
                        String url = PHSnapshot.child("urlAva").getValue(String.class);
                        DataSnapshot ngonNguSnapshot = PHSnapshot.child("ngonNgu");
                        String NgonNgu;
                        if (ngonNguSnapshot.exists()) {
                            NgonNgu = ngonNguSnapshot.getValue(String.class);
                        } else {
                            NgonNgu = "vi";
                        }
                        HocSinh hocSinh = new HocSinh(Mssh, Ten, IDLopHoc, url, NgonNgu);

                        HomeHsActivity.hocSinh = hocSinh;
                        validate = true;
                    }
                }
                handleValidationHS(validate);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void handleValidationHS(boolean isValid) {
        if (isValid){
            Intent intent=new Intent(Login.this, HomeHsActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(Login.this, "MSHS hoặc mật khẩu sai, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }

    }
}