package activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;

public class EliminarAdmin extends AppCompatActivity {
    private Button btnEliminar, btnVolver;
    private EditText et_Rut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eliminaradministrador);

        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolver);
        et_Rut = findViewById(R.id.et_Rut);

        btnEliminar.setOnClickListener(view -> {

        });

        btnVolver.setOnClickListener(view -> {
            finish();
        });
    }

}
