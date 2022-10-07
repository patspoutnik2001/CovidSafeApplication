package com.example.covidsafeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class LocalActivity extends AppCompatActivity {

    ImageButton exportPDF;
    TextView display_name,display_mesures;
    ArrayList<Mesure> mesures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        mesures = new ArrayList<>();

        exportPDF = findViewById(R.id.exportPDF);
        display_name= findViewById(R.id.tv_display_local_name);
        display_mesures=findViewById(R.id.display_all_mesures);
        display_mesures.setMovementMethod(new ScrollingMovementMethod());



        exportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToExport();

            }
        });
    }

    private void goToExport() {
        Intent intent = new Intent(this, ExportActivity.class);
        startActivity(intent);
    }
}