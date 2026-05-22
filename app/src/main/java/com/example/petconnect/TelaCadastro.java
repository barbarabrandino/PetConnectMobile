package com.example.petconnect;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

public class TelaCadastro extends AppCompatActivity {
    Spinner spEstado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
        spEstado = findViewById(R.id.spEstado);

        List<String> estados = Arrays.asList(
                "Selecione um estado",
                "São Paulo",
                "Rio de Janeiro",
                "Minas Gerais"
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                estados
        );

        adapter.setDropDownViewResource(R.layout.spinner_item);

        spEstado.setAdapter(adapter);

    }

}