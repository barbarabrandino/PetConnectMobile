package com.example.petconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petconnect.adapter.PetAdapter;
import com.example.petconnect.database.FavoritosDAO;
import com.example.petconnect.model.Pet;
import com.example.petconnect.repository.PetRepository;

import java.util.List;

public class TelaHome extends AppCompatActivity implements PetAdapter.OnPetClickListener {

    private TextView    tvSubtitle;
    private EditText    etSearch;
    private RecyclerView rvPets;
    private ProgressBar progressBar;
    private TextView    tvEmpty;

    private String filtroTipo    = null;
    private String filtroTamanho = null;
    private String filtroIdade   = null;

    private PetRepository repository;
    private FavoritosDAO  favoritosDAO;
    private PetAdapter    adapter;
    private int           idUsuarioLogado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_home);

        repository   = new PetRepository(this);
        favoritosDAO = new FavoritosDAO(this);

        SharedPreferences prefs = getSharedPreferences("petconnect_prefs", MODE_PRIVATE);
        idUsuarioLogado = prefs.getInt("id_usuario_logado", -1);

        initViews();
        setupRecyclerView();
        setupSearchBar();
        setupFilters();
        setupBottomNav();
        carregarPets();
    }

    private void initViews() {
        tvSubtitle  = findViewById(R.id.tvSubtitle);
        etSearch    = findViewById(R.id.etSearch);
        rvPets      = findViewById(R.id.rvPets);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty     = findViewById(R.id.tvEmpty);

        findViewById(R.id.ivChat).setOnClickListener(v ->
                Toast.makeText(this, "Chat em breve!", Toast.LENGTH_SHORT).show());

        findViewById(R.id.ivSair).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Sair")
                        .setMessage("Deseja sair da conta?")
                        .setPositiveButton("Sair", (dialog, which) -> {
                            Intent intent = new Intent(TelaHome.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancelar", null)
                        .show());
    }

    private void setupRecyclerView() {
        adapter = new PetAdapter(this);
        rvPets.setLayoutManager(new LinearLayoutManager(this));
        rvPets.setAdapter(adapter);
        rvPets.setNestedScrollingEnabled(false);
    }

    private void setupSearchBar() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int i, int b, int c) {}

            @Override
            public void afterTextChanged(Editable s) {
                String texto = s.toString().trim();
                if (texto.isEmpty()) {
                    carregarPets();
                } else {
                    mostrarLoading(true);
                    repository.carregarPorNome(texto, new PetRepository.OnPetsLoadedListener() {
                        @Override public void onSuccess(List<Pet> pets) { exibirPets(pets); }
                        @Override public void onFailure(Exception e)    { mostrarErro(e); }
                    });
                }
            }
        });
    }

    private void setupFilters() {
        String[] tipos    = {"Todos", "Gato", "Cachorro", "Outro"};
        String[] tamanhos = {"Todos", "Pequeno", "Médio", "Grande"};
        String[] idades   = {"Todos", "Filhote", "Adulto", "Idoso"};

        findViewById(R.id.filterTipo).setOnClickListener(v ->
                mostrarDialogFiltro("Tipo", tipos,
                        (TextView) ((LinearLayout) v).getChildAt(0), valor -> {
                            filtroTipo = valor; carregarPets();
                        }));

        findViewById(R.id.filterTamanho).setOnClickListener(v ->
                mostrarDialogFiltro("Tamanho", tamanhos,
                        (TextView) ((LinearLayout) v).getChildAt(0), valor -> {
                            filtroTamanho = valor; carregarPets();
                        }));

        findViewById(R.id.filterIdade).setOnClickListener(v ->
                mostrarDialogFiltro("Idade", idades,
                        (TextView) ((LinearLayout) v).getChildAt(0), valor -> {
                            filtroIdade = valor; carregarPets();
                        }));
    }

    private void mostrarDialogFiltro(String titulo, String[] opcoes,
                                     TextView chipLabel, OnFiltroSelecionado callback) {
        new AlertDialog.Builder(this)
                .setTitle(titulo)
                .setItems(opcoes, (dialog, which) -> {
                    String selecionado = opcoes[which];
                    chipLabel.setText(selecionado);
                    callback.onSelecionado("Todos".equals(selecionado) ? null : selecionado);
                })
                .show();
    }

    interface OnFiltroSelecionado {
        void onSelecionado(String valor);
    }

    private void carregarPets() {
        mostrarLoading(true);
        boolean temFiltro = filtroTipo != null || filtroTamanho != null || filtroIdade != null;
        if (temFiltro) {
            repository.carregarComFiltros(filtroTipo, filtroTamanho, filtroIdade,
                    new PetRepository.OnPetsLoadedListener() {
                        @Override public void onSuccess(List<Pet> pets) { exibirPets(pets); }
                        @Override public void onFailure(Exception e)    { mostrarErro(e); }
                    });
        } else {
            repository.carregarTodos(new PetRepository.OnPetsLoadedListener() {
                @Override public void onSuccess(List<Pet> pets) { exibirPets(pets); }
                @Override public void onFailure(Exception e)    { mostrarErro(e); }
            });
        }
    }

    private void exibirPets(List<Pet> pets) {
        mostrarLoading(false);

        adapter.submitList(pets, () -> {
            if (idUsuarioLogado != -1) {
                for (Pet pet : pets) {
                    String idAnimal = pet.getId();
                    if (idAnimal != null && favoritosDAO.isFavorito(idUsuarioLogado, idAnimal)) {
                        adapter.marcarFavorito(idAnimal);
                    }
                }
            }
        });

        int total = pets.size();
        tvSubtitle.setText(total == 1
                ? "1 animal esperando por uma família"
                : total + " animais esperando por uma família");
        tvEmpty.setVisibility(pets.isEmpty() ? View.VISIBLE : View.GONE);
        rvPets.setVisibility(pets.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void mostrarErro(Exception e) {
        mostrarLoading(false);
        Toast.makeText(this, "Erro ao carregar animais: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void mostrarLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) tvEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onVerPerfil(Pet pet) {
        Intent intent = new Intent(this, TelaPerfil.class);
        intent.putExtra(TelaPerfil.EXTRA_PET_ID,        pet.getId());
        intent.putExtra(TelaPerfil.EXTRA_PET_NOME,      pet.getNome());
        intent.putExtra(TelaPerfil.EXTRA_PET_RACA,      pet.getRaca());
        intent.putExtra(TelaPerfil.EXTRA_PET_IDADE,     pet.getIdade());
        intent.putExtra(TelaPerfil.EXTRA_PET_TAMANHO,   pet.getTamanho());
        intent.putExtra(TelaPerfil.EXTRA_PET_SEXO,      pet.getSexo());      // ✅ CORRIGIDO: era getTipo()
        intent.putExtra(TelaPerfil.EXTRA_PET_TIPO,      pet.getTipo());      // ✅ NOVO: tipo separado
        intent.putExtra(TelaPerfil.EXTRA_PET_DESCRICAO, pet.getDescricao());
        intent.putExtra(TelaPerfil.EXTRA_PET_FOTO,      pet.getFotoUrl());
        intent.putExtra(TelaPerfil.EXTRA_PET_ABRIGO,    pet.getAbrigo());
        intent.putExtra(TelaPerfil.EXTRA_PET_VACINADO,  pet.isVacinado());
        intent.putExtra(TelaPerfil.EXTRA_PET_CASTRADO,  pet.isCastrado());
        startActivity(intent);
    }

    @Override
    public void onFavoritarToggle(Pet pet, boolean favoritado) {
        if (idUsuarioLogado == -1) {
            Toast.makeText(this, "Faça login para favoritar", Toast.LENGTH_SHORT).show();
            return;
        }

        String idAnimal = pet.getId();

        if (idAnimal == null || idAnimal.isEmpty()) {
            Toast.makeText(this, "Erro: ID do animal inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (favoritado) {
            favoritosDAO.adicionar(idUsuarioLogado, idAnimal);
        } else {
            favoritosDAO.remover(idUsuarioLogado, idAnimal);
        }

        adapter.setFavorito(idAnimal, favoritado);

        String msg = favoritado
                ? pet.getNome() + " adicionado aos favoritos!"
                : pet.getNome() + " removido dos favoritos";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setupBottomNav() {
        setNavAtivo(R.id.navInicio);

        findViewById(R.id.navInicio).setOnClickListener(v -> setNavAtivo(R.id.navInicio));

        findViewById(R.id.navFavoritos).setOnClickListener(v -> {
            setNavAtivo(R.id.navFavoritos);
            startActivity(new Intent(this, Favoritos.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        });

        findViewById(R.id.navSolicitacoes).setOnClickListener(v -> {
            setNavAtivo(R.id.navSolicitacoes);
            startActivity(new Intent(this, MinhasSolicitacoes.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        });

        findViewById(R.id.navConfiguracoes).setOnClickListener(v -> {
            setNavAtivo(R.id.navConfiguracoes);
            startActivity(new Intent(this, Configuracoes.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        });
    }

    private void setNavAtivo(int idAtivo) {
        int[] navIds = { R.id.navInicio, R.id.navFavoritos, R.id.navSolicitacoes, R.id.navConfiguracoes };
        for (int id : navIds) {
            View item = findViewById(id);
            if (item != null) item.setSelected(id == idAtivo);
        }
    }
}
