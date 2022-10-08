package com.example.covidsafeapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LocalActivity extends AppCompatActivity {

    ImageButton exportPDF;
    TextView display_name, display_mesures;
    ArrayList<JSONObject> mesures_list;
    Local current_local;
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        mesures_list = new ArrayList<>();
        mQueue = Volley.newRequestQueue(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("idLocal");

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
                                    String name_local = document.getString("nom");

                                    if (idLocal == id) {
                                        current_local = new Local(bat, idLocal, name_local);
                                        display_name.setText(current_local.name);
                                        initMesures();
                                    }
                                }
                                if (current_local == null) {
                                    goToListBat();
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }

        exportPDF = findViewById(R.id.exportPDF);
        display_name = findViewById(R.id.tv_display_local_name);
        display_mesures = findViewById(R.id.display_all_mesures);
        display_mesures.setMovementMethod(new ScrollingMovementMethod());
        if (current_local != null) {

        }


        exportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToExport();
            }
        });
    }

    private void initMesures() {
        String data = "";
        String url = "https://patryk.alwaysdata.net/CovidSafeRoom/api.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("mesures");
                            System.out.println(jsonArray.length());
                            mesures_list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject mes = jsonArray.getJSONObject(i);
                                String date = mes.getString("date_");
                                String idMesure = mes.getString("idMesure");
                                //int id=Integer.getInteger(idMesure);
                                String taux = mes.getString("taux");
                                //int t= Integer.getInteger(taux);
                                String type = mes.getString("typeData");
                                //int typeData= Integer.getInteger(type);
                                String idL = mes.getString("idLocal");
                                //int iL = Integer.getInteger(idL);
                                if (idL.equals(current_local.id + "")) {
                                    mesures_list.add(mes);
                                    display_mesures.append(idMesure + ", " + taux + ", " + type + ", " + date + ", " + idL + "\n\n");
                                }
                            }
                            Toast.makeText(getApplicationContext(), "All data fetched", Toast.LENGTH_SHORT).show();
                            //display_mesures.setText(mesures_list.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);


    }

    private void goToListBat() {
        Intent intent = new Intent(this, ListeActivity.class);
        startActivity(intent);
    }

    private void goToExport() {
        Intent intent = new Intent(this, ExportActivity.class);
        startActivity(intent);
    }
}