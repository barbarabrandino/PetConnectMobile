package com.example.petconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Solicitacoes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacoes);

        setupBotoes();
        setupBottomNav();
    }

    private void setupBotoes() {
        findViewById(R.id.btnVerDetalhes).setOnClickListener(v ->
                Toast.makeText(this, "Detalhes: Thor", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnVerDetalhes2).setOnClickListener(v ->
                Toast.makeText(this, "Detalhes: Luna", Toast.LENGTH_SHORT).show());
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
