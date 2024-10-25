package ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import models.User;

public class Perfilusuario extends AppCompatActivity {

    private Button btn_edit, btn_delete, btn_logout;
    private EditText et_nombre, et_apellido, et_Rut,et_direccion, et_numeroCasa, et_comuna, et_region, et_telefono, et_correo,Espelizacion;
    private RadioButton rbEntrenadores,rbTecnicos,rbEmpresas;
    private DatabaseReference databaseReference;
    private RadioGroup rgGrupoEntrenadores;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfilusuario);

        // Inicializar elementos de la interfaz
        rbEmpresas = findViewById(R.id.rbEmpresas);
        rbEntrenadores = findViewById(R.id.rbEntrenadores);
        rbTecnicos = findViewById(R.id.rbTecnicos);
        rgGrupoEntrenadores = findViewById(R.id.rgGrupoEntrenadores); // El RadioGroup
        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);
        btn_logout = findViewById(R.id.btn_logout);
        et_nombre = findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_Rut = findViewById(R.id.et_Rut);
        et_direccion = findViewById(R.id.et_direccion);
        et_numeroCasa = findViewById(R.id.et_numeroCasa);
        et_comuna = findViewById(R.id.et_comuna);
        et_region = findViewById(R.id.et_region);
        et_telefono = findViewById(R.id.et_telefono);
        et_correo = findViewById(R.id.et_correo);
        Espelizacion = findViewById(R.id.Espelizacion);

        // Obtener el UID del usuario pasado desde la actividad de login
        String userId = getIntent().getStringExtra("userId");

        // Inicializar la referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Cargar los datos del usuario si se pasó un userId
        if (userId != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            cargarDatosUsuario(user);
        }

        // Botón para actualizar perfil
        btn_edit.setOnClickListener(view -> actualizarPerfil());

        // Botón para eliminar perfil
        btn_delete.setOnClickListener(view -> eliminarPerfil());

        // Botón para cerrar sesión
        btn_logout.setOnClickListener(view -> {
            Intent intent = new Intent(Perfilusuario.this, Login.class);
            startActivity(intent);
            finish();
        });
    }


    // Método para cargar los datos del usuario desde Firebase
    private void cargarDatosUsuario(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(Perfilusuario.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el UID del usuario autenticado
        String id = user.getUid();

        // Escuchar la respuesta de la base de datos
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Verificar si existen los datos del usuario
                if (dataSnapshot.exists()) {
                    // Mapear los datos recuperados a un objeto User
                    User usuario = dataSnapshot.getValue(User.class);

                    // Asegurarse de que no sea null
                    if (usuario != null) {
                        // Rellenar los campos de la interfaz con los datos recuperados
                        et_nombre.setText(usuario.getName());
                        et_apellido.setText(usuario.getLastName());
                        et_Rut.setText(usuario.getRut());
                        et_direccion.setText(usuario.getStreet());
                        et_numeroCasa.setText(usuario.getnHome());
                        et_comuna.setText(usuario.getCommune());
                        et_region.setText(usuario.getRegion());
                        et_telefono.setText(usuario.getPhone());
                        et_correo.setText(usuario.getMail());
                        Espelizacion.setText(usuario.getSpecialization());
                        String tipoUsuario = usuario.getUserType();

                        // Verificar el tipo de usuario y seleccionar el RadioButton correspondiente
                        if (tipoUsuario.equals("entrenador")) {
                            rbEntrenadores.setChecked(true);
                        } else if (tipoUsuario.equals("técnico")) {
                            rbTecnicos.setChecked(true);
                        } else if (tipoUsuario.equals("empresa")) {
                            rbEmpresas.setChecked(true);
                        }


                    }
                } else {
                    Toast.makeText(Perfilusuario.this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Perfilusuario.this, "Error al cargar los datos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Error al cargar datos: ", databaseError.toException());
            }
        });
    }

    private void actualizarPerfil() {
        String nombre = et_nombre.getText().toString();
        String apellido = et_apellido.getText().toString();
        String direccion = et_direccion.getText().toString(); // Aquí se toma correctamente la dirección
        String numeroCasa = et_numeroCasa.getText().toString();
        String comuna = et_comuna.getText().toString();
        String region = et_region.getText().toString();
        String telefono = et_telefono.getText().toString();
        String correo = et_correo.getText().toString();
        String specilization = Espelizacion.getText().toString(); // Aquí se toma correctamente la especialización

        // Validar que todos los campos estén completos
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(direccion)
                || TextUtils.isEmpty(numeroCasa) || TextUtils.isEmpty(comuna)
                || TextUtils.isEmpty(region) || TextUtils.isEmpty(telefono)
                || TextUtils.isEmpty(correo) || TextUtils.isEmpty(specilization)) {
            Toast.makeText(Perfilusuario.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el valor del RadioButton seleccionado en el RadioGroup
        String tipoUsuario = "";
        int radioId = rgGrupoEntrenadores.getCheckedRadioButtonId();

        if (radioId == rbEntrenadores.getId()) {
            tipoUsuario = "entrenador";
        } else if (radioId == rbTecnicos.getId()) {
            tipoUsuario = "técnico";
        } else if (radioId == rbEmpresas.getId()) {
            tipoUsuario = "empresa";
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Inicializar la referencia a la base de datos
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);

            // Crear un mapa con los nuevos datos para actualizar
            Map<String, Object> actualizaciones = new HashMap<>();
            actualizaciones.put("name", nombre);
            actualizaciones.put("lastName", apellido);
            actualizaciones.put("street", direccion);
            actualizaciones.put("nHome", numeroCasa);
            actualizaciones.put("commune", comuna);
            actualizaciones.put("region", region);
            actualizaciones.put("phone", telefono);
            actualizaciones.put("mail", correo);
            actualizaciones.put("specialization", specilization); // Nombre de clave corregido
            actualizaciones.put("userType", tipoUsuario);

            // Actualizar los campos específicos en la base de datos de Firebase sin agregar duplicados
            databaseReference.updateChildren(actualizaciones)
                    .addOnSuccessListener(aVoid -> Toast.makeText(Perfilusuario.this, "Perfil actualizado", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(Perfilusuario.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(Perfilusuario.this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }




    // Método para eliminar el perfil del usuario
    private void eliminarPerfil() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            // Inicializar la referencia a la base de datos
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

            // Eliminar el perfil del usuario de la base de datos
            databaseReference.child(userId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        // Ahora, eliminar al usuario de Firebase Authentication
                        user.delete().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Perfilusuario.this, "Perfil y autenticación eliminados", Toast.LENGTH_SHORT).show();

                                // Limpiar los campos después de eliminar el perfil
                                et_nombre.setText("");
                                et_apellido.setText("");
                                et_Rut.setText("");
                                et_direccion.setText("");
                                et_numeroCasa.setText("");
                                et_comuna.setText("");
                                et_region.setText("");
                                et_telefono.setText("");
                                et_correo.setText("");

                                // Redirigir al usuario a la ventana de login después de eliminar el perfil
                                Intent intent = new Intent(Perfilusuario.this, Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Manejar error al eliminar el usuario de Firebase Authentication
                                Toast.makeText(Perfilusuario.this, "Error al eliminar la autenticación", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(Perfilusuario.this, "Error al eliminar el perfil", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(Perfilusuario.this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }

}
