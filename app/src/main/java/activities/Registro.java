package activities;

import static android.widget.Toast.LENGTH_SHORT;
import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.socialab2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import models.User;

public class Registro extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText emailRegister, passwordRegister, confirmPasswordRegister, et_nombre, et_apellido, et_rut, tv_direcion, et_numeroCasa, et_comuna, et_region, et_telefono, et_correo;
    private Button buttonRegister, backButtonRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.registro, container, false);

        // Inicializa Firebase Database y obtiene la referencia
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Referencias a los elementos de la vista
        et_nombre = view.findViewById(R.id.et_nombre);
        et_apellido = view.findViewById(R.id.et_apellido);
        et_rut = view.findViewById(R.id.et_Rut);
        tv_direcion = view.findViewById(R.id.tv_direccion);
        et_numeroCasa = view.findViewById(R.id.et_numeroCasa);
        et_comuna = view.findViewById(R.id.et_comuna);
        et_region = view.findViewById(R.id.et_region);
        et_telefono = view.findViewById(R.id.et_telefono);
        et_correo = view.findViewById(R.id.et_correo);
        emailRegister = view.findViewById(R.id.emailRegister);
        passwordRegister = view.findViewById(R.id.passwordRegister);
        confirmPasswordRegister = view.findViewById(R.id.confirmPasswordRegister);
        buttonRegister = view.findViewById(R.id.buttonRegister);
        backButtonRegister = view.findViewById(R.id.backButtonRegister);

        // Acción para el botón de regresar
        backButtonRegister.setOnClickListener(view1 -> getActivity().getSupportFragmentManager().popBackStack());

        // Acción para el botón de registrar
        buttonRegister.setOnClickListener(v -> guardarUsuario());

        return view;
    }

    // Función para guardar el usuario
    private void guardarUsuario() {
        String email = emailRegister.getText().toString().trim();
        String password = passwordRegister.getText().toString().trim();
        String confirmPassword = confirmPasswordRegister.getText().toString().trim();
        String nombre = et_nombre.getText().toString().trim();
        String apellido = et_apellido.getText().toString().trim();
        String rut = et_rut.getText().toString().trim();
        String direccion = tv_direcion.getText().toString().trim();
        String numeroCasa = et_numeroCasa.getText().toString().trim();
        String comuna = et_comuna.getText().toString().trim();
        String region = et_region.getText().toString().trim();
        String telefono = et_telefono.getText().toString().trim();
        String correo = et_correo.getText().toString().trim();

        // Validar campos obligatorios
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el rut usando la función
        if (!validarRut(rut)) {
            return;
        }

        // Validar el correo electrónico (debe ser de Gmail)
        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(getContext(), "Ingrese un correo Gmail válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar la contraseña
        if (password.length() < 6 || !password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Las contraseñas no coinciden o son menores a 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registrar el usuario en Firebase Authentication
        registerUser(email, password);
    }

    // Validar el RUT chileno
    private boolean validarRut(String rut) {
        rut = rut.replace(".", "").replace("-", "");
        if (rut.length() < 2) {
            Toast.makeText(getContext(), "El RUT es demasiado corto", Toast.LENGTH_SHORT).show();
            return false;
        }

        String rutCuerpo = rut.substring(0, rut.length() - 1);
        char dv = rut.charAt(rut.length() - 1);

        int rutNumero;
        try {
            rutNumero = Integer.parseInt(rutCuerpo);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "El RUT contiene caracteres no válidos", Toast.LENGTH_SHORT).show();
            return false;
        }

        int m = 0, s = 1;
        while (rutNumero != 0) {
            s = (s + rutNumero % 10 * (9 - m++ % 6)) % 11;
            rutNumero /= 10;
        }

        char dvCalculado = (s > 0) ? (char) (s + 47) : 'K';
        if (dvCalculado == dv) {
            return true;
        } else {
            Toast.makeText(getContext(), "El dígito verificador es incorrecto", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Función para registrar el usuario en Firebase
    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Guardar información adicional en la base de datos
                        FirebaseUser user = mAuth.getCurrentUser();
                        guardarDatosAdicionales(user);
                    } else {
                        Toast.makeText(getContext(), "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Guardar datos adicionales del usuario
    private void guardarDatosAdicionales(FirebaseUser user) {
        if (user == null) return;

        String id = user.getUid();
        String nombre = et_nombre.getText().toString().trim();
        String apellido = et_apellido.getText().toString().trim();
        String rut = et_rut.getText().toString().trim();
        String direccion = tv_direcion.getText().toString().trim();
        String numeroCasa = et_numeroCasa.getText().toString().trim();
        String comuna = et_comuna.getText().toString().trim();
        String region = et_region.getText().toString().trim();
        String telefono = et_telefono.getText().toString().trim();
        String correo = et_correo.getText().toString().trim();

        // Asignar valores predeterminados a los campos adicionales
        String userType = "general"; // o el valor que desees
        String specialization = "N/A"; // o el valor que desees

        // Crear el objeto User con los campos requeridos
        User usuario = new User(nombre, apellido, rut, numeroCasa, comuna, region, telefono, correo, userType, specialization, direccion);

        // Guardar en la base de datos
        databaseReference.child(id).setValue(usuario)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al guardar los datos", Toast.LENGTH_SHORT).show());
    }
}



