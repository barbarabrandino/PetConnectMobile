package com.example.petconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Favoritos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        setupBotoes();
        setupBottomNav();
    }

    private void setupBotoes() {
        Button btnDetalhesFavorito = findViewById(R.id.btnDetalhesFavorito);
        Button btnDetalhesFavorito2 = findViewById(R.id.btnDetalhesFavorito2);

        btnDetalhesFavorito.setOnClickListener(v ->
                Toast.makeText(this, "Ver detalhes: Thor", Toast.LENGTH_SHORT).show());

        btnDetalhesFavorito2.setOnClickListener(v ->
                Toast.makeText(this, "Ver detalhes: Luna", Toast.LENGTH_SHORT).show());
    }

    private void setupBottomNav() {
        setNavAtivo(R.id.navFavoritos);

        findViewById(R.id.navInicio).setOnClickListener(v ->
                startActivity(new Intent(this, TelaHome.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));

        findViewById(R.id.navFavoritos).setOnClickListener(v -> {});

        findViewById(R.id.navSolicitacoes).setOnClickListener(v ->
                startActivity(new Intent(this, MinhasSolicitacoes.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));

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
