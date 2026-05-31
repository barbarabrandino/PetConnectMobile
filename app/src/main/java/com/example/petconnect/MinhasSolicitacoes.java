package com.example.petconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MinhasSolicitacoes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_solicitacoes);

        setupBotoes();
        setupBottomNav();
    }

    private void setupBotoes() {
        View btnVerDetalhes = findViewById(R.id.btnVerDetalhes);
        if (btnVerDetalhes != null) {
            btnVerDetalhes.setOnClickListener(v ->
                    Toast.makeText(this, "Detalhes: Thor", Toast.LENGTH_SHORT).show());
        }

        View btnVerDetalhes2 = findViewById(R.id.btnVerDetalhes2);
        if (btnVerDetalhes2 != null) {
            btnVerDetalhes2.setOnClickListener(v ->
                    Toast.makeText(this, "Detalhes: Luna", Toast.LENGTH_SHORT).show());
        }
    }

    private void setupBottomNav() {
        View navInicio = findViewById(R.id.navInicio);
        if (navInicio != null) {
            navInicio.setOnClickListener(v ->
                    startActivity(new Intent(this, TelaHome.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));
        }

        View navFavoritos = findViewById(R.id.navFavoritos);
        if (navFavoritos != null) {
            navFavoritos.setOnClickListener(v ->
                    startActivity(new Intent(this, Favoritos.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));
        }

        View navSolicitacoes = findViewById(R.id.navSolicitacoes);
        if (navSolicitacoes != null) {
            navSolicitacoes.setOnClickListener(v -> {});
        }

        View navConfiguracoes = findViewById(R.id.navConfiguracoes);
        if (navConfiguracoes != null) {
            navConfiguracoes.setOnClickListener(v ->
                    startActivity(new Intent(this, Configuracoes.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));
        }

        setNavAtivo(R.id.navSolicitacoes);
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
