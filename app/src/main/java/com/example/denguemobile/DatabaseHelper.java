package com.example.denguemobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dengueMobile2.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_PACIENTES = "pacientes";
    public static final String COLUMN_NUMERO = "numero";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_TELEFONE = "telefone";
    public static final String COLUMN_IDADE = "idade";
    public static final String COLUMN_REGIAO = "regiao";
    public static final String COLUMN_SINTOMAS = "sintomas";
    public static final String COLUMN_POSSUI_DENGUE = "possui_dengue";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PACIENTES + " (" +
                    COLUMN_NUMERO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOME + " TEXT, " +
                    COLUMN_TELEFONE + " TEXT, " +
                    COLUMN_IDADE + " INTEGER, " +
                    COLUMN_REGIAO + " TEXT, " +
                    COLUMN_SINTOMAS + " TEXT, " +
                    COLUMN_POSSUI_DENGUE + " INTEGER" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PACIENTES);
        onCreate(db);
    }

    public void addPaciente(String nome, String telefone, int idade, String regiao, String sintomas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_TELEFONE, telefone);
        values.put(COLUMN_IDADE, idade);
        values.put(COLUMN_REGIAO, regiao);
        values.put(COLUMN_SINTOMAS, sintomas);
        values.put(COLUMN_POSSUI_DENGUE, 0); // Inicialmente 0, indicando que não possui dengue

        db.insert(TABLE_PACIENTES, null, values);
        db.close();
    }

    public void verificarEDefinirDengue(String nome) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_SINTOMAS + " FROM " + TABLE_PACIENTES +
                " WHERE " + COLUMN_NOME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{nome});

        if (cursor != null && cursor.moveToFirst()) {
            String sintomas = cursor.getString(0);

            if (possuiSintomasDeDengue(sintomas)) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_POSSUI_DENGUE, 1);

                db.update(TABLE_PACIENTES, values, COLUMN_NOME + " = ?", new String[]{nome});
            }
            cursor.close();
        }

        db.close();
    }

    private boolean possuiSintomasDeDengue(String sintomas) {
        // Lista de sintomas que indicam dengue
        String[] sintomasDengue = {"febre", "dor de cabeça", "dor nas articulações", "náusea", "vômito", "dor atrás dos olhos", "fadiga"};

        int count = 0;
        for (String sintoma : sintomasDengue) {
            if (sintomas.toLowerCase().contains(sintoma)) {
                count++;
            }
        }
        return count >= 2;
    }

    public boolean verificarPacientePossuiDengue(String nome) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_POSSUI_DENGUE + " FROM " + TABLE_PACIENTES +
                " WHERE " + COLUMN_NOME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{nome});
        boolean possuiDengue = false;

        if (cursor != null && cursor.moveToFirst()) {
            possuiDengue = cursor.getInt(0) == 1;
            cursor.close();
        }

        db.close();
        return possuiDengue;
    }

    public Cursor getAllPacientes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PACIENTES, null);
    }

    public String getPacientesPorRegiao() {
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder resultado = new StringBuilder();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_REGIAO + ", COUNT(*) as total FROM " + TABLE_PACIENTES + " GROUP BY " + COLUMN_REGIAO, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String regiao = cursor.getString(cursor.getColumnIndex(COLUMN_REGIAO));
                int total = cursor.getInt(cursor.getColumnIndex("total"));
                resultado.append("Região: ").append(regiao).append(" - Total de Pacientes: ").append(total).append("\n");
            } while (cursor.moveToNext());
            cursor.close();
        }

        return resultado.toString();
    }

    public int getTotalPacientesComDengue() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PACIENTES + " WHERE " + COLUMN_POSSUI_DENGUE + " = 1", null);
        int total = 0;

        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getInt(0);
            cursor.close();
        }

        return total;
    }

}
