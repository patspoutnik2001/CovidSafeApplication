package com.example.covidsafeapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity {


    private Button btnToggleDark;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //System.out.println("test");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnToggleDark = (Button)findViewById(R.id.toggleButton);


        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        if (isDarkModeOn) {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //btnToggleDark.setText(R.string.btn_off_dark_mode);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //btnToggleDark.setText(R.string.btn_dark_mode);
        }

        btnToggleDark.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view)
                    {
                        // When user taps the enable/disable
                        // dark mode button
                        if (isDarkModeOn) {

                            // if dark mode is on it
                            // will turn it off
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            // it will set isDarkModeOn
                            // boolean to false
                            editor.putBoolean("isDarkModeOn", false);
                            editor.apply();

                            // change text of Button
                            //btnToggleDark.setText(R.string.btn_dark_mode);
                        }
                        else {

                            // if dark mode is off
                            // it will turn it on
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                            // it will set isDarkModeOn
                            // boolean to true
                            editor.putBoolean("isDarkModeOn", true);
                            editor.apply();

                            // change text of Button
                            //btnToggleDark.setText(R.string.btn_off_dark_mode);
                        }
                    }
                });
    }

}