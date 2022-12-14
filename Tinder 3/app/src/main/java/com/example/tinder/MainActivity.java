package com.example.tinder;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;



import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.example.tinder.Matches.MatchesActivity;
import com.example.tinder.cards.arrayAdapter;
import com.example.tinder.cards.cards;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;



// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Title: MainActivity.java
// Course: CSC 330
// Developer: John Santiago, Peter Wang, Mohamed Bassimbo, Andro Rezkalla
// Date : 12/12/22
// Description: Main source code.
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



public class MainActivity extends AppCompatActivity {
    private cards cards_data[];
    private com.example.tinder.cards.arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private String currentUId;

    private DatabaseReference getUsersDb;

    ListView listView;
    List<cards> rowItems;

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: onCreate()
// Input: savedInstancestate
// Output: void
// Purpose: Create layout for cards
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        checkUserSex();

        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: RemoveFirstObjectInAdapter()
// Input:
// Output: void
// Purpose: Remove objects from array
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }


// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: onLeftCardExit
// Input: dataObject
// Output: void
// Purpose: When a card is swiped left
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

            @Override
            public void onLeftCardExit(Object dataObject) {

                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("nope").child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
            }

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: onRightCardExit()
// Input: dataObject
// Output: void
// Purpose: when a card is swiped right
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yep").child(currentUId).setValue(true);
                isConnectionMatch(userId);
                Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });



        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: isConnectionMatch
// Input: strings
// Output: void
// Purpose: Check if a User is connected based off swipe feature
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yep").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(MainActivity.this, "new Connection", Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: CheckUserSex()
// Input:
// Output: void
// Purpose: Checks the users sex
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private String userSex;
    private String oppositeUserSex;
    public void checkUserSex(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("sex").getValue() != null){
                        userSex = dataSnapshot.child("sex").getValue().toString();
                        switch (userSex){
                            case "Male":
                                oppositeUserSex = "Female";
                                break;
                            case "Female":
                                oppositeUserSex = "Male";
                                break;
                        }
                        getOppositeSexUsers();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: getOppositeSexUsers
// Input:
// Output: void
// Purpose: Get the opposite of the User
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void getOppositeSexUsers(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("sex").getValue() != null) {
                    if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUId) && !dataSnapshot.child("connections").child("yep").hasChild(currentUId) && dataSnapshot.child("sex").getValue().toString().equals(oppositeUserSex)) {
                        String profileImageUrl = "default";
                        if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")) {
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }
                        cards item = new cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(), profileImageUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
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

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: lougoutUser
// Input: View
// Output: void
// Purpose: Log the user out
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, ChooseLoginRegristation.class);
        startActivity(intent);
        finish();
        return;
    }

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: goToSettings
// Input: View
// Output: void
// Purpose: Allow user to interact with the setting button
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void goToSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        return;
    }
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: goToMatches()
// Input: View
// Output: void
// Purpose: allow user to interact with the matches button
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
        startActivity(intent);
        return;
    }



}