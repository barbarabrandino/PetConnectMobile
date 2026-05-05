package com.example.petconnect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConection extends SQLiteOpenHelper {

    private static final String DB_NAME = "petconnect.db";
    private static final int DB_VERSION = 3;

    public static final String TABELA_USUARIO = "usuarios";
    public static final String TABELA_ONG = "ongs";

    public DatabaseConection(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlUsuario = "CREATE TABLE " + TABELA_USUARIO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "cpf TEXT, " +
                "email TEXT UNIQUE, " +
                "senha TEXT, " +
                "cep TEXT, " +
                "estado TEXT, " +
                "cidade TEXT, " +
                "endereco TEXT" +
                ")";

        String sqlOng = "CREATE TABLE " + TABELA_ONG + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "cnpj TEXT, " +
                "email TEXT UNIQUE, " +
                "senha TEXT, " +
                "cep TEXT, " +
                "estado TEXT, " +
                "cidade TEXT, " +
                "endereco TEXT" +
                ")";

        db.execSQL(sqlUsuario);
        db.execSQL(sqlOng);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ONG);
        onCreate(db);
    }
}