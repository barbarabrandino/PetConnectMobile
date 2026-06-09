package com.example.petconnect.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
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
        holder.tvNomeOng.setText(s.getNomeOng()       != null ? s.getNomeOng()    : "ONG não informada");
        holder.tvData.setText(s.getData()             != null ? "Data: " + s.getData() : "");
        holder.tvStatus.setText(s.getStatus());

        atualizarBadge(holder.tvStatus, s.getStatus());

        holder.layoutAcoes.setVisibility(View.GONE);

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

    private void atualizarBadge(TextView tvStatus, String status) {
        int badgeColor;
        switch (status) {
            case "Aprovado": badgeColor = Color.parseColor("#2E7D32"); break;
            case "Recusado": badgeColor = Color.parseColor("#C62828"); break;
            default:         badgeColor = Color.parseColor("#F57F17"); break;
        }
        tvStatus.setTextColor(Color.WHITE);
        tvStatus.setBackgroundTintList(ColorStateList.valueOf(badgeColor));
    }

    private String nvl(String val) {
        return (val != null && !val.isEmpty()) ? val : "Não informado";
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView     tvNomeAnimal, tvNomeOng, tvData, tvStatus;
        Button       btnVerDetalhes;
        LinearLayout layoutAcoes;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeAnimal   = itemView.findViewById(R.id.tvNomeAnimal);
            tvNomeOng      = itemView.findViewById(R.id.tvNomeOng);
            tvData         = itemView.findViewById(R.id.tvData);
            tvStatus       = itemView.findViewById(R.id.tvStatus);
            btnVerDetalhes = itemView.findViewById(R.id.btnVerDetalhes);
            layoutAcoes    = itemView.findViewById(R.id.layoutAcoes);
        }
    }
}