package com.example.petconnect.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.petconnect.database.DatabaseConection;
import com.example.petconnect.model.Pet;

import java.io.File;
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

    public void carregarTodos(OnPetsLoadedListener listener) {
        try {
            String sql = "SELECT a.*, o.nome AS nome_ong " +
                         "FROM animais a " +
                         "LEFT JOIN ongs o ON a.id_ong = o.id " +
                         "ORDER BY a.nome ASC";
            listener.onSuccess(cursorParaLista(banco.rawQuery(sql, null)));
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }

    public void carregarComFiltros(String tipo, String tamanho, String idade,
                                   OnPetsLoadedListener listener) {
        try {
            StringBuilder sql = new StringBuilder(
                    "SELECT a.*, o.nome AS nome_ong " +
                    "FROM animais a " +
                    "LEFT JOIN ongs o ON a.id_ong = o.id WHERE 1=1");
            List<String> args = new ArrayList<>();

            if (tipo != null && !tipo.isEmpty())    { sql.append(" AND a.especie = ?"); args.add(tipo); }
            if (tamanho != null && !tamanho.isEmpty()) { sql.append(" AND a.porte = ?"); args.add(tamanho); }
            if (idade != null && !idade.isEmpty()) {
                switch (idade) {
                    case "Filhote": sql.append(" AND a.idade <= 1"); break;
                    case "Adulto":  sql.append(" AND a.idade BETWEEN 2 AND 7"); break;
                    case "Idoso":   sql.append(" AND a.idade >= 8"); break;
                }
            }
            sql.append(" ORDER BY a.nome ASC");
            listener.onSuccess(cursorParaLista(
                    banco.rawQuery(sql.toString(), args.toArray(new String[0]))));
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }

    public void carregarPorNome(String busca, OnPetsLoadedListener listener) {
        try {
            String sql = "SELECT a.*, o.nome AS nome_ong " +
                         "FROM animais a " +
                         "LEFT JOIN ongs o ON a.id_ong = o.id " +
                         "WHERE a.nome LIKE ? ORDER BY a.nome ASC";
            listener.onSuccess(cursorParaLista(
                    banco.rawQuery(sql, new String[]{busca + "%"})));
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }

    private List<Pet> cursorParaLista(Cursor cursor) {
        List<Pet> lista = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Pet pet = new Pet();
                pet.setId(       String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id"))));
                pet.setNome(     cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                pet.setRaca(     cursor.getString(cursor.getColumnIndexOrThrow("especie")));
                pet.setTamanho(  cursor.getString(cursor.getColumnIndexOrThrow("porte")));
                pet.setTipo(     cursor.getString(cursor.getColumnIndexOrThrow("especie")));
                pet.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));

                int idadeInt = cursor.getInt(cursor.getColumnIndexOrThrow("idade"));
                pet.setIdade(idadeInt == 1 ? "1 ano" : idadeInt + " anos");

                int colOng = cursor.getColumnIndex("nome_ong");
                pet.setAbrigo(colOng >= 0 ? cursor.getString(colOng) : "");

                // ✅ Foto: funciona tanto com caminho local quanto URL
                int colFoto = cursor.getColumnIndex("foto_url");
                String foto = colFoto >= 0 ? cursor.getString(colFoto) : "";
                if (foto == null) foto = "";

                // Se é caminho local, usa o arquivo; senão usa a string como URL
                if (foto.startsWith("/")) {
                    File arquivo = new File(foto);
                    pet.setFotoUrl(arquivo.exists() ? foto : "");
                } else {
                    pet.setFotoUrl(foto);
                }

                pet.setVacinado(false);
                pet.setCastrado(false);
                lista.add(pet);
            }
        } finally {
            cursor.close();
        }
        return lista;
    }

    public long inserir(Pet pet) {
        return banco.insertWithOnConflict(DatabaseConection.TABELA_PET, null,
                petParaValues(pet), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public int atualizar(Pet pet) {
        return banco.update(DatabaseConection.TABELA_PET, petParaValues(pet),
                "id = ?", new String[]{pet.getId()});
    }

    public int excluir(String id) {
        return banco.delete(DatabaseConection.TABELA_PET, "id = ?", new String[]{id});
    }

    private ContentValues petParaValues(Pet pet) {
        ContentValues v = new ContentValues();
        v.put("id", pet.getId()); v.put("nome", pet.getNome());
        v.put("raca", pet.getRaca()); v.put("idade", pet.getIdade());
        v.put("tamanho", pet.getTamanho()); v.put("tipo", pet.getTipo());
        v.put("vacinado", pet.isVacinado() ? 1 : 0);
        v.put("castrado", pet.isCastrado() ? 1 : 0);
        v.put("foto_url", pet.getFotoUrl());
        v.put("abrigo", pet.getAbrigo()); v.put("descricao", pet.getDescricao());
        return v;
    }
}
