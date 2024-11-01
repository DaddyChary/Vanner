package ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private EditText et_nombre, et_apellido, et_Rut, et_calle, et_numeroCasa, et_comuna, et_region, et_telefono, et_email, Espelizacion;
    private DatabaseReference databaseReference;
    private ImageView profileImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfilusuario);

        // Initialize UI elements
        profileImage = findViewById(R.id.profileImage);
        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);
        btn_logout = findViewById(R.id.btn_logout);
        et_nombre = findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_Rut = findViewById(R.id.et_Rut);
        et_calle = findViewById(R.id.et_calle);
        et_numeroCasa = findViewById(R.id.et_numeroCasa);
        et_comuna = findViewById(R.id.et_comuna);
        et_region = findViewById(R.id.et_region);
        et_telefono = findViewById(R.id.et_telefono);
        et_email = findViewById(R.id.et_email);
        Espelizacion = findViewById(R.id.Especialización);

        String userId = getIntent().getStringExtra("userId");
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        if (userId != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            cargarDatosUsuario(user);
        }

        btn_edit.setOnClickListener(view -> actualizarPerfil());
        btn_delete.setOnClickListener(view -> eliminarPerfil());
        btn_logout.setOnClickListener(view -> {
            Intent intent = new Intent(Perfilusuario.this, Login.class);
            startActivity(intent);
            finish();
        });
    }

    private void cargarDatosUsuario(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(Perfilusuario.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = user.getUid();
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User usuario = dataSnapshot.getValue(User.class);
                    if (usuario != null) {
                        et_nombre.setText(usuario.getName());
                        et_apellido.setText(usuario.getLastName());
                        et_Rut.setText(usuario.getRut());
                        et_calle.setText(usuario.getStreet());
                        et_numeroCasa.setText(usuario.getnHome());
                        et_comuna.setText(usuario.getCommune());
                        et_region.setText(usuario.getRegion());
                        et_telefono.setText(usuario.getPhone());
                        et_email.setText(usuario.getMail());
                        Espelizacion.setText(usuario.getSpecialization());
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
        String rut = et_Rut.getText().toString();
        String direccion = et_calle.getText().toString();
        String numeroCasa = et_numeroCasa.getText().toString();
        String comuna = et_comuna.getText().toString();
        String region = et_region.getText().toString();
        String telefono = et_telefono.getText().toString();
        String correo = et_email.getText().toString();
        String specilization = Espelizacion.getText().toString();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(rut) || TextUtils.isEmpty(direccion)
                || TextUtils.isEmpty(numeroCasa) || TextUtils.isEmpty(comuna) || TextUtils.isEmpty(region)
                || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(specilization)) {
            Toast.makeText(Perfilusuario.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate RUT
        if (!validarRut(rut)) {
            Toast.makeText(Perfilusuario.this, "RUT inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);

            Map<String, Object> actualizaciones = new HashMap<>();
            actualizaciones.put("name", nombre);
            actualizaciones.put("lastName", apellido);
            actualizaciones.put("rut", rut);
            actualizaciones.put("street", direccion);
            actualizaciones.put("nHome", numeroCasa);
            actualizaciones.put("commune", comuna);
            actualizaciones.put("region", region);
            actualizaciones.put("phone", telefono);
            actualizaciones.put("mail", correo);
            actualizaciones.put("specialization", specilization);

            databaseReference.updateChildren(actualizaciones)
                    .addOnSuccessListener(aVoid -> Toast.makeText(Perfilusuario.this, "Perfil actualizado", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(Perfilusuario.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(Perfilusuario.this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarRut(String rut) {
        rut = rut.replace(".", "").replace("-", "");
        if (rut.length() < 2) {
            return false;
        }

        String rutCuerpo = rut.substring(0, rut.length() - 1);
        char dv = rut.charAt(rut.length() - 1);

        int rutNumero;
        try {
            rutNumero = Integer.parseInt(rutCuerpo);
        } catch (NumberFormatException e) {
            return false;
        }

        int m = 0, s = 1;
        while (rutNumero != 0) {
            s = (s + rutNumero % 10 * (9 - m++ % 6)) % 11;
            rutNumero /= 10;
        }

        char dvCalculado = (s > 0) ? (char) (s + 47) : 'K';
        return dvCalculado == dv;
    }

    private void eliminarPerfil() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

            databaseReference.child(userId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        user.delete().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Perfilusuario.this, "Perfil y autenticación eliminados", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Perfilusuario.this, Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Perfilusuario.this, "Error al eliminar la autenticación", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(Perfilusuario.this, "Error al eliminar el perfil", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(Perfilusuario.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}
