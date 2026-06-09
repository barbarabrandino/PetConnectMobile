package com.example.petconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.petconnect.database.SolicitacaoDAO;

import java.io.File;

public class TelaPerfil extends AppCompatActivity {

    public static final String EXTRA_PET_ID       = "extra_pet_id";
    public static final String EXTRA_PET_NOME     = "extra_pet_nome";
    public static final String EXTRA_PET_RACA     = "extra_pet_raca";
    public static final String EXTRA_PET_IDADE    = "extra_pet_idade";
    public static final String EXTRA_PET_TAMANHO  = "extra_pet_tamanho";
    public static final String EXTRA_PET_SEXO     = "extra_pet_sexo";
    public static final String EXTRA_PET_TIPO     = "extra_pet_tipo";
    public static final String EXTRA_PET_DESCRICAO= "extra_pet_descricao";
    public static final String EXTRA_PET_FOTO     = "extra_pet_foto";
    public static final String EXTRA_PET_ABRIGO   = "extra_pet_abrigo";
    public static final String EXTRA_PET_VACINADO = "extra_pet_vacinado";
    public static final String EXTRA_PET_CASTRADO = "extra_pet_castrado";

    private SolicitacaoDAO solicitacaoDAO;
    private int    idUsuarioLogado = -1;
    private String idAnimal;
    private String nomeAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil);

        solicitacaoDAO = new SolicitacaoDAO(this);

        SharedPreferences prefs = getSharedPreferences("petconnect_prefs", MODE_PRIVATE);
        idUsuarioLogado = prefs.getInt("id_usuario_logado", -1);

        preencherDados();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button btnSolicitar = findViewById(R.id.btnSolicitar);
        if (btnSolicitar != null && idAnimal != null) {
            atualizarBotaoSolicitar(btnSolicitar, nomeAnimal);
        }
    }

    private void preencherDados() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) { finish(); return; }

        idAnimal   = extras.getString(EXTRA_PET_ID,   "");
        nomeAnimal = extras.getString(EXTRA_PET_NOME,  "");

        String raca     = extras.getString(EXTRA_PET_RACA,     "");
        String idade    = extras.getString(EXTRA_PET_IDADE,    "");
        String tamanho  = extras.getString(EXTRA_PET_TAMANHO,  "");
        String sexo     = extras.getString(EXTRA_PET_SEXO,     "");
        String descricao= extras.getString(EXTRA_PET_DESCRICAO,"");
        String foto     = extras.getString(EXTRA_PET_FOTO,     "");
        String abrigo   = extras.getString(EXTRA_PET_ABRIGO,   "");
        boolean vacinado= extras.getBoolean(EXTRA_PET_VACINADO, false);
        boolean castrado= extras.getBoolean(EXTRA_PET_CASTRADO, false);

        ImageView ivFoto      = findViewById(R.id.ivPerfilFoto);
        TextView  tvNome      = findViewById(R.id.tvPerfilNome);
        TextView  tvRaca      = findViewById(R.id.tvPerfilRaca);
        TextView  tvIdade     = findViewById(R.id.tvPerfilIdade);
        TextView  tvTamanho   = findViewById(R.id.tvPerfilTamanho);
        TextView  tvSexo      = findViewById(R.id.tvPerfilSexo);
        TextView  tvDescricao = findViewById(R.id.tvPerfilDescricao);
        TextView  tvAbrigo    = findViewById(R.id.tvPerfilAbrigo);
        TextView  tagVacinado = findViewById(R.id.tagPerfilVacinado);
        TextView  tagCastrado = findViewById(R.id.tagPerfilCastrado);
        Button    btnSolicitar= findViewById(R.id.btnSolicitar);
        ImageView btnVoltar   = findViewById(R.id.ivVoltar);

        tvNome.setText(nomeAnimal);
        tvRaca.setText(raca);
        tvIdade.setText(idade.isEmpty()    ? "—" : idade);
        tvTamanho.setText(tamanho.isEmpty() ? "—" : tamanho);
        tvSexo.setText(sexo.isEmpty()      ? "—" : sexo);
        tvDescricao.setText(descricao);
        tvAbrigo.setText(abrigo.isEmpty()  ? "—" : abrigo);

        tagVacinado.setVisibility(vacinado ? View.VISIBLE : View.GONE);
        tagCastrado.setVisibility(castrado ? View.VISIBLE : View.GONE);

        if (foto != null && foto.startsWith("/")) {
            Glide.with(this).load(new File(foto)).centerCrop()
                    .placeholder(R.drawable.ic_cat_placeholder)
                    .error(R.drawable.ic_cat_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivFoto);
        } else {
            Glide.with(this).load(foto).centerCrop()
                    .placeholder(R.drawable.ic_cat_placeholder)
                    .error(R.drawable.ic_cat_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivFoto);
        }

        btnVoltar.setOnClickListener(v -> finish());

        atualizarBotaoSolicitar(btnSolicitar, nomeAnimal);

        btnSolicitar.setOnClickListener(v -> {
            if (idUsuarioLogado == -1) {
                Toast.makeText(this, "Faça login para enviar uma solicitação",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, InteresseAdocaoActivity.class);
            intent.putExtra("id_animal",   idAnimal);
            intent.putExtra("nome_animal", nomeAnimal);
            startActivity(intent);
        });
    }

    private void atualizarBotaoSolicitar(Button btn, String nomePet) {
        if (idUsuarioLogado != -1 && idAnimal != null && !idAnimal.isEmpty()) {
            boolean jaSolicitou = solicitacaoDAO.jaSolicitou(idUsuarioLogado, idAnimal);
            if (jaSolicitou) {
                btn.setText("Solicitação Enviada ✓");
                btn.setEnabled(false);
            } else {
                btn.setText("Quero Adotar " + nomePet + "!");
                btn.setEnabled(true);
            }
        }
    }
}