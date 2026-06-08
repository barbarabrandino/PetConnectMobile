package com.example.petconnect;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petconnect.database.DatabaseConection;

import java.util.Arrays;
import java.util.List;

public class TelaCadastro extends AppCompatActivity {

    Spinner spEstado;
    EditText etNome;
    EditText etCpf;
    EditText etEmail;
    EditText etSenha;
    EditText etCep;
    EditText etCidade;
    EditText etEndereco;
    Button btnCadastrar;
    DatabaseConection banco;

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

        banco = new DatabaseConection(this);

        etNome     = findViewById(R.id.etNome);
        etCpf      = findViewById(R.id.etCpf);
        etEmail    = findViewById(R.id.etEmail);
        etSenha    = findViewById(R.id.etSenha);
        etCep      = findViewById(R.id.etCep);
        etCidade   = findViewById(R.id.etCidade);
        etEndereco = findViewById(R.id.etEndereco);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(view -> {

            String nome     = etNome.getText().toString().trim();
            String cpf      = etCpf.getText().toString().trim().replaceAll("[^0-9]", "");
            String email    = etEmail.getText().toString().trim();
            String senha    = etSenha.getText().toString().trim();
            String cep      = etCep.getText().toString().trim();
            String estado   = spEstado.getSelectedItem().toString(); // lê do Spinner
            String cidade   = etCidade.getText().toString().trim();
            String endereco = etEndereco.getText().toString().trim();


            if (TextUtils.isEmpty(nome)) {
                etNome.setError("Digite o nome");
                return;
            }
            if (TextUtils.isEmpty(cpf)) {
                etCpf.setError("Digite o CPF");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Digite o email");
                return;
            }
            if (TextUtils.isEmpty(senha)) {
                etSenha.setError("Digite a senha");
                return;
            }
            if (estado.equals("Selecione um estado")) {
                Toast.makeText(this, "Selecione um estado", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = banco.getWritableDatabase();


            Cursor cursorCpf = db.rawQuery(
                    "SELECT * FROM usuarios WHERE cpf=?",
                    new String[]{cpf}
            );
            if (cursorCpf.moveToFirst()) {
                Toast.makeText(this, "CPF já cadastrado!", Toast.LENGTH_SHORT).show();
                cursorCpf.close();
                db.close();
                return;
            }
            cursorCpf.close();


            Cursor cursorEmail = db.rawQuery(
                    "SELECT * FROM usuarios WHERE email=?",
                    new String[]{email}
            );
            if (cursorEmail.moveToFirst()) {
                Toast.makeText(this, "Email já cadastrado!", Toast.LENGTH_SHORT).show();
                cursorEmail.close();
                db.close();
                return;
            }
            cursorEmail.close();


            ContentValues values = new ContentValues();
            values.put("nome",     nome);
            values.put("cpf",      cpf);      // salvo sem formatação
            values.put("email",    email);
            values.put("senha",    senha);
            values.put("cep",      cep);
            values.put("estado",   estado);
            values.put("cidade",   cidade);
            values.put("endereco", endereco);

            long resultado = db.insert("usuarios", null, values);
            db.close();

            if (resultado != -1) {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TelaCadastro.this, TelaHome.class));
                finish();
            } else {
                Toast.makeText(this, "Erro ao cadastrar!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
