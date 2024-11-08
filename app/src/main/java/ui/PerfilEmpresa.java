package ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import activities.MainActivity;
import models.Jobs;

public class PerfilEmpresa extends AppCompatActivity {

    private LinearLayout linearLayoutJobs; // Contenedor para mostrar las tarjetas de empleos
    private DatabaseReference jobsDatabase; // Referencia a la base de datos de empleos en Firebase
    private FirebaseAuth mAuth; // Autenticación de Firebase
    private FirebaseUser currentUser; // Usuario actualmente autenticado
    private static final String TAG = "PerfilEmpresa"; // Tag para logs
    private String userEmail; // Email del usuario autenticado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfilempresa);

        // Inicializa Firebase Auth y la base de datos
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userEmail = currentUser != null ? currentUser.getEmail() : null;
        jobsDatabase = FirebaseDatabase.getInstance().getReference("jobs");

        // Inicializa el LinearLayout para mostrar las tarjetas de empleos
        linearLayoutJobs = findViewById(R.id.linearLayoutJobs);

        // Configura el botón para crear un nuevo empleo
        Button btnCreateJob = findViewById(R.id.btnCreateJob);
        btnCreateJob.setOnClickListener(v -> showAddEditJobDialog(null));

        // Configura el botón para cerrar sesión
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión en Firebase
                mAuth.signOut();

                // Redirigir al usuario a MainActivity
                Intent intent = new Intent(PerfilEmpresa.this, MainActivity.class);
                startActivity(intent);

