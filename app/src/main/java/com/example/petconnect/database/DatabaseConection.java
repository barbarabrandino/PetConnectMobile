package com.example.petconnect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConection extends SQLiteOpenHelper {

    private static final String DB_NAME    = "petconnect.db";
    private static final int    DB_VERSION = 8; // incrementado para adicionar tabela favoritos

    public static final String TABELA_USUARIO   = "usuarios";
    public static final String TABELA_ONG       = "ongs";
    public static final String TABELA_ANIMAL    = "animais";
    public static final String TABELA_PET       = "pets";
    public static final String TABELA_FAVORITOS = "favoritos"; // ← nova

    public DatabaseConection(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABELA_USUARIO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, cpf TEXT, email TEXT UNIQUE, senha TEXT, " +
                "cep TEXT, estado TEXT, cidade TEXT, endereco TEXT)");

        db.execSQL("CREATE TABLE " + TABELA_ONG + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, cnpj TEXT, email TEXT UNIQUE, senha TEXT, " +
                "cep TEXT, estado TEXT, cidade TEXT, endereco TEXT)");

        db.execSQL("CREATE TABLE " + TABELA_ANIMAL + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, especie TEXT, idade INTEGER, " +
                "porte TEXT, sexo TEXT, descricao TEXT, " +
                "foto_url TEXT, id_ong INTEGER)");

        db.execSQL("CREATE TABLE " + TABELA_PET + " (" +
                "id TEXT PRIMARY KEY, nome TEXT, raca TEXT, idade TEXT, " +
                "tamanho TEXT, tipo TEXT, vacinado INTEGER, castrado INTEGER, " +
                "foto_url TEXT, abrigo TEXT, descricao TEXT)");

        // ✅ Tabela favoritos: id_usuario + id_animal (chave composta única)
        db.execSQL("CREATE TABLE " + TABELA_FAVORITOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_usuario INTEGER NOT NULL, " +
                "id_animal INTEGER NOT NULL, " +
                "UNIQUE(id_usuario, id_animal))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            try { db.execSQL("ALTER TABLE " + TABELA_ANIMAL + " ADD COLUMN foto_url TEXT"); } catch (Exception ignored) {}
        }
        if (oldVersion < 8) {
            // Adiciona tabela favoritos sem apagar dados existentes
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELA_FAVORITOS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_usuario INTEGER NOT NULL, " +
                    "id_animal INTEGER NOT NULL, " +
                    "UNIQUE(id_usuario, id_animal))");
        }
    }
}