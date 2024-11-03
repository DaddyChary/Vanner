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

        cargarCorreos();

        btnEliminarAdmin.setOnClickListener(view -> {
            String correo = et_correo.getText().toString().trim();
            if (!correo.isEmpty()) {
                eliminarUsuarioPorCorreo(correo);
            } else {
                Toast.makeText(EliminarAdmin.this, "Por favor, introduce un correo.", Toast.LENGTH_SHORT).show();
            }
        });

        btnVolver.setOnClickListener(view -> finish());
    }

    private void cargarCorreos() {
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
        correoTextView.setOnClickListener(view -> et_correo.setText(correo));

        row.addView(correoTextView);
        tbl_eliminar_administradores.addView(row);
    }

    private void eliminarUsuarioPorCorreo(String correo) {
        databaseReference.orderByChild("mail").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        userSnapshot.getRef().removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(EliminarAdmin.this, "Usuario eliminado de la base de datos.", Toast.LENGTH_SHORT).show();
                                    et_correo.setText("");
                                    cargarCorreos();

                                    // Llama a eliminarAuthPorCorreo después de eliminar en la base de datos
                                    eliminarAuthPorCorreo(correo);
                                })
                                .addOnFailureListener(e -> Toast.makeText(EliminarAdmin.this, "Error al eliminar el usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                } else {
                    Toast.makeText(EliminarAdmin.this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EliminarAdmin.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void eliminarAuthPorCorreo(String correo) {
        Log.d("EliminarAdmin", "Correo a eliminar: " + correo);

        FirebaseFunctions.getInstance()
                .getHttpsCallable("eliminarUsuarioPorCorreo")
                .call(Collections.singletonMap("correo", correo))
                .addOnSuccessListener(httpsCallableResult -> {
                    Toast.makeText(EliminarAdmin.this, "Usuario eliminado de Firebase Auth.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EliminarAdmin.this, "Error al eliminar el usuario de Auth: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



}
