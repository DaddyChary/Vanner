package activities;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.User;
import ui.Login;
import ui.Registro;

public class CrearUsuarioAdmin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private Button btn_crear, btn_volver;
    private EditText et_nombre, et_apellido, et_Rut, et_direccion, et_numeroCasa, et_comuna, et_region, et_telefono, et_password, et_confirmPassword,et_correo;
    private RadioGroup rgGrupoTipo;
    private RadioButton rb_administrador, rb_usuario;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crearusuarioadministrador);

        // Inicializa Firebase Database y obtiene la referencia
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        btn_crear = findViewById(R.id.btn_crear);
        btn_volver = findViewById(R.id.btn_volver);
        et_nombre = findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_Rut = findViewById(R.id.et_Rut);
        et_direccion = findViewById(R.id.et_direccion);
        et_numeroCasa = findViewById(R.id.et_numeroCasa);
        et_comuna = findViewById(R.id.et_comuna);
        et_region = findViewById(R.id.et_region);
        et_telefono = findViewById(R.id.et_telefono);
        et_password = findViewById(R.id.et_password);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        et_correo = findViewById(R.id.et_correo);
        rgGrupoTipo = findViewById(R.id.rgGrupoTipo);
        rb_administrador = findViewById(R.id.rb_administrador);
        rb_usuario = findViewById(R.id.rb_usuario);

        btn_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = et_nombre.getText().toString();
                String apellido = et_apellido.getText().toString();
                String rut = et_Rut.getText().toString();
                String direccion = et_direccion.getText().toString();
                String numeroCasa = et_numeroCasa.getText().toString();
                String comuna = et_comuna.getText().toString();
                String region = et_region.getText().toString();
                String telefono = et_telefono.getText().toString();
                String password = et_password.getText().toString();
                String confirmPassword = et_confirmPassword.getText().toString();
                String correo = et_correo.getText().toString();
                int idTipoUsuario = rgGrupoTipo.getCheckedRadioButtonId();
                RadioButton rbChecked = findViewById(idTipoUsuario);
                String tipoUsuario = rbChecked.getText().toString();

                guardarUsuario(correo, password, confirmPassword, nombre, apellido, rut, direccion, numeroCasa, comuna, region, telefono);
            }
        });
    }

                // Función para guardar el usuario
        private void guardarUsuario(String email, String password, String confirmPassword, String nombre, String apellido, String rut, String direccion, String numeroCasa, String comuna, String region, String telefono) {

            // Validar campos obligatorios
            if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(CrearUsuarioAdmin.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar el rut usando la función
            if (!validarRut(rut)) {
                return;
            }

            // Validar el correo electrónico (debe ser de Gmail)
            if (!email.endsWith("@gmail.com")) {
                Toast.makeText(CrearUsuarioAdmin.this, "Ingrese un correo Gmail válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar la contraseña
            if (password.length() < 6 || !password.equals(confirmPassword)) {
                Toast.makeText(CrearUsuarioAdmin.this, "Las contraseñas no coinciden o son menores a 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            // Registrar el usuario en Firebase Authentication
            registerUser(email, password);
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
                            Toast.makeText(CrearUsuarioAdmin.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Validar el RUT chileno
        private boolean validarRut(String rut) {
            rut = rut.replace(".", "").replace("-", "");
            if (rut.length() < 2) {
                Toast.makeText(CrearUsuarioAdmin.this, "El RUT es demasiado corto", Toast.LENGTH_SHORT).show();
                return false;
            }

            String rutCuerpo = rut.substring(0, rut.length() - 1);
            char dv = rut.charAt(rut.length() - 1);

            int rutNumero;
            try {
                rutNumero = Integer.parseInt(rutCuerpo);
            } catch (NumberFormatException e) {
                Toast.makeText(CrearUsuarioAdmin.this, "El RUT contiene caracteres no válidos", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CrearUsuarioAdmin.this, "El dígito verificador es incorrecto", Toast.LENGTH_SHORT).show();
                return false;
            }

        }

    private void guardarDatosAdicionales(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(CrearUsuarioAdmin.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el ID del usuario
        String id = user.getUid();

        // Obtener los valores de los campos
        String nombre = et_nombre.getText().toString().trim();
        String apellido = et_apellido.getText().toString().trim();
        String rut = et_Rut.getText().toString().trim();
        String direccion = et_direccion.getText().toString().trim(); // Corregido si es un campo editable
        String numeroCasa = et_numeroCasa.getText().toString().trim();
        String comuna = et_comuna.getText().toString().trim();
        String region = et_region.getText().toString().trim();
        String telefono = et_telefono.getText().toString().trim();
        String correo = et_correo.getText().toString().trim(); // Correo correcto
        int idTipoUsuario = rgGrupoTipo.getCheckedRadioButtonId();
        RadioButton rbChecked = findViewById(idTipoUsuario);
        String tipoUsuario = rbChecked.getText().toString();

        // Validar que todos los campos requeridos no estén vacíos
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(rut) ||
                TextUtils.isEmpty(direccion) || TextUtils.isEmpty(numeroCasa) ||
                TextUtils.isEmpty(comuna) || TextUtils.isEmpty(region) ||
                TextUtils.isEmpty(telefono) || TextUtils.isEmpty(correo) ||
                TextUtils.isEmpty(tipoUsuario)) {

            Toast.makeText(CrearUsuarioAdmin.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log para verificar que los valores no estén vacíos
        Log.d("Registro", "Datos a guardar: " + nombre + ", " + apellido + ", " + rut + ", " + direccion);

        // Asignar valores predeterminados a los campos adicionales
        String specialization = "N/A"; // Valor predeterminado

        // Crear el objeto User con los campos requeridos
        User usuario = new User(nombre, apellido, rut, numeroCasa, comuna, region, telefono, correo, specialization, direccion, tipoUsuario);

        // Inicializar la referencia de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("usuarios");

        // Guardar el objeto User en la base de datos usando el UID del usuario
        databaseReference.child(id).setValue(usuario)
                .addOnSuccessListener(aVoid -> {
                    // Datos guardados exitosamente
                    Toast.makeText(CrearUsuarioAdmin.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CrearUsuarioAdmin.this, Login.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Error al guardar los datos
                    Toast.makeText(CrearUsuarioAdmin.this, "Error al guardar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseError", "Error al guardar datos: ", e);
                });
    }
}


