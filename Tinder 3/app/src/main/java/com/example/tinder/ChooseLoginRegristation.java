package com.example.tinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Title: ChooseLoginRegristation.java
// Course: CSC 330
// Developer: John Santiago, Peter Wang, Mohamed Bassimbo, Andro Rezkalla
// Date : 12/12/22
// Description: Choose Login or Register source code.
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


public class ChooseLoginRegristation extends AppCompatActivity {


    private Button mLogin, mRegister;


// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: onCreate()
// Input: Bundle
// Output: void
// Purpose: set the layout for Login/Registration page
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_regristation);


        mLogin = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);


// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: mLogin()
// Input:
// Output: void
// Purpose:Allow user to interact with Login button
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseLoginRegristation.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Name: mRegister()
// Input:
// Output: void
// Purpose:Allow user to interact with register button
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseLoginRegristation.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

    }
}