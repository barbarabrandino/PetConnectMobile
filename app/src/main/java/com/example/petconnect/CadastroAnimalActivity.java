package com.example.petconnect;

import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.petconnect.database.AnimalDAO;
import com.example.petconnect.model.Animal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class CadastroAnimalActivity extends AppCompatActivity {

    private EditText     etNome, etIdade, etDescricao, etFotoUrl;
    private Spinner      spinnerEspecie, spinnerPorte;
    private LinearLayout btnSexoMacho, btnSexoFemea;
    private Button       btnCadastrar, btnCancelar, btnEscolherFoto;
    private ImageView    ivPreviewFoto;

    private String sexoSelecionado = null;
    private String fotoUrlFinal    = "";

    private final ActivityResultLauncher<String> galeriaLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {

                            String caminhoLocal = copiarImagemParaInterno(uri);
                            if (caminhoLocal != null) {
                                fotoUrlFinal = caminhoLocal;
                                etFotoUrl.setText("");
                                Glide.with(this).load(new File(caminhoLocal)).centerCrop().into(ivPreviewFoto);
                                ivPreviewFoto.setVisibility(View.VISIBLE);
                            } else {

                                fotoUrlFinal = uri.toString();
                                Glide.with(this).load(uri).centerCrop().into(ivPreviewFoto);
                                ivPreviewFoto.setVisibility(View.VISIBLE);
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal);

        initViews();
        setupSpinners();
        setupSexoBotoes();
        setupFoto();
        setupBotoes();
    }

    private void initViews() {
        etNome          = findViewById(R.id.etNome);
        etIdade         = findViewById(R.id.etIdade);
        etDescricao     = findViewById(R.id.etDescricao);
        spinnerEspecie  = findViewById(R.id.spinnerEspecie);
        spinnerPorte    = findViewById(R.id.spinnerPorte);
        btnSexoMacho    = findViewById(R.id.btnSexoMacho);
        btnSexoFemea    = findViewById(R.id.btnSexoFemea);
        btnCadastrar    = findViewById(R.id.btnCadastrarAnimal);
        btnCancelar     = findViewById(R.id.btnCancelar);
        btnEscolherFoto = findViewById(R.id.btnEscolherFoto);
        ivPreviewFoto   = findViewById(R.id.ivPreviewFoto);
        etFotoUrl       = findViewById(R.id.etFotoUrl);
    }

    private void setupSpinners() {
        ArrayAdapter<String> adapterEspecie = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Selecione", "Cachorro", "Gato", "Coelho", "Ave", "Outro"});
        adapterEspecie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEspecie.setAdapter(adapterEspecie);

        ArrayAdapter<String> adapterPorte = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Selecione", "Pequeno", "Médio", "Grande"});
        adapterPorte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPorte.setAdapter(adapterPorte);
    }

    private void setupSexoBotoes() {
        btnSexoMacho.setOnClickListener(v -> selecionarSexo("Macho"));
        btnSexoFemea.setOnClickListener(v -> selecionarSexo("Fêmea"));
    }

    private void selecionarSexo(String sexo) {
        sexoSelecionado = sexo;
        int corVerde   = getResources().getColor(R.color.verdebtn, null);
        int corInativo = getResources().getColor(R.color.text_dark, null);
        TextView tvMacho = btnSexoMacho.findViewById(R.id.tvSexoMacho);
        TextView tvFemea = btnSexoFemea.findViewById(R.id.tvSexoFemea);
        if (sexo.equals("Macho")) {
            tvMacho.setTextColor(corVerde);
            tvFemea.setTextColor(corInativo);
            aplicarBordaAtiva(btnSexoMacho, true);
            aplicarBordaAtiva(btnSexoFemea, false);
        } else {
            tvFemea.setTextColor(corVerde);
            tvMacho.setTextColor(corInativo);
            aplicarBordaAtiva(btnSexoFemea, true);
            aplicarBordaAtiva(btnSexoMacho, false);
        }
    }

    private void aplicarBordaAtiva(LinearLayout view, boolean ativo) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(12f);
        shape.setStroke(ativo ? 2 : 1, getResources().getColor(
                ativo ? R.color.verdebtn : R.color.divider_color, null));
        shape.setColor(getResources().getColor(android.R.color.white, null));
        view.setBackground(shape);
    }

    private void setupFoto() {
        btnEscolherFoto.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Adicionar foto")
                        .setItems(new String[]{"Escolher da galeria", "Inserir URL"}, (dialog, which) -> {
                            if (which == 0) {
                                galeriaLauncher.launch("image/*");
                            } else {
                                etFotoUrl.setVisibility(View.VISIBLE);
                                etFotoUrl.requestFocus();
                            }
                        })
                        .show());

        etFotoUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String url = etFotoUrl.getText().toString().trim();
                if (!url.isEmpty()) {
                    fotoUrlFinal = url;
                    Glide.with(this).load(url).centerCrop()
                            .placeholder(R.drawable.ic_cat_placeholder)
                            .error(R.drawable.ic_cat_placeholder)
                            .into(ivPreviewFoto);
                    ivPreviewFoto.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private String copiarImagemParaInterno(Uri uri) {
        try {
            File dir = new File(getFilesDir(), "fotos_animais");
            if (!dir.exists()) dir.mkdirs();

            String nomeArquivo = "animal_" + UUID.randomUUID().toString() + ".jpg";
            File destino = new File(dir, nomeArquivo);

            InputStream in = getContentResolver().openInputStream(uri);
            OutputStream out = new FileOutputStream(destino);

            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);

            in.close();
            out.close();

            return destino.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    private void setupBotoes() {
        btnCadastrar.setOnClickListener(v -> tentarCadastrar());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void tentarCadastrar() {
        String nome      = etNome.getText().toString().trim();
        String idadeStr  = etIdade.getText().toString().trim();
        String descricao = etDescricao.getText().toString().trim();
        String especie   = spinnerEspecie.getSelectedItem().toString();
        String porte     = spinnerPorte.getSelectedItem().toString();

        String urlCampo = etFotoUrl.getText().toString().trim();
        if (!urlCampo.isEmpty()) fotoUrlFinal = urlCampo;

        if (nome.isEmpty())              { etNome.setError("Informe o nome"); etNome.requestFocus(); return; }
        if (especie.equals("Selecione")) { Toast.makeText(this, "Selecione a espécie", Toast.LENGTH_SHORT).show(); return; }
        if (idadeStr.isEmpty())          { etIdade.setError("Informe a idade"); etIdade.requestFocus(); return; }
        if (porte.equals("Selecione"))   { Toast.makeText(this, "Selecione o porte", Toast.LENGTH_SHORT).show(); return; }
        if (sexoSelecionado == null)     { Toast.makeText(this, "Selecione o sexo", Toast.LENGTH_SHORT).show(); return; }
        if (descricao.isEmpty())         { etDescricao.setError("Informe uma descrição"); etDescricao.requestFocus(); return; }

        SharedPreferences prefs = getSharedPreferences("petconnect_prefs", MODE_PRIVATE);
        int idOng = prefs.getInt("id_ong_logada", -1);
        if (idOng == -1) {
            Toast.makeText(this, "Erro: ONG não identificada. Faça login novamente.", Toast.LENGTH_LONG).show();
            return;
        }

        Animal animal = new Animal();
        animal.setNome(nome);
        animal.setEspecie(especie);
        animal.setIdade(Integer.parseInt(idadeStr));
        animal.setPorte(porte);
        animal.setSexo(sexoSelecionado);
        animal.setDescricao(descricao);
        animal.setFotoUrl(fotoUrlFinal);
        animal.setIdOng(idOng);

        AnimalDAO dao = new AnimalDAO(this);
        long resultado = dao.inserir(animal);

        if (resultado != -1) {
            Toast.makeText(this, "Animal cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao cadastrar. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }
}
