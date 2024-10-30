package activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EliminarAdmin extends AppCompatActivity {
    private Button btnEliminarAdmin, btnVolver;
    private EditText et_correo;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eliminaradministrador);

        btnEliminarAdmin = findViewById(R.id.btnEliminarAdmin);
        btnVolver = findViewById(R.id.btnVolver);
        et_correo = findViewById(R.id.et_correo);

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        auth = FirebaseAuth.getInstance();

        btnEliminarAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = et_correo.getText().toString().trim();
                if (!correo.isEmpty()) {
                    eliminarUsuarioPorCorreo(correo);
                } else {
                    Toast.makeText(EliminarAdmin.this, "Por favor, introduce un correo.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnVolver.setOnClickListener(view -> finish());
    }

    private void eliminarUsuarioPorCorreo(String correo) {
        Log.d("EliminarAdmin", "Correo ingresado: " + correo);  // Log para verificar el correo ingresado
        FirebaseFunctions.getInstance()
                .getHttpsCallable("obtenerUidPorCorreo")
                .call(Collections.singletonMap("email", correo))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = (String) ((HashMap) task.getResult().getData()).get("uid");
                        eliminarUsuarioDeDB(uid);
                    } else {
                        Toast.makeText(EliminarAdmin.this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void eliminarUsuarioDeDB(String uid) {
        // Busca y elimina al usuario en Realtime Database
        databaseReference.child(uid).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EliminarAdmin.this, "Usuario eliminado de la base de datos.", Toast.LENGTH_SHORT).show();
                        eliminarUsuarioDeAuth(uid);  // Eliminar en Firebase Authentication
                    } else {
                        Toast.makeText(EliminarAdmin.this, "Error al eliminar el usuario de la base de datos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void eliminarUsuarioDeAuth(String uid) {
        FirebaseFunctions.getInstance()
                .getHttpsCallable("eliminarUsuario")
                .call(Collections.singletonMap("uid", uid))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EliminarAdmin.this, "Usuario eliminado de autenticación.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EliminarAdmin.this, "Error al eliminar de autenticación: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
