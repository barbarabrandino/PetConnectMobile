package com.example.petconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petconnect.database.DatabaseConection;

public class DashboardOng extends AppCompatActivity {

    private TextView txtNumPets, txtNumSolicitacoes, tvNomeOng;
    private Button btnCadastrarPet, btnVerSolicitacoes, btnVerAnimais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_ong);

        initViews();
        setupBotoes();
        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarDados();
        setNavAtivo(R.id.navInicio);
    }

    private void initViews() {
        txtNumPets         = findViewById(R.id.txtNumPets);
        txtNumSolicitacoes = findViewById(R.id.txtNumSolicitacoes);
        tvNomeOng          = findViewById(R.id.tvNomeOng);
        btnCadastrarPet    = findViewById(R.id.btnCadastrarPet);
        btnVerSolicitacoes = findViewById(R.id.btnVerSolicitacoes);
        btnVerAnimais      = findViewById(R.id.btnVerAnimais);

        findViewById(R.id.btnSair).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Sair da conta")
                        .setMessage("Tem certeza que deseja sair?")
                        .setPositiveButton("Sair", (dialog, which) -> {
                            getSharedPreferences("petconnect_prefs", MODE_PRIVATE)
                                    .edit()
                                    .remove("email_logado")
                                    .remove("id_ong_logada")
                                    .remove("nome_ong_logada")
                                    .apply();
                            Intent intent = new Intent(this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancelar", null)
                        .show()
        );
    }

    private void carregarDados() {
        SharedPreferences prefs = getSharedPreferences("petconnect_prefs", MODE_PRIVATE);
        int    idOng   = prefs.getInt("id_ong_logada", -1);
        String nomeOng = prefs.getString("nome_ong_logada", "Minha ONG");

        if (tvNomeOng != null) tvNomeOng.setText(nomeOng);

        if (idOng == -1) {
            txtNumPets.setText("0");
            txtNumSolicitacoes.setText("0");
            return;
        }

        DatabaseConection con = new DatabaseConection(this);
        SQLiteDatabase db = con.getReadableDatabase();

        Cursor cursorAnimais = db.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseConection.TABELA_ANIMAL + " WHERE id_ong = ?",
                new String[]{ String.valueOf(idOng) }
        );
        if (cursorAnimais.moveToFirst()) txtNumPets.setText(String.valueOf(cursorAnimais.getInt(0)));
        cursorAnimais.close();

        Cursor cursorSolic = db.rawQuery(
                "SELECT COUNT(DISTINCT s.id) FROM " + DatabaseConection.TABELA_SOLICITACOES + " s " +
                        "LEFT JOIN " + DatabaseConection.TABELA_ANIMAL + " a " +
                        "       ON s.id_animal = CAST(a.id AS TEXT) AND a.id_ong = ? " +
                        "LEFT JOIN " + DatabaseConection.TABELA_PET + " p " +
                        "       ON s.id_animal = CAST(p.id AS TEXT) AND p.id_ong = ? " +
                        "WHERE a.id IS NOT NULL OR p.id IS NOT NULL",
                new String[]{ String.valueOf(idOng), String.valueOf(idOng) }
        );
        if (cursorSolic.moveToFirst()) txtNumSolicitacoes.setText(String.valueOf(cursorSolic.getInt(0)));
        cursorSolic.close();

        db.close();
    }

    private void setupBotoes() {
        btnCadastrarPet.setOnClickListener(v ->
                startActivity(new Intent(this, CadastroAnimalActivity.class))
        );

        btnVerAnimais.setOnClickListener(v ->
                startActivity(new Intent(this, AnimaisCadastradosActivity.class))
        );

        btnVerSolicitacoes.setOnClickListener(v -> {
            setNavAtivo(R.id.navSolicitacoes);
            startActivity(new Intent(this, SolicitacoesOng.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        });
    }

    private void setupBottomNav() {
        findViewById(R.id.navInicio).setOnClickListener(v -> setNavAtivo(R.id.navInicio));

        findViewById(R.id.navSolicitacoes).setOnClickListener(v -> {
            setNavAtivo(R.id.navSolicitacoes);
            startActivity(new Intent(this, SolicitacoesOng.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        });

        findViewById(R.id.navConfiguracoes).setOnClickListener(v -> {
            setNavAtivo(R.id.navConfiguracoes);
            startActivity(new Intent(this, Configuracoes.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        });
    }

    private void setNavAtivo(int idAtivo) {
        int[] navIds = { R.id.navInicio, R.id.navSolicitacoes, R.id.navConfiguracoes };
        for (int id : navIds) {
            View item = findViewById(id);
            if (item != null) item.setSelected(id == idAtivo);
        }
    }
}