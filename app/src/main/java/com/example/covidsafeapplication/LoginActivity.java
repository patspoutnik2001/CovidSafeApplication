package com.example.covidsafeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    TextView email_input,pass_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        loginBtn = (Button) findViewById(R.id.btn_login);
        email_input = (TextView) findViewById(R.id.email_input);
        pass_input = (TextView) findViewById(R.id.password_input);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = email_input.getText().toString().trim();
        String pass = pass_input.getText().toString().trim();

        if (email.equals("abc@gmail.com")&&pass.equals("admin1234")){
            Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("login_status","true");
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Email or Password incorrect", Toast.LENGTH_SHORT).show();

        }
    }
}