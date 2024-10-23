package activities;

import static android.widget.Toast.LENGTH_SHORT;
import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import models.User;

public class Registro extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText emailRegister, passwordRegister, confirmPasswordRegister,et_nombre,et_apellido,et_rut,tv_direcion,et_numeroCasa,et_comuna,et_region,et_telefono,et_correo;
    private Button buttonRegister, backButtonRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicializa Firebase Database y obtiene la referencia
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Nodo "contactos" en la base de datos
        databaseReference = database.getReference("users");

        // Obtiene la instancia actual de FirebaseAuth para manejar la autenticación de usuarios
        mAuth = FirebaseAuth.getInstance();

        et_nombre = findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_rut = findViewById(R.id.et_Rut);
        tv_direcion = findViewById(R.id.tv_direccion);
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
                finish();
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
                String name = et_nombre.getText().toString();
                String lastName = et_apellido.getText().toString();
                String street = tv_direcion.getText().toString();
                String nHome = et_numeroCasa.getText().toString();
                String rut = et_rut.getText().toString();
                String comunne = et_comuna.getText().toString();
                String region = et_region.getText().toString();
                String phone = et_telefono.getText().toString();
                String mail = et_correo.getText().toString();

                //Pasando el rut a la funcion para validarlo
                validarRut(rut);
                // Pasar los valores a la función registerUser
                registerUser(email, password, confirmPassword);

            }
        });
    }

    //Funcion para validar rut chileno
    public String validarRut(String rut) {
        // Eliminar puntos y guión
        rut = rut.replace(".", "").replace("-", "");

        // Verificar que el RUT tenga al menos 2 caracteres
        if (rut.length() < 2) {
            Toast.makeText(Registro.this, "El RUT es demasiado corto", LENGTH_SHORT).show();
            return "El RUT es demasiado corto";
        }

        // Separar el cuerpo del dígito verificador
        String rutCuerpo = rut.substring(0, rut.length() - 1);
        char dv = rut.charAt(rut.length() - 1);

        // Convertir el cuerpo del RUT a entero
        int rutNumero;
        try {
            rutNumero = Integer.parseInt(rutCuerpo);
        } catch (NumberFormatException e) {
            Toast.makeText(Registro.this, "El RUT contiene caracteres no válidos", LENGTH_SHORT).show();
            return "El RUT contiene caracteres no válidos";
        }

        // Calcular el dígito verificador
        int m = 0, s = 1;
        while (rutNumero != 0) {
            s = (s + rutNumero % 10 * (9 - m++ % 6)) % 11;
            rutNumero /= 10;
        }
        char dvCalculado = (s > 0) ? (char) (s + 47) : 'K';

        // Comparar el dígito verificador calculado con el proporcionado
        if (dvCalculado == dv) {
            Toast.makeText(Registro.this, "El RUT es válido", LENGTH_SHORT).show();
            return "El RUT es válido";
        } else {
            Toast.makeText(Registro.this, "El dígito verificador es incorrecto", LENGTH_SHORT).show();
            return "El dígito verificador es incorrecto";
        }
    }

    //Funcion para agregar un usuario
    private void AddUser(User user) {
        String id = databaseReference.push().getKey();
        databaseReference.child(id).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Usuario guardado correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar el usuario", Toast.LENGTH_SHORT).show();
                });
    }

    // Función para registrar al usuario
    private void registerUser(String email, String password, String confirmPassword) {
        // Validar que el email no esté vacío y que sea de Gmail
        if (email.isEmpty() || !email.endsWith("@gmail.com")) {
            Toast.makeText(Registro.this, "Por favor, ingrese un correo de Gmail válido", LENGTH_SHORT).show();
            return;
        }

        //se valida que el usuario tenga que ingresar 6 digitos y que el campo no este vacio.
        if (password.isEmpty() || confirmPassword.isEmpty() || password.length() < 6) {
            Toast.makeText(Registro.this, "Por favor, ingrese una contraseña válida de al menos 6 caracteres", LENGTH_SHORT).show();
            return;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            Toast.makeText(Registro.this, "Las contraseñas no coinciden", LENGTH_SHORT).show();
            return;
        }

        // Crear usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user); // Actualiza la UI con el usuario registrado
                        } else {
                            //log.w("AUTH","createUserWithEmail:failure",task.getException());
                            Toast.makeText(Registro.this, "Error de Autentificación", LENGTH_SHORT).show();
                            updateUI(null); // Manejar error
                        }
                    }
                });
    }

    //  Función para actualizar la UI después del registro
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(Registro.this, Perfilusuario.class);
            startActivity(intent);
        } else {
            Toast.makeText(Registro.this, "Autenticación fallida. Por favor, inténtalo de nuevo.", LENGTH_SHORT).show();
        }
    }
}



