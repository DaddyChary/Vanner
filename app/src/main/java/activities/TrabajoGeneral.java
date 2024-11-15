package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.socialab2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import activities.MarchUsuario;

public class TrabajoGeneral extends AppCompatActivity {

    private MaterialCardView cardView;
    private MaterialButton btnRegresarTarjeta;
    private MaterialButton btnViewJobDetails;
    private MaterialTextView jobTitle1, jobTitle2, jobDescription2, salary, jobVacancies2, jobModality, jobDeadline;
    private ImageView jobImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trabajogeneral);

        // Inicialización de vistas
        cardView = findViewById(R.id.card); // Asegurarse de que esto es MaterialCardView
        btnRegresarTarjeta = findViewById(R.id.btnRegresarTarjeta);
        btnViewJobDetails = findViewById(R.id.btnViewJobDetails);
        jobTitle1 = findViewById(R.id.jobTitle1);
        jobTitle2 = findViewById(R.id.jobTitle2);
        jobDescription2 = findViewById(R.id.jobDescription2);
        salary = findViewById(R.id.salary);
        jobVacancies2 = findViewById(R.id.jobVacancies2);
        jobModality = findViewById(R.id.jobModality);
        jobDeadline = findViewById(R.id.jobDeadline);
        jobImage = findViewById(R.id.jobImage);

        // Configuración de los listeners y lógica adicional
        btnRegresarTarjeta.setOnClickListener(view -> {
            // Lógica para regresar
            finish();
        });

        btnViewJobDetails.setOnClickListener(view -> {
            // Lógica para ver detalles del trabajo
            Intent intent = new Intent(TrabajoGeneral.this, MarchUsuario.class);
            startActivity(intent);
        });
    }
}
