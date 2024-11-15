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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrabajoGeneral extends AppCompatActivity {

    private MaterialButton btnRegresarTarjeta;
    private LinearLayout jobListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trabajogeneral); // Asegúrate de que el archivo XML se llame trabajogeneral.xml

        // Inicialización de vistas
        btnRegresarTarjeta = findViewById(R.id.btnRegresarTarjeta);
        jobListContainer = findViewById(R.id.jobListContainer);

        // Configuración del botón de regreso
        btnRegresarTarjeta.setOnClickListener(view -> finish());

        // Cargar trabajos desde Firebase
        loadJobsFromFirebase();
    }

    private void loadJobsFromFirebase() {
        DatabaseReference jobsRef = FirebaseDatabase.getInstance().getReference().child("jobs");

        // Listener para cargar los trabajos desde Firebase
        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    // Obtener los datos del trabajo
                    String companyName = jobSnapshot.child("companyName").getValue(String.class);
                    String deadline = jobSnapshot.child("deadline").getValue(String.class);
                    String description = jobSnapshot.child("description").getValue(String.class);
                    String salary = jobSnapshot.child("salary").getValue(String.class);
                    String title = jobSnapshot.child("title").getValue(String.class);
                    String vacancies = jobSnapshot.child("vacancies").getValue(String.class);
                    String mode = jobSnapshot.child("mode").getValue(String.class);

                    // Verificar que al menos un campo importante tenga contenido
                    if ((title != null && !title.trim().isEmpty()) ||
                            (description != null && !description.trim().isEmpty())) {

                        // Inflar el layout de la tarjeta
                        View jobCard = getLayoutInflater().inflate(R.layout.job_card_match, jobListContainer, false);

                        // Asignar datos a los elementos de la tarjeta
                        TextView jobTitle = jobCard.findViewById(R.id.jobTitle);
                        TextView jobDescription = jobCard.findViewById(R.id.jobDescription);
                        TextView jobSalary = jobCard.findViewById(R.id.jobSalary);
                        TextView jobVacancies = jobCard.findViewById(R.id.jobVacancies);
                        TextView jobMode = jobCard.findViewById(R.id.jobMode);
                        TextView jobDeadline = jobCard.findViewById(R.id.jobDeadline);
                        MaterialButton btnVerUsuarios = jobCard.findViewById(R.id.btnVerUsuarios);
                        ImageView jobImage = jobCard.findViewById(R.id.jobImage);

                        // Asignar valores a los elementos
                        jobTitle.setText(title != null ? title : "Título no disponible");
                        jobDescription.setText(description != null ? description : "Sin descripción");
                        jobSalary.setText(salary != null ? "Sueldo: " + salary : "Sueldo no disponible");
                        jobVacancies.setText(vacancies != null ? "Vacantes: " + vacancies : "Vacantes no disponibles");
                        jobMode.setText(mode != null ? "Modalidad: " + mode : "Modalidad no especificada");
                        jobDeadline.setText(deadline != null ? "Fecha límite: " + deadline : "Sin fecha límite");

                        // Configuración del botón "Ver"
                        btnVerUsuarios.setOnClickListener(view -> {
                            // Pasar los datos al detalle del empleo
                            Intent intent = new Intent(TrabajoGeneral.this, MarchUsuario.class);
                            intent.putExtra("title", title);
                            intent.putExtra("description", description);
                            intent.putExtra("salary", salary);
                            intent.putExtra("vacancies", vacancies);
                            intent.putExtra("mode", mode);
                            intent.putExtra("deadline", deadline);
                            startActivity(intent);
                        });

                        // Agregar la tarjeta al contenedor
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
