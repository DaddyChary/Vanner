package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.android.material.button.MaterialButton;

import ui.PerfilEmpresa;

public class Menu_Empresa extends AppCompatActivity {

    // Definimos los botones
    private MaterialButton btnEditarPerfilEntrenador;
    private MaterialButton btnMensaje;
    private MaterialButton btnVolver;
    private MaterialButton btnPerfilEmpresa;
    private MaterialButton btnCerrarseccion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_empresa);
        btnPerfilEmpresa = findViewById(R.id.btnPerfilEmpresa);
        btnEditarPerfilEntrenador = findViewById(R.id.btnEditarPerfilEntrenador);
        btnMensaje = findViewById(R.id.btnMensaje);
        btnCerrarseccion = findViewById(R.id.btnCerrarseccion);

        btnEditarPerfilEntrenador.setOnClickListener(v -> abrirPerfilEntrenador());
        btnCerrarseccion.setOnClickListener(v -> cerrarSeccion());
        btnPerfilEmpresa.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_Empresa.this, PerfilEmpresa.class);
                startActivity( intent );
            }
        } );

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


    private void cerrarSeccion() {
        finish();
    }
}
