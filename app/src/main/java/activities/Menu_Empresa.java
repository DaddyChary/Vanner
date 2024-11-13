package activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.android.material.button.MaterialButton;

public class Menu_Empresa extends AppCompatActivity {

    // Definimos los botones
    private MaterialButton btnEditarPerfilEntrenador;
    private MaterialButton btnMensaje;
    private MaterialButton btnVolver;
    private MaterialButton btnCerrarseccion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_empresa);

        btnEditarPerfilEntrenador = findViewById(R.id.btnEditarPerfilEntrenador);
        btnMensaje = findViewById(R.id.btnMensaje);
        btnVolver = findViewById(R.id.btnVolver);
        btnCerrarseccion = findViewById(R.id.btnCerrarseccion);

        btnEditarPerfilEntrenador.setOnClickListener(v -> abrirPerfilEntrenador());
        btnVolver.setOnClickListener(v -> volverAInicio());
        btnCerrarseccion.setOnClickListener(v -> cerrarSeccion());
    }

    private void abrirPerfilEntrenador() {
        Intent intent = new Intent(Menu_Empresa.this, Menu_entrenador.class);
        startActivity(intent);
    }

    /*private void abrirMensajes() {
        // Aquí va la lógica para abrir la actividad de mensajes
        Intent intent = new Intent(Menu_Empresa.this, MensajesActivity.class);
        startActivity(intent);
    }*/

    private void volverAInicio() {
        Intent intent = new Intent(Menu_Empresa.this, MainActivity.class);
        startActivity(intent);
    }

    private void cerrarSeccion() {
        finish();
    }
}
