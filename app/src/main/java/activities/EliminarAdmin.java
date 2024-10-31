package activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
    private TableLayout tbl_eliminar_administradores;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eliminaradministrador);

        btnEliminarAdmin = findViewById(R.id.btnEliminarAdmin);
        btnVolver = findViewById(R.id.btnVolver);
        et_correo = findViewById(R.id.et_correo);
        tbl_eliminar_administradores = findViewById(R.id.tbl_eliminar_administradores);

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        auth = FirebaseAuth.getInstance();

        // Cargar correos en el TableLayout
        cargarCorreos();

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

    private void cargarCorreos() {
        // Limpiar la tabla antes de cargar los correos
        tbl_eliminar_administradores.removeAllViews();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String correo = userSnapshot.child("mail").getValue(String.class);
                    if (correo != null) {
                        agregarFilaCorreo(correo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EliminarAdmin.this, "Error al cargar los correos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarFilaCorreo(String correo) {
        TableRow row = new TableRow(this);
        TextView correoTextView = new TextView(this);

        correoTextView.setText(correo);
        correoTextView.setPadding(16, 16, 16, 16);
        correoTextView.setOnClickListener(view -> et_correo.setText(correo));  // Copiar correo al campo de búsqueda

        row.addView(correoTextView);
        tbl_eliminar_administradores.addView(row);
    }

    private void eliminarUsuarioPorCorreo(String correo) {
        Log.d("EliminarAdmin", "Correo ingresado: " + correo);  // Verifica el correo ingresado

        FirebaseFunctions.getInstance()
                .getHttpsCallable("obtenerUidPorCorreo")
                .call(Collections.singletonMap("email", correo))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getData() != null) {
                        String uid = (String) ((HashMap) task.getResult().getData()).get("uid");
                        if (uid != null && !uid.isEmpty()) {
                            Log.d("EliminarAdmin", "UID obtenido: " + uid);  // Verifica que el UID se ha obtenido correctamente
                            eliminarUsuarioDeDB(uid);
                        } else {
                            Log.e("EliminarAdmin", "Error: UID no encontrado");
                            Toast.makeText(EliminarAdmin.this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("EliminarAdmin", "Error al obtener UID: " + (task.getException() != null ? task.getException().getMessage() : "No data"));
                        Toast.makeText(EliminarAdmin.this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void eliminarUsuarioDeDB(String uid) {
        Log.d("EliminarAdmin", "Intentando eliminar en DB UID: " + uid);  // Verifica el UID antes de eliminar en DB

        databaseReference.child(uid).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("EliminarAdmin", "Usuario eliminado de la base de datos con éxito.");
                        Toast.makeText(EliminarAdmin.this, "Usuario eliminado de la base de datos.", Toast.LENGTH_SHORT).show();
                        eliminarUsuarioDeAuth(uid);  // Intenta eliminar en Firebase Authentication
                    } else {
                        Log.e("EliminarAdmin", "Error al eliminar el usuario de la base de datos: " + task.getException().getMessage());
                        Toast.makeText(EliminarAdmin.this, "Error al eliminar el usuario de la base de datos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void eliminarUsuarioDeAuth(String uid) {
        Log.d("EliminarAdmin", "Intentando eliminar en Auth UID: " + uid);  // Verifica el UID antes de eliminar en Auth

        FirebaseFunctions.getInstance()
                .getHttpsCallable("eliminarUsuario")
                .call(Collections.singletonMap("uid", uid))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("EliminarAdmin", "Usuario eliminado de autenticación con éxito.");
                        Toast.makeText(EliminarAdmin.this, "Usuario eliminado de autenticación.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("EliminarAdmin", "Error al eliminar de autenticación: " + task.getException().getMessage());
                        Toast.makeText(EliminarAdmin.this, "Error al eliminar de autenticación: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}