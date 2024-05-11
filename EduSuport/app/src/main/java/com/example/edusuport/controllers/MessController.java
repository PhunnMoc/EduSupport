package com.example.edusuport.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edusuport.activity.Messages;
import com.example.edusuport.adapter.MessagesAdapter;
import com.example.edusuport.model.Account;
import com.example.edusuport.model.GiaoVien;
import com.example.edusuport.model.LopHoc;
import com.example.edusuport.model.MessageList;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MessController {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    ArrayList<MessageList> list=new ArrayList<MessageList>();

    public interface DataRetrievedCallback_MessageList {
        void onDataRetrieved(ArrayList<MessageList> MessageList);
    }
    public interface DataRetrievedCallback_Message {
        void onDataRetrieved(MessageList MessageList);
    }
    public interface DataRetrievedCallback_User {
        void onDataRetrieved(Account account);
    }
    String chatKey="";
    String lastMsg="";
    public void loadPartner_idCuren(String idCurUse, RecyclerView messageRecyclerView, Context context){
        list.clear();

        myRef.child("chat").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int getChatCounts = (int) task.getResult().getChildrenCount();

                if (getChatCounts > 0) {
                    for (DataSnapshot dataSnapshot1 : task.getResult().getChildren()) {
                        final String getKey = dataSnapshot1.getKey();


                        if (dataSnapshot1.hasChild("users_1") && dataSnapshot1.hasChild("users_2")) {
                            final String getUserOne = dataSnapshot1.child("users_1").getValue(String.class);
                            final String getUserTwo = dataSnapshot1.child("users_2").getValue(String.class);
                            final String roleOne = dataSnapshot1.child("role_1").getValue(String.class);
                            final String roleTwo = dataSnapshot1.child("role_2").getValue(String.class);

                            String Idpartner="";
                            String role="";
                            chatKey="";

                            if ( getUserTwo.equals(idCurUse) || (getUserOne.equals(idCurUse))) {
                                lastMsg="";
                                for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){
                                    String getMessageKey = chatDataSnapshot.getKey();
                                    //final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTs(Messages.this, getKey));

                                    lastMsg = chatDataSnapshot.child("msg").getValue(String.class);

                                }
                                chatKey="";
                                chatKey = getKey;
                                if(getUserTwo.equals(idCurUse) )
                                {
                                    Idpartner=getUserOne;
                                    role=roleOne;

                                }
                                else{
                                    Idpartner=getUserTwo;
                                    role=roleTwo;
                                }
                                MessageList messageList = new MessageList("", "", "", lastMsg, "","", chatKey);
                                chatKey="";lastMsg="";
                                getUser(Idpartner, role, new DataRetrievedCallback_User() {
                                    @Override
                                    public void onDataRetrieved(Account account) {
                                        messageList.setIdpartner(account.getIdTK());
                                        messageList.setName(account.getTen());
                                        messageList.setPhone(account.getPhoneNum());
                                        messageList.setProfilePic(account.getUrlAva());
                                        messageList.setRole(account.getRole());
                                        list.add(messageList);
                                        messageRecyclerView.setAdapter(new MessagesAdapter(list, context));

                                    }
                                });



                            }

                        }

                    }
                }

            }
        });

    }


    public void getUser(String id, String role,DataRetrievedCallback_User callbackUser){
        myRef.child(role).orderByKey().equalTo(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    DataSnapshot dataSnapshot = task.getResult().getChildren().iterator().next();
                    String ten = dataSnapshot.child("ten").getValue(String.class);
                    String urlAva = dataSnapshot.child("urlAva").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    Account acc=new Account(id,ten,urlAva,phone,role,"","","");
                    callbackUser.onDataRetrieved(acc);
                }

            }
        });
    }

    public void getListUserHS_idGV(String idCurUse,String idLopHoc, DataRetrievedCallback_MessageList callbackLopHoc){

        ArrayList<Task<Void>> tasks = new ArrayList<>();

        myRef.child("hocsinh").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<MessageList> list = new ArrayList<>(); // Khởi tạo danh sách ở đây

                for (DataSnapshot lophocSnapshot : task.getResult().getChildren()) {
                    String id = lophocSnapshot.getKey();
                    String ten = lophocSnapshot.child("ten").getValue(String.class);
                    String idlophoc = lophocSnapshot.child("idlophoc").getValue(String.class);
                    String urlava = lophocSnapshot.child("urlAva").getValue(String.class);
                    String phone = lophocSnapshot.child("phone").getValue(String.class);

                    if (idlophoc != null && idlophoc.equals(idLopHoc)) {
                        Task<DataSnapshot> chatTask = myRef.child("chat").get();
                        chatKey="";

                        tasks.add(chatTask.continueWithTask(new Continuation<DataSnapshot, Task<Void>>() {
                            @Override
                            public Task<Void> then(@NonNull Task<DataSnapshot> task) throws Exception {
                                ArrayList<Task<Void>> messageTasks = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1 : task.getResult().getChildren()) {
                                    final String getKey = dataSnapshot1.getKey();

                                    if (dataSnapshot1.hasChild("users_1") && dataSnapshot1.hasChild("users_2")) {
                                        final String getUserOne = dataSnapshot1.child("users_1").getValue(String.class);
                                        final String getUserTwo = dataSnapshot1.child("users_2").getValue(String.class);
                                        final String roleOne = dataSnapshot1.child("role_1").getValue(String.class);
                                        final String roleTwo = dataSnapshot1.child("role_2").getValue(String.class);

                                        if ( (getUserOne.equals(id) && getUserTwo.equals(idCurUse)) || (getUserOne.equals(idCurUse)&& getUserTwo.equals(id))) {
                                            lastMsg="";
                                            for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){
                                                String getMessageKey = chatDataSnapshot.getKey();
                                                //final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTs(Messages.this, getKey));

                                                lastMsg = chatDataSnapshot.child("msg").getValue(String.class);

                                            }
                                            chatKey="";
                                            chatKey = getKey;
                                            Log.d("CHafffffffffffffff", String.valueOf(lastMsg+chatKey));
                                }}}

                                list.add(new MessageList(id, ten, phone, lastMsg, urlava, "hocsinh", chatKey));
                                chatKey="";lastMsg="";
                                Log.d("CHafffffff10", String.valueOf(lastMsg+chatKey));
                                return Tasks.whenAll(messageTasks);
                            }
                        }));
                    }
                }

                Tasks.whenAll(tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> allTask) {
                        callbackLopHoc.onDataRetrieved(list); // Gọi callback khi tất cả các lệnh đã hoàn thành
                    }
                });
            }
        });}

    public void getListUserPH_idGV(String idCurUse,String idLopHoc, DataRetrievedCallback_MessageList callbackLopHoc){

        ArrayList<Task<Void>> tasks = new ArrayList<>();

        myRef.child("phuhuynh").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<MessageList> list = new ArrayList<>(); // Khởi tạo danh sách ở đây

                for (DataSnapshot lophocSnapshot : task.getResult().getChildren()) {
                    String id = lophocSnapshot.getKey();
                    String ten = lophocSnapshot.child("ten").getValue(String.class);
                    String idlophoc = lophocSnapshot.child("idlophoc").getValue(String.class);
                    String urlava = lophocSnapshot.child("urlAva").getValue(String.class);
                    String phone = lophocSnapshot.child("phone").getValue(String.class);

                    if (idlophoc != null && idlophoc.equals(idLopHoc)) {
                        Task<DataSnapshot> chatTask = myRef.child("chat").get();
                        chatKey="";

                        tasks.add(chatTask.continueWithTask(new Continuation<DataSnapshot, Task<Void>>() {
                            @Override
                            public Task<Void> then(@NonNull Task<DataSnapshot> task) throws Exception {
                                ArrayList<Task<Void>> messageTasks = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1 : task.getResult().getChildren()) {
                                    final String getKey = dataSnapshot1.getKey();

                                    if (dataSnapshot1.hasChild("users_1") && dataSnapshot1.hasChild("users_2")) {
                                        final String getUserOne = dataSnapshot1.child("users_1").getValue(String.class);
                                        final String getUserTwo = dataSnapshot1.child("users_2").getValue(String.class);
                                        final String roleOne = dataSnapshot1.child("role_1").getValue(String.class);
                                        final String roleTwo = dataSnapshot1.child("role_2").getValue(String.class);

                                        if ( (getUserOne.equals(id) && getUserTwo.equals(idCurUse)) || (getUserOne.equals(idCurUse)&& getUserTwo.equals(id))) {
                                            lastMsg="";
                                            for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){
                                                String getMessageKey = chatDataSnapshot.getKey();
                                                //final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTs(Messages.this, getKey));

                                                lastMsg = chatDataSnapshot.child("msg").getValue(String.class);

                                            }
                                            chatKey="";
                                            chatKey = getKey;
                                                       }}}

                                list.add(new MessageList(id, ten, phone, lastMsg, urlava, "phuhuynh", chatKey));
                                chatKey="";lastMsg="";
                                return Tasks.whenAll(messageTasks);
                            }
                        }));
                    }
                }

                Tasks.whenAll(tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> allTask) {
                        callbackLopHoc.onDataRetrieved(list); // Gọi callback khi tất cả các lệnh đã hoàn thành
                    }
                });
            }
        });}
    public void getGV_idGV(String idCurUse,String idGV, DataRetrievedCallback_Message callback){
        myRef.child("giaovien").orderByKey().equalTo(idGV).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            DataSnapshot dataSnapshot = task.getResult().getChildren().iterator().next();
                            String id = dataSnapshot.getKey();
                            String ten = dataSnapshot.child("ten").getValue(String.class);
                            String sdt = dataSnapshot.child("phone").getValue(String.class);
                            String urlAva = dataSnapshot.child("urlAva").getValue(String.class);
                            chatKey="";
                            myRef.child("chat").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {

                                    for (DataSnapshot dataSnapshot1 : task.getResult().getChildren()) {
                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey="";

                                        if (dataSnapshot1.hasChild("users_1") && dataSnapshot1.hasChild("users_2")) {
                                            final String getUserOne = dataSnapshot1.child("users_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("users_2").getValue(String.class);
                                            Log.e("test ",String.valueOf(getUserOne+getUserTwo));
                                            Log.e("test ",String.valueOf(id+idCurUse));
                                            if ( (getUserOne.equals(id) && getUserTwo.equals(idCurUse)) || (getUserOne.equals(idCurUse)&& getUserTwo.equals(id))) {
                                                for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){
                                                    String getMessageKey = chatDataSnapshot.getKey();
                                                    //final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTs(Messages.this, getKey));
                                                    lastMsg="";
                                                    lastMsg = chatDataSnapshot.child("msg").getValue(String.class);

                                                }
                                                chatKey="";
                                                chatKey = getKey;


                                            }


                                        }}

                                    MessageList messageList=new MessageList(id,ten,sdt,lastMsg,urlAva,"giaovien",chatKey);
                                    chatKey="";lastMsg="";
                                    callback.onDataRetrieved(messageList);
                                }
                            });

                            Log.e("chatKeyReal",String.valueOf("1"));





                        }
                    }

        });
    }
    public void getGV(String idCurent,String idLopHoc, DataRetrievedCallback_Message callbackLopHoc){
        myRef.child("lophoc").orderByKey().equalTo(idLopHoc).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    DataSnapshot dataSnapshot = task.getResult().getChildren().iterator().next();
                    String id = dataSnapshot.getKey();
                    String idgv = dataSnapshot.child("idgiaovien").getValue(String.class);

                    getGV_idGV(idCurent,idgv, new DataRetrievedCallback_Message() {
                        @Override
                        public void onDataRetrieved(MessageList MessageList) {
                            Log.e("test ",String.valueOf(MessageList));
                            callbackLopHoc.onDataRetrieved(MessageList);
                            chatKey="";lastMsg="";

                        }
                    });}}});

    }
    public void getPH(String idCurUse, DataRetrievedCallback_Message callbackLopHoc){
        myRef.child("phuhuynh").orderByKey().equalTo(idCurUse+"PH").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    DataSnapshot dataSnapshot = task.getResult().getChildren().iterator().next();
                    String id = dataSnapshot.getKey();
                    String ten = dataSnapshot.child("ten").getValue(String.class);
                    String sdt = dataSnapshot.child("phone").getValue(String.class);
                    String urlAva = dataSnapshot.child("urlAva").getValue(String.class);

                    myRef.child("chat").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            chatKey="";
                            for (DataSnapshot dataSnapshot1 : task.getResult().getChildren()) {
                                final String getKey = dataSnapshot1.getKey();

                                chatKey="";
                                if (dataSnapshot1.hasChild("users_1") && dataSnapshot1.hasChild("users_2")) {
                                    final String getUserOne = dataSnapshot1.child("users_1").getValue(String.class);
                                    final String getUserTwo = dataSnapshot1.child("users_2").getValue(String.class);
                                    Log.e("test ",String.valueOf(getUserOne+getUserTwo));
                                    Log.e("test ",String.valueOf(id+idCurUse));
                                    if ( (getUserOne.equals(id) && getUserTwo.equals(idCurUse)) || (getUserOne.equals(idCurUse)&& getUserTwo.equals(id))) {
                                        for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){
                                            String getMessageKey = chatDataSnapshot.getKey();
                                            //final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTs(Messages.this, getKey));
                                            lastMsg="";
                                            lastMsg = chatDataSnapshot.child("msg").getValue(String.class);

                                        }
                                        chatKey="";
                                        chatKey = getKey;
                                        break;
                                    }


                                }}

                        }

                    });callbackLopHoc.onDataRetrieved(new MessageList(id,ten,sdt,lastMsg,urlAva,"phuhuynh",chatKey));
                    chatKey="";lastMsg="";



                }}});

    }
    public void getHS_idPH(String idCurUse, DataRetrievedCallback_Message callbackLopHoc){
        Log.d("idCurUse.substring(0,idCurUse.length()-2)",idCurUse.substring(0,idCurUse.length()-2));
        myRef.child("hocsinh").orderByKey().equalTo(idCurUse.substring(0,idCurUse.length()-2)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    DataSnapshot dataSnapshot = task.getResult().getChildren().iterator().next();
                    String id = dataSnapshot.getKey();
                    String ten = dataSnapshot.child("ten").getValue(String.class);
                    String sdt = dataSnapshot.child("phone").getValue(String.class);
                    String urlAva = dataSnapshot.child("urlAva").getValue(String.class);

                    myRef.child("chat").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            chatKey="";
                            for (DataSnapshot dataSnapshot1 : task.getResult().getChildren()) {
                                final String getKey = dataSnapshot1.getKey();
                                chatKey="";

                                if (dataSnapshot1.hasChild("users_1") && dataSnapshot1.hasChild("users_2")) {
                                    final String getUserOne = dataSnapshot1.child("users_1").getValue(String.class);
                                    final String getUserTwo = dataSnapshot1.child("users_2").getValue(String.class);
                                    Log.e("test ",String.valueOf(getUserOne+getUserTwo));
                                    Log.e("test ",String.valueOf(id+idCurUse));
                                    if ( (getUserOne.equals(id) && getUserTwo.equals(idCurUse)) || (getUserOne.equals(idCurUse)&& getUserTwo.equals(id))) {
                                        for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){
                                            String getMessageKey = chatDataSnapshot.getKey();
                                            //final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTs(Messages.this, getKey));
                                            lastMsg="";
                                            lastMsg = chatDataSnapshot.child("msg").getValue(String.class);

                                        }
                                        chatKey="";
                                        chatKey = getKey;

                                    }


                                }}
                            callbackLopHoc.onDataRetrieved(new MessageList(id,ten,sdt,lastMsg,urlAva,"hocsinh",chatKey));
                            chatKey="";lastMsg="";
                        }

                    });

                }}});

    }
}
