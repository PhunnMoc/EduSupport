package com.example.edusuport.controllers;

import com.example.edusuport.model.DonXinNghiHoc;
import com.example.edusuport.model.TheLat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DonXinNghiHoc_PHController {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    ArrayList<TheLat> listTheLat=new ArrayList<TheLat>();
    public interface UploadCallback {
        void onUploadComplete();
        void onUploadFailed(String errorMessage); // Optionally, handle upload failure
    }
    public interface DataRetrievedCallback_TheLat {
        void onDataRetrieved(ArrayList<TheLat> thelatList);
    }

    public void addDonXinNghiHoc(String idGV, String id, UploadCallback callback){
        String idDon=myRef.push().getKey();
        DonXinNghiHoc donXinNghiHoc=new DonXinNghiHoc();
       // myRef.child("donxinnghihoc").child(idDon).setValue();
    }


}
