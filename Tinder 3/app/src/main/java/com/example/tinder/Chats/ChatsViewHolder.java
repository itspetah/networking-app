package com.example.tinder.Chats;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinder.R;

// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Title: ChatsViewHolder.java
// Course: CSC 330
// Developer: John Santiago, Peter Wang, Mohamed Bassimbo, Andro Rezkalla
// Date : 12/12/22
// Description: ViewHolder main source code.
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public class ChatsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView mMessage;
    public LinearLayout mContainer;

    public ChatsViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMessage = itemView.findViewById(R.id.message);
        mContainer = itemView.findViewById(R.id.container);
    }

    @Override
    public void onClick(View view) {
    }
}