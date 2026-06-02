package com.example.petconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petconnect.adapter.SolicitacaoAdapter;
import com.example.petconnect.database.SolicitacaoDAO;
import com.example.petconnect.model.Solicitacao;

import java.util.List;

public class Solicitacoes extends AppCompatActivity {

    private RecyclerView      rvSolicitacoes;
    private View              tvVazio;
    private SolicitacaoDAO    dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacoes);

        rvSolicitacoes = findViewById(R.id.rvSolicitacoes);
        tvVazio        = findViewById(R.id.tvVazio);
        dao            = new SolicitacaoDAO(this);

        rvSolicitacoes.setLayoutManager(new LinearLayoutManager(this));

        carregarSolicitacoes();
        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega ao voltar de outra tela
        carregarSolicitacoes();
    }

    private void carregarSolicitacoes() {
        int idUsuario = getIdUsuarioLogado();

        List<Solicitacao> lista = dao.listarPorUsuario(idUsuario);

        if (lista.isEmpty()) {
            tvVazio.setVisibility(View.VISIBLE);
            rvSolicitacoes.setVisibility(View.GONE);
        } else {
            tvVazio.setVisibility(View.GONE);
            rvSolicitacoes.setVisibility(View.VISIBLE);
            rvSolicitacoes.setAdapter(new SolicitacaoAdapter(this, lista));
        }
    }

    /**
     * Pega o ID do usuário salvo no SharedPreferences no momento do login.
     * Ajuste a chave "id_usuario" / "prefs_usuario" conforme o seu projeto.
     */
    private int getIdUsuarioLogado() {
        SharedPreferences prefs = getSharedPreferences("prefs_usuario", MODE_PRIVATE);
        return prefs.getInt("id_usuario", -1);
    }

    private void setupBottomNav() {
        setNavAtivo(R.id.navSolicitacoes);

        findViewById(R.id.navInicio).setOnClickListener(v ->
                startActivity(new Intent(this, TelaHome.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));

        findViewById(R.id.navFavoritos).setOnClickListener(v ->
                startActivity(new Intent(this, Favoritos.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));

        findViewById(R.id.navSolicitacoes).setOnClickListener(v -> {});

        findViewById(R.id.navConfiguracoes).setOnClickListener(v ->
                startActivity(new Intent(this, Configuracoes.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));
    }

    private void setNavAtivo(int idAtivo) {
        int[] navIds = {
            R.id.navInicio,
            R.id.navFavoritos,
            R.id.navSolicitacoes,
            R.id.navConfiguracoes
        };
        for (int id : navIds) {
            View item = findViewById(id);
            if (item != null) item.setSelected(id == idAtivo);
        }
    }
}
