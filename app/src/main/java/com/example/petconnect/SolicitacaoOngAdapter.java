package com.example.petconnect;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petconnect.database.DatabaseConection;
import com.example.petconnect.model.Solicitacao;

import java.util.List;

public class SolicitacaoOngAdapter extends RecyclerView.Adapter<SolicitacaoOngAdapter.ViewHolder> {

    private final Context           context;
    private final List<Solicitacao> lista;
    private final boolean           isOng;

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

        String statusAtual = s.getStatus() != null ? s.getStatus().trim().toLowerCase() : "";

        holder.tvNomeAnimal.setText(nvl(s.getNomeAnimal(), "Animal"));
        holder.tvNomeOng.setText(nvl(s.getNomeOng(), "Usuario nao informado"));
        holder.tvData.setText(s.getData() != null ? "Data: " + s.getData() : "");
        holder.tvStatus.setText(nvl(s.getStatus(), "Em analise"));

        atualizarBadge(holder.tvStatus, statusAtual);

        boolean isPendente = statusAtual.equals("pendente")
                          || statusAtual.equals("em analise")
                          || statusAtual.equals("em analise")
                          || statusAtual.contains("analise")
                          || statusAtual.contains("análise")
                          || statusAtual.isEmpty();

        if (isOng && isPendente) {
            holder.layoutAcoes.setVisibility(View.VISIBLE);
        } else {
            holder.layoutAcoes.setVisibility(View.GONE);
        }

        holder.btnVerDetalhes.setOnClickListener(v -> mostrarDetalhes(s));

        holder.btnAprovar.setOnClickListener(v ->
                confirmarAcao(s, position, "Aprovado", "Aprovar solicitacao?",
                        "Deseja aprovar a adocao de " + nvl(s.getNomeAnimal(), "este animal") + "?"));

        holder.btnRecusar.setOnClickListener(v ->
                confirmarAcao(s, position, "Recusado", "Recusar solicitacao?",
                        "Deseja recusar a adocao de " + nvl(s.getNomeAnimal(), "este animal") + "?"));
    }

    private void mostrarDetalhes(Solicitacao s) {
        String msg =
                "Animal: "         + nvl(s.getNomeAnimal(),      "Nao informado") + "\n" +
                "Solicitante: "    + nvl(s.getNomeSolicitante(), nvl(s.getNomeOng(), "Nao informado")) + "\n" +
                "E-mail: "         + nvl(s.getEmailUsuario(),    "Nao informado") + "\n" +
                "CPF: "            + nvl(s.getCpfUsuario(),      "Nao informado") + "\n" +
                "Telefone: "       + nvl(s.getTelefone(),        "Nao informado") + "\n" +
                "Endereco: "       + nvl(s.getEnderecoUsuario(), "Nao informado") + "\n" +
                "CEP: "            + nvl(s.getCepUsuario(),      "Nao informado") + "\n" +
                "Cidade/Estado: "  + nvl(s.getCidadeUsuario(),   "") + " / " + nvl(s.getEstadoUsuario(), "Nao informado") + "\n" +
                "Moradia: "        + nvl(s.getMoradia(),         "Nao informado") + "\n" +
                "Outros animais: " + nvl(s.getOutrosAnimais(),   "Nao informado") + "\n" +
                "Experiencia: "    + nvl(s.getExperiencia(),     "Nao informado") + "\n" +
                "Observacoes: "    + nvl(s.getObservacoes(),     "Nenhuma")       + "\n" +
                "Status: "         + nvl(s.getStatus(),          "Em analise")    + "\n" +
                "Data: "           + nvl(s.getData(),            "Nao informada");

        new AlertDialog.Builder(context)
                .setTitle("Solicitacao #" + s.getId())
                .setMessage(msg)
                .setPositiveButton("Fechar", null)
                .show();
    }

    private void confirmarAcao(Solicitacao s, int position, String novoStatus, String titulo, String msg) {
        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(msg)
                .setPositiveButton("Confirmar", (d, w) -> {
                    atualizarStatus(s.getId(), novoStatus);
                    s.setStatus(novoStatus);
                    lista.set(position, s);
                    notifyItemChanged(position);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void atualizarStatus(int id, String novoStatus) {
        DatabaseConection con = new DatabaseConection(context);
        SQLiteDatabase db = con.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", novoStatus);
        db.update(DatabaseConection.TABELA_SOLICITACOES, cv, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    private void atualizarBadge(TextView tv, String statusLower) {
        int cor;
        if (statusLower.contains("aprovado"))      cor = Color.parseColor("#2E7D32");
        else if (statusLower.contains("recusado")) cor = Color.parseColor("#C62828");
        else                                       cor = Color.parseColor("#F57F17");
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundTintList(ColorStateList.valueOf(cor));
    }

    private String nvl(String val, String fallback) {
        return (val != null && !val.isEmpty()) ? val : fallback;
    }

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