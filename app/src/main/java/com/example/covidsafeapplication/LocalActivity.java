package com.example.covidsafeapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
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
import com.google.type.DateTime;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LocalActivity extends AppCompatActivity {

    ImageButton exportPDF;
    TextView display_name, display_mesures;
    ArrayList<JSONObject> mesures_list;
    ArrayList<Mesure> mesures;
    Local current_local;
    private RequestQueue mQueue;
    private String strForExport="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        mesures_list = new ArrayList<>();
        mesures = new ArrayList<>();
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



        exportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToExport();
            }
        });
    }

    private void initMesures() {
        String url = "https://patryk.alwaysdata.net/CovidSafeRoom/api.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("mesures");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH);
                            System.out.println(jsonArray.length());
                            mesures.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject mes = jsonArray.getJSONObject(i);
                                //String dateStr = mes.getString("date_");
                                Calendar date = Calendar.getInstance();

                                date.setTime(sdf.parse(mes.getString("date_")));
                                String idMesure = mes.getString("idMesure").trim();
                                //System.out.println("id mesure: "+idMesure);
                                //int id=Integer.getInteger(idMesure);
                                String tauxMesure = mes.getString("taux").trim();
                                //int taux= Integer.getInteger(tauxMesure);
                                String typeMesure = mes.getString("typeData").trim();
                                //int type= Integer.getInteger(typeMesure);
                                String idL = mes.getString("idLocal").trim();
                                //int iL = Integer.getInteger(idL);
                                if (idL.equals(current_local.id + "")) {
                                    //mesures_list.add(mes);
                                    Mesure mesure = new Mesure(idMesure,tauxMesure,typeMesure,idL,date);
                                    mesures.add(mesure);
                                    //display_mesures.append(idMesure + ", " + tauxMesure + ", " + getTypeMesure(typeMesure) + ", " + date.toString() + ", " + idL + "\n\n");
                                }
                            }
                            Toast.makeText(getApplicationContext(), "All data fetched", Toast.LENGTH_SHORT).show();
                            //display_mesures.setText(mesures_list.toString());
                            displayMesures();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
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

    private void displayMesures() {
        displayOne("1");
        displayOne("2");
        displayOne("3");


    }

    private void displayOne(String t) {
        for (int i = 0; i < 7; i++) {
            int cpt=0;
            int current_day_sum=0;
            for (Mesure item:mesures) {
                //System.out.println(item.id+" : "+Calendar.getInstance().get(Calendar.DATE) + " >< "+item.date.get(Calendar.DATE));
                if (Calendar.getInstance().get(Calendar.DATE)-i==item.date.get(Calendar.DATE) && item.typeData.equals(t)) {
                    current_day_sum+= Integer.parseInt(item.taux);
                    cpt++;
                }
            }
            if (cpt!=0) {
                current_day_sum = current_day_sum / cpt;
            }
            String tempStr =Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-i+"/"+Calendar.getInstance().get(Calendar.MONTH)+": Avarage "+getTypeMesure(t)+ ": " + current_day_sum+ "\r\n";
            strForExport+=(tempStr);
            display_mesures.append(tempStr);
        }
    }

    private void goToListBat() {
        Intent intent = new Intent(this, ListeActivity.class);
        startActivity(intent);
    }
    private String getTypeMesure(String t){
        if (t.equals("1"))
            return "Temp";
        if (t.equals("2"))
            return getString(R.string.hum_string);
        if (t.equals("3"))
            return "CO2";
        return getString(R.string.taux_error);
    }

    private void goToExport() {
        Intent intent = new Intent(this, ExportActivity.class);
        intent.putExtra("exportStr", strForExport);
        intent.putExtra("localStr", current_local.name);
        startActivity(intent);
    }
}