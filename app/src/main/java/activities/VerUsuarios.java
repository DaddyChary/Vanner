package activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialab2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import models.User;

public class VerUsuarios extends AppCompatActivity {

    private EditText editTextBuscar;
    private Button buttonVolver;
    private TableLayout tblUser;
    private DatabaseReference databaseReference;
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verusuario);

        // Inicializar componentes de la interfaz
        editTextBuscar = findViewById(R.id.editTextBuscar);
        buttonVolver = findViewById(R.id.buttonVolver);
        tblUser = findViewById(R.id.tblUser);

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Cargar usuarios desde la base de datos
        cargarUsuarios();

        // Configurar la búsqueda reactiva
        editTextBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchEmail = s.toString();
                if (searchEmail.isEmpty()) {
                    mostrarUsuarios(userList);  // Muestra todos los usuarios si el campo está vacío
                } else {
                    filtrarUsuariosPorCorreo(searchEmail);  // Filtra usuarios en tiempo real
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Configurar el botón de volver
        buttonVolver.setOnClickListener(view -> finish());
    }

    private void cargarUsuarios() {
        // Limpiar la tabla antes de cargar
        tblUser.removeAllViews();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                mostrarUsuarios(userList);  // Mostrar todos los usuarios después de cargar
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VerUsuarios.this, "Error al cargar usuarios: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarUsuarios(List<User> usuarios) {
        // Limpiar la tabla antes de mostrar los datos
        tblUser.removeAllViews();

        // Agregar fila de encabezado
        TableRow headerRow = new TableRow(this);
        headerRow.addView(createTextView("Correo"));
        tblUser.addView(headerRow);

        // Llenar filas solo con los correos de los usuarios
        for (User user : usuarios) {
            TableRow row = new TableRow(this);
            row.addView(createTextView(user.getMail()));
            tblUser.addView(row);
        }
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        return textView;
    }

    private void filtrarUsuariosPorCorreo(String correo) {
        List<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            if (user.getMail().toLowerCase().contains(correo.toLowerCase())) {
                filteredList.add(user);
            }
        }
        mostrarUsuarios(filteredList);  // Mostrar los usuarios filtrados
    }
}

