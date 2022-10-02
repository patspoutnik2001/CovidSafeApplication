package com.example.covidsafeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    TextView emailOutput,userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            Intent intent = new Intent(ProfileActivity.this, ListeActivity.class);
            startActivity(intent);
        }

        emailOutput = findViewById(R.id.output_email);
        userID = findViewById(R.id.userId_output);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        emailOutput.setText(email);
        userID.setText(uId);

    }
}