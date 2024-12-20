package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
    private RadioGroup rgGrupoTipo;
    private RadioButton rb_administrador, rb_usuario, rb_entrenador, rb_empresa;
    private Button btn_editar, btn_volver, btnBuscar;
    private DatabaseReference databaseReference;
    private TableLayout tableUsuarios;
    private String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editaradministrador);

        // Inicialización de vistas
        tableUsuarios = findViewById(R.id.tableUsuarios);
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
        rgGrupoTipo = findViewById(R.id.rgGrupoTipo);
        rb_administrador = findViewById(R.id.rb_administrador);
        rb_usuario = findViewById(R.id.rb_usuario);
        rb_empresa = findViewById(R.id.rb_empresa);
        rb_entrenador = findViewById(R.id.rb_entrenador);
        btn_editar = findViewById(R.id.btn_editar);
        btn_volver = findViewById(R.id.btn_volver);
        btnBuscar = findViewById(R.id.btnBuscar);

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Cargar todos los usuarios en la tabla al iniciar
        cargarUsuariosEnTabla();

        // Configura el botón de búsqueda y edición
        btnBuscar.setOnClickListener(view -> cargarUsuarioPorCorreo(et_correo_buscar.getText().toString().trim()));
        btn_editar.setOnClickListener(view -> editarUsuario());
        btn_volver.setOnClickListener(view -> volver());
    }

    private void cargarUsuariosEnTabla() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tableUsuarios.removeAllViews(); // Limpia la tabla antes de cargar

                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    User usuario = usuarioSnapshot.getValue(User.class);
                    if (usuario != null) {
                        TableRow row = new TableRow(EditarAdmin.this);
                        TextView tvCorreo = new TextView(EditarAdmin.this);
                        tvCorreo.setText(usuario.getMail());
                        tvCorreo.setPadding(8, 8, 8, 8);
                        row.addView(tvCorreo);

                        // Añade la fila a la tabla
                        tableUsuarios.addView(row);

                        // Configura el listener de clic en cada fila
                        row.setOnClickListener(v -> cargarDatosUsuarioEnCampos(usuarioSnapshot.getKey(), usuario));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditarAdmin.this, "Error al cargar los usuarios: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDatosUsuarioEnCampos(String id, User usuario) {
        usuarioId = id;
        et_nombre.setText(usuario.getName());
        et_apellido.setText(usuario.getLastName());
        et_Rut.setText(usuario.getRut());
        et_calle.setText(usuario.getStreet());
        et_numeroCasa.setText(usuario.getnHome());
        et_comuna.setText(usuario.getCommune());
        et_region.setText(usuario.getRegion());
        et_telefono.setText(usuario.getPhone());
        et_correo.setText(usuario.getMail());

        // Selección del tipo de usuario en el RadioGroup
        if ("usuario".equals(usuario.getUserType())) {
            rgGrupoTipo.check(R.id.rb_usuario);
        } else if ("tecnico".equals(usuario.getUserType())) {
            rgGrupoTipo.check(R.id.rb_entrenador);
        } else if ("Empresa".equals(usuario.getUserType())) {
            rgGrupoTipo.check(R.id.rb_empresa);
        } else if ("administrador".equals(usuario.getUserType())) {
            rgGrupoTipo.check(R.id.rb_administrador);
        }
    }

    private void cargarUsuarioPorCorreo(String correo) {
        if (!correo.isEmpty()) {
            Query query = databaseReference.orderByChild("mail").equalTo(correo);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                            User usuario = usuarioSnapshot.getValue(User.class);
                            cargarDatosUsuarioEnCampos(usuarioSnapshot.getKey(), usuario);
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
        if (usuarioId != null) {
            String nombre = et_nombre.getText().toString().trim();
            String apellido = et_apellido.getText().toString().trim();
            String rut = et_Rut.getText().toString().trim();
            String calle = et_calle.getText().toString().trim();
            String numeroCasa = et_numeroCasa.getText().toString().trim();
            String comuna = et_comuna.getText().toString().trim();
            String region = et_region.getText().toString().trim();
            String telefono = et_telefono.getText().toString().trim();
            String correo = et_correo.getText().toString().trim();

            // Guarda el tipo de usuario seleccionado
            String tipoUsuario;
            if (rgGrupoTipo.getCheckedRadioButtonId() == R.id.rb_usuario) {
                tipoUsuario = "usuario";
            } else if (rgGrupoTipo.getCheckedRadioButtonId() == R.id.rb_entrenador) {
                tipoUsuario = "tecnico";
            } else if (rgGrupoTipo.getCheckedRadioButtonId() == R.id.rb_empresa) {
                tipoUsuario = "Empresa";
            } else {
                tipoUsuario = "administrador";
            }

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
        finish(); // Finaliza la actividad actual y vuelve a la anterior
    }
}
