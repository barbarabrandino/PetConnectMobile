package com.example.petconnect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.petconnect.database.DatabaseConection;
import com.example.petconnect.model.Animal;

import java.util.ArrayList;
import java.util.List;

public class AnimalDAO {

    private SQLiteDatabase banco;
    private DatabaseConection conexao;

    public AnimalDAO(Context context) {
        conexao = new DatabaseConection(context);
        banco = conexao.getWritableDatabase();
    }

    // Inserir o animal
    public long inserir(Animal animal) {

        ContentValues values = new ContentValues();

        values.put("nome", animal.getNome());
        values.put("especie", animal.getEspecie());
        values.put("idade", animal.getIdade());
        values.put("porte", animal.getPorte());
        values.put("sexo", animal.getSexo());
        values.put("descricao", animal.getDescricao());
        values.put("id_ong", animal.getIdOng());

        return banco.insert(DatabaseConection.TABELA_ANIMAL,
                null, values);
    }

    // Listar o animal
    public List<Animal> listar() {

        List<Animal> lista = new ArrayList<>();

        Cursor cursor = banco.rawQuery(
                "SELECT * FROM " +
                        DatabaseConection.TABELA_ANIMAL,
                null
        );

        while (cursor.moveToNext()) {

            Animal animal = new Animal();

            animal.setId(
                    cursor.getInt(0)
            );

            animal.setNome(
                    cursor.getString(1)
            );

            animal.setEspecie(
                    cursor.getString(2)
            );

            animal.setIdade(
                    cursor.getInt(3)
            );

            animal.setPorte(
                    cursor.getString(4)
            );

            animal.setSexo(
                    cursor.getString(5)
            );

            animal.setDescricao(
                    cursor.getString(6)
            );

            animal.setIdOng(
                    cursor.getInt(7)
            );

            lista.add(animal);
        }

        cursor.close();

        return lista;
    }

    // Atualizar o animal
    public int atualizar(Animal animal) {

        ContentValues values = new ContentValues();

        values.put("nome", animal.getNome());
        values.put("especie", animal.getEspecie());
        values.put("idade", animal.getIdade());
        values.put("porte", animal.getPorte());
        values.put("sexo", animal.getSexo());
        values.put("descricao", animal.getDescricao());

        return banco.update(
                DatabaseConection.TABELA_ANIMAL,
                values,
                "id = ?",
                new String[]{
                        String.valueOf(animal.getId())
                }
        );
    }

    // Excluir o animal
    public int excluir(int id) {

        return banco.delete(
                DatabaseConection.TABELA_ANIMAL,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }
}