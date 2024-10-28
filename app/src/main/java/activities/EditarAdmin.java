package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;

public class EditarAdmin extends AppCompatActivity {

    private EditText et_nombre, et_apellido, et_Rut, et_direccion, et_numeroCasa, et_comuna, et_region, et_telefono, et_correo;
    private RadioGroup rg_tipo_usuario;
    private RadioButton rb_administrador, rb_usuario;
    private Button btn_editar, btn_volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editaradministrador);

        et_nombre = findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_Rut = findViewById(R.id.et_Rut);
        //et_direccion = findViewById(R.id.et_direccion);
        et_numeroCasa = findViewById(R.id.et_numeroCasa);
        et_comuna = findViewById(R.id.et_comuna);
        et_region = findViewById(R.id.et_region);
        et_telefono = findViewById(R.id.et_telefono);
        et_correo = findViewById(R.id.et_correo);
        rg_tipo_usuario = findViewById(R.id.rg_tipo_usuario);
        rb_administrador = findViewById(R.id.rb_administrador);
        rb_usuario = findViewById(R.id.rb_usuario);
        btn_editar = findViewById(R.id.btn_editar);
        btn_volver = findViewById(R.id.btn_volver);

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarUsuario();
            }
        });

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });
    }

    private void editarUsuario() {
        String nombre = et_nombre.getText().toString().trim();
        String apellido = et_apellido.getText().toString().trim();
        String rut = et_Rut.getText().toString().trim();
        String direccion = et_direccion.getText().toString().trim();
        String numeroCasa = et_numeroCasa.getText().toString().trim();
        String comuna = et_comuna.getText().toString().trim();
        String region = et_region.getText().toString().trim();
        String telefono = et_telefono.getText().toString().trim();
        String correo = et_correo.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || rut.isEmpty() || direccion.isEmpty() ||
                numeroCasa.isEmpty() || comuna.isEmpty() || region.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = rg_tipo_usuario.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Por favor, selecciona un tipo de usuario.", Toast.LENGTH_SHORT).show();
            return;
        }
        String tipoUsuario = selectedId == R.id.rb_administrador ? "Administrador" : "Usuario";

        StringBuilder mensaje = new StringBuilder("Nombre: ").append(nombre)
                .append("\nApellido: ").append(apellido)
                .append("\nRut: ").append(rut)
                .append("\nDirección: ").append(direccion)
                .append("\nNúmero de casa: ").append(numeroCasa)
                .append("\nComuna: ").append(comuna)
                .append("\nRegión: ").append(region)
                .append("\nTeléfono: ").append(telefono)
                .append("\nCorreo: ").append(correo)
                .append("\nTipo de usuario: ").append(tipoUsuario);

        Toast.makeText(this, mensaje.toString(), Toast.LENGTH_LONG).show();
    }

    private void volver() {
        finish();
    }
}
