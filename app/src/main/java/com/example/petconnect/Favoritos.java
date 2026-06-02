package com.example.petconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petconnect.adapter.PetAdapter;
import com.example.petconnect.database.DatabaseConection;
import com.example.petconnect.database.FavoritosDAO;
import com.example.petconnect.model.Pet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Favoritos extends AppCompatActivity implements PetAdapter.OnPetClickListener {

    private RecyclerView  rvFavoritos;
    private ProgressBar   progressBar;
    private TextView      tvEmpty;
    private PetAdapter    adapter;
    private FavoritosDAO  favoritosDAO;
    private int           idUsuarioLogado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        favoritosDAO = new FavoritosDAO(this);

        SharedPreferences prefs = getSharedPreferences("petconnect_prefs", MODE_PRIVATE);
        idUsuarioLogado = prefs.getInt("id_usuario_logado", -1);

        rvFavoritos = findViewById(R.id.rvFavoritos);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty     = findViewById(R.id.tvEmpty);

        adapter = new PetAdapter(this);
        rvFavoritos.setLayoutManager(new LinearLayoutManager(this));
        rvFavoritos.setAdapter(adapter);

        setupBottomNav();
        carregarFavoritos();
    }

    private void carregarFavoritos() {
        if (idUsuarioLogado == -1) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText("Faça login para ver seus favoritos.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);

        // Busca os IDs salvos localmente no SQLite
        List<String> idsFavoritos = favoritosDAO.listarIdsFavoritos(idUsuarioLogado);

        if (idsFavoritos.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        }

        // Monta placeholders para o IN (?, ?, ...)
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < idsFavoritos.size(); i++) {
            placeholders.append(i == 0 ? "?" : ", ?");
        }

        try {
            SQLiteDatabase db = new DatabaseConection(this).getReadableDatabase();

            String sql = "SELECT a.*, o.nome AS nome_ong " +
                         "FROM animais a " +
                         "LEFT JOIN ongs o ON a.id_ong = o.id " +
                         "WHERE a.id IN (" + placeholders + ") " +
                         "ORDER BY a.nome ASC";

            Cursor cursor = db.rawQuery(sql, idsFavoritos.toArray(new String[0]));
            List<Pet> petsFavoritos = cursorParaLista(cursor);

            progressBar.setVisibility(View.GONE);

            if (petsFavoritos.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
            } else {
                adapter.submitList(new ArrayList<>(petsFavoritos), () -> {
                    for (Pet p : petsFavoritos) {
                        adapter.marcarFavorito(p.getId());
                    }
                });
            }

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Erro ao carregar favoritos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private List<Pet> cursorParaLista(Cursor cursor) {
        List<Pet> lista = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Pet pet = new Pet();
                pet.setId(       String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id"))));
                pet.setNome(     cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                pet.setRaca(     cursor.getString(cursor.getColumnIndexOrThrow("especie")));
                pet.setTamanho(  cursor.getString(cursor.getColumnIndexOrThrow("porte")));
                pet.setTipo(     cursor.getString(cursor.getColumnIndexOrThrow("especie")));
                pet.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));

                int idadeInt = cursor.getInt(cursor.getColumnIndexOrThrow("idade"));
                pet.setIdade(idadeInt == 1 ? "1 ano" : idadeInt + " anos");

                int colOng = cursor.getColumnIndex("nome_ong");
                pet.setAbrigo(colOng >= 0 ? cursor.getString(colOng) : "");

                int colFoto = cursor.getColumnIndex("foto_url");
                String foto = colFoto >= 0 ? cursor.getString(colFoto) : "";
                if (foto == null) foto = "";
                if (foto.startsWith("/")) {
                    File arquivo = new File(foto);
                    pet.setFotoUrl(arquivo.exists() ? foto : "");
                } else {
                    pet.setFotoUrl(foto);
                }

                pet.setVacinado(false);
                pet.setCastrado(false);
                lista.add(pet);
            }
        } finally {
            cursor.close();
        }
        return lista;
    }

    @Override
    public void onVerPerfil(Pet pet) {
        // TODO: abrir tela de detalhes
    }

    @Override
    public void onFavoritarToggle(Pet pet, boolean favoritado) {
        if (idUsuarioLogado == -1) return;

        String idAnimal = pet.getId();
        if (favoritado) {
            favoritosDAO.adicionar(idUsuarioLogado, idAnimal);
            adapter.setFavorito(idAnimal, true);
        } else {
            favoritosDAO.remover(idUsuarioLogado, idAnimal);
            // Remove da lista visualmente
            List<Pet> atual = new ArrayList<>(adapter.getCurrentList());
            atual.removeIf(p -> p.getId().equals(idAnimal));
            adapter.submitList(atual);
            if (atual.isEmpty()) tvEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(this, pet.getNome() + " removido dos favoritos", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNav() {
        setNavAtivo(R.id.navFavoritos);

        findViewById(R.id.navInicio).setOnClickListener(v ->
                startActivity(new Intent(this, TelaHome.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));

        findViewById(R.id.navFavoritos).setOnClickListener(v -> {});

        findViewById(R.id.navSolicitacoes).setOnClickListener(v ->
                startActivity(new Intent(this, MinhasSolicitacoes.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));

        findViewById(R.id.navConfiguracoes).setOnClickListener(v ->
                startActivity(new Intent(this, Configuracoes.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)));
    }

    private void setNavAtivo(int idAtivo) {
        int[] navIds = { R.id.navInicio, R.id.navFavoritos, R.id.navSolicitacoes, R.id.navConfiguracoes };
        for (int id : navIds) {
            View item = findViewById(id);
            if (item != null) item.setSelected(id == idAtivo);
        }
    }
}
