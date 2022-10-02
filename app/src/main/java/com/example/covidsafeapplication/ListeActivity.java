package com.example.covidsafeapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListeActivity extends AppCompatActivity {

    ImageButton openMaps;
    Button loguot,btnProfile,btn_db;
    ArrayList<Batiment> batiments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        openMaps = findViewById(R.id.openMaps);
        btnProfile = findViewById(R.id.btn_go_to_profile);
        loguot =  findViewById(R.id.logout_btn);
        btn_db = findViewById(R.id.btn_test_db);
        batiments = new ArrayList<>();
        getBatiments();
        //TODO: quand on click sur un batiment-> envois le id du batiment a la nouvelle activite

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        loguot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        openMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // intent to map activity
                Intent intent = new Intent(ListeActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getBatiments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        db.collection("batiment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int i=Integer.parseInt( document.get("idBatiment").toString());
                                batiments.add(new Batiment(i,document.getString("nomBatiment")));
                                System.out.println(batiments.size());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}