package com.example.petconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petconnect.adapter.AnimalAdapter;
import com.example.petconnect.database.DatabaseConection;
import com.example.petconnect.model.Animal;

import java.util.ArrayList;
import java.util.List;

public class AnimaisCadastradosActivity extends AppCompatActivity {

    private RecyclerView recyclerAnimais;
    private TextView tvListaVazia;
    private AnimalAdapter adapter;
    private final List<Animal> listaAnimais = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animais_cadastrados);

        recyclerAnimais = findViewById(R.id.recyclerAnimais);
        tvListaVazia    = findViewById(R.id.tvListaVazia);

        recyclerAnimais.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AnimalAdapter(
                listaAnimais,
                // OnEditarListener
                animal -> {
                    Intent i = new Intent(this, EditarAnimal.class);
                    i.putExtra("id_animal", animal.getId());
                    i.putExtra("nome",      animal.getNome());
                    i.putExtra("especie",   animal.getEspecie());
                    i.putExtra("porte",     animal.getPorte());
                    i.putExtra("idade",     animal.getIdade());
                    i.putExtra("foto_url",  animal.getFotoUrl());
                    startActivity(i);
                },
                // OnExcluirListener
                animal -> new AlertDialog.Builder(this)
                        .setTitle("Excluir animal")
                        .setMessage("Deseja excluir " + animal.getNome() + "? Esta ação não pode ser desfeita.")
                        .setPositiveButton("Excluir", (d, w) -> excluirAnimal(animal))
                        .setNegativeButton("Cancelar", null)
                        .show()
        );

        recyclerAnimais.setAdapter(adapter);
        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarAnimais();
    }

    private void carregarAnimais() {
        listaAnimais.clear();

        SharedPreferences prefs = getSharedPreferences("petconnect_prefs", MODE_PRIVATE);
        int idOng = prefs.getInt("id_ong_logada", -1);

        if (idOng == -1) {
            atualizarVisibilidade();
            return;
        }

        DatabaseConection con = new DatabaseConection(this);
        SQLiteDatabase db = con.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT id, nome, especie, porte, idade, foto_url FROM "
                        + DatabaseConection.TABELA_ANIMAL
                        + " WHERE id_ong = ? ORDER BY nome ASC",
                new String[]{ String.valueOf(idOng) }
        );

        while (c.moveToNext()) {
            Animal animal = new Animal();
            animal.setId(     c.getInt(0));
            animal.setNome(   c.getString(1));
            animal.setEspecie(c.getString(2));
            animal.setPorte(  c.getString(3));
            animal.setIdade(  c.getInt(4));
            animal.setFotoUrl(c.getString(5) != null ? c.getString(5) : "");
            listaAnimais.add(animal);
        }
        c.close();
        db.close();

        adapter.notifyDataSetChanged();
        atualizarVisibilidade();
    }

    private void atualizarVisibilidade() {
        boolean vazia = listaAnimais.isEmpty();
        tvListaVazia.setVisibility(vazia ? View.VISIBLE : View.GONE);
        recyclerAnimais.setVisibility(vazia ? View.GONE  : View.VISIBLE);
    }

    private void excluirAnimal(Animal animal) {
        DatabaseConection con = new DatabaseConection(this);
        SQLiteDatabase db = con.getWritableDatabase();
        db.delete(
                DatabaseConection.TABELA_ANIMAL,
                "id = ?",
                new String[]{ String.valueOf(animal.getId()) }
        );
        db.close();
        carregarAnimais();
    }
}