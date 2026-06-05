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
import com.example.petconnect.model.Solicitacao;

import java.util.List;

public class SolicitacaoAdapter extends RecyclerView.Adapter<SolicitacaoAdapter.ViewHolder> {

    private final Context           context;
    private final List<Solicitacao> lista;

    public SolicitacaoAdapter(Context context, List<Solicitacao> lista) {
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
        holder.tvNomeOng.setText(s.getNomeOng() != null ? s.getNomeOng() : "ONG não informada");
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

        // Abre dialog com os dados da solicitação do usuário
        holder.btnVerDetalhes.setOnClickListener(v -> {
            String mensagem =
                    "Animal: " + nvl(s.getNomeAnimal()) + "\n" +
                            "ONG: "    + nvl(s.getNomeOng())    + "\n" +
                            "Status: " + nvl(s.getStatus())     + "\n" +
                            "Data: "   + nvl(s.getData());

            new AlertDialog.Builder(context)
                    .setTitle("Solicitação #" + s.getId())
                    .setMessage(mensagem)
                    .setPositiveButton("Fechar", null)
                    .show();
        });
    }

    /** Retorna o valor ou "Não informado" se nulo/vazio. */
    private String nvl(String val) {
        return (val != null && !val.isEmpty()) ? val : "Não informado";
    }

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