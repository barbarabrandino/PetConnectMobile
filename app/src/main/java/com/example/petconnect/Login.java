package com.example.petconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petconnect.database.DatabaseConection;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";


    private static final String WEB_CLIENT_ID =
            "1079108649290-v3hhdbcc43lesm65epdolpe95an32njg.apps.googleusercontent.com";


    private EditText etCpfCnpjLogin, etSenhaLogin;
    private MaterialButton btnEntrar, btnCadastroUsuario, btnCadastroOng, btnGoogleSignIn;


    private DatabaseConection banco;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private boolean loginEmAndamento = false;

    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        loginEmAndamento = false;
                        Task<GoogleSignInAccount> task =
                                GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleGoogleSignInResult(task);
                    }
            );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        banco = new DatabaseConection(this);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        etCpfCnpjLogin     = findViewById(R.id.etCpfCnpjLogin);
        etSenhaLogin       = findViewById(R.id.etSenhaLogin);
        btnEntrar          = findViewById(R.id.btnEntrar);
        btnCadastroUsuario = findViewById(R.id.btnCadastroUsuario);
        btnCadastroOng     = findViewById(R.id.btnCadastroOng);
        btnGoogleSignIn    = findViewById(R.id.btnGoogleSignIn);

        btnEntrar.setOnClickListener(v -> fazerLoginLocal());
        btnGoogleSignIn.setOnClickListener(v -> iniciarFluxoGoogle());
        btnCadastroUsuario.setOnClickListener(v ->
                startActivity(new Intent(this, TelaCadastro.class)));
        btnCadastroOng.setOnClickListener(v ->
                startActivity(new Intent(this, TelaCadastroOng.class)));
    }


    private void fazerLoginLocal() {
        String cpfCnpj = etCpfCnpjLogin.getText().toString().trim().replaceAll("[^0-9]", "");
        String senha   = etSenhaLogin.getText().toString().trim();

        if (TextUtils.isEmpty(cpfCnpj)) {
            etCpfCnpjLogin.setError("Digite o CPF ou CNPJ");
            return;
        }
        if (TextUtils.isEmpty(senha)) {
            etSenhaLogin.setError("Digite a senha");
            return;
        }

        SQLiteDatabase db = banco.getReadableDatabase();
        Cursor cursorUsuario = null;
        Cursor cursorOng     = null;

        try {
            cursorUsuario = db.rawQuery(
                    "SELECT * FROM usuarios WHERE cpf=? AND senha=?",
                    new String[]{cpfCnpj, senha});

            cursorOng = db.rawQuery(
                    "SELECT * FROM ongs WHERE cnpj=? AND senha=?",
                    new String[]{cpfCnpj, senha});

            SharedPreferences prefs =
                    getSharedPreferences("petconnect_prefs", MODE_PRIVATE);

            if (cursorUsuario.moveToFirst()) {
                int    idUsuario   = cursorUsuario.getInt(
                        cursorUsuario.getColumnIndexOrThrow("id"));
                String emailLogado = cursorUsuario.getString(
                        cursorUsuario.getColumnIndexOrThrow("email"));

                prefs.edit()
                        .putString("email_logado",      emailLogado)
                        .putInt("id_usuario_logado",    idUsuario)
                        .putInt("id_ong_logada",        -1)
                        .putString("tipo_usuario",      "usuario")
                        .putBoolean("login_via_google", false)
                        .apply();

                Toast.makeText(this, "Login de usuário realizado!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, TelaHome.class));
                finish();

            } else if (cursorOng.moveToFirst()) {
                int    idOng   = cursorOng.getInt(
                        cursorOng.getColumnIndexOrThrow("id"));
                String nomeOng = cursorOng.getString(
                        cursorOng.getColumnIndexOrThrow("nome"));

                prefs.edit()
                        .putInt("id_ong_logada",        idOng)
                        .putString("nome_ong_logada",   nomeOng)
                        .putInt("id_usuario_logado",    -1)
                        .putString("tipo_usuario",      "ong")
                        .putBoolean("login_via_google", false)
                        .apply();

                Toast.makeText(this, "Login da ONG realizado!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, DashboardOng.class));
                finish();

            } else {
                Toast.makeText(this, "CPF/CNPJ ou senha inválidos", Toast.LENGTH_SHORT).show();
            }

        } finally {
            if (cursorUsuario != null) cursorUsuario.close();
            if (cursorOng     != null) cursorOng.close();
            db.close();
        }
    }

    private void iniciarFluxoGoogle() {
        loginEmAndamento = true;
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "Google Sign-In OK. IdToken nulo? " + (account.getIdToken() == null));
            autenticarFirebaseComGoogle(account.getIdToken());
        } catch (ApiException e) {

            Log.e(TAG, "Google sign-in falhou. Código: " + e.getStatusCode(), e);
            String motivo;
            switch (e.getStatusCode()) {
                case 10:
                    motivo = "SHA-1 não registrado no Firebase (código 10)";
                    break;
                case 7:
                    motivo = "Sem conexão com a internet (código 7)";
                    break;
                case 12500:
                    motivo = "Atualize o Google Play Services (código 12500)";
                    break;
                default:
                    motivo = "Falha no Google Sign-In (código " + e.getStatusCode() + ")";
            }
            Toast.makeText(this, motivo, Toast.LENGTH_LONG).show();
        }
    }

    private void autenticarFirebaseComGoogle(String idToken) {
        if (idToken == null) {
            Toast.makeText(this, "Token Google nulo. Tente novamente.", Toast.LENGTH_LONG).show();
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            salvarPrefsGoogle(firebaseUser);
                            Toast.makeText(this,
                                    "Login com Google realizado!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, TelaHome.class));
                            finish();
                        }
                    } else {
                        // Mostra o erro REAL do Firebase na tela
                        Exception ex = task.getException();
                        String erroDetalhado;

                        if (ex instanceof FirebaseAuthInvalidCredentialsException) {
                            erroDetalhado = "Token inválido. Tente novamente.";
                        } else if (ex instanceof FirebaseAuthUserCollisionException) {
                            erroDetalhado = "Este e-mail já está em uso com outro método de login.";
                        } else {
                            erroDetalhado = ex != null ? ex.getMessage() : "Erro desconhecido";
                        }

                        Log.e(TAG, "signInWithCredential falhou: " + erroDetalhado, ex);
                        Toast.makeText(this, "Erro Firebase: " + erroDetalhado,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void salvarPrefsGoogle(FirebaseUser user) {
        String email       = user.getEmail()       != null ? user.getEmail()       : "";
        String nome        = user.getDisplayName() != null ? user.getDisplayName() : "Usuário Google";
        String firebaseUid = user.getUid();

        getSharedPreferences("petconnect_prefs", MODE_PRIVATE)
                .edit()
                .putString("email_logado",        email)
                .putString("nome_logado",          nome)
                .putString("firebase_uid",         firebaseUid)
                .putInt("id_usuario_logado",       -1)
                .putInt("id_ong_logada",           -1)
                .putString("tipo_usuario",         "usuario")
                .putBoolean("login_via_google",    true)
                .apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!loginEmAndamento) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                startActivity(new Intent(this, TelaHome.class));
                finish();
            }
        }
    }
}