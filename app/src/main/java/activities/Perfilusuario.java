package activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import models.User;
import ui.Registro;

public class Perfilusuario extends AppCompatActivity {

    private Button btn_edit,btn_delete,btn_logout;
    private EditText et_nombre,et_apellido,et_Rut,et_direccion,et_numeroCasa,et_comuna,et_region,et_telefono,et_correo;
    private RadioButton rbEntrenadores, rbTecnicos, rbEmpresas;
    private RadioGroup rgGrupoEntrenadores;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfilusuario);

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
        rbEntrenadores = findViewById(R.id.rbEntrenadores);
        rbTecnicos = findViewById(R.id.rbTecnicos);
        rbEmpresas = findViewById(R.id.rbEmpresas);
        rgGrupoEntrenadores = findViewById(R.id.rgGrupoEntrenadores);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Perfilusuario.this, Login.class);
                startActivity(intent);
            }
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

        // Inicializar la referencia a la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("usuarios").child(id);

        // Escuchar la respuesta de la base de datos
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    }
                } else {
                    // Si no existen datos para el usuario
                    Toast.makeText(Perfilusuario.this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores al obtener los datos
                Toast.makeText(Perfilusuario.this, "Error al cargar los datos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Error al cargar datos: ", databaseError.toException());
            }
        });
    }
}
