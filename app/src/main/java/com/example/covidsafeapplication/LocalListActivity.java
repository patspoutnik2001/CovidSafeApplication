package com.example.covidsafeapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocalListActivity extends AppCompatActivity {


    ListView local_listView;
    TextView display_tv;
    Batiment batiment;
    ArrayList<Local> locals = new ArrayList<>();

    String url="";
    static float lon, lat=0;
    TextView tvTemp;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_list);

        context=this;
        local_listView = findViewById(R.id.local_list);
        display_tv = findViewById(R.id.local_name_display);
        tvTemp = findViewById(R.id.tvTemp);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("batid");
            String name = extras.getString("batName");
            batiment = new Batiment(id, name);
        }

        //  tvTemp.setText("Temperature: " + city);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("local")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int bat = Integer.parseInt(document.get("idBatiment").toString());
                                int idLocal = Integer.parseInt(document.get("idLocal").toString());

                                if (bat == batiment.idBatiment)
                                    locals.add(new Local(bat, idLocal, document.getString("nom")));


                            }
                            System.out.println("locals: " + locals.size());
                            LocalListAdapter adapter = new LocalListAdapter(getApplicationContext(), locals);
                            local_listView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        display_tv.setText(batiment.nomBatiment);

        local_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LocalListActivity.this, LocalActivity.class);
                intent.putExtra("idLocal", locals.get(i).id);
                startActivity(intent);
            }
        });

        loadWeatherByCityName();
    }
    private void loadWeatherByCityName() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("batiment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int i=Integer.parseInt( document.get("idBatiment").toString());
                                if( i == batiment.idBatiment){

                                    // get latitude and longitude as float
                                    lat = Float.parseFloat(document.get("latitude").toString());
                                    lon = Float.parseFloat(document.get("longitude").toString());
                                    url = "https://api.openweathermap.org/data/2.5/weather?lat="+lat + "&lon="+ lon + "&appid=5bdff022d351650cee55cec49c82c67b&units=metric";

                                    // get the temp from openweather api
                                    Ion.with(context)
                                            .load(url)
                                            .asJsonObject()
                                            .setCallback((e, result) -> {
                                                if (e != null) {
                                                    Toast.makeText(context, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                if (result != null) {
                                                    System.out.println(url);
                                                    String temp = result.get("main").getAsJsonObject().get("temp").getAsString();
                                                    System.out.println(temp);
                                                    // transform the temp from kelvin to celsius
                                                    //float celsius = Float.parseFloat(temp) - 283.15f + 1;
                                                    tvTemp.setText("Temperature: " + temp + "°C");
                                                }
                                            });
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });




    }
}