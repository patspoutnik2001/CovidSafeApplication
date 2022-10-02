package com.example.covidsafeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class LocalActivity extends AppCompatActivity {

    ImageButton exportPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        exportPDF = findViewById(R.id.exportPDF);

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