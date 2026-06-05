package com.example.petconnect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petconnect.R;
import com.example.petconnect.model.Solicitacao;

import java.util.List;

public class SolicitacaoOngAdapter extends RecyclerView.Adapter<SolicitacaoOngAdapter.ViewHolder> {

    private final Context context;
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
        // nomeOng aqui guarda o nome do usuário solicitante
        holder.tvNomeOng.setText("Solicitante: " + (s.getNomeOng() != null ? s.getNomeOng() : "Usuário"));
        holder.tvData.setText(s.getData() != null ? "Data: " + s.getData() : "");
        holder.tvStatus.setText(s.getStatus());

        switch (s.getStatus()) {
            case "Aprovado":
                holder.tvStatus.setTextColor(Color.parseColor("#388E3C"));
                break;
            case "Recusado":
                holder.tvStatus.setTextColor(Color.parseColor("#D32F2F"));
                break;
            default:
                holder.tvStatus.setTextColor(Color.parseColor("#FBC02D"));
                break;
        }

        holder.btnVerDetalhes.setOnClickListener(v ->
                Toast.makeText(context,
                        "Solicitação #" + s.getId() + " — " + s.getNomeAnimal(),
                        Toast.LENGTH_SHORT).show()
        );
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