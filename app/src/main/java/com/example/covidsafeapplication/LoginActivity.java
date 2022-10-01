package com.example.covidsafeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn,registerBtn;
    TextView email_input,pass_input;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        loginBtn = (Button) findViewById(R.id.btn_login);
        registerBtn = (Button) findViewById(R.id.btn_register);
        email_input = (TextView) findViewById(R.id.email_input);
        pass_input = (TextView) findViewById(R.id.password_input);
        mAuth = FirebaseAuth.getInstance();

        //TODO: verif bug
        if (mAuth.getCurrentUser() != null){
            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
            //finish();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void registerUser() {
        String email = email_input.getText().toString().trim();
        String pass = pass_input.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();

                }
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