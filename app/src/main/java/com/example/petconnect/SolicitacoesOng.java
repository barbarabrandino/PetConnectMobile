package com.example.petconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petconnect.database.DatabaseConection;
import com.example.petconnect.model.Solicitacao;

import java.util.ArrayList;
import java.util.List;

public class SolicitacoesOng extends AppCompatActivity {

    private RecyclerView rvSolicitacoes;
    private TextView     tvVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacoes_ong);

        rvSolicitacoes = findViewById(R.id.rvSolicitacoes);
        tvVazio        = findViewById(R.id.tvVazio);
        rvSolicitacoes.setLayoutManager(new LinearLayoutManager(this));

        carregarSolicitacoes();
        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarSolicitacoes();
        setNavAtivo(R.id.navSolicitacoes);
    }

    private void carregarSolicitacoes() {
        SharedPreferences prefs = getSharedPreferences("petconnect_prefs", MODE_PRIVATE);
        boolean isOng    = "ong".equals(prefs.getString("tipo_usuario", ""));
        int     idOng    = prefs.getInt("id_ong_logada",    -1);
        int     idUsuario= prefs.getInt("id_usuario_logado", -1);

        DatabaseConection con = new DatabaseConection(this);
        SQLiteDatabase db = con.getReadableDatabase();

        String sql;

        if (isOng) {
            if (idOng == -1) { mostrarVazio(); return; }

            sql = "SELECT s.id, s.status, s.data, " +
                    "       COALESCE(a.nome, p.nome, 'Animal')    AS nome_animal, " +
                    "       COALESCE(u.nome, u.email, 'Usuário')  AS nome_usuario, " +
                    "       COALESCE(u.email,    '')              AS email_usuario, " +
                    "       COALESCE(u.cpf,      '')              AS cpf_usuario, " +
                    "       COALESCE(u.endereco, '')              AS endereco_usuario, " +
                    "       COALESCE(u.cep,      '')              AS cep_usuario, " +
                    "       COALESCE(u.estado,   '')              AS estado_usuario, " +
                    "       COALESCE(u.cidade,   '')              AS cidade_usuario, " +
                    // ← campos do formulário de adoção
                    "       COALESCE(s.nome_solicitante, '')      AS nome_solicitante, " +
                    "       COALESCE(s.telefone,         '')      AS telefone, " +
                    "       COALESCE(s.moradia,          '')      AS moradia, " +
                    "       COALESCE(s.outros_animais,   '')      AS outros_animais, " +
                    "       COALESCE(s.experiencia,      '')      AS experiencia, " +
                    "       COALESCE(s.observacoes,      '')      AS observacoes " +
                    "FROM "      + DatabaseConection.TABELA_SOLICITACOES + " s " +
                    "LEFT JOIN " + DatabaseConection.TABELA_ANIMAL + " a " +
                    "       ON s.id_animal = CAST(a.id AS TEXT) AND a.id_ong = " + idOng +
                    " LEFT JOIN " + DatabaseConection.TABELA_PET + " p " +
                    "       ON s.id_animal = CAST(p.id AS TEXT) AND p.id_ong = " + idOng +
                    " LEFT JOIN " + DatabaseConection.TABELA_USUARIO + " u ON s.id_usuario = u.id " +
                    "WHERE a.id_ong = " + idOng + " OR p.id_ong = " + idOng +
                    " ORDER BY s.id DESC";
        } else {
            if (idUsuario == -1) { mostrarVazio(); return; }

            sql = "SELECT s.id, s.status, s.data, " +
                    "       COALESCE(a.nome, p.nome, 'Animal')    AS nome_animal, " +
                    "       COALESCE(u.nome, u.email, 'Usuário')  AS nome_usuario, " +
                    "       COALESCE(u.email,    '')              AS email_usuario, " +
                    "       COALESCE(u.cpf,      '')              AS cpf_usuario, " +
                    "       COALESCE(u.endereco, '')              AS endereco_usuario, " +
                    "       COALESCE(u.cep,      '')              AS cep_usuario, " +
                    "       COALESCE(u.estado,   '')              AS estado_usuario, " +
                    "       COALESCE(u.cidade,   '')              AS cidade_usuario, " +
                    "       COALESCE(s.nome_solicitante, '')      AS nome_solicitante, " +
                    "       COALESCE(s.telefone,         '')      AS telefone, " +
                    "       COALESCE(s.moradia,          '')      AS moradia, " +
                    "       COALESCE(s.outros_animais,   '')      AS outros_animais, " +
                    "       COALESCE(s.experiencia,      '')      AS experiencia, " +
                    "       COALESCE(s.observacoes,      '')      AS observacoes " +
                    "FROM "      + DatabaseConection.TABELA_SOLICITACOES + " s " +
                    "LEFT JOIN " + DatabaseConection.TABELA_ANIMAL + " a ON s.id_animal = CAST(a.id AS TEXT) " +
                    "LEFT JOIN " + DatabaseConection.TABELA_PET    + " p ON s.id_animal = CAST(p.id AS TEXT) " +
                    "LEFT JOIN " + DatabaseConection.TABELA_USUARIO + " u ON s.id_usuario = u.id " +
                    "WHERE s.id_usuario = " + idUsuario +
                    " ORDER BY s.id DESC";
        }

        List<Solicitacao> lista = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            Solicitacao s = new Solicitacao();
            s.setId             (cursor.getInt   (cursor.getColumnIndexOrThrow("id")));
            s.setStatus         (cursor.getString(cursor.getColumnIndexOrThrow("status")));
            s.setData           (cursor.getString(cursor.getColumnIndexOrThrow("data")));
            s.setNomeAnimal     (cursor.getString(cursor.getColumnIndexOrThrow("nome_animal")));
            s.setNomeOng        (cursor.getString(cursor.getColumnIndexOrThrow("nome_usuario")));
            s.setEmailUsuario   (cursor.getString(cursor.getColumnIndexOrThrow("email_usuario")));
            s.setCpfUsuario     (cursor.getString(cursor.getColumnIndexOrThrow("cpf_usuario")));
            s.setEnderecoUsuario(cursor.getString(cursor.getColumnIndexOrThrow("endereco_usuario")));
            s.setCepUsuario     (cursor.getString(cursor.getColumnIndexOrThrow("cep_usuario")));
            s.setEstadoUsuario  (cursor.getString(cursor.getColumnIndexOrThrow("estado_usuario")));
            s.setCidadeUsuario  (cursor.getString(cursor.getColumnIndexOrThrow("cidade_usuario")));
            // ← campos do formulário
            s.setNomeSolicitante(cursor.getString(cursor.getColumnIndexOrThrow("nome_solicitante")));
            s.setTelefone       (cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
            s.setMoradia        (cursor.getString(cursor.getColumnIndexOrThrow("moradia")));
            s.setOutrosAnimais  (cursor.getString(cursor.getColumnIndexOrThrow("outros_animais")));
            s.setExperiencia    (cursor.getString(cursor.getColumnIndexOrThrow("experiencia")));
            s.setObservacoes    (cursor.getString(cursor.getColumnIndexOrThrow("observacoes")));
            lista.add(s);
        }
        cursor.close();
        db.close();

        if (lista.isEmpty()) {
            mostrarVazio();
        } else {
            tvVazio.setVisibility(View.GONE);
            rvSolicitacoes.setVisibility(View.VISIBLE);
            rvSolicitacoes.setAdapter(new SolicitacaoOngAdapter(this, lista, isOng));
        }
    }

    private void mostrarVazio() {
        tvVazio.setVisibility(View.VISIBLE);
        rvSolicitacoes.setVisibility(View.GONE);
    }

    private void setupBottomNav() {
        setNavAtivo(R.id.navSolicitacoes);
        findViewById(R.id.navInicio).setOnClickListener(v ->
                startActivity(new Intent(this, DashboardOng.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));
        findViewById(R.id.navSolicitacoes).setOnClickListener(v -> {});
        findViewById(R.id.navConfiguracoes).setOnClickListener(v ->
                startActivity(new Intent(this, Configuracoes.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));
    }

    private void setNavAtivo(int idAtivo) {
        int[] navIds = { R.id.navInicio, R.id.navSolicitacoes, R.id.navConfiguracoes };
        for (int id : navIds) {
            View item = findViewById(id);
            if (item != null) item.setSelected(id == idAtivo);
        }
    }
}