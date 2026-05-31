package com.example.petconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.materialswitch.MaterialSwitch;

public class Configuracoes extends AppCompatActivity {

    private MaterialSwitch switchNotificacoes;
    private Button btnAlterarSenha;
    private Button btnTermos;
    private Button btnPrivacidade;
    private Button btnSairConta;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        prefs = getSharedPreferences("petconnect_prefs", MODE_PRIVATE);

        initViews();
        setupSwitches();
        setupBotoes();
        setupBottomNav();
    }

    private void initViews() {
        switchNotificacoes = findViewById(R.id.switchNotificacoes);
        btnAlterarSenha    = findViewById(R.id.btnAlterarSenha);
        btnTermos          = findViewById(R.id.btnTermos);
        btnPrivacidade     = findViewById(R.id.btnPrivacidade);
        btnSairConta       = findViewById(R.id.btnSairConta);

        switchNotificacoes.setChecked(prefs.getBoolean("notificacoes_ativas", true));
    }

    private void setupSwitches() {
        switchNotificacoes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notificacoes_ativas", isChecked).apply();
            Toast.makeText(this,
                    isChecked ? "Notificações ativadas" : "Notificações desativadas",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void setupBotoes() {
        btnAlterarSenha.setOnClickListener(v ->
                startActivity(new Intent(this, AlterarSenha.class))
        );

        btnTermos.setOnClickListener(v ->
                startActivity(new Intent(this, TermosDeUso.class))
        );

        btnPrivacidade.setOnClickListener(v ->
                startActivity(new Intent(this, PoliticaPrivacidade.class))
        );

        btnSairConta.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Sair da conta")
                        .setMessage("Tem certeza que deseja sair?")
                        .setPositiveButton("Sair", (dialog, which) -> {
                            prefs.edit()
                                    .remove("email_logado")
                                    .apply();
                            Intent intent = new Intent(this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancelar", null)
                        .show()
        );
    }

    private void setupBottomNav() {
        findViewById(R.id.navInicio).setOnClickListener(v ->
                startActivity(new Intent(this, TelaHome.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP))
        );

        findViewById(R.id.navFavoritos).setOnClickListener(v ->
                startActivity(new Intent(this, Favoritos.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP))
        );

        findViewById(R.id.navSolicitacoes).setOnClickListener(v ->
                startActivity(new Intent(this, MinhasSolicitacoes.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP))
        );

        setNavAtivo(R.id.navConfiguracoes);
        findViewById(R.id.navConfiguracoes).setOnClickListener(v -> {});
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
