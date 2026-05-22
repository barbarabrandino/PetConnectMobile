package com.example.petconnect;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnEntrarOng;
    Button btnEntrarAdotante;
    Button btnCriarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        // Botões
        btnEntrarOng = findViewById(R.id.btnEntrarOng);
        btnEntrarAdotante = findViewById(R.id.btnEntrarAdotante);
        btnCriarConta = findViewById(R.id.btnCriarConta2);

        // Navegação
        btnEntrarAdotante.setOnClickListener(view -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    Login.class
            );

            startActivity(intent);

        });
        btnEntrarOng.setOnClickListener(view -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    Login.class
            );

            startActivity(intent);

        });
        btnCriarConta.setOnClickListener(view -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    Login.class
            );

            startActivity(intent);

        });

    }
}




