package com.example.covidsafeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;

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
    }

    void goToApi(){
        Intent apiIntent = new Intent(this, APITestActivity.class);
        startActivity(apiIntent);
    }



}