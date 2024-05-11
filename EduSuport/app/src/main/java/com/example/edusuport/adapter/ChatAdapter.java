package com.example.edusuport.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edusuport.R;
import com.example.edusuport.model.ChatList;
import com.example.edusuport.model.MemoryData;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatList> chatLists;
    private final Context context;
    private String idCurUser;

    public ChatAdapter(List<ChatList> chatLists, Context context,String idCurUser) {
        this.chatLists = chatLists;
        this.context = context;
        this.idCurUser = idCurUser;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_textchat, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        ChatList list2 = chatLists.get(position);

        if (chatLists.get(position).getidUser().equals(idCurUser)){
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);
            holder.myMsg.setText(list2.getMessage());
            holder.myTime.setText(list2.getDate() + " " + list2.getTime());
        }
        else {
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);
            holder.oppMsg.setText(list2.getMessage());
            holder.oppTime.setText(list2.getDate() + " " + list2.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public void updateChatList(List<ChatList> chatLists){
        this.chatLists = chatLists;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout oppoLayout, myLayout;
        private TextView oppMsg, myMsg;
        private TextView oppTime, myTime;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            oppMsg = itemView.findViewById(R.id.oppoMsg);
            myMsg = itemView.findViewById(R.id.myMsg);
            oppTime = itemView.findViewById(R.id.oppoMsgTime);
            myTime = itemView.findViewById(R.id.myMsgTime);
        }
    }
}
