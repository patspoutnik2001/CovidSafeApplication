package com.example.covidsafeapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBatiment extends AppCompatActivity {

    Button btn_addBatiment;
    TextView nomBatiment_input,adresse_input;
    Geocoder geocoder;
    int bati_count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_batiment);
        btn_addBatiment = (Button) findViewById(R.id.btn_addBatiment);
        nomBatiment_input = (TextView) findViewById(R.id.nomBatiment_input);
        adresse_input = (TextView) findViewById(R.id.adresse_input);



        btn_addBatiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBatiment();
            }
        });
    }

    private double[] testLocalisation(String _a) {
        geocoder = new Geocoder(this);
        double [] cord = new double[2];
        try {
            List<Address> location =geocoder.getFromLocationName(_a, 1);
            cord[0]=location.get(0).getLatitude();
            cord[1]=location.get(0).getLongitude();
            System.out.println(geocoder.getFromLocationName(_a, 1));

        } catch (IOException e) {
            System.out.println("l'adresse n'existe pas !!!!!!!!!!!!");

        }
        return cord;
    }


    private void addBatiment() {
        String nom = nomBatiment_input.getText().toString().trim();
        String adresse = adresse_input.getText().toString().trim();
        double[] cord = testLocalisation(adresse);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        bati_count=0;

        db.collection("batiment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        bati_count++;
                    }
                    bati_count++;
                    Map<String, Object> bati = new HashMap<>();
                    bati.put("idBatiment", bati_count);
                    bati.put("nomBatiment", nom);
                    bati.put("address", adresse);
                    bati.put("latitude", cord[0]);
                    bati.put("longitude", cord[1]);

                    db.collection("batiment")
                            .add(bati)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Added with Success", Toast.LENGTH_SHORT).show();
                                        goback();

                                    }else{
                                        Toast.makeText(getApplicationContext(), "Cant add new building", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });





    }

     void goback(){
         Intent intent = new Intent(this, ListeActivity.class);
         startActivity(intent);
     }


}