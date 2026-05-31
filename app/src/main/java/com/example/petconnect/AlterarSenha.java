package com.example.petconnect;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petconnect.database.DatabaseConection;
import com.google.android.material.textfield.TextInputEditText;

public class AlterarSenha extends AppCompatActivity {

    private TextInputEditText etSenhaAtual, etNovaSenha, etConfirmarSenha;
    private DatabaseConection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        db = new DatabaseConection(this);

        etSenhaAtual     = findViewById(R.id.etSenhaAtual);
        etNovaSenha      = findViewById(R.id.etNovaSenha);
        etConfirmarSenha = findViewById(R.id.etConfirmarSenha);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        findViewById(R.id.btnSalvarSenha).setOnClickListener(v -> alterarSenha());
    }

    private void alterarSenha() {
        String senhaAtual     = etSenhaAtual.getText().toString().trim();
        String novaSenha      = etNovaSenha.getText().toString().trim();
        String confirmarSenha = etConfirmarSenha.getText().toString().trim();

        // Validações
        if (senhaAtual.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (novaSenha.length() < 6) {
            Toast.makeText(this, "A nova senha deve ter pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!novaSenha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recupera e-mail do usuário logado via SharedPreferences
        SharedPreferences prefs = getSharedPreferences("petconnect_prefs", MODE_PRIVATE);
        String emailLogado = prefs.getString("email_logado", null);

        if (emailLogado == null) {
            Toast.makeText(this, "Sessão expirada. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase sqlDb = db.getWritableDatabase();

        // Verifica se a senha atual está correta
        Cursor cursor = sqlDb.query(
                DatabaseConection.TABELA_USUARIO,
                new String[]{"id"},
                "email = ? AND senha = ?",
                new String[]{emailLogado, senhaAtual},
                null, null, null
        );

        if (cursor.getCount() == 0) {
            cursor.close();
            Toast.makeText(this, "Senha atual incorreta.", Toast.LENGTH_SHORT).show();
            return;
        }
        cursor.close();

        // Atualiza a senha
        ContentValues values = new ContentValues();
        values.put("senha", novaSenha);
        int rows = sqlDb.update(
                DatabaseConection.TABELA_USUARIO,
                values,
                "email = ?",
                new String[]{emailLogado}
        );

        if (rows > 0) {
            Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao alterar senha. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }
}
