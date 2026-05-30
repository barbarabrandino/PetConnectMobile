package com.example.petconnect;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.example.petconnect.model.Pet;
import com.example.petconnect.repository.PetRepository;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PetAdapter.OnPetClickListener {


    private TextView tvSubtitle;
    private EditText etSearch;
    private RecyclerView rvPets;
    private ProgressBar progressBar;
    private TextView tvEmpty;


    private String filtroTipo     = null;
    private String filtroTamanho  = null;
    private String filtroIdade    = null;


    private PetRepository repository;
    private ListenerRegistration listenerRegistration;


    private PetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new PetRepository();

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
                Toast.makeText(this, "Chat", Toast.LENGTH_SHORT).show());
    }

    private void setupRecyclerView() {
        adapter = new PetAdapter(this);
        rvPets.setLayoutManager(new LinearLayoutManager(this));
        rvPets.setAdapter(adapter);
        // Desativa scroll interno pois está dentro de NestedScrollView
        rvPets.setNestedScrollingEnabled(false);
    }


    private void setupSearchBar() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int i, int b, int c) {}

            @Override
            public void afterTextChanged(Editable s) {
                String texto = s.toString().trim();
                removerListener();

                if (texto.isEmpty()) {
                    carregarPets(); // volta aos filtros normais
                } else {
                    mostrarLoading(true);
                    listenerRegistration = repository.observarPorNome(texto, new PetRepository.OnPetsLoadedListener() {
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
                mostrarDialogFiltro("Tipo", tipos, (TextView) ((LinearLayout) v).getChildAt(0), valor -> {
                    filtroTipo = valor;
                    carregarPets();
                })
        );

        findViewById(R.id.filterTamanho).setOnClickListener(v ->
                mostrarDialogFiltro("Tamanho", tamanhos, (TextView) ((LinearLayout) v).getChildAt(0), valor -> {
                    filtroTamanho = valor;
                    carregarPets();
                })
        );

        findViewById(R.id.filterIdade).setOnClickListener(v ->
                mostrarDialogFiltro("Idade", idades, (TextView) ((LinearLayout) v).getChildAt(0), valor -> {
                    filtroIdade = valor;
                    carregarPets();
                })
        );
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
        removerListener();
        mostrarLoading(true);

        boolean temFiltro = filtroTipo != null || filtroTamanho != null || filtroIdade != null;

        if (temFiltro) {
            listenerRegistration = repository.observarComFiltros(
                    filtroTipo, filtroTamanho, filtroIdade,
                    new PetRepository.OnPetsLoadedListener() {
                        @Override public void onSuccess(List<Pet> pets) { exibirPets(pets); }
                        @Override public void onFailure(Exception e)    { mostrarErro(e); }
                    });
        } else {
            listenerRegistration = repository.observarTodos(
                    new PetRepository.OnPetsLoadedListener() {
                        @Override public void onSuccess(List<Pet> pets) { exibirPets(pets); }
                        @Override public void onFailure(Exception e)    { mostrarErro(e); }
                    });
        }
    }

    private void exibirPets(List<Pet> pets) {
        mostrarLoading(false);
        adapter.submitList(pets);

        int total = pets.size();
        tvSubtitle.setText(total == 1
                ? "1 animal esperando por uma família"
                : total + " animais esperando por uma família");

        tvEmpty.setVisibility(pets.isEmpty() ? View.VISIBLE : View.GONE);
        rvPets.setVisibility(pets.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void mostrarErro(Exception e) {
        mostrarLoading(false);
        Toast.makeText(this, "Erro ao carregar animais: " + e.getMessage(),
                Toast.LENGTH_LONG).show();
    }

    private void mostrarLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            tvEmpty.setVisibility(View.GONE);
        }
    }



    @Override
    public void onVerPerfil(Pet pet) {
        // TODO: navegar para PetDetailActivity
        Toast.makeText(this, "Ver perfil: " + pet.getNome(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavoritarToggle(Pet pet, boolean favoritado) {
        adapter.toggleFavorito(pet.getId());
        String msg = favoritado
                ? pet.getNome() + " adicionado aos favoritos!"
                : pet.getNome() + " removido dos favoritos";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // TODO: persistir favorito no Firestore ou SharedPreferences
    }



    private void setupBottomNav() {
        findViewById(R.id.navInicio).setOnClickListener(v ->
                Toast.makeText(this, "Início", Toast.LENGTH_SHORT).show());
        findViewById(R.id.navAnimais).setOnClickListener(v ->
                Toast.makeText(this, "Animais", Toast.LENGTH_SHORT).show());
        findViewById(R.id.navParceiros).setOnClickListener(v ->
                Toast.makeText(this, "Parceiros", Toast.LENGTH_SHORT).show());
        findViewById(R.id.navCadastros).setOnClickListener(v ->
                Toast.makeText(this, "Cadastros", Toast.LENGTH_SHORT).show());
        findViewById(R.id.navInformacoes).setOnClickListener(v ->
                Toast.makeText(this, "Informações", Toast.LENGTH_SHORT).show());
    }



    @Override
    protected void onStop() {
        super.onStop();
        removerListener(); // evita vazamento de memória
    }

    private void removerListener() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }
}
