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

import androidx.appcompat.app.AppCompatActivity;

import com.example.petconnect.database.DatabaseConection;

public class TelaCadastro extends AppCompatActivity {

    EditText etNome;
    EditText etCpf;
    EditText etEmail;
    EditText etSenha;
    EditText etCep;
    EditText etEstado;
    EditText etCidade;
    EditText etEndereco;

    Button btnCadastrar;

    DatabaseConection banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        banco = new DatabaseConection(this);

        // CAMPOS

        etNome = findViewById(R.id.etNome);
        etCpf = findViewById(R.id.etCpf);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etCep = findViewById(R.id.etCep);
        etEstado = findViewById(R.id.etEstado);
        etCidade = findViewById(R.id.etCidade);
        etEndereco = findViewById(R.id.etEndereco);

        btnCadastrar = findViewById(R.id.btnCadastrar);

        // BOTÃO CADASTRAR

        btnCadastrar.setOnClickListener(view -> {

            String nome = etNome.getText().toString().trim();
            String cpf = etCpf.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String senha = etSenha.getText().toString().trim();
            String cep = etCep.getText().toString().trim();
            String estado = etEstado.getText().toString().trim();
            String cidade = etCidade.getText().toString().trim();
            String endereco = etEndereco.getText().toString().trim();

            // VALIDAÇÕES

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

            SQLiteDatabase db = banco.getWritableDatabase();

            // VERIFICA CPF

            Cursor cursorCpf = db.rawQuery(
                    "SELECT * FROM usuarios WHERE cpf=?",
                    new String[]{cpf}
            );

            if (cursorCpf.moveToFirst()) {

                Toast.makeText(
                        TelaCadastro.this,
                        "CPF já cadastrado!",
                        Toast.LENGTH_SHORT
                ).show();

                cursorCpf.close();
                return;
            }

            // VERIFICA EMAIL

            Cursor cursorEmail = db.rawQuery(
                    "SELECT * FROM usuarios WHERE email=?",
                    new String[]{email}
            );

            if (cursorEmail.moveToFirst()) {

                Toast.makeText(
                        TelaCadastro.this,
                        "Email já cadastrado!",
                        Toast.LENGTH_SHORT
                ).show();

                cursorEmail.close();
                return;
            }

            // SALVAR

            ContentValues values = new ContentValues();

            values.put("nome", nome);
            values.put("cpf", cpf);
            values.put("email", email);
            values.put("senha", senha);
            values.put("cep", cep);
            values.put("estado", estado);
            values.put("cidade", cidade);
            values.put("endereco", endereco);

            long resultado = db.insert(
                    "usuarios",
                    null,
                    values
            );

            // RESULTADO

            if (resultado != -1) {

                Toast.makeText(
                        TelaCadastro.this,
                        "Cadastro realizado com sucesso!",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(
                        TelaCadastro.this,
                        TelaHome.class
                );

                startActivity(intent);

                finish();

            } else {

                Toast.makeText(
                        TelaCadastro.this,
                        "Erro ao cadastrar!",
                        Toast.LENGTH_SHORT
                ).show();
            }

            cursorCpf.close();
            cursorEmail.close();
            db.close();
        });
    }
}