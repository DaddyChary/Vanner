package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

        // Listener para cargar los trabajos una sola vez desde Firebase
        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    // Crear una tarjeta para cada trabajo usando el layout job_card.xml
                    // Inflar el layout de la tarjeta desde job_card_match.xml
                    View jobCard = getLayoutInflater().inflate(R.layout.job_card_match, jobListContainer, false);

                    // Asignar valores a los elementos de la tarjeta
                    TextView jobTitle = jobCard.findViewById(R.id.jobTitle);
                    TextView jobDescription = jobCard.findViewById(R.id.jobDescription);
                    TextView jobSalary = jobCard.findViewById(R.id.jobSalary);
                    TextView jobVacancies = jobCard.findViewById(R.id.jobVacancies);
                    TextView jobMode = jobCard.findViewById(R.id.jobMode);
                    TextView jobDeadline = jobCard.findViewById(R.id.jobDeadline);
                    MaterialButton btnVerUsuarios = jobCard.findViewById(R.id.btnVerUsuarios);
                    ImageView jobImage = jobCard.findViewById(R.id.jobImage);

                    // Configuración de datos en los TextViews y otros elementos
                    jobTitle.setText("Ejemplo de Título");
                    jobDescription.setText("Descripción del trabajo");
                    jobSalary.setText("Sueldo: $2000");
                    jobVacancies.setText("Vacantes: 3");
                    jobMode.setText("Modalidad: Remoto");
                    jobDeadline.setText("Fecha límite: 01/12/2024");

                    // Configuración de la imagen (opcional)

                    // Agregar la tarjeta al contenedor
                    jobListContainer.addView(jobCard);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores (puedes mostrar un mensaje o log en caso de fallo)
            }
        });
    }
}
