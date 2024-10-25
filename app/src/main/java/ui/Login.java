package ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import activities.MainActivity;
import activities.OlvidarPass;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button loginButtton,backButton;
    private TextView forgotPasswordTextView;
    private EditText emailLogin,passwordEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        emailLogin = findViewById(R.id.emailLogin);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButtton = findViewById(R.id.loginButton);
        backButton = findViewById(R.id.backButton);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, OlvidarPass.class);
                startActivity(intent);
            }
        });

        loginButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLogin.getText().toString();
                String password = passwordEditText.getText().toString();

                singIn(email,password);
            }
        });
        }

    private void singIn(String email, String password) {

        if (email.isEmpty() || !email.endsWith("@gmail.com")) {
            Toast.makeText(Login.this, "Por favor, ingrese un correo de Gmail válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty() || password.length() < 6) {
            Toast.makeText(Login.this, "Por favor, ingrese una contraseña válida de al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Autenticación exitosa
                            Log.d("AUTH", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Obtener el UID del usuario
                                String userId = user.getUid();

                                // Referencia a la base de datos para el usuario
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);

                                // Recuperar los datos del usuario
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // Obtener el tipo de usuario
                                            String tipoUsuario = dataSnapshot.child("userType").getValue(String.class);

                                            if (tipoUsuario != null && tipoUsuario.equals("admin")) {
                                                // Mensaje para admin
                                                Toast.makeText(Login.this, "Bienvenido Admin", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Mensaje para cualquier otro tipo de usuario
                                                Toast.makeText(Login.this, "Bienvenido Usuario", Toast.LENGTH_SHORT).show();
                                            }

                                            // Iniciar la actividad de perfil y pasar el UID del usuario
                                            Intent intent = new Intent(Login.this, Perfilusuario.class);
                                            intent.putExtra("userId", userId); // Pasar UID del usuario
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(Login.this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e("DB_ERROR", "Error al obtener datos del usuario", databaseError.toException());
                                    }
                                });
                            }
                        } else {
                            // Si la autenticación falla
                            Log.w("AUTH", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Credenciales Incorrectas", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


        private void updateUI(FirebaseUser user){
        if (user != null) {
            Intent intent = new Intent(Login.this, Perfilusuario.class);
            startActivity(intent);
        } else {
            Toast.makeText(Login.this, "Inicio de sesión fallido. Por favor, inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }


}
