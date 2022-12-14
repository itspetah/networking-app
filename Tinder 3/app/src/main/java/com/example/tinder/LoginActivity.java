package com.example.tinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Title: LoginActivity.java
// Course: CSC 330
// Developer: John Santiago, Peter Wang, Mohamed Bassimbo, Andro Rezkalla
// Date : 12/12/22
// Description: Login source code.
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public class LoginActivity extends AppCompatActivity {

    private Button mLogin;
    private Button mlogBack;
    private EditText mEmail, mPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: onCreate()
// Input: Bundle
// Output: void
// Purpose: set the layout for Login page
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };


        mLogin = (Button) findViewById(R.id.login);
        mlogBack = (Button) findViewById(R.id.logBack);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);


// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: mLogin()
// Input:
// Output: void
// Purpose:Allow user Login into pre-existing account
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "sign in error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: mlogBack()
// Input:
// Output: void
// Purpose:Allow user to interact with back button
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



        mlogBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ChooseLoginRegristation.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, ChooseLoginRegristation.class);
        startActivity(intent);
        finish();
        return;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}