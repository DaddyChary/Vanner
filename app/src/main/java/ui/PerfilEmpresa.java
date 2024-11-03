package ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialab2.R;
import com.google.android.material.card.MaterialCardView;

public class PerfilEmpresa  extends AppCompatActivity {
    private Button btnCreateJob,btnLogout,btn_edit,btn_delete;
    private  MaterialCardView Jobcard;
    private TextView jobTitle,jobDescription,jobSalary,jobVacancies,jobMode,jobDeadline;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfilempresa);

        btnCreateJob = findViewById(R.id.btnCreateJob);
        btnLogout = findViewById(R.id.btnLogout);
        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);




    }
}
