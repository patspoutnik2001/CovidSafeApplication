package com.example.covidsafeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    Button apiBtn,loginBtn,photoBtn;
    String login_status=null;
    ImageButton settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();//pour prendre des variables qu'on passe entre les activites
        if (extras!=null){
            login_status = extras.getString("login_status");
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
        if (login_status!=null && login_status.equals("true")){
            loginBtn.setVisibility(View.GONE);//on cache le button car on est connected
        }

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
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(this, ListeActivity.class);
        startActivity(intent);
    }

    void goToApi(){
        Intent apiIntent = new Intent(this, APITestActivity.class);
        startActivity(apiIntent);
    }



}