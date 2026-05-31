package com.example.petconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petconnect.database.DatabaseConection;

public class DashboardOng extends AppCompatActivity {

    private TextView txtNumPets, txtNumSolicitacoes, tvNomeOng;
    private Button btnCadastrarPet, btnVerSolicitacoes, btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_ong);

        initViews();
        setupBotoes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza os cards toda vez que voltar ao dashboard
        carregarDados();
    }

    private void initViews() {
        txtNumPets         = findViewById(R.id.txtNumPets);
        txtNumSolicitacoes = findViewById(R.id.txtNumSolicitacoes);
        tvNomeOng          = findViewById(R.id.tvNomeOng);
        btnCadastrarPet    = findViewById(R.id.btnCadastrarPet);
        btnVerSolicitacoes = findViewById(R.id.btnVerSolicitacoes);
        btnSair            = findViewById(R.id.btnSair);
    }

    private void carregarDados() {
        SharedPreferences prefs = getSharedPreferences("petconnect_prefs", MODE_PRIVATE);
        int    idOng    = prefs.getInt("id_ong_logada", -1);
        String nomeOng  = prefs.getString("nome_ong_logada", "Minha ONG");

        if (tvNomeOng != null) {
            tvNomeOng.setText(nomeOng);
        }

        if (idOng == -1) {
            txtNumPets.setText("0");
            txtNumSolicitacoes.setText("0");
            return;
        }

        DatabaseConection con = new DatabaseConection(this);
        SQLiteDatabase db = con.getReadableDatabase();

        // Conta animais cadastrados por esta ONG na tabela "animais"
        Cursor cursorAnimais = db.rawQuery(
            "SELECT COUNT(*) FROM " + DatabaseConection.TABELA_ANIMAL +
            " WHERE id_ong = ?",
            new String[]{ String.valueOf(idOng) }
        );
        if (cursorAnimais.moveToFirst()) {
            txtNumPets.setText(String.valueOf(cursorAnimais.getInt(0)));
        }
        cursorAnimais.close();

        // Conta pets na tabela "pets" (PetRepository)
        // Como não há tabela de solicitações ainda, exibe total de pets cadastrados
        Cursor cursorPets = db.rawQuery(
            "SELECT COUNT(*) FROM " + DatabaseConection.TABELA_PET,
            null
        );
        if (cursorPets.moveToFirst()) {
            txtNumSolicitacoes.setText(String.valueOf(cursorPets.getInt(0)));
        }
        cursorPets.close();

        db.close();
    }

    private void setupBotoes() {
        btnCadastrarPet.setOnClickListener(v ->
            startActivity(new Intent(this, CadastroAnimalActivity.class))
        );

        btnVerSolicitacoes.setOnClickListener(v ->
            startActivity(new Intent(this, Solicitacoes.class))
        );

        btnSair.setOnClickListener(v ->
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
}
