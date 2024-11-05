package ui;

import android.os.Bundle;
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

import models.Jobs;

public class PerfilEmpresa extends AppCompatActivity {

    private LinearLayout linearLayoutJobs;
    private DatabaseReference jobsDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfilempresa);

        // Inicializar Firebase Auth y Database
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        jobsDatabase = FirebaseDatabase.getInstance().getReference("jobs");

        // Inicializar LinearLayout para mostrar las tarjetas de empleos
        linearLayoutJobs = findViewById(R.id.linearLayoutJobs);

        // Configurar el botón de crear empleo
        Button btnCreateJob = findViewById(R.id.btnCreateJob);
        btnCreateJob.setOnClickListener(v -> showAddEditJobDialog(null, false));

        // Cargar y mostrar los empleos desde Firebase
        loadJobsFromFirebase();
    }

    private void loadJobsFromFirebase() {
        jobsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                linearLayoutJobs.removeAllViews(); // Limpiar las vistas previas

                for (DataSnapshot jobSnapshot : snapshot.getChildren()) {
                    try {
                        Jobs job = jobSnapshot.getValue(Jobs.class);
                        if (job != null) {
                            addJobCard(job);
                        }
                    } catch (DatabaseException e) {
                        Toast.makeText(PerfilEmpresa.this, "Error al convertir datos de Firebase", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PerfilEmpresa.this, "Error al cargar empleos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para crear y añadir una tarjeta de empleo al LinearLayout
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

        // Asignar datos a los elementos de la tarjeta
        tvJobTitle.setText(job.getTitle());
        tvJobDescription.setText(job.getDescription());
        tvJobSalary.setText("Sueldo: " + job.getSalary());
        tvJobVacancies.setText("Vacantes: " + job.getVacancies());
        tvJobMode.setText("Modalidad: " + job.getMode());
        tvJobDeadline.setText("Fecha límite: " + job.getDeadline());

        // Configurar botones de editar y eliminar
        btnEdit.setOnClickListener(v -> showAddEditJobDialog(job, true));
        btnDelete.setOnClickListener(v -> deleteJobFromFirebase(job.getId()));

        // Añadir la tarjeta al LinearLayout
        linearLayoutJobs.addView(jobCard);
    }

    // Método para mostrar el diálogo de agregar/editar empleo
    private void showAddEditJobDialog(Jobs job, boolean isEditing) {
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
        EditText etCompanyName = dialogView.findViewById(R.id.etCompanyName);
        EditText etCompanyEmail = dialogView.findViewById(R.id.etCompanyEmail);

        // Configurar campos si estamos editando
        if (isEditing && job != null) {
            etJobTitle.setText(job.getTitle());
            etJobDescription.setText(job.getDescription());
            etJobSalary.setText(job.getSalary());
            etJobVacancies.setText(job.getVacancies());
            etJobMode.setText(job.getMode());
            etJobDeadline.setText(job.getDeadline());
            etCompanyName.setText(job.getCompanyName());
            etCompanyEmail.setText(job.getMail());
        }

        dialogBuilder.setPositiveButton(isEditing ? "Actualizar" : "Agregar", (dialog, which) -> {
            String title = etJobTitle.getText().toString().trim();
            String description = etJobDescription.getText().toString().trim();
            String salary = etJobSalary.getText().toString().trim();
            String vacancies = etJobVacancies.getText().toString().trim();
            String mode = etJobMode.getText().toString().trim();
            String deadline = etJobDeadline.getText().toString().trim();
            String companyName = etCompanyName.getText().toString().trim();
            String mail = etCompanyEmail.getText().toString().trim();

            if (isEditing && job != null) {
                updateJobInFirebase(new Jobs(job.getId(), title, description, salary, vacancies, mode, deadline, companyName, mail));
            } else {
                addJobToFirebase(new Jobs(null, title, description, salary, vacancies, mode, deadline, companyName, mail));
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    // Métodos para agregar, actualizar y eliminar empleos en Firebase
    private void addJobToFirebase(Jobs job) {
        String jobId = jobsDatabase.push().getKey();
        job.setId(jobId);
        jobsDatabase.child(jobId).setValue(job).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(PerfilEmpresa.this, "Empleo agregado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PerfilEmpresa.this, "Error al agregar empleo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateJobInFirebase(Jobs job) {
        jobsDatabase.child(job.getId()).setValue(job).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(PerfilEmpresa.this, "Empleo actualizado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PerfilEmpresa.this, "Error al actualizar empleo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteJobFromFirebase(String jobId) {
        jobsDatabase.child(jobId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(PerfilEmpresa.this, "Empleo eliminado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PerfilEmpresa.this, "Error al eliminar empleo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

