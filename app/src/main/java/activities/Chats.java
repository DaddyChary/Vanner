package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.socialab2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Chats extends AppCompatActivity {

    private TextInputEditText etMessage, etMessage2;
    private MaterialButton btnBack;
    private ImageView ivProfilePhoto, ivProfilePhoto2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        // Inicializar los elementos de la interfaz
        etMessage = findViewById(R.id.et_message);
        etMessage2 = findViewById(R.id.et_message2);
        btnBack = findViewById(R.id.btn_back);
        ivProfilePhoto = findViewById(R.id.iv_profile_photo);
        ivProfilePhoto2 = findViewById(R.id.iv_profile_photo2);

        // Configurar el botón de retroceso
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Cierra la actividad actual y vuelve a la anterior
            }
        });

        // Opcional: configurar el envío de mensajes en los campos de texto
        etMessage.setOnEditorActionListener((textView, i, keyEvent) -> {
            String message = etMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                enviarMensaje(message);
                etMessage.setText(""); // Limpia el campo después de enviar
            }
            return true;
        });

        etMessage2.setOnEditorActionListener((textView, i, keyEvent) -> {
            String message = etMessage2.getText().toString().trim();
            if (!message.isEmpty()) {
                enviarMensaje(message);
                etMessage2.setText(""); // Limpia el campo después de enviar
            }
            return true;
        });
    }

    // Método para enviar el mensaje (por ahora muestra un Toast con el mensaje)
    private void enviarMensaje(String mensaje) {
        Toast.makeText(this, "Mensaje enviado: " + mensaje, Toast.LENGTH_SHORT).show();
        // Aquí puedes agregar la lógica para enviar el mensaje, como guardarlo en una base de datos o enviar a través de una API
    }
}
