package com.example.petconnect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FavoritosDAO {

    private final SQLiteDatabase banco;

    public FavoritosDAO(Context context) {
        banco = new DatabaseConection(context).getWritableDatabase();
    }

    /** Adiciona um animal aos favoritos do usuário. Ignora se já existir. */
    public void adicionar(int idUsuario, int idAnimal) {
        ContentValues values = new ContentValues();
        values.put("id_usuario", idUsuario);
        values.put("id_animal",  idAnimal);
        banco.insertWithOnConflict(
                DatabaseConection.TABELA_FAVORITOS,
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE
        );
    }

    /** Remove um animal dos favoritos do usuário. */
    public void remover(int idUsuario, int idAnimal) {
        banco.delete(
                DatabaseConection.TABELA_FAVORITOS,
                "id_usuario = ? AND id_animal = ?",
                new String[]{ String.valueOf(idUsuario), String.valueOf(idAnimal) }
        );
    }

    /** Verifica se um animal já é favorito do usuário. */
    public boolean isFavorito(int idUsuario, int idAnimal) {
        Cursor cursor = banco.rawQuery(
                "SELECT 1 FROM " + DatabaseConection.TABELA_FAVORITOS +
                        " WHERE id_usuario = ? AND id_animal = ?",
                new String[]{ String.valueOf(idUsuario), String.valueOf(idAnimal) }
        );
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    /** Retorna os IDs de animais favoritos de um usuário. */
    public Cursor listarIdsFavoritos(int idUsuario) {
        return banco.rawQuery(
                "SELECT id_animal FROM " + DatabaseConection.TABELA_FAVORITOS +
                        " WHERE id_usuario = ?",
                new String[]{ String.valueOf(idUsuario) }
        );
    }
}