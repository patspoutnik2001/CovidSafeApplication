package com.example.covidsafeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ActivityMainBinding extends AppCompatActivity {


//    ActivityMainBinding binding;
    ArrayList<String> tauxList;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    ArrayAdapter<String> listAdapter;
    Button fetchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchButton = (Button) findViewById(R.id.btn_get_data);
//        initList();
        tauxList = new ArrayList<>();

//        fetchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });

        new fetchData().start();

    }


//    private void initList() {
//        tauxList = new ArrayList<>();
//        listAdapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item,tauxList);
//        //binding.tauxList.setAdapter(listAdapter);
//    }


    class fetchData extends Thread{


        String data = "";

        @Override
        public void run() {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {


                    progressDialog = new ProgressDialog(ActivityMainBinding.this);
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                }
            });

            try {
                URL url = new URL( "https://patryk.alwaysdata.net/CovidSafeRoom/api.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine())!=null){
                    data= data+line;
                }

                if (!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray mesures = jsonObject.getJSONArray("taux");
                    System.out.println(mesures);
                    tauxList.clear();

                    for (int i = 0; i < mesures.length(); i++) {
                        JSONObject mes = mesures.getJSONObject(i);
                        String mesure = mes.getString("taux");
                        tauxList.add(mesure);
                        System.out.println(mesure);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}