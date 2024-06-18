package com.example.denguemobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class AdicionarPacienteActivity extends AppCompatActivity {

    private EditText editNome, editTelefone, editIdade, editRegiao, editSintomas;
    private Spinner spinnerSintomas;
    private Button btnConfirmar, btnBack, btnAdicionarSintoma;
    private DatabaseHelper dbHelper;
    private List<String> sintomasSelecionados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_paciente);

        dbHelper = new DatabaseHelper(this);

        editNome = findViewById(R.id.editNome);
        editTelefone = findViewById(R.id.editTelefone);
        editIdade = findViewById(R.id.editIdade);
        editRegiao = findViewById(R.id.editRegiao);
        spinnerSintomas = findViewById(R.id.spinnerSintomas);
        editSintomas = findViewById(R.id.editSintomas);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        btnBack = findViewById(R.id.btnBack);
        btnAdicionarSintoma = findViewById(R.id.btnAdicionarSintoma);
        sintomasSelecionados = new ArrayList<>();

        // Configurar o Spinner de sintomas
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sintomas_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSintomas.setAdapter(adapter);

        btnAdicionarSintoma.setOnClickListener(v -> adicionarSintoma());

        btnConfirmar.setOnClickListener(v -> {
            adicionarPaciente();
            mostrarDiagnosticoEmPopup();
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void adicionarSintoma() {
        String sintomaSelecionado = spinnerSintomas.getSelectedItem().toString();
        if (!sintomaSelecionado.isEmpty() && !sintomasSelecionados.contains(sintomaSelecionado)) {
            sintomasSelecionados.add(sintomaSelecionado);
            editSintomas.setText(sintomasSelecionados.toString().replaceAll("[\\[\\]]", ""));
        }
    }

    private void adicionarPaciente() {
        String nome = editNome.getText().toString();
        String telefone = editTelefone.getText().toString();
        String idadeStr = editIdade.getText().toString();
        String regiao = editRegiao.getText().toString();
        String sintomas = editSintomas.getText().toString();

        if (nome.isEmpty() || telefone.isEmpty() || idadeStr.isEmpty() || regiao.isEmpty() || sintomas.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int idade = Integer.parseInt(idadeStr);

            // Adicionando paciente ao banco de dados
            dbHelper.addPaciente(nome, telefone, idade, regiao, sintomas);

            // Verificar e atualizar status de dengue
            dbHelper.verificarEDefinirDengue(nome);

            Toast.makeText(this, "Paciente adicionado com sucesso", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Idade inválida", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDiagnosticoEmPopup() {
        String nome = editNome.getText().toString();

        boolean possuiDengue = dbHelper.verificarPacientePossuiDengue(nome);
        String diagnostico = possuiDengue ? "Paciente possui dengue" : "Paciente não possui dengue";

        new AlertDialog.Builder(this)
                .setTitle("Diagnóstico")
                .setMessage(diagnostico)
                .setPositiveButton("OK", (dialog, which) -> perguntarAdicionarOutroPaciente())
                .show();
    }

    private void perguntarAdicionarOutroPaciente() {
        new AlertDialog.Builder(this)
                .setTitle("Adicionar Outro Paciente")
                .setMessage("Deseja adicionar outro paciente?")
                .setPositiveButton("Sim", (dialog, which) -> limparCampos())
                .setNegativeButton("Não", (dialog, which) -> irParaResultadoActivity())
                .show();
    }

    private void limparCampos() {
        editNome.setText("");
        editTelefone.setText("");
        editIdade.setText("");
        editRegiao.setText("");
        editSintomas.setText("");
        sintomasSelecionados.clear();
    }

    private void irParaResultadoActivity() {
        Intent intent = new Intent(this, ResultadoActivity.class);
        startActivity(intent);
    }
}
