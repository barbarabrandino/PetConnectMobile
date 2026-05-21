package com.example.petconnect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConection extends SQLiteOpenHelper {

    private static final String DB_NAME = "petconnect.db";
    private static final int DB_VERSION = 4;

    //tabelas do banco
    public static final String TABELA_USUARIO = "usuarios";
    public static final String TABELA_ONG = "ongs";

    public static final String TABELA_ANIMAL = "animais";

    public DatabaseConection(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //tabela usuario
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

        //tabela ONGS
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

        //Tabela animais
        String sqlAnimal = "CREATE TABLE " + TABELA_ANIMAL + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "especie TEXT, " +
                "idade INTEGER, " +
                "porte TEXT, " +
                "sexo TEXT, " +
                "descricao TEXT, " +
                "id_ong INTEGER" +
                ")";

        db.execSQL(sqlUsuario);
        db.execSQL(sqlOng);
        db.execSQL(sqlAnimal);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ANIMAL);
        onCreate(db);
    }
}