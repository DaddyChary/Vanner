package com.example.socialab2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OlvidarPass extends AppCompatActivity {
    private Button backButtonOlvidarPass,recuperarPassButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.olvidarpass);

        backButtonOlvidarPass = findViewById(R.id.backButtonOlvidarPass);
        recuperarPassButton = findViewById(R.id.recuperarPassButton);

        backButtonOlvidarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}