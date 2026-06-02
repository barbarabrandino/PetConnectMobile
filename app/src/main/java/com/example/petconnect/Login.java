package com.example.petconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petconnect.database.DatabaseConection;

public class Login extends AppCompatActivity {

    EditText etCpfCnpjLogin, etSenhaLogin;

    Button btnEntrar;
    Button btnCadastroUsuario;
    Button btnCadastroOng;

    DatabaseConection banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        banco = new DatabaseConection(this);

        etCpfCnpjLogin = findViewById(R.id.etCpfCnpjLogin);
        etSenhaLogin   = findViewById(R.id.etSenhaLogin);

        btnEntrar          = findViewById(R.id.btnEntrar);
        btnCadastroUsuario = findViewById(R.id.btnCadastroUsuario);
        btnCadastroOng     = findViewById(R.id.btnCadastroOng);

        btnEntrar.setOnClickListener(view -> {

            String cpfCnpj = etCpfCnpjLogin.getText().toString().trim().replaceAll("[^0-9]", "");
            String senha   = etSenhaLogin.getText().toString().trim();

            if (TextUtils.isEmpty(cpfCnpj)) {
                etCpfCnpjLogin.setError("Digite o CPF ou CNPJ");
                return;
            }
            if (TextUtils.isEmpty(senha)) {
                etSenhaLogin.setError("Digite a senha");
                return;
            }

            SQLiteDatabase db = banco.getReadableDatabase();

            Cursor cursorUsuario = db.rawQuery(
                    "SELECT * FROM usuarios WHERE cpf=? AND senha=?",
                    new String[]{cpfCnpj, senha}
            );

            Cursor cursorOng = db.rawQuery(
                    "SELECT * FROM ongs WHERE cnpj=? AND senha=?",
                    new String[]{cpfCnpj, senha}
            );

            SharedPreferences prefs = getSharedPreferences("petconnect_prefs", MODE_PRIVATE);

            if (cursorUsuario.moveToFirst()) {

                int idUsuario = cursorUsuario.getInt(
                        cursorUsuario.getColumnIndexOrThrow("id")
                );
                String emailLogado = cursorUsuario.getString(
                        cursorUsuario.getColumnIndexOrThrow("email")
                );

                prefs.edit()
                        .putString("email_logado", emailLogado)
                        .putInt("id_usuario_logado", idUsuario)  // ← correção
                        .putInt("id_ong_logada", -1)
                        .apply();

                Toast.makeText(Login.this, "Login de usuário realizado!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, TelaHome.class));
                finish();

            } else if (cursorOng.moveToFirst()) {

                int idOng      = cursorOng.getInt(cursorOng.getColumnIndexOrThrow("id"));
                String nomeOng = cursorOng.getString(cursorOng.getColumnIndexOrThrow("nome"));

                prefs.edit()
                        .putInt("id_ong_logada", idOng)
                        .putString("nome_ong_logada", nomeOng)
                        .putInt("id_usuario_logado", -1)  // limpa sessão de usuário anterior
                        .apply();

                Toast.makeText(Login.this, "Login da ONG realizado!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, DashboardOng.class));
                finish();

            } else {
                Toast.makeText(Login.this, "CPF/CNPJ ou senha inválidos", Toast.LENGTH_SHORT).show();
            }

            cursorUsuario.close();
            cursorOng.close();
            db.close();
        });

        btnCadastroUsuario.setOnClickListener(view ->
                startActivity(new Intent(Login.this, TelaCadastro.class))
        );

        btnCadastroOng.setOnClickListener(view ->
                startActivity(new Intent(Login.this, TelaCadastroOng.class))
        );
    }
}
