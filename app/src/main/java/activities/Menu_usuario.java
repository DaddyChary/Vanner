package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kotlin.collections.IntIterator;
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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();  // Obtener el ID del usuario autenticado
                    Toast.makeText(Menu_usuario.this, "Abriendo Perfil Entrenador...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Menu_usuario.this, Perfilusuario.class);
                    intent.putExtra("userId", userId);  // Pasar el userId a la actividad Perfilusuario
                    startActivity(intent);
                } else {
                    Toast.makeText(Menu_usuario.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                }
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
        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_usuario.this,TrabajoGeneral.class);
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
