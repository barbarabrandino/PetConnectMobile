package com.example.petconnect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.petconnect.model.Animal;

import java.util.ArrayList;
import java.util.List;

public class AnimalDAO {

    private SQLiteDatabase banco;
    private DatabaseConection conexao;

    public AnimalDAO(Context context) {
        conexao = new DatabaseConection(context);
        banco   = conexao.getWritableDatabase();
    }

    public long inserir(Animal animal) {
        ContentValues values = new ContentValues();
        values.put("nome",      animal.getNome());
        values.put("especie",   animal.getEspecie());
        values.put("idade",     animal.getIdade());
        values.put("porte",     animal.getPorte());
        values.put("sexo",      animal.getSexo());
        values.put("descricao", animal.getDescricao());
        values.put("foto_url",  animal.getFotoUrl()); // ← novo
        values.put("id_ong",    animal.getIdOng());
        return banco.insert(DatabaseConection.TABELA_ANIMAL, null, values);
    }

    public List<Animal> listar() {
        List<Animal> lista = new ArrayList<>();
        Cursor cursor = banco.rawQuery(
                "SELECT * FROM " + DatabaseConection.TABELA_ANIMAL, null);
        while (cursor.moveToNext()) {
            Animal animal = new Animal();
            animal.setId(       cursor.getInt(   cursor.getColumnIndexOrThrow("id")));
            animal.setNome(     cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            animal.setEspecie(  cursor.getString(cursor.getColumnIndexOrThrow("especie")));
            animal.setIdade(    cursor.getInt(   cursor.getColumnIndexOrThrow("idade")));
            animal.setPorte(    cursor.getString(cursor.getColumnIndexOrThrow("porte")));
            animal.setSexo(     cursor.getString(cursor.getColumnIndexOrThrow("sexo")));
            animal.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            animal.setFotoUrl(  cursor.getString(cursor.getColumnIndexOrThrow("foto_url"))); // ← novo
            animal.setIdOng(    cursor.getInt(   cursor.getColumnIndexOrThrow("id_ong")));
            lista.add(animal);
        }
        cursor.close();
        return lista;
    }

    public int atualizar(Animal animal) {
        ContentValues values = new ContentValues();
        values.put("nome",      animal.getNome());
        values.put("especie",   animal.getEspecie());
        values.put("idade",     animal.getIdade());
        values.put("porte",     animal.getPorte());
        values.put("sexo",      animal.getSexo());
        values.put("descricao", animal.getDescricao());
        values.put("foto_url",  animal.getFotoUrl()); // ← novo
        return banco.update(DatabaseConection.TABELA_ANIMAL, values,
                "id = ?", new String[]{String.valueOf(animal.getId())});
    }

    public int excluir(int id) {
        return banco.delete(DatabaseConection.TABELA_ANIMAL,
                "id = ?", new String[]{String.valueOf(id)});
    }
}
