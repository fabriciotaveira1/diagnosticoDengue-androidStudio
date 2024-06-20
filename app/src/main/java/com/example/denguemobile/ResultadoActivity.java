package com.example.denguemobile;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

public class ResultadoActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        dbHelper = new DatabaseHelper(this);

        LinearLayout pacientesContainer = findViewById(R.id.pacientesContainer);
        TextView totalPacientesRegiao = findViewById(R.id.totalPacientesRegiao);
        TextView totalPacientesDengue = findViewById(R.id.totalPacientesDengue);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        // Exibir total de pacientes por região
        String pacientesPorRegiao = dbHelper.getPacientesPorRegiao();
        totalPacientesRegiao.setText(pacientesPorRegiao);

        // Exibir total de pacientes com dengue
        int totalDengue = dbHelper.getTotalPacientesComDengue();
        totalPacientesDengue.setText("Total de Pacientes com Dengue: " + totalDengue);

        // Obter e exibir todos os pacientes
        Cursor cursor = dbHelper.getAllPacientes();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int numero = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMERO));
                String nome = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOME));
                String telefone = String.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TELEFONE)));
                int idade = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_IDADE));
                String regiao = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_REGIAO));
                String sintomas = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SINTOMAS));
                int possuiDengue = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_POSSUI_DENGUE));

                Paciente paciente = new Paciente(numero, nome, telefone, idade, regiao, sintomas);

                String pacienteInfo = "Nome: " + paciente.getNome() + "\n" +
                        "Telefone: " + paciente.getTelefone() + "\n" +
                        "Idade: " + paciente.getIdade() + "\n" +
                        "Região: " + paciente.getRegiao() + "\n" +
                        "Sintomas: " + paciente.getSintomas() + "\n" +
                        "Possui Dengue: " + (possuiDengue == 1 ? "Sim" : "Não");

                TextView pacienteView = new TextView(this);
                pacienteView.setText(pacienteInfo);
                pacienteView.setPadding(16, 16, 16, 16);

                pacientesContainer.addView(pacienteView);
            } while (cursor.moveToNext());
            cursor.close();
        }

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Encerra a atividade atual e volta para a anterior
            }
        });
    }
}
