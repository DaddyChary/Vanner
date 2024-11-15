package activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MarchUsuario extends AppCompatActivity {

    private TextView etDescripción, etSueldo, etVacantes, etModalidad, etFechaLimite;
    private ImageButton btnRechazar, btnAprobar;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machusuario);

        // Inicialización de vistas
        etDescripción = findViewById(R.id.etDescripción);
        etSueldo = findViewById(R.id.etSueldo);
        etVacantes = findViewById(R.id.etVacantes);
        etModalidad = findViewById(R.id.etModalidad);
        etFechaLimite = findViewById(R.id.etFechaLimite);
        btnRechazar = findViewById(R.id.btnRechazar);
        btnAprobar = findViewById(R.id.btnAprobar);

        // Recibir los datos del Intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String salary = getIntent().getStringExtra("salary");
        String vacancies = getIntent().getStringExtra("vacancies");
        String mode = getIntent().getStringExtra("mode");
        String deadline = getIntent().getStringExtra("deadline");

        // Llenar los campos con los datos
        etDescripción.setText(description != null ? description : "Sin descripción");
        etSueldo.setText(salary != null ? "Sueldo: " + salary : "Sueldo no disponible");
        etVacantes.setText(vacancies != null ? "Vacantes: " + vacancies : "Vacantes no disponibles");
        etModalidad.setText(mode != null ? "Modalidad: " + mode : "Modalidad no especificada");
        etFechaLimite.setText(deadline != null ? "Fecha límite: " + deadline : "Sin fecha límite");

        // Obtener el tipo de usuario
        getUserType();

        // Configurar el botón de Rechazar
        btnRechazar.setOnClickListener(view -> {
            if (canMakeMatch()) {
                // Acción al rechazar
                updateJobStatus(title, "Rechazado");
                Toast.makeText(MarchUsuario.this, "Trabajo rechazado", Toast.LENGTH_SHORT).show();
                finish();  // Regresar a la actividad anterior
            }
        });

        // Configurar el botón de Aprobar
        btnAprobar.setOnClickListener(view -> {
            if (canMakeMatch()) {
                // Acción al aprobar
                updateJobStatus(title, "Aprobado");
                Toast.makeText(MarchUsuario.this, "Trabajo aprobado", Toast.LENGTH_SHORT).show();
                finish();  // Regresar a la actividad anterior
            }
        });
    }

    private void getUserType() {
        // Obtener el ID del usuario actual
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Obtener el tipo de usuario desde Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Leer el tipo de usuario del campo "userType"
                userType = dataSnapshot.child("userType").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MarchUsuario.this, "Error al obtener tipo de usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean canMakeMatch() {
        // Verificar si el tipo de usuario puede hacer el "match"
        if ("usuario".equals(userType)) {
            // Los usuarios solo pueden hacer match con entrenadores
            Toast.makeText(this, "Puedes hacer match solo con entrenadores", Toast.LENGTH_SHORT).show();
            return true; // Permite que el usuario haga la acción
        } else if ("entrenador".equals(userType)) {
            // Los entrenadores solo pueden hacer match con empresas
            Toast.makeText(this, "Puedes hacer match solo con empresas", Toast.LENGTH_SHORT).show();
            return true; // Permite que el entrenador haga la acción
        } else {
            // Si el tipo de usuario no es reconocido, no se puede hacer el match
            Toast.makeText(this, "Tipo de usuario no válido", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void updateJobStatus(String jobTitle, String status) {
        // Obtén una referencia a la base de datos de Firebase
        DatabaseReference jobRef = FirebaseDatabase.getInstance().getReference("jobs");

        // Buscar el trabajo por el título (o por otro identificador único)
        jobRef.child(jobTitle).child("status").setValue(status);
    }
}
