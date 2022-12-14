package com.example.tinder.Chats;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;



import com.example.tinder.Matches.MatchesActivity;
import com.example.tinder.R;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Title: ChatActivity.java
// Course: CSC 330
// Developer: John Santiago, Peter Wang, Mohamed Bassimbo, Andro Rezkalla
// Date : 12/12/22
// Description: Chat main source code.
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private EditText mSendEditText;

    private Button mSendButton, mMessageBack;

    private String currentUserID, matchId, chatId;

    DatabaseReference mDatabaseUser, mDatabaseChat;

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: onCreate()
// Input:
// Output: void
// Purpose: create layout for Chat page
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchId = getIntent().getExtras().getString("matchId");

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches").child(matchId).child("ChatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        getChatId();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setHasFixedSize(true);
        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatsAdapter(getDataSetChat(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendEditText = findViewById(R.id.message);
        mSendButton = findViewById(R.id.send);
        mMessageBack = findViewById(R.id.mesgBack);

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: mSendButton()
// Input:
// Output: void
// Purpose: Allow user to interact with send button to send a message
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: mMessageBack()
// Input:
// Output: void
// Purpose: Allow user to interact with back button
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        mMessageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, MatchesActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

    }
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: sendMessage()
// Input:
// Output: void
// Purpose: Allow user to send a message
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private void sendMessage() {
        String sendMessageText = mSendEditText.getText().toString();

        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserID);
            newMessage.put("text", sendMessageText);

            newMessageDb.setValue(newMessage);
        }
        mSendEditText.setText(null);
    }

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: getChatId()
// Input:
// Output: void
// Purpose: retrieve Chat Id created upon matching
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: getChatMessage()
// Input:
// Output: void
// Purpose: Get Message from dataBase
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String createdByUser = null;

                    if(dataSnapshot.child("text").getValue()!=null){
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("createdByUser").getValue()!=null){
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }

                    if(message!=null && createdByUser!=null){
                        Boolean currentUserBoolean = false;
                        if(createdByUser.equals(currentUserID)){
                            currentUserBoolean = true;
                        }
                        ChatsObject newMessage = new ChatsObject(message, currentUserBoolean);
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private ArrayList<ChatsObject> resultsChat = new ArrayList<ChatsObject>();
    private List<ChatsObject> getDataSetChat() {
        return resultsChat;
    }
}