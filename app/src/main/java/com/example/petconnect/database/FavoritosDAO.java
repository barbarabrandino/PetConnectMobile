package com.example.petconnect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoritosDAO {

    private final SQLiteDatabase banco;

    public FavoritosDAO(Context context) {
        banco = new DatabaseConection(context).getWritableDatabase();
    }


    public void adicionar(int idUsuario, String idAnimal) {
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


    public void remover(int idUsuario, String idAnimal) {
        banco.delete(
                DatabaseConection.TABELA_FAVORITOS,
                "id_usuario = ? AND id_animal = ?",
                new String[]{ String.valueOf(idUsuario), idAnimal }
        );
    }


    public boolean isFavorito(int idUsuario, String idAnimal) {
        Cursor cursor = banco.rawQuery(
                "SELECT 1 FROM " + DatabaseConection.TABELA_FAVORITOS +
                        " WHERE id_usuario = ? AND id_animal = ?",
                new String[]{ String.valueOf(idUsuario), idAnimal }
        );
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }


    public List<String> listarIdsFavoritos(int idUsuario) {
        List<String> ids = new ArrayList<>();
        Cursor cursor = banco.rawQuery(
                "SELECT id_animal FROM " + DatabaseConection.TABELA_FAVORITOS +
                        " WHERE id_usuario = ?",
                new String[]{ String.valueOf(idUsuario) }
        );
        while (cursor.moveToNext()) {
            ids.add(cursor.getString(0));
        }
        cursor.close();
        return ids;
    }
}