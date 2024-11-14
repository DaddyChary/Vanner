package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.socialab2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ui.Perfilusuario;

public class Menu_entrenador extends AppCompatActivity {

    private MaterialButton btnChat, btnEditarPerfilEntrenador, btnMatch, btnCerrarseccion;
    private MaterialTextView titleText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_entrenador);

        btnChat = findViewById(R.id.btnChat);
        btnEditarPerfilEntrenador = findViewById(R.id.btnEditarPerfilEntrenador);
        btnMatch = findViewById(R.id.btnMatch);
        btnCerrarseccion = findViewById(R.id.btnCerrarseccion);
        titleText = findViewById(R.id.titleText);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_entrenador.this, "Abriendo Chat...", Toast.LENGTH_SHORT).show();
            }
        });

        btnEditarPerfilEntrenador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();  // Obtener el ID del usuario autenticado
                    Toast.makeText(Menu_entrenador.this, "Abriendo Perfil Entrenador...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Menu_entrenador.this, Perfilusuario.class);
                    intent.putExtra("userId", userId);  // Pasar el userId a la actividad Perfilusuario
                    startActivity(intent);
                } else {
                    Toast.makeText(Menu_entrenador.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_entrenador.this, "Buscando un Match...", Toast.LENGTH_SHORT).show();
            }
        });

        btnCerrarseccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_entrenador.this, "Sección Cerrada.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Menu_entrenador.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
