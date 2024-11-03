package ui;

import static com.example.socialab2.R.id.etCompanyName;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.android.material.card.MaterialCardView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Jobs;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PerfilEmpresa extends AppCompatActivity {

    // Variables
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference jobsDatabase;

    private Button btnCreateJob;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfilempresa);

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser(); // Obtiene el usuario actual

        // Inicializa la referencia de Firebase Database
        jobsDatabase = FirebaseDatabase.getInstance().getReference("jobs");

        // Botones
        btnCreateJob = findViewById(R.id.btnCreateJob);
        btnLogout = findViewById(R.id.btnLogout);

        // Configura el botón para agregar un nuevo empleo
        btnCreateJob.setOnClickListener(v -> showAddEditJobDialog(null, false));
    }

    // Método para mostrar el diálogo de agregar/editar empleo
    private void showAddEditJobDialog(final Jobs job, final boolean isEditing) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_edit_job, null);
        builder.setView(dialogView);

        // Referencias a los EditText en el diálogo
        EditText etJobTitle = dialogView.findViewById(R.id.etJobTitle);
        EditText etJobDescription = dialogView.findViewById(R.id.etJobDescription);
        EditText etJobSalary = dialogView.findViewById(R.id.etJobSalary);
        EditText etJobVacancies = dialogView.findViewById(R.id.etJobVacancies);
        EditText etJobMode = dialogView.findViewById(R.id.etJobMode);
        EditText etJobDeadline = dialogView.findViewById(R.id.etJobDeadline);
        EditText etCompanyName = dialogView.findViewById(R.id.etCompanyName);
        EditText etCompanyEmail = dialogView.findViewById(R.id.etCompanyEmail);

        // Asigna el correo del usuario actual y deshabilita la edición
        if (currentUser != null) {
            etCompanyEmail.setText(currentUser.getEmail());
            etCompanyEmail.setEnabled(false); // Hace que el campo no sea editable
        }

        // Si estamos editando, establece los valores actuales
        if (isEditing && job != null) {
            etJobTitle.setText(job.getTitle());
            etJobDescription.setText(job.getDescription());
            etJobSalary.setText(job.getSalary());
            etJobVacancies.setText(job.getVacancies());
            etJobMode.setText(job.getMode());
            etJobDeadline.setText(job.getDeadline());
            etCompanyName.setText(job.getCompanyName());
        }

        builder.setTitle(isEditing ? "Editar Empleo" : "Agregar Empleo")
                .setPositiveButton(isEditing ? "Guardar Cambios" : "Agregar", (dialog, which) -> {
                    // Obtiene los datos del formulario
                    String title = etJobTitle.getText().toString().trim();
                    String description = etJobDescription.getText().toString().trim();
                    String salary = etJobSalary.getText().toString().trim();
                    String vacancies = etJobVacancies.getText().toString().trim();
                    String mode = etJobMode.getText().toString().trim();
                    String deadline = etJobDeadline.getText().toString().trim();
                    String companyName = etCompanyName.getText().toString().trim();
                    String mail = currentUser != null ? currentUser.getEmail() : ""; // Obtiene el correo del usuario logueado

                    // Logs para verificar los valores
                    Log.d("JOB_INFO", "Title: " + title);
                    Log.d("JOB_INFO", "Description: " + description);
                    Log.d("JOB_INFO", "Salary: " + salary);
                    Log.d("JOB_INFO", "Vacancies: " + vacancies);
                    Log.d("JOB_INFO", "Mode: " + mode);
                    Log.d("JOB_INFO", "Deadline: " + deadline);
                    Log.d("JOB_INFO", "CompanyName: " + companyName);
                    Log.d("JOB_INFO", "Email: " + mail);

                    if (isEditing) {
                        if (job != null && job.getId() != null) {
                            // Actualizar el empleo en Firebase solo si el job y el jobId no son nulos
                            updateJobInFirebase(job.getId(), title, description, salary, vacancies, mode, deadline, companyName, mail);
                        } else {
                            Toast.makeText(PerfilEmpresa.this, "No se puede editar: el empleo no existe.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Agregar el nuevo empleo a Firebase
                        addJobToFirebase(title, description, salary, vacancies, mode, deadline, companyName, mail);
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    // Método para agregar un nuevo empleo a Firebase
    private void addJobToFirebase(String title, String description, String salary, String vacancies, String mode,
                                  String deadline, String companyName, String mail) {
        // Genera una clave única para cada empleo
        String jobId = jobsDatabase.push().getKey();

        // Crea un objeto de empleo con los datos proporcionados
        Jobs newJob = new Jobs(jobId, title, description, salary, vacancies, mode, deadline, companyName, mail);

        // Asegúrate de que el ID no sea nulo antes de guardar
        if (jobId != null) {
            jobsDatabase.child(jobId).setValue(newJob)
                    .addOnSuccessListener(aVoid -> {
                        // Mensaje de éxito
                        Toast.makeText(PerfilEmpresa.this, "Empleo agregado exitosamente", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Mensaje de error
                        Toast.makeText(PerfilEmpresa.this, "Error al agregar empleo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(PerfilEmpresa.this, "Error al generar el ID del empleo", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para actualizar un empleo existente en Firebase
    private void updateJobInFirebase(String jobId, String title, String description, String salary, String vacancies, String mode,
                                     String deadline, String companyName, String mail) {
        if (jobId != null) {
            Jobs updatedJob = new Jobs(jobId, title, description, salary, vacancies, mode, deadline, companyName, mail);
            jobsDatabase.child(jobId).setValue(updatedJob)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(PerfilEmpresa.this, "Empleo actualizado exitosamente", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PerfilEmpresa.this, "Error al actualizar empleo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(PerfilEmpresa.this, "ID del empleo no válido", Toast.LENGTH_SHORT).show();
        }
    }
}
