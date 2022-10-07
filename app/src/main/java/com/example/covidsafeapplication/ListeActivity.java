package com.example.covidsafeapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListeActivity extends AppCompatActivity {

    ImageButton openMaps;
    Button loguot,btnProfile,btn_export,btn_add_bati;
    ListView batiments_listV;
    ArrayList<Batiment> batiments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        openMaps = findViewById(R.id.openMaps);
        btn_add_bati=findViewById(R.id.btn_add_bati);
        btnProfile = findViewById(R.id.btn_go_to_profile);
        loguot =  findViewById(R.id.logout_btn);
        btn_export = findViewById(R.id.btn_export);
        batiments_listV = findViewById(R.id.batiments_view);
        batiments = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                            CustomBaseAdapter adapter= new CustomBaseAdapter(getApplicationContext(),batiments);
                            batiments_listV.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        System.out.println(batiments.size()+" size");



        btn_add_bati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddBatiment();
            }
        });


        batiments_listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.w("batiments list view","item id: "+l );
                Intent intent = new Intent(ListeActivity.this, LocalListActivity.class);
                intent.putExtra("batid", batiments.get(i).idBatiment);
                intent.putExtra("batName", batiments.get(i).nomBatiment);
                startActivity(intent);
            }
        });


        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListeActivity.this, ExportActivity.class);
                startActivity(intent);
            }
        });

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
                SharedPreferences settings = getApplicationContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear().commit();
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

    private void goToAddBatiment() {
        Intent intent = new Intent(this, AddBatiment.class);
        startActivity(intent);
    }

}