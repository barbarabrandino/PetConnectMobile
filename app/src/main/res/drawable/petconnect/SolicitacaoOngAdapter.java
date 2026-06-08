package com.example.petconnect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petconnect.R;

import java.util.List;

public class SolicitacaoOngAdapter extends RecyclerView.Adapter<SolicitacaoOngAdapter.ViewHolder> {

    private final Context          context;
    private final List<Solicitacao> lista;

    public SolicitacaoOngAdapter(Context context, List<Solicitacao> lista) {
        this.context = context;
        this.lista   = lista;
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

        switch (s.getStatus()) {
            case "Aprovado":
                holder.tvStatus.setTextColor(Color.parseColor("#388E3C")); // verde
                break;
            case "Recusado":
                holder.tvStatus.setTextColor(Color.parseColor("#D32F2F")); // vermelho
                break;
            default: // Em análise
                holder.tvStatus.setTextColor(Color.parseColor("#FBC02D")); // amarelo
                break;
        }

        // Abre o dialog com todos os dados do solicitante
        holder.btnVerDetalhes.setOnClickListener(v -> mostrarDetalhes(s));
    }

    // ── Dialog de detalhes ─────────────────────────────────────────────────

    private void mostrarDetalhes(Solicitacao s) {
        String mensagem =
                "Animal: "    + nvl(s.getNomeAnimal())      + "\n" +
                        "Status: "    + nvl(s.getStatus())          + "\n" +
                        "Data: "      + nvl(s.getData())            + "\n\n" +
                        "── Dados do solicitante ──\n" +
                        "Nome: "      + nvl(s.getNomeOng())         + "\n" +
                        "E-mail: "    + nvl(s.getEmailUsuario())    + "\n" +
                        "CPF: "       + nvl(s.getCpfUsuario())      + "\n" +
                        "CEP: "       + nvl(s.getCepUsuario())      + "\n" +
                        "Estado: "    + nvl(s.getEstadoUsuario())   + "\n" +
                        "Cidade: "    + nvl(s.getCidadeUsuario())   + "\n" +
                        "Endereço: "  + nvl(s.getEnderecoUsuario());

        new AlertDialog.Builder(context)
                .setTitle("Detalhes da Solicitação #" + s.getId())
                .setMessage(mensagem)
                .setPositiveButton("Fechar", null)
                .show();
    }

    /** Retorna o valor ou "Não informado" se nulo/vazio. */
    private String nvl(String val) {
        return (val != null && !val.isEmpty()) ? val : "Não informado";
    }

    // ── Boilerplate ────────────────────────────────────────────────────────

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeAnimal, tvNomeOng, tvData, tvStatus;
        Button   btnVerDetalhes;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeAnimal   = itemView.findViewById(R.id.tvNomeAnimal);
            tvNomeOng      = itemView.findViewById(R.id.tvNomeOng);
            tvData         = itemView.findViewById(R.id.tvData);
            tvStatus       = itemView.findViewById(R.id.tvStatus);
            btnVerDetalhes = itemView.findViewById(R.id.btnVerDetalhes);
        }
    }
}