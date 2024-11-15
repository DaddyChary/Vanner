package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MatchEmpresa extends AppCompatActivity {

    private TextInputEditText etNombre, etApellido, etRut, etCalle, etNumeroCasa, etComuna, etRegion, etTelefono, etEmailRegister;
    private ImageButton btnRechazar, btnAprobar;
    private MaterialButton buttonVolver;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machempresa);

        // Conexión con las vistas del XML
        etNombre = findViewById(R.id.et_nombre);
        etApellido = findViewById(R.id.et_apellido);
        etRut = findViewById(R.id.et_Rut);
        etCalle = findViewById(R.id.et_calle);
        etNumeroCasa = findViewById(R.id.et_numeroCasa);
        etComuna = findViewById(R.id.et_comuna);
        etRegion = findViewById(R.id.et_region);
        etTelefono = findViewById(R.id.et_telefono);
        etEmailRegister = findViewById(R.id.emailRegister);
        btnRechazar = findViewById(R.id.btnRechazar);
        btnAprobar = findViewById(R.id.btnAprobar);
        buttonVolver = findViewById(R.id.buttonVolver);

        // Obtener el tipo de usuario
        getUserType(() -> {
            if ("empresa".equals(userType)) {
                loadTrainerData();
            }
        });

        // Configuración del botón de rechazar
        btnRechazar.setOnClickListener(v -> {
            updateTrainerStatus("Rechazado");
            redirectToMenu();  // Redirige a la ventana de menú
        });

        // Configuración del botón de aprobar
        btnAprobar.setOnClickListener(v -> {
            updateTrainerStatus("Aprobado");
            showMatchSentMessage(); // Muestra el mensaje de "Match enviado"
            finish();  // Cierra la actividad y vuelve a la anterior
        });

        // Configuración del botón volver
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MatchEmpresa.this,TrabajoGeneral.class);
            }
        });
    }

    private void getUserType(Runnable callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userType = dataSnapshot.child("userType").getValue(String.class);
                callback.run();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MatchEmpresa.this, "Error al obtener tipo de usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTrainerData() {
        DatabaseReference trainerRef = FirebaseDatabase.getInstance().getReference("trainers");

        trainerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Cargar datos del entrenador
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String apellido = snapshot.child("apellido").getValue(String.class);
                    String rut = snapshot.child("rut").getValue(String.class);
                    String calle = snapshot.child("calle").getValue(String.class);
                    String numeroCasa = snapshot.child("numeroCasa").getValue(String.class);
                    String comuna = snapshot.child("comuna").getValue(String.class);
                    String region = snapshot.child("region").getValue(String.class);
                    String telefono = snapshot.child("telefono").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    // Mostrar datos en los campos correspondientes
                    etNombre.setText(nombre);
                    etApellido.setText(apellido);
                    etRut.setText(rut);
                    etCalle.setText(calle);
                    etNumeroCasa.setText(numeroCasa);
                    etComuna.setText(comuna);
                    etRegion.setText(region);
                    etTelefono.setText(telefono);
                    etEmailRegister.setText(email);

                    break; // Solo cargar el primer entrenador (puedes adaptar esto según tu lógica)
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MatchEmpresa.this, "Error al cargar datos del entrenador", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTrainerStatus(String status) {
        DatabaseReference trainerRef = FirebaseDatabase.getInstance().getReference("trainers");
        String nombre = etNombre.getText().toString();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "No hay datos de entrenador para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        trainerRef.child(nombre).child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    // El estado se actualizó correctamente
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MatchEmpresa.this, "Error al actualizar estado", Toast.LENGTH_SHORT).show();
                });
    }

    private void showMatchSentMessage() {
        Toast.makeText(MatchEmpresa.this, "Match enviado", Toast.LENGTH_SHORT).show();
    }

    private void redirectToMenu() {
        // Redirige al menú (puedes reemplazar "MenuActivity.class" por el nombre de tu actividad de menú)
        Intent intent = new Intent(MatchEmpresa.this, Menu_entrenador.class);
        startActivity(intent);
        finish();  // Cierra esta actividad
    }
}
