package com.example.petconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnComecar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        btnComecar = findViewById(R.id.btnComecar);

        btnComecar.setOnClickListener(view -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    Login.class
            );

            startActivity(intent);
        });
    }
}