                // Finalizar la actividad actual para evitar que el usuario regrese usando el botón "Atrás"
                finish();
            }
        });

        // Carga y muestra los empleos desde Firebase
        loadJobsFromFirebase();
    }

    // Método para cargar los empleos desde Firebase y mostrarlos en el LinearLayout
    private void loadJobsFromFirebase() {
        if (userEmail == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Escucha los empleos creados solo por el usuario logueado
        jobsDatabase.orderByChild("mail").equalTo(userEmail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        linearLayoutJobs.removeAllViews(); // Limpiar vistas anteriores

                        for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                            if (jobSnapshot.exists() && jobSnapshot.getValue() instanceof java.util.Map) {
                                try {
                                    Jobs job = jobSnapshot.getValue(Jobs.class);
                                    if (job != null) {
                                        addJobCard(job); // Agregar una tarjeta para cada empleo
                                    }
                                } catch (DatabaseException e) {
                                    Log.e(TAG, "Error converting data", e);
                                    Toast.makeText(PerfilEmpresa.this, "Error al convertir los datos", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e(TAG, "Invalid or incomplete data: " + jobSnapshot.toString());
                                Toast.makeText(PerfilEmpresa.this, "Datos en formato incorrecto", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PerfilEmpresa.this, "Error al cargar empleos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para crear y agregar una tarjeta de empleo al LinearLayout
    private void addJobCard(Jobs job) {
        View jobCard = getLayoutInflater().inflate(R.layout.job_card, null);

        TextView tvJobTitle = jobCard.findViewById(R.id.tvJobTitle);
        TextView tvJobDescription = jobCard.findViewById(R.id.tvJobDescription);
        TextView tvJobSalary = jobCard.findViewById(R.id.tvJobSalary);
        TextView tvJobVacancies = jobCard.findViewById(R.id.tvJobVacancies);
        TextView tvJobMode = jobCard.findViewById(R.id.tvJobMode);
        TextView tvJobDeadline = jobCard.findViewById(R.id.tvJobDeadline);
        Button btnEdit = jobCard.findViewById(R.id.btn_edit);
        Button btnDelete = jobCard.findViewById(R.id.btn_delete);

        // Asigna los datos del empleo a los elementos de la tarjeta
        tvJobTitle.setText(job.getTitle());
        tvJobDescription.setText(job.getDescription());
        tvJobSalary.setText("Sueldo: " + job.getSalary());
        tvJobVacancies.setText("Vacantes: " + job.getVacancies());
        tvJobMode.setText("Modalidad: " + job.getMode());
        tvJobDeadline.setText("Fecha límite: " + job.getDeadline());

        // Configura los botones de editar y eliminar
        btnEdit.setOnClickListener(v -> showAddEditJobDialog(job));
        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog(job.getId()));

        // Agrega la tarjeta al LinearLayout
        linearLayoutJobs.addView(jobCard);
    }

    // Método para mostrar el cuadro de diálogo de agregar/editar empleo
    private void showAddEditJobDialog(Jobs job) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_edit_job, null);
        dialogBuilder.setView(dialogView);

        EditText etJobTitle = dialogView.findViewById(R.id.etJobTitle);
        EditText etJobDescription = dialogView.findViewById(R.id.etJobDescription);
        EditText etJobSalary = dialogView.findViewById(R.id.etJobSalary);
        EditText etJobVacancies = dialogView.findViewById(R.id.etJobVacancies);
        EditText etJobMode = dialogView.findViewById(R.id.etJobMode);
        EditText etJobDeadline = dialogView.findViewById(R.id.etJobDeadline);
        EditText etCompanyEmail = dialogView.findViewById(R.id.etCompanyEmail);
        EditText etCompanyName = dialogView.findViewById(R.id.etCompanyName); // Nuevo campo para el nombre de la empresa

        Button btnDialogAction = dialogView.findViewById(R.id.btnUpdate);
        Button btnDialogCancel = dialogView.findViewById(R.id.btnCancel);

        // Muestra el correo de la empresa actual y evita que se edite
        etCompanyEmail.setText(userEmail);
        etCompanyEmail.setEnabled(false);

        // Configura campos si estamos editando
        if (job != null) {
            etJobTitle.setText(job.getTitle());
            etJobDescription.setText(job.getDescription());
            etJobSalary.setText(job.getSalary());
            etJobVacancies.setText(job.getVacancies());
            etJobMode.setText(job.getMode());
            etJobDeadline.setText(job.getDeadline());
            etCompanyName.setText(job.getCompanyName()); // Establecer el nombre de la empresa si existe
            btnDialogAction.setText("Actualizar");
        } else {
            btnDialogAction.setText("Agregar");
        }

        AlertDialog dialog = dialogBuilder.create();

        // Acción para el botón de "Agregar/Actualizar"
        btnDialogAction.setOnClickListener(v -> {
            String title = etJobTitle.getText().toString().trim();
            String description = etJobDescription.getText().toString().trim();
            String salary = etJobSalary.getText().toString().trim();
            String vacancies = etJobVacancies.getText().toString().trim();
            String mode = etJobMode.getText().toString().trim();
            String deadline = etJobDeadline.getText().toString().trim();
            String companyName = etCompanyName.getText().toString().trim(); // Obtener el nombre de la empresa

            if (job != null) {
                updateJobInFirebase(new Jobs(job.getId(), title, description, salary, vacancies, mode, deadline, companyName, userEmail));
            } else {
                addJobToFirebase(new Jobs(null, title, description, salary, vacancies, mode, deadline, companyName, userEmail));
            }
            dialog.dismiss();
        });

        // Configuración del botón "Cancelar"
        btnDialogCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Método para agregar un trabajo a Firebase
    private void addJobToFirebase(Jobs job) {
        String jobId = jobsDatabase.push().getKey();
        job.setId(jobId);
        jobsDatabase.child(jobId).setValue(job).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Empleo agregado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al agregar el empleo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para actualizar un trabajo en Firebase
    private void updateJobInFirebase(Jobs job) {
        jobsDatabase.child(job.getId()).setValue(job).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Empleo actualizado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar el empleo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para mostrar el cuadro de diálogo de confirmación para eliminar
    private void showDeleteConfirmationDialog(String jobId) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar empleo")
                .setMessage("¿Está seguro de eliminar este empleo?")
                .setPositiveButton("Sí", (dialog, which) -> deleteJobFromFirebase(jobId))
                .setNegativeButton("No", null)
                .show();
    }

    // Método para eliminar un trabajo de Firebase
    private void deleteJobFromFirebase(String jobId) {
        jobsDatabase.child(jobId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Empleo eliminado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al eliminar el empleo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
