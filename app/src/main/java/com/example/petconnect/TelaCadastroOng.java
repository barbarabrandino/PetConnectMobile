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

public class TelaCadastroOng extends AppCompatActivity {

    EditText etNome;
    EditText etCnpj;
    EditText etEmail;
    EditText etSenha;
    EditText etCep;
    EditText etEstado;
    EditText etCidade;
    EditText etEndereco;

    Button btnCadastrarOng;

    DatabaseConection banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro_ong);

        banco = new DatabaseConection(this);

        // Campos na tabela

        etNome = findViewById(R.id.etNomeOng);
        etCnpj = findViewById(R.id.etCnpj);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etCep = findViewById(R.id.etCep);
        etEstado = findViewById(R.id.etEstado);
        etCidade = findViewById(R.id.etCidade);
        etEndereco = findViewById(R.id.etEndereco);

        btnCadastrarOng = findViewById(R.id.btnCadastrarOng);

        // Botão de cadastrar Ong

        btnCadastrarOng.setOnClickListener(view -> {

            String nome = etNome.getText().toString().trim();
            String cnpj = etCnpj.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String senha = etSenha.getText().toString().trim();
            String cep = etCep.getText().toString().trim();
            String estado = etEstado.getText().toString().trim();
            String cidade = etCidade.getText().toString().trim();
            String endereco = etEndereco.getText().toString().trim();

            // Validação por CNPJ

            if (TextUtils.isEmpty(nome)) {
                etNome.setError("Digite o nome da ONG");
                return;
            }

            if (TextUtils.isEmpty(cnpj)) {
                etCnpj.setError("Digite o CNPJ");
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

            // Verefica o CNPJ

            Cursor cursorCnpj = db.rawQuery(
                    "SELECT * FROM ongs WHERE cnpj=?",
                    new String[]{cnpj}
            );

            if (cursorCnpj.moveToFirst()) {

                Toast.makeText(
                        TelaCadastroOng.this,
                        "CNPJ já cadastrado!",
                        Toast.LENGTH_SHORT
                ).show();

                cursorCnpj.close();
                return;
            }

            // Verifica se o email é existente

            Cursor cursorEmail = db.rawQuery(
                    "SELECT * FROM ongs WHERE email=?",
                    new String[]{email}
            );

            if (cursorEmail.moveToFirst()) {

                Toast.makeText(
                        TelaCadastroOng.this,
                        "Email já cadastrado!",
                        Toast.LENGTH_SHORT
                ).show();

                cursorEmail.close();
                return;
            }

            // salvar as infos das ongs

            ContentValues values = new ContentValues();

            values.put("nome", nome);
            values.put("cnpj", cnpj);
            values.put("email", email);
            values.put("senha", senha);
            values.put("cep", cep);
            values.put("estado", estado);
            values.put("cidade", cidade);
            values.put("endereco", endereco);

            long resultado = db.insert(
                    "ongs",
                    null,
                    values
            );

            // Feedback de sucesso/invalidação de conta

            if (resultado != -1) {

                Toast.makeText(
                        TelaCadastroOng.this,
                        "ONG cadastrada com sucesso!",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(
                        TelaCadastroOng.this,
                        DashboardOng.class
                );

                startActivity(intent);

                finish();

            } else {

                Toast.makeText(
                        TelaCadastroOng.this,
                        "Erro ao cadastrar ONG!",
                        Toast.LENGTH_SHORT
                ).show();
            }

            cursorCnpj.close();
            cursorEmail.close();
            db.close();
        });
    }
}