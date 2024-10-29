package activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import models.User;

public class EditarAdmin extends AppCompatActivity {

    private EditText et_nombre, et_apellido, et_Rut, et_calle, et_numeroCasa, et_comuna, et_region, et_telefono, et_correo, et_correo_buscar;
    private RadioGroup rg_tipo_usuario;
    private RadioButton rb_administrador, rbTecnicos;
    private Button btn_editar, btn_volver, btnBuscar;
    private DatabaseReference databaseReference;
    private String usuarioId; // Variable para almacenar el ID del usuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editaradministrador);

        // Inicializa los campos de la interfaz
        et_correo_buscar = findViewById(R.id.et_correo_buscar);
        et_nombre = findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_Rut = findViewById(R.id.et_Rut);
        et_calle = findViewById(R.id.et_calle);
        et_numeroCasa = findViewById(R.id.et_numeroCasa);
        et_comuna = findViewById(R.id.et_comuna);
        et_region = findViewById(R.id.et_region);
        et_telefono = findViewById(R.id.et_telefono);
        et_correo = findViewById(R.id.et_correo);
        rg_tipo_usuario = findViewById(R.id.rg_tipo_usuario);
        rbTecnicos = findViewById(R.id.rbTecnicos);
        rb_administrador = findViewById(R.id.rb_administrador); // Asegúrate de que este ID sea correcto
        btn_editar = findViewById(R.id.btn_editar);
        btn_volver = findViewById(R.id.btn_volver);
        btnBuscar = findViewById(R.id.btnBuscar);

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Configura el botón de búsqueda
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correoIngresado = et_correo_buscar.getText().toString().trim();
                cargarUsuarioPorCorreo(correoIngresado);
            }
        });

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarUsuario();
            }
        });

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volver();
            }
        });
    }

    private void cargarUsuarioPorCorreo(String correo) {
        if (!correo.isEmpty()) {
            Query query = databaseReference.orderByChild("mail").equalTo(correo);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                            usuarioId = usuarioSnapshot.getKey(); // Guarda el ID del usuario
                            User usuario = usuarioSnapshot.getValue(User.class);
                            if (usuario != null) {
                                et_nombre.setText(usuario.getName());
                                et_apellido.setText(usuario.getLastName());
                                et_Rut.setText(usuario.getRut());
                                et_calle.setText(usuario.getStreet());
                                et_numeroCasa.setText(usuario.getnHome());
                                et_comuna.setText(usuario.getCommune());
                                et_region.setText(usuario.getRegion());
                                et_telefono.setText(usuario.getPhone());
                                et_correo.setText(usuario.getMail());

                                if ("general".equals(usuario.getUserType())) {
                                    rg_tipo_usuario.check(R.id.rb_usuario);
                                } else if ("tecnico".equals(usuario.getUserType())) {
                                    rg_tipo_usuario.check(R.id.rbTecnicos);
                                }
                            }
                        }
                    } else {
                        Toast.makeText(EditarAdmin.this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(EditarAdmin.this, "Error al cargar el usuario: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Por favor, introduce un correo.", Toast.LENGTH_SHORT).show();
        }
    }

    private void editarUsuario() {
        if (usuarioId != null) { // Asegúrate de que se haya cargado un usuario
            // Obtén los valores de los campos
            String nombre = et_nombre.getText().toString().trim();
            String apellido = et_apellido.getText().toString().trim();
            String rut = et_Rut.getText().toString().trim();
            String calle = et_calle.getText().toString().trim();
            String numeroCasa = et_numeroCasa.getText().toString().trim();
            String comuna = et_comuna.getText().toString().trim();
            String region = et_region.getText().toString().trim();
            String telefono = et_telefono.getText().toString().trim();
            String correo = et_correo.getText().toString().trim();
            String tipoUsuario = rg_tipo_usuario.getCheckedRadioButtonId() == R.id.rb_usuario ? "general" : "tecnico";

            // Crea un mapa para actualizar los datos en Firebase
            Map<String, Object> usuarioUpdates = new HashMap<>();
            usuarioUpdates.put("name", nombre);
            usuarioUpdates.put("lastName", apellido);
            usuarioUpdates.put("rut", rut);
            usuarioUpdates.put("street", calle);
            usuarioUpdates.put("nHome", numeroCasa);
            usuarioUpdates.put("commune", comuna);
            usuarioUpdates.put("region", region);
            usuarioUpdates.put("phone", telefono);
            usuarioUpdates.put("mail", correo);
            usuarioUpdates.put("userType", tipoUsuario);

            // Realiza la actualización en Firebase
            databaseReference.child(usuarioId).updateChildren(usuarioUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditarAdmin.this, "Usuario actualizado exitosamente.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditarAdmin.this, "Error al actualizar el usuario.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Por favor, busca un usuario antes de editar.", Toast.LENGTH_SHORT).show();
        }
    }

    private void volver() {
        finish();
    }
}
