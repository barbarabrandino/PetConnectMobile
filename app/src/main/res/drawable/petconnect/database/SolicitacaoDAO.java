package com.example.petconnect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SolicitacaoDAO {

    private final SQLiteDatabase banco;

    public SolicitacaoDAO(Context context) {
        banco = new DatabaseConection(context).getWritableDatabase();
    }

    /** Insere uma nova solicitação. Retorna true se inseriu com sucesso. */
    public boolean inserir(int idUsuario, String idAnimal) {
        Log.d("SOLICITACAO", "Inserindo: idUsuario=" + idUsuario + " idAnimal=" + idAnimal);

        String dataHoje = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date());
        ContentValues values = new ContentValues();
        values.put("id_usuario", idUsuario);
        values.put("id_animal",  idAnimal);
        values.put("status",     "Em análise");
        values.put("data",       dataHoje);

        long resultado = banco.insertWithOnConflict(
                DatabaseConection.TABELA_SOLICITACOES, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);

        Log.d("SOLICITACAO", "Resultado insert: " + resultado);
        return resultado != -1;
    }

    /** Verifica se o usuário já enviou solicitação para este animal. */
    public boolean jaSolicitou(int idUsuario, String idAnimal) {
        Cursor cursor = banco.rawQuery(
                "SELECT id FROM " + DatabaseConection.TABELA_SOLICITACOES +
                        " WHERE id_usuario = ? AND id_animal = ?",
                new String[]{String.valueOf(idUsuario), idAnimal});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    /** Carrega todas as solicitações de um usuário com JOIN para pegar o nome do animal. */
    public List<Solicitacao> listarPorUsuario(int idUsuario) {
        List<Solicitacao> lista = new ArrayList<>();
        String sql =
                "SELECT s.id, s.status, s.data, " +
                        "       COALESCE(p.nome, a.nome) AS nome_animal, " +
                        "       COALESCE(o.nome, '') AS nome_ong " +
                        "FROM " + DatabaseConection.TABELA_SOLICITACOES + " s " +
                        "LEFT JOIN " + DatabaseConection.TABELA_PET + " p ON s.id_animal = CAST(p.id AS TEXT) " +
                        "LEFT JOIN " + DatabaseConection.TABELA_ANIMAL + " a ON s.id_animal = CAST(a.id AS TEXT) " +
                        "LEFT JOIN ongs o ON p.id_ong = o.id OR a.id_ong = o.id " +
                        "WHERE s.id_usuario = ? " +
                        "ORDER BY s.id DESC";
        Cursor cursor = banco.rawQuery(sql, new String[]{String.valueOf(idUsuario)});
        while (cursor.moveToNext()) {
            Solicitacao s = new Solicitacao();
            s.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            s.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
            s.setData(cursor.getString(cursor.getColumnIndexOrThrow("data")));
            s.setNomeAnimal(cursor.getString(cursor.getColumnIndexOrThrow("nome_animal")));
            s.setNomeOng(cursor.getString(cursor.getColumnIndexOrThrow("nome_ong")));
            lista.add(s);
        }
        cursor.close();
        return lista;
    }

    /** Atualiza o status de uma solicitação (usado pelo lado da ONG). */
    public int atualizarStatus(int idSolicitacao, String novoStatus) {
        ContentValues values = new ContentValues();
        values.put("status", novoStatus);
        return banco.update(DatabaseConection.TABELA_SOLICITACOES, values,
                "id = ?", new String[]{String.valueOf(idSolicitacao)});
    }
}