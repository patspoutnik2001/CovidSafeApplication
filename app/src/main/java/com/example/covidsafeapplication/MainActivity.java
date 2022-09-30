package com.example.covidsafeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.covidsafeapplication.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    Button apiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        apiBtn = (Button) findViewById(R.id.btnGoToApi);
        apiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToApi();
            }
        });



        Button loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });


        // if the button photo is taken open back camera
        Button photoBtn = (Button) findViewById(R.id.qrCodePhoto);
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPhoto();
                System.out.println("test");
            }
        });


        // if the button settings is clicked open settings
        ImageButton settingsBtn = findViewById(R.id.settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });





    }

    private void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void goToPhoto() {
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    void goToApi(){
        Intent apiIntent = new Intent(this, ActivityMainBinding.class);
        startActivity(apiIntent);
    }



}