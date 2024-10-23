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
    private EditText emailRegister, passwordRegister, confirmPasswordRegister;
    private Button buttonRegister, backButtonRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicializa Firebase Database y obtiene la referencia
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users"); // Nodo "contactos" en la base de datos
        mAuth = FirebaseAuth.getInstance();
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

                // Pasar los valores a la función registerUser
                registerUser(email, password, confirmPassword);

            }
        });
    }
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



