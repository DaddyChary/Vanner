package ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
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
    private RadioGroup rgGrupoTipo;
    private RadioButton rbEntrenador, rbAdministrador, rbUsuario, rbEmpresa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicializa Firebase Auth y Database
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Inicializa vistas
        rgGrupoTipo = findViewById(R.id.rgGrupoTipo);
        rbEntrenador = findViewById(R.id.rb_entrenador);
        rbUsuario = findViewById(R.id.rb_usuario);
        rbEmpresa = findViewById(R.id.rb_empresa);
        emailRegister = findViewById(R.id.emailRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        confirmPasswordRegister = findViewById(R.id.confirmPasswordRegister);
        buttonRegister = findViewById(R.id.buttonRegister);
        backButtonRegister = findViewById(R.id.backButtonRegister);

        // Regresar a MainActivity
        backButtonRegister.setOnClickListener(view -> {
            Intent intent = new Intent(Registro.this, MainActivity.class);
            startActivity(intent);
        });

        // Acción para el botón de registro
        buttonRegister.setOnClickListener(view -> {
            String email = emailRegister.getText().toString().trim();
            String password = passwordRegister.getText().toString().trim();
            String confirmPassword = confirmPasswordRegister.getText().toString().trim();
            String tipoUsuario = obtenerTipoUsuario();

            guardarUsuario(email, password, confirmPassword, tipoUsuario);
        });
    }

    // Obtener el tipo de usuario
    private String obtenerTipoUsuario() {
        int selectedId = rgGrupoTipo.getCheckedRadioButtonId();

        if (selectedId == rbUsuario.getId()) {
            return "Usuario";
        } else if (selectedId == rbEmpresa.getId()) {
            return "Empresa";
        } else if (selectedId == rbEntrenador.getId()) {
            return "Entrenador";
        }
        return "General";
    }

    // Guardar usuario y validación de entrada
    private void guardarUsuario(String email, String password, String confirmPassword, String tipoUsuario) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(Registro.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(Registro.this, "Ingrese un correo Gmail válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6 || !password.equals(confirmPassword)) {
            Toast.makeText(Registro.this, "Las contraseñas no coinciden o son menores a 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registro en Firebase Auth
        registerUser(email, password, tipoUsuario);
    }

    // Registro en Firebase Auth
    private void registerUser(String email, String password, String tipoUsuario) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            guardarDatosAdicionales(user, email, tipoUsuario);
                        }
                    } else {
                        Toast.makeText(Registro.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Guardar datos adicionales en Realtime Database
    private void guardarDatosAdicionales(FirebaseUser user, String email, String tipoUsuario) {
        String id = user.getUid();
        User usuario = new User("", "", "", "", "", "", "", email, tipoUsuario, "N/A", "");

        databaseReference.child(id).setValue(usuario)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Registro.this, Login.class));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Registro.this, "Error al guardar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseError", "Error al guardar datos: ", e);
                });
    }
}
