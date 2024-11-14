package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.android.material.button.MaterialButton;

import ui.Perfilusuario;

public class Menu_usuario extends AppCompatActivity {

    private MaterialButton btnPerfilUsuario;
    private MaterialButton btnChatEntrenadores;
    private MaterialButton btnMatch;
    private MaterialButton btnCerrarSesion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_usuario);

        // Inicializar los botones
        btnPerfilUsuario = findViewById(R.id.btnPerfilUsuario);
        btnChatEntrenadores = findViewById(R.id.btnChatEntrenadores);
        btnMatch = findViewById(R.id.btnMatch);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Configurar el botón Editar Perfil Usuario
        btnPerfilUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_usuario.this, Perfilusuario.class);
                startActivity(intent);
            }
        });

        // Configurar el botón Chat Entrenadores
        btnChatEntrenadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_usuario.this, Chats.class);
                startActivity(intent);
            }
        });

        // Configurar el botón Match
        /*btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_usuario.this, MatchActivity.class);
                startActivity(intent);
            }
        });*/

        // Configurar el botón Cerrar Sesión
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });
    }

    // Método para cerrar sesión
    private void cerrarSesion() {
        // Aquí puedes agregar la lógica de Firebase Authentication para cerrar sesión
        // FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        finish();
    }
}
