package com.example.petconnect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.petconnect.model.Solicitacao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SolicitacaoDAO {

    private final Context context;

    public SolicitacaoDAO(Context context) {
        this.context = context;
    }

    public boolean inserir(int idUsuario, String idAnimal) {
        return inserirCompleto(idUsuario, idAnimal, "", "", "", "", "", "");
    }

    public boolean inserirCompleto(int idUsuario, String idAnimal,
                                   String nomeSolicitante, String telefone,
                                   String moradia, String outrosAnimais,
                                   String experiencia, String observacoes) {
        try {
            DatabaseConection con = new DatabaseConection(context);
            SQLiteDatabase db = con.getWritableDatabase();

            String data = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date());

            ContentValues cv = new ContentValues();
            cv.put("id_usuario",       idUsuario);
            cv.put("id_animal",        idAnimal);
            cv.put("status",           "Em análise");
            cv.put("data",             data);
            cv.put("nome_solicitante", nomeSolicitante);
            cv.put("telefone",         telefone);
            cv.put("moradia",          moradia);
            cv.put("outros_animais",   outrosAnimais);
            cv.put("experiencia",      experiencia);
            cv.put("observacoes",      observacoes);

            long result = db.insertWithOnConflict(
                    DatabaseConection.TABELA_SOLICITACOES,
                    null, cv,
                    SQLiteDatabase.CONFLICT_IGNORE);
            db.close();
            return result != -1;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean jaSolicitou(int idUsuario, String idAnimal) {
        try {
            DatabaseConection con = new DatabaseConection(context);
            SQLiteDatabase db = con.getReadableDatabase();
            Cursor c = db.rawQuery(
                    "SELECT id FROM " + DatabaseConection.TABELA_SOLICITACOES +
                            " WHERE id_usuario = ? AND id_animal = ?",
                    new String[]{String.valueOf(idUsuario), idAnimal});
            boolean existe = c.moveToFirst();
            c.close();
            db.close();
            return existe;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Solicitacao> listarPorUsuario(int idUsuario) {
        List<Solicitacao> lista = new ArrayList<>();
        try {
            DatabaseConection con = new DatabaseConection(context);
            SQLiteDatabase db = con.getReadableDatabase();

            String sql =
                    "SELECT s.id, s.status, s.data, " +
                            "       COALESCE(a.nome, p.nome, 'Animal') AS nome_animal, " +
                            "       COALESCE(o1.nome, o2.nome, '')     AS nome_ong " +
                            "FROM "      + DatabaseConection.TABELA_SOLICITACOES + " s " +
                            "LEFT JOIN " + DatabaseConection.TABELA_ANIMAL + " a ON s.id_animal = CAST(a.id AS TEXT) " +
                            "LEFT JOIN " + DatabaseConection.TABELA_PET    + " p ON s.id_animal = CAST(p.id AS TEXT) " +
                            "LEFT JOIN " + DatabaseConection.TABELA_ONG    + " o1 ON a.id_ong = o1.id " +
                            "LEFT JOIN " + DatabaseConection.TABELA_ONG    + " o2 ON p.id_ong = o2.id " +
                            "WHERE s.id_usuario = ? ORDER BY s.id DESC";

            Cursor c = db.rawQuery(sql, new String[]{String.valueOf(idUsuario)});
            while (c.moveToNext()) {
                Solicitacao s = new Solicitacao();
                s.setId(        c.getInt(   c.getColumnIndexOrThrow("id")));
                s.setStatus(    c.getString(c.getColumnIndexOrThrow("status")));
                s.setData(      c.getString(c.getColumnIndexOrThrow("data")));
                s.setNomeAnimal(c.getString(c.getColumnIndexOrThrow("nome_animal")));
                s.setNomeOng(   c.getString(c.getColumnIndexOrThrow("nome_ong")));
                lista.add(s);
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}