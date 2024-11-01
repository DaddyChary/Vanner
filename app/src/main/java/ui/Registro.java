package ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import activities.MainActivity;
import models.User;

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText emailRegister, passwordRegister, confirmPasswordRegister;
    private Button buttonRegister, backButtonRegister;
    private RadioGroup rgGrupoEntrenadores;
    private RadioButton rbEntrenadores, rbTecnicos, rbEmpresas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicializa Firebase Database y obtiene la referencia
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        // Referencias a los elementos de la vista
        rgGrupoEntrenadores = findViewById(R.id.rgGrupoEntrenadores);
        rbEntrenadores = findViewById(R.id.rbEntrenadores);
        rbEmpresas = findViewById(R.id.rbEmpresas);
        rbTecnicos = findViewById(R.id.rbTecnicos);
        emailRegister = findViewById(R.id.emailRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        confirmPasswordRegister = findViewById(R.id.confirmPasswordRegister);
        buttonRegister = findViewById(R.id.buttonRegister);
        backButtonRegister = findViewById(R.id.backButtonRegister);

        // Acción para el botón de regresar
        backButtonRegister.setOnClickListener(view -> {
            Intent intent = new Intent(Registro.this, MainActivity.class);
            startActivity(intent);
        });

        // Acción para el botón de registrar
        buttonRegister.setOnClickListener(view -> {
            // Obtener los valores de los campos de texto
            String email = emailRegister.getText().toString().trim();
            String password = passwordRegister.getText().toString().trim();
            String confirmPassword = confirmPasswordRegister.getText().toString().trim();

            // Obtener el tipo de usuario seleccionado
            String tipoUsuario = obtenerTipoUsuario();

            // Llamar a la función guardarUsuario
            guardarUsuario(email, password, confirmPassword, tipoUsuario);
        });
    }

    // Función para obtener el tipo de usuario seleccionado
    private String obtenerTipoUsuario() {
        int selectedId = rgGrupoEntrenadores.getCheckedRadioButtonId();
        if (selectedId == rbEntrenadores.getId()) {
            return "Entrenador";
        } else if (selectedId == rbTecnicos.getId()) {
            return "Tecnico";
        } else if (selectedId == rbEmpresas.getId()) {
            return "Empresa";
        }
        return "General";  // Valor predeterminado si no se selecciona nada
    }

    // Función para guardar el usuario
    private void guardarUsuario(String email, String password, String confirmPassword, String tipoUsuario) {

        // Validar campos obligatorios
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(Registro.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el correo electrónico (debe ser de Gmail)
        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(Registro.this, "Ingrese un correo Gmail válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar la contraseña
        if (password.length() < 6 || !password.equals(confirmPassword)) {
            Toast.makeText(Registro.this, "Las contraseñas no coinciden o son menores a 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registrar el usuario en Firebase Authentication
        registerUser(email, password, tipoUsuario);
    }

    // Función para registrar el usuario en Firebase
    private void registerUser(String email, String password, String tipoUsuario) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Guardar información adicional en la base de datos
                        FirebaseUser user = mAuth.getCurrentUser();
                        guardarDatosAdicionales(user, email, tipoUsuario);
                    } else {
                        Toast.makeText(Registro.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Guardar datos adicionales incluyendo el tipo de usuario en la base de datos
    private void guardarDatosAdicionales(FirebaseUser user, String email, String tipoUsuario) {
        if (user == null) {
            Toast.makeText(Registro.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el ID del usuario
        String id = user.getUid();

        // Guardar el objeto User en la base de datos solo con el correo y el tipo de usuario
        User usuario = new User("", "", "", "", "", "", "", email, tipoUsuario, "N/A", "");

        // Inicializar la referencia de la base de datos
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Guardar el objeto User en la base de datos usando el UID del usuario
        databaseReference.child(id).setValue(usuario)
                .addOnSuccessListener(aVoid -> {
                    // Datos guardados exitosamente
                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registro.this, Login.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Error al guardar los datos
                    Toast.makeText(Registro.this, "Error al guardar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseError", "Error al guardar datos: ", e);
                });
    }
}




