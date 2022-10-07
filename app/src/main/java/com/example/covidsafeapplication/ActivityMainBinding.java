package com.example.covidsafeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


    ArrayList<String> tauxList;
    TextView display;
    Button fetch_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tauxList = new ArrayList<>();
        //display = (TextView) findViewById(R.id.display);
        fetch_btn = (Button) findViewById(R.id.btn_fetch_data);
        if (fetch_btn!=null){
            fetch_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fetchData();
                }
            });
        }

    }


    public void fetchData(){
        String data = "";
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
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Toast.makeText(getApplicationContext(), "All data fetched", Toast.LENGTH_SHORT).show();
        display.setText(tauxList.toString());

    }
}