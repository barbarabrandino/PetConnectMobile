package com.example.petconnect.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petconnect.R;
import com.example.petconnect.database.DatabaseConection;
import com.example.petconnect.model.Solicitacao;

import java.util.List;

public class SolicitacaoOngAdapter extends RecyclerView.Adapter<SolicitacaoOngAdapter.ViewHolder> {

    private final Context           context;
    private final List<Solicitacao> lista;
    private final boolean           isOng; // true = ONG (pode aprovar/recusar) | false = usuário (só visualiza)

    public SolicitacaoOngAdapter(Context context, List<Solicitacao> lista, boolean isOng) {
        this.context = context;
        this.lista   = lista;
        this.isOng   = isOng;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_solicitacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Solicitacao s = lista.get(position);

        holder.tvNomeAnimal.setText(s.getNomeAnimal() != null ? s.getNomeAnimal() : "Animal");
        holder.tvNomeOng.setText("Solicitante: " + (s.getNomeOng() != null ? s.getNomeOng() : "Usuário"));
        holder.tvData.setText(s.getData() != null ? "Data: " + s.getData() : "");
        holder.tvStatus.setText(s.getStatus());

        // Badge de status
        atualizarBadge(holder.tvStatus, s.getStatus());

        // Botões Aprovar/Recusar: só visíveis para ONG quando status é "Em análise"
        if (isOng && "Em análise".equals(s.getStatus())) {
            holder.layoutAcoes.setVisibility(View.VISIBLE);
        } else {
            holder.layoutAcoes.setVisibility(View.GONE);
        }

        holder.btnVerDetalhes.setOnClickListener(v -> mostrarDetalhes(s));

        holder.btnAprovar.setOnClickListener(v ->
                confirmarAcao(s, position, holder, "Aprovar",
                        "Confirma a aprovação desta solicitação?", "Aprovado"));

        holder.btnRecusar.setOnClickListener(v ->
                confirmarAcao(s, position, holder, "Recusar",
                        "Confirma a recusa desta solicitação?", "Recusado"));
    }

    // ── Confirmação e atualização no banco ────────────────────────────────

    private void confirmarAcao(Solicitacao s, int position, ViewHolder holder,
                               String titulo, String mensagem, String novoStatus) {
        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    if (atualizarStatusNoBanco(s.getId(), novoStatus)) {
                        s.setStatus(novoStatus);
                        // Atualiza o badge e esconde os botões sem recarregar tudo
                        atualizarBadge(holder.tvStatus, novoStatus);
                        holder.tvStatus.setText(novoStatus);
                        holder.layoutAcoes.setVisibility(View.GONE);
                        notifyItemChanged(position);
                        Toast.makeText(context,
                                "Solicitação " + novoStatus.toLowerCase() + " com sucesso!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Erro ao atualizar. Tente novamente.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private boolean atualizarStatusNoBanco(int idSolicitacao, String novoStatus) {
        try {
            DatabaseConection con = new DatabaseConection(context);
            SQLiteDatabase db = con.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("status", novoStatus);
            int rows = db.update(
                    DatabaseConection.TABELA_SOLICITACOES,
                    values,
                    "id = ?",
                    new String[]{String.valueOf(idSolicitacao)}
            );
            db.close();
            return rows > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // ── Badge de status ───────────────────────────────────────────────────

    private void atualizarBadge(TextView tvStatus, String status) {
        int badgeColor;
        switch (status) {
            case "Aprovado":
                badgeColor = Color.parseColor("#2E7D32");
                break;
            case "Recusado":
                badgeColor = Color.parseColor("#C62828");
                break;
            default: // Em análise
                badgeColor = Color.parseColor("#F57F17");
                break;
        }
        tvStatus.setTextColor(Color.WHITE);
        tvStatus.setBackgroundTintList(ColorStateList.valueOf(badgeColor));
    }

    // ── Dialog de detalhes ────────────────────────────────────────────────

    private void mostrarDetalhes(Solicitacao s) {
        String mensagem =
                "Animal: "   + nvl(s.getNomeAnimal())       + "\n" +
                        "Status: "   + nvl(s.getStatus())           + "\n" +
                        "Data: "     + nvl(s.getData())             + "\n\n" +
                        "── Dados do solicitante ──\n" +
                        "Nome: "     + nvl(s.getNomeOng())          + "\n" +
                        "E-mail: "   + nvl(s.getEmailUsuario())     + "\n" +
                        "CPF: "      + nvl(s.getCpfUsuario())       + "\n" +
                        "CEP: "      + nvl(s.getCepUsuario())       + "\n" +
                        "Estado: "   + nvl(s.getEstadoUsuario())    + "\n" +
                        "Cidade: "   + nvl(s.getCidadeUsuario())    + "\n" +
                        "Endereço: " + nvl(s.getEnderecoUsuario());

        new AlertDialog.Builder(context)
                .setTitle("Detalhes da Solicitação #" + s.getId())
                .setMessage(mensagem)
                .setPositiveButton("Fechar", null)
                .show();
    }

    private String nvl(String val) {
        return (val != null && !val.isEmpty()) ? val : "Não informado";
    }

    // ── Boilerplate ───────────────────────────────────────────────────────

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView     tvNomeAnimal, tvNomeOng, tvData, tvStatus;
        Button       btnVerDetalhes, btnAprovar, btnRecusar;
        LinearLayout layoutAcoes;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeAnimal   = itemView.findViewById(R.id.tvNomeAnimal);
            tvNomeOng      = itemView.findViewById(R.id.tvNomeOng);
            tvData         = itemView.findViewById(R.id.tvData);
            tvStatus       = itemView.findViewById(R.id.tvStatus);
            btnVerDetalhes = itemView.findViewById(R.id.btnVerDetalhes);
            btnAprovar     = itemView.findViewById(R.id.btnAprovar);
            btnRecusar     = itemView.findViewById(R.id.btnRecusar);
            layoutAcoes    = itemView.findViewById(R.id.layoutAcoes);
        }
    }
}