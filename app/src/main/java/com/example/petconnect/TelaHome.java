package com.example.petconnect;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TelaHome extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_home);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnItemSelectedListener(item -> {

            // HOME
            if (item.getItemId() == R.id.menu_home) {

                return true;
            }

            // FAVORITOS
            else if (item.getItemId() == R.id.menu_favoritos) {

                Intent intent = new Intent(
                        TelaHome.this,
                        Favoritos.class
                );

                startActivity(intent);

                return true;
            }

            // SOLICITAÇÕES
            else if (item.getItemId() == R.id.menu_solicitacoes) {

                Intent intent = new Intent(
                        TelaHome.this,
                        MinhasSolicitacoes.class
                );

                startActivity(intent);

                return true;
            }

            // CONFIGURAÇÕES
            else if (item.getItemId() == R.id.menu_config) {

                Intent intent = new Intent(
                        TelaHome.this,
                        Configuracoes.class
                );

                startActivity(intent);

                return true;
            }

            return false;
        });
    }
}