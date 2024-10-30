package activities;

import android.os.Bundle;
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
    private Button buttonBuscar, buttonVolver;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verusuario);

        // Inicializar componentes de la interfaz
        editTextBuscar = findViewById(R.id.editTextBuscar);
        buttonBuscar = findViewById(R.id.buttonBuscar);
        buttonVolver = findViewById(R.id.buttonVolver);
        recyclerView = findViewById(R.id.recyclerView);

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Cargar usuarios desde la base de datos
        cargarUsuarios();

        // Configurar el botón de búsqueda
        buttonBuscar.setOnClickListener(view -> {
            String searchEmail = editTextBuscar.getText().toString();
            filtrarUsuariosPorCorreo(searchEmail);
        });

        // Configurar el botón de volver
        buttonVolver.setOnClickListener(view -> finish());
    }

    private void cargarUsuarios() {
        // Limpiar la tabla antes de cargar
        recyclerView.removeAllViews();

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
                mostrarUsuarios(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VerUsuarios.this, "Error al cargar usuarios: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarUsuarios(List<User> usuarios) {
        // Agregar fila de encabezado a la tabla
        TableRow headerRow = new TableRow(this);
        headerRow.addView(createTextView("Nombre"));
        headerRow.addView(createTextView("Apellido"));
        headerRow.addView(createTextView("Correo"));
        recyclerView.addView(headerRow);

        // Llenar filas con los datos de los usuarios
        for (User user : usuarios) {
            TableRow row = new TableRow(this);
            row.addView(createTextView(user.getName()));
            row.addView(createTextView(user.getLastName()));
            row.addView(createTextView(user.getMail()));
            recyclerView.addView(row);
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
        mostrarUsuarios(filteredList);
    }
}

