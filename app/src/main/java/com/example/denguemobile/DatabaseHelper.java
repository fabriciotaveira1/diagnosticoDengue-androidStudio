package com.example.denguemobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dengueMobile.db";
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

    public void addPaciente(String nome, String telefone, int idade, String regiao, String sintomas, int possuiDengue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_TELEFONE, telefone);
        values.put(COLUMN_IDADE, idade);
        values.put(COLUMN_REGIAO, regiao);
        values.put(COLUMN_SINTOMAS, sintomas);
        values.put(COLUMN_POSSUI_DENGUE, possuiDengue);

        db.insert(TABLE_PACIENTES, null, values);
        db.close();
    }

    public int contarPacientesComDengue() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_PACIENTES +
                " WHERE " + COLUMN_POSSUI_DENGUE + " = 1";

        Cursor cursor = db.rawQuery(query, null);
        int count = 0;

        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        db.close();
        return count;
    }

    public Map<String, Integer> contarPacientesComDenguePorRegiao() {
        SQLiteDatabase db = this.getReadableDatabase();
        Map<String, Integer> pacientesPorRegiao = new HashMap<>();

        String query = "SELECT " + COLUMN_REGIAO + ", COUNT(*) FROM " + TABLE_PACIENTES +
                " WHERE " + COLUMN_POSSUI_DENGUE + " = 1" +
                " GROUP BY " + COLUMN_REGIAO;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String regiao = cursor.getString(0);
                int count = cursor.getInt(1);
                pacientesPorRegiao.put(regiao, count);
            }
            cursor.close();
        }

        db.close();
        return pacientesPorRegiao;
    }
}
