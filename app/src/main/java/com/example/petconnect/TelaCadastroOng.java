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

        etNome     = findViewById(R.id.etNomeOng);
        etCnpj     = findViewById(R.id.etCnpj);
        etEmail    = findViewById(R.id.etEmail);
        etSenha    = findViewById(R.id.etSenha);
        etCep      = findViewById(R.id.etCep);
        etEstado   = findViewById(R.id.etEstado);
        etCidade   = findViewById(R.id.etCidade);
        etEndereco = findViewById(R.id.etEndereco);
        btnCadastrarOng = findViewById(R.id.btnCadastrarOng);

        btnCadastrarOng.setOnClickListener(view -> {

            String nome     = etNome.getText().toString().trim();
            // remove formatação para salvar igual ao que o login busca
            String cnpj     = etCnpj.getText().toString().trim().replaceAll("[^0-9]", "");
            String email    = etEmail.getText().toString().trim();
            String senha    = etSenha.getText().toString().trim();
            String cep      = etCep.getText().toString().trim();
            String estado   = etEstado.getText().toString().trim();
            String cidade   = etCidade.getText().toString().trim();
            String endereco = etEndereco.getText().toString().trim();

            // VALIDAÇÕES
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

            // VERIFICA CNPJ
            Cursor cursorCnpj = db.rawQuery(
                    "SELECT * FROM ongs WHERE cnpj=?",
                    new String[]{cnpj}
            );
            if (cursorCnpj.moveToFirst()) {
                Toast.makeText(this, "CNPJ já cadastrado!", Toast.LENGTH_SHORT).show();
                cursorCnpj.close();
                db.close();
                return;
            }
            cursorCnpj.close();

            // VERIFICA EMAIL
            Cursor cursorEmail = db.rawQuery(
                    "SELECT * FROM ongs WHERE email=?",
                    new String[]{email}
            );
            if (cursorEmail.moveToFirst()) {
                Toast.makeText(this, "Email já cadastrado!", Toast.LENGTH_SHORT).show();
                cursorEmail.close();
                db.close();
                return;
            }
            cursorEmail.close();

            // SALVAR
            ContentValues values = new ContentValues();
            values.put("nome",     nome);
            values.put("cnpj",     cnpj);     // salvo sem formatação
            values.put("email",    email);
            values.put("senha",    senha);
            values.put("cep",      cep);
            values.put("estado",   estado);
            values.put("cidade",   cidade);
            values.put("endereco", endereco);

            long resultado = db.insert("ongs", null, values);
            db.close();

            if (resultado != -1) {
                Toast.makeText(this, "ONG cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TelaCadastroOng.this, DashboardOng.class));
                finish();
            } else {
                Toast.makeText(this, "Erro ao cadastrar ONG!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
