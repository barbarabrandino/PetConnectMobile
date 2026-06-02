package com.example.petconnect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConection extends SQLiteOpenHelper {

    private static final String NOME_BANCO   = "petconnect.db";
    private static final int    VERSAO_BANCO = 8; // ← incrementado para criar tabela solicitacoes

    public static final String TABELA_ANIMAL       = "animais";
    public static final String TABELA_FAVORITOS    = "favoritos";
    public static final String TABELA_USUARIO      = "usuarios";
    public static final String TABELA_PET          = "pets";
    public static final String TABELA_ONG          = "ongs";
    public static final String TABELA_SOLICITACOES = "solicitacoes";

    public DatabaseConection(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELA_USUARIO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "cpf TEXT," +
                "email TEXT UNIQUE," +
                "senha TEXT," +
                "cep TEXT," +
                "estado TEXT," +
                "cidade TEXT," +
                "endereco TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELA_ONG + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "cnpj TEXT," +
                "email TEXT UNIQUE," +
                "senha TEXT," +
                "cep TEXT," +
                "estado TEXT," +
                "cidade TEXT," +
                "endereco TEXT," +
                "telefone TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELA_ANIMAL + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "especie TEXT," +
                "idade INTEGER," +
                "porte TEXT," +
                "sexo TEXT," +
                "descricao TEXT," +
                "foto_url TEXT," +
                "id_ong INTEGER)");

        // Tabela "pets" — usada pelo DashboardOng / PetRepository
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELA_PET + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "especie TEXT," +
                "idade INTEGER," +
                "porte TEXT," +
                "sexo TEXT," +
                "descricao TEXT," +
                "foto_url TEXT," +
                "id_ong INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELA_FAVORITOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER NOT NULL," +
                "id_animal TEXT NOT NULL," +
                "UNIQUE(id_usuario, id_animal))");

        // ✅ Nova tabela de solicitações de adoção
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELA_SOLICITACOES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER NOT NULL," +
                "id_animal TEXT NOT NULL," +
                "status TEXT NOT NULL DEFAULT 'Em análise'," +
                "data TEXT," +
                "UNIQUE(id_usuario, id_animal))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Adiciona tabela de solicitações se não existir (migração segura)
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELA_SOLICITACOES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER NOT NULL," +
                "id_animal TEXT NOT NULL," +
                "status TEXT NOT NULL DEFAULT 'Em análise'," +
                "data TEXT," +
                "UNIQUE(id_usuario, id_animal))");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
