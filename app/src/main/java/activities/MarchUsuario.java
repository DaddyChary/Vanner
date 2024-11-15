package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        // Inicialización de vistas (campos de texto y botones)
        etDescripción = findViewById(R.id.etDescripción);
        etSueldo = findViewById(R.id.etSueldo);
        etVacantes = findViewById(R.id.etVacantes);
        etModalidad = findViewById(R.id.etModalidad);
        etFechaLimite = findViewById(R.id.etFechaLimite);
        btnRechazar = findViewById(R.id.btnRechazar);
        btnAprobar = findViewById(R.id.btnAprobar);
        buttonVolver = findViewById(R.id.buttonVolver);

        // Recibir los datos del Intent (transmisión de datos entre actividades)
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String salary = getIntent().getStringExtra("salary");
        String vacancies = getIntent().getStringExtra("vacancies");
        String mode = getIntent().getStringExtra("mode");
        String deadline = getIntent().getStringExtra("deadline");

        // Llenar los campos con los datos recibidos
        etDescripción.setText(description != null ? description : "Sin descripción");
        etSueldo.setText(salary != null ? "Sueldo: " + salary : "Sueldo no disponible");
        etVacantes.setText(vacancies != null ? "Vacantes: " + vacancies : "Vacantes no disponibles");
        etModalidad.setText(mode != null ? "Modalidad: " + mode : "Modalidad no especificada");
        etFechaLimite.setText(deadline != null ? "Fecha límite: " + deadline : "Sin fecha límite");

        // Cargar trabajos de empresas sin filtrar por tipo de usuario
        cargarTrabajosCreadoPorEmpresas();

        // Configurar el botón de "Volver"
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el tipo de usuario desde Firebase o SharedPreferences
                String tipoUsuario = obtenerTipoDeUsuario(); // Implementa esta función para obtener el tipo de usuario

                Intent intent;
                // Redirigir según el tipo de usuario
                if (tipoUsuario.equals("entrenador")) {
                    intent = new Intent(MarchUsuario.this, Menu_entrenador.class); // Para entrenador
                    startActivity(intent);

                } else if (tipoUsuario.equals("empresa")) {
                    intent = new Intent(MarchUsuario.this, Menu_Empresa.class); // Para empresa
                    startActivity(intent);

                } else {
                    intent = new Intent(MarchUsuario.this, Menu_usuario.class);
                    // Para otros tipos de usuarios (o por defecto)
                    startActivity(intent);

                }
                startActivity(intent);

            }
        });

        // Configurar el botón de Rechazar
        btnRechazar.setOnClickListener(view -> {
            // Acción al rechazar
            actualizarEstadoTrabajo(title, "Rechazado");
            finish();  // Regresar a la actividad anterior sin mostrar mensaje
        });

        // Configurar el botón de Aprobar
        btnAprobar.setOnClickListener(view -> {
            // Acción al aprobar
            actualizarEstadoTrabajo(title, "Aprobado");
            mostrarMensajeMatchEnviado(); // Muestra el mensaje de "Match enviado"
            finish();  // Regresar a la actividad anterior
        });
    }

    // Método para obtener el tipo de usuario
    private String obtenerTipoDeUsuario() {
        // Aquí deberías obtener el tipo de usuario, por ejemplo, desde Firebase o SharedPreferences
        // Suponiendo que se guarda en Firebase o SharedPreferences, lo recuperas aquí
        // Por ejemplo, con SharedPreferences:
        // SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        // return preferences.getString("tipo_usuario", "generico");

        // O si es desde Firebase:
        // DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);
        // userRef.child("tipo").get().addOnCompleteListener(...);

        return "entrenador"; // Cambia esta línea por el valor real obtenido
    }

    // Método para cargar los trabajos creados por las empresas
    private void cargarTrabajosCreadoPorEmpresas() {
        DatabaseReference trabajosRef = FirebaseDatabase.getInstance().getReference("trabajos");

        trabajosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot trabajoSnapshot : dataSnapshot.getChildren()) {
                    // Extraer datos del trabajo
                    String title = trabajoSnapshot.child("title").getValue(String.class);
                    String description = trabajoSnapshot.child("description").getValue(String.class);
                    String salary = trabajoSnapshot.child("salary").getValue(String.class);
                    String vacancies = trabajoSnapshot.child("vacancies").getValue(String.class);
                    String mode = trabajoSnapshot.child("mode").getValue(String.class);
                    String deadline = trabajoSnapshot.child("deadline").getValue(String.class);

                    // Mostrar los datos en los campos de texto
                    etDescripción.setText(description != null ? description : "Sin descripción");
                    etSueldo.setText(salary != null ? "Sueldo: " + salary : "Sueldo no disponible");
                    etVacantes.setText(vacancies != null ? "Vacantes: " + vacancies : "Vacantes no disponibles");
                    etModalidad.setText(mode != null ? "Modalidad: " + mode : "Modalidad no especificada");
                    etFechaLimite.setText(deadline != null ? "Fecha límite: " + deadline : "Sin fecha límite");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Mostrar error en caso de fallo al cargar los trabajos
                Toast.makeText(MarchUsuario.this, "Error al cargar trabajos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para actualizar el estado de un trabajo
    private void actualizarEstadoTrabajo(String jobTitle, String estado) {
        DatabaseReference trabajoRef = FirebaseDatabase.getInstance().getReference("trabajos");

        // Buscar el trabajo por su título (o cualquier otro identificador único)
        trabajoRef.child(jobTitle).child("estado").setValue(estado);
    }

    // Mostrar el mensaje de "Match enviado"
    private void mostrarMensajeMatchEnviado() {
        Toast.makeText(MarchUsuario.this, "Match enviado", Toast.LENGTH_SHORT).show();
    }
}
