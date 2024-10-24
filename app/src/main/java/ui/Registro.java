package ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText emailRegister, passwordRegister, confirmPasswordRegister, et_nombre, et_apellido, et_rut, tv_direccion, et_numeroCasa, et_comuna, et_region, et_telefono, et_correo;
    private Button buttonRegister, backButtonRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) { // Corrección de savedInstanceStates a savedInstanceState
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicializa Firebase Database y obtiene la referencia
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(this);

        // Referencias a los elementos de la vista
        et_nombre = findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_rut = findViewById(R.id.et_Rut);
        tv_direccion = findViewById(R.id.tv_direccion);  // Mantengo tv_direccion
        et_numeroCasa = findViewById(R.id.et_numeroCasa);
        et_comuna = findViewById(R.id.et_comuna);
        et_region = findViewById(R.id.et_region);
        et_telefono = findViewById(R.id.et_telefono);
        et_correo = findViewById(R.id.et_correo);
        emailRegister = findViewById(R.id.emailRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        confirmPasswordRegister = findViewById(R.id.confirmPasswordRegister);
        buttonRegister = findViewById(R.id.buttonRegister);
        backButtonRegister = findViewById(R.id.backButtonRegister);

        // Acción para el botón de regresar
        backButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registro.this, MainActivity.class);
                startActivity(intent); // Ahora la actividad cambia a MainActivity
            }
        });

        // Acción para el botón de registrar
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los valores de los campos de texto
                String email = emailRegister.getText().toString();
                String password = passwordRegister.getText().toString();
                String confirmPassword = confirmPasswordRegister.getText().toString();
                String nombre = et_nombre.getText().toString();
                String apellido = et_apellido.getText().toString();
                String rut = et_rut.getText().toString();
                String direccion = tv_direccion.getText().toString(); // Uso tv_direccion como dirección
                String numeroCasa = et_numeroCasa.getText().toString();
                String comuna = et_comuna.getText().toString();
                String region = et_region.getText().toString();
                String telefono = et_telefono.getText().toString();
                String correo = et_correo.getText().toString();

                // Llamar a la función guardarUsuario
                guardarUsuario(email, password, confirmPassword, nombre, apellido, rut, direccion, numeroCasa, comuna, region, telefono, correo);

            }
        });
    }

    // Función para guardar el usuario
    private void guardarUsuario(String email, String password, String confirmPassword, String nombre, String apellido, String rut, String direccion, String numeroCasa, String comuna, String region, String telefono, String correo) {

        // Validar campos obligatorios
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(Registro.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el rut usando la función
        if (!validarRut(rut)) {
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
        registerUser(email, password);
    }

    // Validar el RUT chileno
    private boolean validarRut(String rut) {
        rut = rut.replace(".", "").replace("-", "");
        if (rut.length() < 2) {
            Toast.makeText(Registro.this, "El RUT es demasiado corto", Toast.LENGTH_SHORT).show();
            return false;
        }

        String rutCuerpo = rut.substring(0, rut.length() - 1);
        char dv = rut.charAt(rut.length() - 1);

        int rutNumero;
        try {
            rutNumero = Integer.parseInt(rutCuerpo);
        } catch (NumberFormatException e) {
            Toast.makeText(Registro.this, "El RUT contiene caracteres no válidos", Toast.LENGTH_SHORT).show();
            return false;
        }

        int m = 0, s = 1;
        while (rutNumero != 0) {
            s = (s + rutNumero % 10 * (9 - m++ % 6)) % 11;
            rutNumero /= 10;
        }

        char dvCalculado = (s > 0) ? (char) (s + 47) : 'K';
        if (dvCalculado == dv) {
            return true;
        } else {
            Toast.makeText(Registro.this, "El dígito verificador es incorrecto", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Función para registrar el usuario en Firebase
    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Guardar información adicional en la base de datos
                        FirebaseUser user = mAuth.getCurrentUser();
                        guardarDatosAdicionales(user);
                    } else {
                        Toast.makeText(Registro.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Guardar datos adicionales del usuario
    private void guardarDatosAdicionales(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(Registro.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el ID del usuario
        String id = user.getUid();

        // Obtener los valores de los campos
        String nombre = et_nombre.getText().toString().trim();
        String apellido = et_apellido.getText().toString().trim();
        String rut = et_rut.getText().toString().trim();
        String direccion = tv_direccion.getText().toString().trim();
        String numeroCasa = et_numeroCasa.getText().toString().trim();
        String comuna = et_comuna.getText().toString().trim();
        String region = et_region.getText().toString().trim();
        String telefono = et_telefono.getText().toString().trim();
        String correo = et_correo.getText().toString().trim();

        // Validar que todos los campos requeridos no estén vacíos
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(rut) ||
                TextUtils.isEmpty(direccion) || TextUtils.isEmpty(numeroCasa) ||
                TextUtils.isEmpty(comuna) || TextUtils.isEmpty(region) ||
                TextUtils.isEmpty(telefono) || TextUtils.isEmpty(correo)) {

            Toast.makeText(Registro.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Asignar valores predeterminados a los campos adicionales
        String userType = "general"; // Valor predeterminado
        String specialization = "N/A"; // Valor predeterminado

        // Crear el objeto User con los campos requeridos
        User usuario = new User(nombre, apellido, rut, numeroCasa, comuna, region, telefono, correo, userType, specialization, direccion);


        // Inicializar la referencia de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("usuarios");

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





