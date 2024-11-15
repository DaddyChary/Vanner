package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.socialab2.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrabajoGeneral extends AppCompatActivity {

    private MaterialButton btnRegresarTarjeta;
    private LinearLayout jobListContainer;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trabajogeneral);

        // Inicialización de vistas
        btnRegresarTarjeta = findViewById(R.id.btnRegresarTarjeta);
        jobListContainer = findViewById(R.id.jobListContainer);

        // Configuración del botón de regreso
        btnRegresarTarjeta.setOnClickListener(view -> finish());

        // Obtener el tipo de usuario y cargar trabajos
        getUserType();
    }

    private void getUserType() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userType = dataSnapshot.child("userType").getValue(String.class);
                loadJobsFromFirebase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores
            }
        });
    }

    private void loadJobsFromFirebase() {
        DatabaseReference jobsRef = FirebaseDatabase.getInstance().getReference().child("jobs");

        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobListContainer.removeAllViews();

                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    String jobId = jobSnapshot.getKey(); // Obtener el ID del trabajo
                    String companyName = jobSnapshot.child("companyName").getValue(String.class);
                    String createdBy = jobSnapshot.child("createdBy").getValue(String.class);
                    String deadline = jobSnapshot.child("deadline").getValue(String.class);
                    String description = jobSnapshot.child("description").getValue(String.class);
                    String salary = jobSnapshot.child("salary").getValue(String.class);
                    String title = jobSnapshot.child("title").getValue(String.class);
                    String vacancies = jobSnapshot.child("vacancies").getValue(String.class);
                    String mode = jobSnapshot.child("mode").getValue(String.class);

                    if ("entrenador".equals(userType) && !"empresa".equals(createdBy)) {
                        continue;
                    }

                    if ((title != null && !title.trim().isEmpty()) ||
                            (description != null && !description.trim().isEmpty())) {

                        View jobCard = getLayoutInflater().inflate(R.layout.job_card_match, jobListContainer, false);

                        TextView jobTitle = jobCard.findViewById(R.id.jobTitle);
                        TextView jobDescription = jobCard.findViewById(R.id.jobDescription);
                        TextView jobSalary = jobCard.findViewById(R.id.jobSalary);
                        TextView jobVacancies = jobCard.findViewById(R.id.jobVacancies);
                        TextView jobMode = jobCard.findViewById(R.id.jobMode);
                        TextView jobDeadline = jobCard.findViewById(R.id.jobDeadline);
                        MaterialButton btnVerUsuarios = jobCard.findViewById(R.id.btnVerUsuarios);

                        jobTitle.setText(title != null ? title : "Título no disponible");
                        jobDescription.setText(description != null ? description : "Sin descripción");
                        jobSalary.setText(salary != null ? "Sueldo: " + salary : "Sueldo no disponible");
                        jobVacancies.setText(vacancies != null ? "Vacantes: " + vacancies : "Vacantes no disponibles");
                        jobMode.setText(mode != null ? "Modalidad: " + mode : "Modalidad no especificada");
                        jobDeadline.setText(deadline != null ? "Fecha límite: " + deadline : "Sin fecha límite");

                        btnVerUsuarios.setOnClickListener(view -> {
                            Intent intent = new Intent(TrabajoGeneral.this, MarchUsuario.class);
                            intent.putExtra("jobId", jobId); // Pasar el ID del trabajo a la siguiente actividad
                            startActivity(intent);
                        });

                        jobListContainer.addView(jobCard);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores
            }
        });
    }
}
