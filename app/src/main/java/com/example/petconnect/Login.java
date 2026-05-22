package com.example.petconnect;

import android.content.Intent;
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

        // Banco
        banco = new DatabaseConection(this);

        // Campos
        etCpfCnpjLogin = findViewById(R.id.etCpfCnpjLogin);
        etSenhaLogin = findViewById(R.id.etSenhaLogin);

        // Botões
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastroUsuario = findViewById(R.id.btnCadastroUsuario);
        btnCadastroOng = findViewById(R.id.btnCadastroOng);

        // LOGIN
        btnEntrar.setOnClickListener(view -> {

            String cpfCnpj = etCpfCnpjLogin
                    .getText()
                    .toString()
                    .trim();

            String senha = etSenhaLogin
                    .getText()
                    .toString()
                    .trim();

            // Valida CPF/CNPJ
            if (TextUtils.isEmpty(cpfCnpj)) {

                etCpfCnpjLogin.setError(
                        "Digite o CPF ou CNPJ"
                );

                return;
            }

            // Valida senha
            if (TextUtils.isEmpty(senha)) {

                etSenhaLogin.setError(
                        "Digite a senha"
                );

                return;
            }

            SQLiteDatabase db = banco.getReadableDatabase();

            // LOGIN USUÁRIO
            Cursor cursorUsuario = db.rawQuery(
                    "SELECT * FROM usuarios WHERE cpf=? AND senha=?",
                    new String[]{cpfCnpj, senha}
            );

            // LOGIN ONG
            Cursor cursorOng = db.rawQuery(
                    "SELECT * FROM ongs WHERE cnpj=? AND senha=?",
                    new String[]{cpfCnpj, senha}
            );

            // USUÁRIO
            if (cursorUsuario.moveToFirst()) {

                Toast.makeText(
                        Login.this,
                        "Login de usuário realizado!",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(
                        Login.this,
                        TelaHome.class
                );

                startActivity(intent);
            }

            // ONG
            else if (cursorOng.moveToFirst()) {

                Toast.makeText(
                        Login.this,
                        "Login da ONG realizado!",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(
                        Login.this,
                        DashboardOng.class
                );

                startActivity(intent);
            }

            // ERRO LOGIN
            else {

                Toast.makeText(
                        Login.this,
                        "CPF/CNPJ ou senha inválidos",
                        Toast.LENGTH_SHORT
                ).show();
            }

            cursorUsuario.close();
            cursorOng.close();
            db.close();
        });

        // CADASTRO USUÁRIO
        btnCadastroUsuario.setOnClickListener(view -> {

            Intent intent = new Intent(
                    Login.this,
                    TelaCadastro.class
            );

            startActivity(intent);
        });

        // CADASTRO ONG
        btnCadastroOng.setOnClickListener(view -> {

            Intent intent = new Intent(
                    Login.this,
                    TelaCadastroOng.class
            );

            startActivity(intent);
        });
    }
}