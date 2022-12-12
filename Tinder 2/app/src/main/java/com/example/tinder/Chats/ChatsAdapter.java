package com.example.tinder.Chats;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;

import com.example.tinder.R;


import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsViewHolder>{

    private List<ChatsObject> chatList;
    private Context context;

    public ChatsAdapter(List<ChatsObject> matchesList, Context context){
        this.chatList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        ChatsViewHolder rcv = new ChatsViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMessage());
        if (chatList.get(position).getCurrentUser()){
            holder.mContainer.setGravity(Gravity.END);
            holder.mMessage.setTextColor(Color.WHITE);
            holder.mMessage.setBackground(ContextCompat.getDrawable(context, R.drawable.msg_background));
        }else{
            holder.mContainer.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.BLACK);
            holder.mMessage.setBackground(ContextCompat.getDrawable(context, R.drawable.opp_msg_back));
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}