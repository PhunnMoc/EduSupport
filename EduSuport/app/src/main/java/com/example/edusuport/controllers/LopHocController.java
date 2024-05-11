package com.example.edusuport.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.edusuport.DBHelper.DBHelper;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.MonHoc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LopHocController {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String idhs;

    DangTaiTaiLieuController dangTaiTaiLieuController=new DangTaiTaiLieuController();
    StorageReference myRefStora= FirebaseStorage.getInstance().getReference();

    ArrayList<LopHoc> list=new ArrayList<LopHoc>();
    public interface DataRetrievedCallback_LopHoc {
        void onDataRetrieved(ArrayList<LopHoc> monHocList);
    }

    public interface UploadCallback {
        void onUploadComplete();
        void onUploadFailed(String errorMessage); // Optionally, handle upload failure
    }
    public void getListLopHoc_idGV(String idGV,DataRetrievedCallback_LopHoc callbackLopHoc){
        myRef.child("lophoc").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DBHelper dbHelper = new DBHelper();
                for (DataSnapshot lophocSnapshot : dataSnapshot.getChildren()) {
                    String idLop = lophocSnapshot.getKey();
                    String idGiaovien = lophocSnapshot.child(dbHelper.FieldIDGiaoVien).getValue(String.class);
                    if (idGiaovien != null && idGiaovien.equals(idGV)) {
                        String tenLop = lophocSnapshot.child(dbHelper.FieldTenLop).getValue(String.class);
                        long siSo = lophocSnapshot.child(dbHelper.FieldSoLuong).getValue(long.class);
                        Log.d("Giaos ien",String.valueOf(tenLop));
                        list.add(new LopHoc(idLop,idGV,tenLop, siSo));
                    }
                }
                callbackLopHoc.onDataRetrieved(list);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("nônnooono",String.valueOf(databaseError));
            }
        });
    }
    public void deleteLopHoc(String idLop, Context mContext,UploadCallback callback){
        idhs="";
        myRef.child("lophoc").child(idLop).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myRef.child("hocsinh").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                idhs=dataSnapshot.getKey();
                                if(dataSnapshot.child("idlophoc").getValue(String.class).equals(idLop))
                                {
                                    XoaHocSinh(idhs);
                                }

                            }
                }}});


                myRef.child("groupFlashCard").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                String keyGF=dataSnapshot.getKey();
                                if(dataSnapshot.child("idLop").getValue(String.class).equals(idLop)){
                                    myRef.child("groupFlashCard").child(keyGF).removeValue();
                                }
                            }
                        }
                    }
                });
                myRef.child("groupFlashCard").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                String keyGF=dataSnapshot.getKey();
                                if(dataSnapshot.child("idLop").getValue(String.class).equals(idLop)){
                                    myRef.child("groupFlashCard").child(keyGF).removeValue();
                                }
                            }
                        }
                    }
                });

                myRef.child("taiLieuFile").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                String keyF=dataSnapshot.getKey();
                                if(dataSnapshot.child("idLop").getValue(String.class).equals(idLop)){
                                    myRef.child("taiLieuFile").child(keyF).removeValue();
                                }
                            }
                        }
                    }
                });
                myRef.child("thoikhoabieu").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                String TKB=dataSnapshot.getKey();
                                if(dataSnapshot.child("idlophoc").getValue(String.class).equals(idLop)){
                                    myRef.child("thoikhoabieu").child(TKB).removeValue();
                                }
                            }
                        }
                    }
                });

                callback.onUploadComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(mContext, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void XoaHocSinh(String idhs){
        myRef.child("hocsinh").child(idhs).removeValue();
        myRef.child("phuhuynh").child(idhs+"PH").removeValue();
        myRef.child("nhanxet").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        String keyNX=dataSnapshot.getKey();
                        if(dataSnapshot.child("mshs").getValue(String.class).equals(idhs)){
                            myRef.child("nhanxet").child(keyNX).removeValue();
                        }
                    }
                }
            }
        });

        myRef.child("donxinnghihoc").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        String keydon=dataSnapshot.getKey();
                        if(dataSnapshot.child("mshs").getValue(String.class).equals(idhs)){
                            myRef.child("donxinnghihoc").child(keydon).removeValue();
                        }
                    }
                }
            }
        });
        myRef.child("thongbao").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        String keyTB=dataSnapshot.getKey();
                        if(dataSnapshot.child("idnguoinhan").getValue(String.class).equals(idhs)
                                ||dataSnapshot.child("idnguoinhan").getValue(String.class).equals(idhs+"PH")){
                            myRef.child("thongbao").child(keyTB).removeValue();
                        }
                    }
                }
            }
        });

        myRef.child("thugopy").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        String ketGY=dataSnapshot.getKey();
                        if(dataSnapshot.child("idnguoigui").getValue(String.class).equals(idhs+"PH")){
                            myRef.child("thugopy").child(ketGY).removeValue();
                        }
                    }
                }
            }
        });
        myRef.child("diem").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        String keyDiem=dataSnapshot.getKey();
                        if(dataSnapshot.child("mshs").getValue(String.class).equals(idhs)){
                            myRef.child("diem").child(keyDiem).removeValue();
                        }
                    }
                }
            }
        });
        myRef.child("chat").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        String keyChat=dataSnapshot.getKey();
                        if((dataSnapshot.child("users_1").getValue(String.class).equals(idhs))
                                ||(dataSnapshot.child("users_2").getValue(String.class).equals(idhs))
                                ||(dataSnapshot.child("users_1").getValue(String.class).equals(idhs+"PH"))
                                ||(dataSnapshot.child("users_2").getValue(String.class).equals(idhs+"PH")) ){
                            myRef.child("chat").child(keyChat).removeValue();
                        }
                    }
                }
            }
        });
    }
}
