package com.example.edusuport.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.edusuport.model.MonHoc;
import com.example.edusuport.model.NhomThe;
import com.example.edusuport.model.TheLat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TheLat_HSGVController {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    StorageReference myRefStora= FirebaseStorage.getInstance().getReference();

    ArrayList<TheLat> listTheLat=new ArrayList<TheLat>();
    public interface UploadCallback {
        void onUploadComplete();
        void onUploadFailed(String errorMessage); // Optionally, handle upload failure
    }
    public interface DataRetrievedCallback_TheLat {
        void onDataRetrieved(ArrayList<TheLat> thelatList);
    }
    public void getListTheLat(String idNhomThe,DataRetrievedCallback_TheLat callback){
        myRef.child("groupFlashCard").child(idNhomThe).child("theLat").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        String idThe = dataSnapshot.getKey(); // Lấy ID của môn học từ khóa
                        String cauHoi = dataSnapshot.child("cauHoi").getValue(String.class); // Lấy tên môn học từ giá trị
                        String cauTraLoi = dataSnapshot.child("cauTraLoi").getValue(String.class);

                        listTheLat.add(new TheLat(idThe,idNhomThe,cauHoi,cauTraLoi));

                    }
                    // Gọi callback với danh sách dữ liệu đã lấy được
                    callback.onDataRetrieved(listTheLat);
                }

            }
        });

    }
    public void addTheLat(String idNhomThe,String cauhoi,String cautraloi, UploadCallback callback){
        String id=myRef.push().getKey();
        TheLat theLat=new TheLat(id,idNhomThe,cauhoi,cautraloi);
        myRef.child("groupFlashCard").child(idNhomThe).child("theLat").child(id).setValue(theLat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onUploadComplete();
            }
        });

    }
    public void editTheLat(String idGFC, String idthelat, String newCauHoi, String newCauTraLoi, DangTaiTaiLieuController.UploadCallback callback){
        Map<String, Object> updates = new HashMap<>();
        updates.put("groupFlashCard/" + idGFC + "/theLat/"+idthelat+"/cauHoi",newCauHoi );
        updates.put("groupFlashCard/" + idGFC + "/theLat/"+idthelat+"/cauTraLoi", newCauTraLoi);
        myRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onUploadComplete();
            }
        });
    }
    public void deleteTheLat(String idGFC, String idthelat, Context mContext, DangTaiTaiLieuController.UploadCallback callback){
        myRef.child("groupFlashCard").child(idGFC).child("theLat").child(idthelat).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onUploadComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(mContext, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
