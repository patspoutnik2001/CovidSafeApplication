package com.example.covidsafeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    Button apiBtn,loginBtn,photoBtn, addBtimentBtn;
    ImageButton settingsBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings =getApplicationContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        Boolean logged = settings.getBoolean("logged",false);
        String email = settings.getString("email","");
        String pass = settings.getString("pass","");
        if (logged){
            Intent intent = new Intent(this, ListeActivity.class);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(getApplicationContext(), "Welcome "+email, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });
        }

        apiBtn = (Button) findViewById(R.id.btnGoToApi);
        apiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToApi();
            }
        });

        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });

        // if the button photo is taken open back camera
        photoBtn = (Button) findViewById(R.id.qrCodePhoto);
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPhoto();
                System.out.println("test photo");
            }
        });

        // if the button settings is clicked open settings
        settingsBtn = findViewById(R.id.settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });

        // if the button settings is clicked open settings
        addBtimentBtn = findViewById(R.id.btnAjoutBatiment);
        addBtimentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddBatiment();
            }
        });
    }

    private void goToAddBatiment() {
        Intent intent = new Intent(this, AddBatiment.class);
        startActivity(intent);
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
        // TODO remettre les lignes en commentaire que le login fonctionne
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);


    }

    void goToApi(){
        Intent apiIntent = new Intent(this, APITestActivity.class);
        startActivity(apiIntent);
    }



}