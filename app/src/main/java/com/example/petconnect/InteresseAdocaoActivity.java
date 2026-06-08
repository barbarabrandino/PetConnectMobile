package com.example.petconnect;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petconnect.database.SolicitacaoDAO;

public class InteresseAdocaoActivity extends AppCompatActivity {

    private EditText etNome, etTelefone, etMoradia, etAnimais, etExperiencia, etObservacoes;
    private String idAnimal;
    private String nomeAnimal;
    private int    idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesse_adocao);

        idAnimal   = getIntent().getStringExtra("id_animal");
        nomeAnimal = getIntent().getStringExtra("nome_animal");

        SharedPreferences prefs = getSharedPreferences("petconnect_prefs", MODE_PRIVATE);
        idUsuarioLogado = prefs.getInt("id_usuario_logado", -1);

        // Atualiza título com nome do animal
        TextView txtTitulo = findViewById(R.id.txtTitulo);
        txtTitulo.setText("Interesse em adotar " + nomeAnimal);

        etNome        = findViewById(R.id.etNome);
        etTelefone    = findViewById(R.id.etTelefone);
        etMoradia     = findViewById(R.id.etMoradia);
        etAnimais     = findViewById(R.id.etAnimais);
        etExperiencia = findViewById(R.id.etExperiencia);
        etObservacoes = findViewById(R.id.etObservacoes);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        ((Button) findViewById(R.id.btnEnviar)).setOnClickListener(v -> enviarSolicitacao());
    }

    private void enviarSolicitacao() {
        String nome       = etNome.getText().toString().trim();
        String telefone   = etTelefone.getText().toString().trim();
        String moradia    = etMoradia.getText().toString().trim();
        String animais    = etAnimais.getText().toString().trim();
        String experiencia= etExperiencia.getText().toString().trim();
        String observacoes= etObservacoes.getText().toString().trim();

        if (nome.isEmpty())     { etNome.setError("Informe seu nome");         etNome.requestFocus();     return; }
        if (telefone.isEmpty()) { etTelefone.setError("Informe seu telefone"); etTelefone.requestFocus(); return; }
        if (moradia.isEmpty())  { etMoradia.setError("Informe sua moradia");   etMoradia.requestFocus();  return; }

        if (idUsuarioLogado == -1) {
            Toast.makeText(this, "Erro: usuário não identificado.", Toast.LENGTH_SHORT).show();
            return;
        }

        SolicitacaoDAO dao = new SolicitacaoDAO(this);
        boolean sucesso = dao.inserirCompleto(
                idUsuarioLogado, idAnimal,
                nome, telefone, moradia,
                animais, experiencia, observacoes);

        if (sucesso) {
            Toast.makeText(this,
                    "Solicitação enviada! A ONG entrará em contato em breve.",
                    Toast.LENGTH_LONG).show();
            finish(); // volta para TelaPerfil que atualiza o botão no onResume
        } else {
            Toast.makeText(this,
                    "Você já enviou uma solicitação para este animal.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}