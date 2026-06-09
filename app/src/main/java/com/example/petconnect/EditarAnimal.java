package com.example.petconnect;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.petconnect.database.DatabaseConection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class EditarAnimal extends AppCompatActivity {

    private EditText etNome, etEspecie, etPorte, etIdade, etFotoUrl;
    private ImageView ivPreviewFoto;
    private int idAnimal;
    private String fotoUrlFinal = "";

    private final ActivityResultLauncher<String> galeriaLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            String caminhoLocal = copiarImagemParaInterno(uri);
                            if (caminhoLocal != null) {
                                fotoUrlFinal = caminhoLocal;
                                etFotoUrl.setText("");
                                Glide.with(this).load(new File(caminhoLocal))
                                        .centerCrop().into(ivPreviewFoto);
                            } else {
                                fotoUrlFinal = uri.toString();
                                Glide.with(this).load(uri)
                                        .centerCrop().into(ivPreviewFoto);
                            }
                            ivPreviewFoto.setVisibility(View.VISIBLE);
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_animal);

        etNome        = findViewById(R.id.etNome);
        etEspecie     = findViewById(R.id.etEspecie);
        etPorte       = findViewById(R.id.etRaca);
        etIdade       = findViewById(R.id.etIdade);
        etFotoUrl     = findViewById(R.id.etFotoUrl);
        ivPreviewFoto = findViewById(R.id.ivPreviewFoto);


        idAnimal = getIntent().getIntExtra("id_animal", -1);
        etNome.setText(getIntent().getStringExtra("nome"));
        etEspecie.setText(getIntent().getStringExtra("especie"));
        etPorte.setText(getIntent().getStringExtra("porte"));
        etIdade.setText(String.valueOf(getIntent().getIntExtra("idade", 0)));


        fotoUrlFinal = getIntent().getStringExtra("foto_url");
        if (fotoUrlFinal == null) fotoUrlFinal = "";
        if (!fotoUrlFinal.isEmpty()) {
            ivPreviewFoto.setVisibility(View.VISIBLE);
            if (fotoUrlFinal.startsWith("/")) {
                Glide.with(this).load(new File(fotoUrlFinal))
                        .centerCrop()
                        .placeholder(R.drawable.ic_cat_placeholder)
                        .into(ivPreviewFoto);
            } else {
                Glide.with(this).load(fotoUrlFinal)
                        .centerCrop()
                        .placeholder(R.drawable.ic_cat_placeholder)
                        .into(ivPreviewFoto);
            }
        }


        findViewById(R.id.btnEscolherFoto).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Trocar foto")
                        .setItems(new String[]{"Escolher da galeria", "Inserir URL"}, (dialog, which) -> {
                            if (which == 0) {
                                galeriaLauncher.launch("image/*");
                            } else {
                                etFotoUrl.setVisibility(View.VISIBLE);
                                etFotoUrl.requestFocus();
                            }
                        })
                        .show()
        );


        etFotoUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String url = etFotoUrl.getText().toString().trim();
                if (!url.isEmpty()) {
                    fotoUrlFinal = url;
                    Glide.with(this).load(url)
                            .centerCrop()
                            .placeholder(R.drawable.ic_cat_placeholder)
                            .error(R.drawable.ic_cat_placeholder)
                            .into(ivPreviewFoto);
                    ivPreviewFoto.setVisibility(View.VISIBLE);
                }
            }
        });

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
        ((Button) findViewById(R.id.btnSalvar)).setOnClickListener(v -> salvarEdicao());
    }

    private void salvarEdicao() {
        String nome    = etNome.getText().toString().trim();
        String especie = etEspecie.getText().toString().trim();
        String porte   = etPorte.getText().toString().trim();
        String idadeStr = etIdade.getText().toString().trim();

        String urlCampo = etFotoUrl.getText().toString().trim();
        if (!urlCampo.isEmpty()) fotoUrlFinal = urlCampo;

        if (nome.isEmpty())    { etNome.setError("Informe o nome");     etNome.requestFocus();    return; }
        if (especie.isEmpty()) { etEspecie.setError("Informe a espécie"); etEspecie.requestFocus(); return; }
        if (porte.isEmpty())   { etPorte.setError("Informe o porte");   etPorte.requestFocus();   return; }
        if (idadeStr.isEmpty()){ etIdade.setError("Informe a idade");   etIdade.requestFocus();   return; }
        if (idAnimal == -1)    { Toast.makeText(this, "Erro: animal inválido.", Toast.LENGTH_SHORT).show(); return; }

        DatabaseConection con = new DatabaseConection(this);
        SQLiteDatabase db = con.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("nome",     nome);
        cv.put("especie",  especie);
        cv.put("porte",    porte);
        cv.put("idade",    Integer.parseInt(idadeStr));
        cv.put("foto_url", fotoUrlFinal);

        int linhas = db.update(
                DatabaseConection.TABELA_ANIMAL,
                cv,
                "id = ?",
                new String[]{ String.valueOf(idAnimal) }
        );
        db.close();

        if (linhas > 0) {
            Toast.makeText(this, "Animal atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao atualizar. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private String copiarImagemParaInterno(Uri uri) {
        try {
            File dir = new File(getFilesDir(), "fotos_animais");
            if (!dir.exists()) dir.mkdirs();

            String nomeArquivo = "animal_" + UUID.randomUUID() + ".jpg";
            File destino = new File(dir, nomeArquivo);

            InputStream in   = getContentResolver().openInputStream(uri);
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
}