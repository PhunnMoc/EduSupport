package com.example.edusuport.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.edusuport.R;
import com.example.edusuport.activity.Chat;
import com.example.edusuport.activity.Messages;
import com.example.edusuport.activity.Messages_HS;
import com.example.edusuport.activity.Messages_PH;
import com.example.edusuport.model.MessageList;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private List<MessageList> messageLists;
    private final Context context;

    public MessagesAdapter(List<MessageList> messageLists, Context context) {
        this.messageLists = messageLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MessageList list2 = messageLists.get(position);
        if (!list2.getProfilePic().isEmpty()){
                Picasso.get().load(list2.getProfilePic()).into(holder.profilePicture);
        }
        else {
            Picasso.get().load(R.drawable.profile).into(holder.profilePicture);
        }

        holder.name.setText(list2.getName());
        holder.lastMsg.setText(list2.getLastMsg());
        holder.unseenMsg.setVisibility(View.GONE);
          holder.lastMsg.setTextColor(Color.parseColor("#959595"));

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("id", list2.getIdpartner());
                intent.putExtra("name", list2.getName());
                intent.putExtra("profilePic",list2.getProfilePic());
                intent.putExtra("phone",list2.getPhone());
                intent.putExtra("chat_key", list2.getChatKey());
                intent.putExtra("role", list2.getRole());
                Log.d("sos", list2.getChatKey()+ list2.getName());
                if(context.getClass()==Messages.class){
                    intent.putExtra("roleUser", "giaovien");
                }
                if(context.getClass()== Messages_HS.class){
                    intent.putExtra("roleUser", "hocsinh");
                }
                if(context.getClass()== Messages_PH.class){
                    intent.putExtra("roleUser", "phuhuynh");
                }
                context.startActivity(intent);
            }
        });
    }


    public void updateData(List<MessageList> messageLists){
        this.messageLists = messageLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profilePicture;
        private TextView name;
        private TextView lastMsg;
        private TextView unseenMsg;
        private LinearLayout rootLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            name = itemView.findViewById(R.id.name);
            lastMsg = itemView.findViewById(R.id.lastMsg);
            unseenMsg = itemView.findViewById(R.id.unseenMsg);
            rootLayout = itemView.findViewById(R.id.msglayout);
        }
    }
}
