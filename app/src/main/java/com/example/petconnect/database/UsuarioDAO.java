
package com.example.petconnect.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.petconnect.model.Usuario;

public class UsuarioDAO {

    private SQLiteDatabase db;
    private DatabaseConection con;

    public UsuarioDAO(Context context) {
        con = new DatabaseConection(context);
    }

    // 🔹 INSERT
    public boolean inserir(Usuario usuario) {

        db = con.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("cpf", usuario.getCpf());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());
        values.put("cep", usuario.getCep());
        values.put("estado", usuario.getEstado());
        values.put("cidade", usuario.getCidade());
        values.put("endereco", usuario.getEndereco());

        long resultado = db.insert(DatabaseConection.TABELA_USUARIO, null, values);

        return resultado != -1;
    }

    // 🔐 LOGIN (igual ao seu padrão)
    public boolean login(String email, String senha) {

        db = con.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseConection.TABELA_USUARIO +
                        " WHERE email=? AND senha=?",
                new String[]{email, senha}
        );

        boolean existe = cursor.getCount() > 0;
        cursor.close();

        return existe;
    }

    public Usuario buscarUsuario(String email, String senha) {

        db = con.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseConection.TABELA_USUARIO +
                        " WHERE email=? AND senha=?",
                new String[]{email, senha}
        );

        if (cursor.moveToFirst()) {

            Usuario usuario = new Usuario();

            usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            usuario.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            usuario.setCpf(cursor.getString(cursor.getColumnIndexOrThrow("cpf")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            usuario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow("senha")));
            usuario.setCep(cursor.getString(cursor.getColumnIndexOrThrow("cep")));
            usuario.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
            usuario.setCidade(cursor.getString(cursor.getColumnIndexOrThrow("cidade")));
            usuario.setEndereco(cursor.getString(cursor.getColumnIndexOrThrow("endereco")));

            cursor.close();
            return usuario;
        }

        cursor.close();
        return null;
    }
}