package com.example.petconnect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.petconnect.model.Ongs;

public class OngDAO {

    private SQLiteDatabase db;
    private DatabaseConection con;

    public OngDAO(Context context) {
        con = new DatabaseConection(context);
    }

    // INSERT
    public boolean inserir(Ongs ong) {

        db = con.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", ong.getNome());
        values.put("cnpj", ong.getCnpj());
        values.put("telefone", ong.getEmail());
        values.put("email", ong.getEmail());
        values.put("senha", ong.getSenha());
        values.put("cep", ong.getCep());
        values.put("estado", ong.getEstado());
        values.put("cidade", ong.getCidade());
        values.put("endereco", ong.getEndereco());

        long resultado = db.insert(DatabaseConection.TABELA_ONG, null, values);

        db.close();

        return resultado != -1;
    }

    // LOGIN
    public boolean login(String email, String senha) {

        db = con.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseConection.TABELA_ONG +
                        " WHERE email=? AND senha=?",
                new String[]{email, senha}
        );

        boolean existe = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return existe;
    }

    // BUSCAR ONG COMPLETA
    public Ongs buscarOng(String email, String senha) {

        db = con.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseConection.TABELA_ONG +
                        " WHERE email=? AND senha=?",
                new String[]{email, senha}
        );

        if (cursor.moveToFirst()) {

            Ongs ong = new Ongs();

            ong.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            ong.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            ong.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
            ong.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
            ong.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            ong.setSenha(cursor.getString(cursor.getColumnIndexOrThrow("senha")));
            ong.setCep(cursor.getString(cursor.getColumnIndexOrThrow("cep")));
            ong.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
            ong.setCidade(cursor.getString(cursor.getColumnIndexOrThrow("cidade")));
            ong.setEndereco(cursor.getString(cursor.getColumnIndexOrThrow("endereco")));

            cursor.close();
            db.close();

            return ong;
        }

        cursor.close();
        db.close();

        return null;
    }
}