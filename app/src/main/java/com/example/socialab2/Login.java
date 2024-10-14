package com.example.socialab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private Button loginButtton,backButton;
    private TextView forgotPasswordTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginButtton = findViewById(R.id.loginButton);
        backButton = findViewById(R.id.backButton);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,OlvidarPass.class);
                startActivity(intent);
            }
        });

    }
}
