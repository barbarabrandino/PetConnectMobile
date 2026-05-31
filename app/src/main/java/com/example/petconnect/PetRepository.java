package com.example.petconnect.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.petconnect.database.DatabaseConection;
import com.example.petconnect.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetRepository {

    public interface OnPetsLoadedListener {
        void onSuccess(List<Pet> pets);
        void onFailure(Exception e);
    }

    private final SQLiteDatabase banco;

    public PetRepository(Context context) {
        banco = new DatabaseConection(context).getWritableDatabase();
    }

    // ── Inserir / substituir ──────────────────────────────────
    public long inserir(Pet pet) {
        ContentValues values = petParaValues(pet);
        return banco.insertWithOnConflict(
                DatabaseConection.TABELA_PET,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE   // funciona como upsert
        );
    }

    // ── Listar todos (equivalente a observarTodos) ────────────
    public void carregarTodos(OnPetsLoadedListener listener) {
        try {
            List<Pet> pets = cursorParaLista(
                    banco.rawQuery(
                            "SELECT * FROM " + DatabaseConection.TABELA_PET +
                            " ORDER BY nome ASC",
                            null)
            );
            listener.onSuccess(pets);
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }

    // ── Filtros (equivalente a observarComFiltros) ────────────
    public void carregarComFiltros(String tipo, String tamanho, String idade,
                                   OnPetsLoadedListener listener) {
        try {
            StringBuilder sql = new StringBuilder(
                    "SELECT * FROM " + DatabaseConection.TABELA_PET + " WHERE 1=1");
            List<String> args = new ArrayList<>();

            if (tipo != null && !tipo.isEmpty()) {
                sql.append(" AND tipo = ?");
                args.add(tipo);
            }
            if (tamanho != null && !tamanho.isEmpty()) {
                sql.append(" AND tamanho = ?");
                args.add(tamanho);
            }
            if (idade != null && !idade.isEmpty()) {
                sql.append(" AND idade = ?");
                args.add(idade);
            }

            sql.append(" ORDER BY nome ASC");

            List<Pet> pets = cursorParaLista(
                    banco.rawQuery(sql.toString(), args.toArray(new String[0]))
            );
            listener.onSuccess(pets);
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }

    // ── Busca por nome (equivalente a observarPorNome) ────────
    public void carregarPorNome(String busca, OnPetsLoadedListener listener) {
        try {
            List<Pet> pets = cursorParaLista(
                    banco.rawQuery(
                            "SELECT * FROM " + DatabaseConection.TABELA_PET +
                            " WHERE nome LIKE ? ORDER BY nome ASC",
                            new String[]{ busca + "%" })
            );
            listener.onSuccess(pets);
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }

    // ── Atualizar ─────────────────────────────────────────────
    public int atualizar(Pet pet) {
        return banco.update(
                DatabaseConection.TABELA_PET,
                petParaValues(pet),
                "id = ?",
                new String[]{ pet.getId() }
        );
    }

    // ── Excluir ───────────────────────────────────────────────
    public int excluir(String id) {
        return banco.delete(
                DatabaseConection.TABELA_PET,
                "id = ?",
                new String[]{ id }
        );
    }

    // ── Helper: Pet → ContentValues ───────────────────────────
    private ContentValues petParaValues(Pet pet) {
        ContentValues values = new ContentValues();
        values.put("id",        pet.getId());
        values.put("nome",      pet.getNome());
        values.put("raca",      pet.getRaca());
        values.put("idade",     pet.getIdade());
        values.put("tamanho",   pet.getTamanho());
        values.put("tipo",      pet.getTipo());
        values.put("vacinado",  pet.isVacinado()  ? 1 : 0);
        values.put("castrado",  pet.isCastrado()  ? 1 : 0);
        values.put("foto_url",  pet.getFotoUrl());
        values.put("abrigo",    pet.getAbrigo());
        values.put("descricao", pet.getDescricao());
        return values;
    }

    // ── Helper: Cursor → List<Pet> ────────────────────────────
    private List<Pet> cursorParaLista(Cursor cursor) {
        List<Pet> lista = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Pet pet = new Pet();
                pet.setId(       cursor.getString(cursor.getColumnIndexOrThrow("id")));
                pet.setNome(     cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                pet.setRaca(     cursor.getString(cursor.getColumnIndexOrThrow("raca")));
                pet.setIdade(    cursor.getString(cursor.getColumnIndexOrThrow("idade")));
                pet.setTamanho(  cursor.getString(cursor.getColumnIndexOrThrow("tamanho")));
                pet.setTipo(     cursor.getString(cursor.getColumnIndexOrThrow("tipo")));
                pet.setVacinado( cursor.getInt(   cursor.getColumnIndexOrThrow("vacinado")) == 1);
                pet.setCastrado( cursor.getInt(   cursor.getColumnIndexOrThrow("castrado")) == 1);
                pet.setFotoUrl(  cursor.getString(cursor.getColumnIndexOrThrow("foto_url")));
                pet.setAbrigo(   cursor.getString(cursor.getColumnIndexOrThrow("abrigo")));
                pet.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                lista.add(pet);
            }
        } finally {
            cursor.close();
        }
        return lista;
    }
}
