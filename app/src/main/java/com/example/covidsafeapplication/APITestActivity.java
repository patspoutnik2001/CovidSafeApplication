package com.example.covidsafeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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

public class APITestActivity extends AppCompatActivity {


    ArrayList<String> tauxList;
    TextView display;
    Button fetch_btn;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apitest);

        tauxList = new ArrayList<>();
        display = (TextView) findViewById(R.id.data_output);
        fetch_btn = (Button) findViewById(R.id.btn_fetch_data);

        mQueue = Volley.newRequestQueue(this);
        if (fetch_btn!=null){
            fetch_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    fetchData();
                }
            });
        }
    }

    private void fetchData() {
        String data = "";
        String url =  "https://patryk.alwaysdata.net/CovidSafeRoom/api.php";
        //String url =  "https://patryk.alwaysdata.net/mesure.json";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject mesure = jsonArray.getJSONObject(i);
                                String id = mesure.getString("idMesure");
                                String taux = mesure.getString("taux");
                                String typeData = mesure.getString("typeData");
                                String date = mesure.getString("date_");
                                String idLocal = mesure.getString("idLocal");
                                display.append(id+", "+taux+", "+typeData+", "+date+", "+idLocal+"\n\n");
                            }
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
}