package com.example.denguemobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Aplicar padding para as barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar os botões
        Button btnAdicionarPaciente = findViewById(R.id.btnAdicionarPaciente);
        Button btnVerPacientes = findViewById(R.id.btnVerPacientes);

        btnAdicionarPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirAdicionarPacienteActivity();
            }
        });

        btnVerPacientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirResultadoActivity();
            }
        });
    }

    private void abrirAdicionarPacienteActivity() {
        Intent intent = new Intent(MainActivity.this, AdicionarPacienteActivity.class);
        startActivity(intent);
    }

    private void abrirResultadoActivity() {
        Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
        startActivity(intent);
    }
}
