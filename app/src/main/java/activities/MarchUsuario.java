package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.socialab2.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MarchUsuario extends AppCompatActivity {

    private TextView etDescripción, etSueldo, etVacantes, etModalidad, etFechaLimite;
    private ImageButton btnRechazar, btnAprobar;
    private MaterialButton buttonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machusuario);

        etDescripción = findViewById(R.id.etDescripción);
        etSueldo = findViewById(R.id.etSueldo);
        etVacantes = findViewById(R.id.etVacantes);
        etModalidad = findViewById(R.id.etModalidad);
        etFechaLimite = findViewById(R.id.etFechaLimite);
        btnRechazar = findViewById(R.id.btnRechazar);
        btnAprobar = findViewById(R.id.btnAprobar);
        buttonVolver = findViewById(R.id.buttonVolver);

        String jobId = getIntent().getStringExtra("jobId");

        cargarTrabajoEspecifico(jobId);

        buttonVolver.setOnClickListener(view -> finish());

        btnRechazar.setOnClickListener(view -> {
            actualizarEstadoTrabajo(jobId, "Rechazado");
            finish();
        });

        btnAprobar.setOnClickListener(view -> {
            actualizarEstadoTrabajo(jobId, "Aprobado");
            mostrarMensajeMatchEnviado();
            finish();
        });
    }

    private void cargarTrabajoEspecifico(String jobId) {
        DatabaseReference trabajoRef = FirebaseDatabase.getInstance().getReference("jobs").child(jobId);
        trabajoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String description = dataSnapshot.child("description").getValue(String.class);
                    String salary = dataSnapshot.child("salary").getValue(String.class);
                    String vacancies = dataSnapshot.child("vacancies").getValue(String.class);
                    String mode = dataSnapshot.child("mode").getValue(String.class);
                    String deadline = dataSnapshot.child("deadline").getValue(String.class);

                    etDescripción.setText(description != null ? description : "Sin descripción");
                    etSueldo.setText(salary != null ? "Sueldo: " + salary : "Sueldo no disponible");
                    etVacantes.setText(vacancies != null ? "Vacantes: " + vacancies : "Vacantes no disponibles");
                    etModalidad.setText(mode != null ? "Modalidad: " + mode : "Modalidad no especificada");
                    etFechaLimite.setText(deadline != null ? "Fecha límite: " + deadline : "Sin fecha límite");
                } else {
                    Toast.makeText(MarchUsuario.this, "No se encontró el trabajo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MarchUsuario.this, "Error al cargar trabajo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarEstadoTrabajo(String jobId, String estado) {
        DatabaseReference trabajoRef = FirebaseDatabase.getInstance().getReference("jobs").child(jobId);
        trabajoRef.child("estado").setValue(estado);
    }

    private void mostrarMensajeMatchEnviado() {
        Toast.makeText(MarchUsuario.this, "Match enviado", Toast.LENGTH_SHORT).show();
    }
}
