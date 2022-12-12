package com.example.tinder.Chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.tinder.Matches.MatchesActivity;
import com.example.tinder.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private String currentUserId, matchId, chatId;

    private EditText mSendEditText;
    private ImageButton mSendButton;
    private TextView mName;
    private LinearLayoutManager mLinearLayoutManager;

    DatabaseReference mDatabaseUser, mDatabaseChat;

    MaterialToolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        matchId = getIntent().getExtras().getString("matchId");
        String Name = getIntent().getExtras().getString("name");

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches").child(matchId).child("chatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        mName = findViewById(R.id.t_v_name);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mName.setText(Name);

        getChatId();

        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mlinearLayoutManager.setStackFromEnd(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatsAdapter(getDataSetChat(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);
        mRecyclerView.smoothScrollToPosition(mChatAdapter.getItemCount());

        mSendEditText = findViewById(R.id.messageText);
        mSendButton = findViewById(R.id.send);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String sendMessageText = mSendEditText.getText().toString();
        if (!sendMessageText.isEmpty()) {
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map newMessage = new HashMap();
            newMessage.put("createByUser", currentUserId);
            newMessage.put("text", sendMessageText);

            newMessageDb.setValue(newMessage);
        }
        mSendEditText.setText(null);
    }

    private void getChatId() {
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    chatId = Objects.requireNonNull(snapshot.getValue()).toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String message = null;
                    String createdByUser = null;
                    if (snapshot.child("text").getValue() != null) {
                        message = snapshot.child("text").getValue().toString();
                    }
                    if (snapshot.child("createByUser").getValue() != null) {
                        createdByUser = snapshot.child("createByUser").getValue().toString();
                    }

                    if (message != null && createdByUser != null) {
                        Boolean currentUserBoolean = false;
                        if (createdByUser.equals(currentUserId)) {
                            currentUserBoolean = true;
                        }
                        ChatsObject newMessage = new ChatsObject(message, currentUserBoolean);
                        resultChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private ArrayList<ChatsObject> resultChat = new ArrayList<ChatsObject>();

    private List<ChatsObject> getDataSetChat() {
        return resultChat;
    }
}