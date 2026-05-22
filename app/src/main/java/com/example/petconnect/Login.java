package com.example.petconnect;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    EditText etEmailLogin, etSenhaLogin;

    Button btnEntrar;
    Button btnCadastroUsuario;
    Button btnCadastroOng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // EditTexts
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etSenhaLogin = findViewById(R.id.etSenhaLogin);

        // Botões
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastroUsuario = findViewById(R.id.btnCadastroUsuario);
        btnCadastroOng = findViewById(R.id.btnCadastroOng);

        // Navegação
        btnEntrar.setOnClickListener(view -> {

            String email = etEmailLogin.getText().toString().trim();
            String senha = etSenhaLogin.getText().toString().trim();

            // Verifica se os campos estão vazios!!!!
            if (TextUtils.isEmpty(email)) {
                etEmailLogin.setError("Digite o email");
                return;
            }

            if (TextUtils.isEmpty(senha)) {
                etSenhaLogin.setError("Digite a senha");
                return;
            }

            // Login simples
            Toast.makeText(
                    Login.this,
                    "Login realizado com sucesso!",
                    Toast.LENGTH_SHORT
            ).show();

            // Abre a tela principal
            Intent intent = new Intent(
                    Login.this,
                    TelaHome.class
            );

            startActivity(intent);
        });

        // Cadastro do Adotante
        btnCadastroUsuario.setOnClickListener(view -> {

            Intent intent = new Intent(
                    Login.this,
                    TelaCadastro.class
            );

            startActivity(intent);
        });

        // Cadastro da Ong
        btnCadastroOng.setOnClickListener(view -> {

            Intent intent = new Intent(
                    Login.this,
                    TelaCadastroOng.class
            );

            startActivity(intent);
        });
    }
}