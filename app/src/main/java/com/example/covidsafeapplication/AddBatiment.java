package com.example.covidsafeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class AddBatiment extends AppCompatActivity {

    Button btn_addBatiment;
    TextView nomBatiment_input,adresse_input;
    private FirebaseAuth mAuth;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_add_batiment);
        btn_addBatiment = (Button) findViewById(R.id.btn_addBatiment);
        nomBatiment_input = (TextView) findViewById(R.id.nomBatiment_input);
        adresse_input = (TextView) findViewById(R.id.adresse_input);



        btn_addBatiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addBatiment();
                // TODO: remettre add batiment

                testLocalisation();
            }
        });
    }

    private void testLocalisation() {
        geocoder = new Geocoder(this);

        try {
            geocoder.getFromLocationName(adresse_input.getText().toString(), 1);
            System.out.println(geocoder.getFromLocationName(adresse_input.getText().toString(), 1));
        } catch (IOException e) {
            System.out.println("l'adresse n'existe pas !!!!!!!!!!!!");

        }

    }


    private void addBatiment() {
        String nom = nomBatiment_input.getText().toString().trim();
        String adresse = adresse_input.getText().toString().trim();

        //TODO : il aime pas cette ligne psk ça correspond pas a ce que firebase attend
        mAuth.createUserWithEmailAndPassword(nom,adresse).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Batiment Added", Toast.LENGTH_SHORT).show();
                    batimentAdded();
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void batimentAdded(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}