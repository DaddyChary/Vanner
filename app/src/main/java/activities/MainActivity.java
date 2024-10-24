package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.socialab2.R;

public class MainActivity extends AppCompatActivity {
    private Button loginButton,registroButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        loginButton = findViewById(R.id.loginButton);
        registroButton = findViewById(R.id.registroButton);

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
//                Intent intent = new Intent(MainActivity.this, Registro.class);
//                startActivity(intent);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.registro, new Registro()); // Asegúrate de usar el ID correcto del contenedor donde se colocan los fragmentos
                transaction.addToBackStack(null); // Esto te permite volver al fragmento anterior
                transaction.commit();
            }
        });
        }
    }