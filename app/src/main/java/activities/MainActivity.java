package activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;

import ui.Login;
import ui.Registro;

public class MainActivity extends AppCompatActivity {
    private Button loginButton,registroButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginButton);
        registroButton = findViewById(R.id.registroButton);

        if (loginButton == null) {
            Log.e("MainActivity", "loginButton is null");
        }
        if (registroButton == null) {
            Log.e("MainActivity", "registroButton is null");
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        registroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Registro.class);
                startActivity(intent);
            }
        });
    }

}