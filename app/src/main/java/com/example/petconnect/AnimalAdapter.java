package com.example.petconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.petconnect.R;
import com.example.petconnect.model.Animal;

import java.io.File;
import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {

    public interface OnEditarListener {
        void onEditar(Animal animal);
    }

    public interface OnExcluirListener {
        void onExcluir(Animal animal);
    }

    private final List<Animal> lista;
    private final OnEditarListener onEditar;
    private final OnExcluirListener onExcluir;

    public AnimalAdapter(List<Animal> lista,
                         OnEditarListener onEditar,
                         OnExcluirListener onExcluir) {
        this.lista     = lista;
        this.onEditar  = onEditar;
        this.onExcluir = onExcluir;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_animal_card, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        holder.bind(lista.get(position));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class AnimalViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivFoto;
        private final TextView tvNome, tvEspecie, tvPorte;
        private final Button btnEditar, btnExcluir;

        AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoto     = itemView.findViewById(R.id.ivAnimalFoto);
            tvNome     = itemView.findViewById(R.id.tvAnimalNome);
            tvEspecie  = itemView.findViewById(R.id.tvAnimalEspecie);
            tvPorte    = itemView.findViewById(R.id.tvAnimalPorte);
            btnEditar  = itemView.findViewById(R.id.btnEditarAnimal);
            btnExcluir = itemView.findViewById(R.id.btnExcluirAnimal);
        }

        void bind(Animal animal) {
            Context ctx = itemView.getContext();

            tvNome.setText(animal.getNome());
            tvEspecie.setText(animal.getEspecie() != null ? animal.getEspecie() : "");
            tvPorte.setText(animal.getPorte()     != null ? animal.getPorte()   : "");

            String foto = animal.getFotoUrl();
            if (foto != null && foto.startsWith("/")) {
                Glide.with(ctx)
                        .load(new File(foto))
                        .centerCrop()
                        .placeholder(R.drawable.ic_cat_placeholder)
                        .error(R.drawable.ic_cat_placeholder)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivFoto);
            } else if (foto != null && !foto.isEmpty()) {
                Glide.with(ctx)
                        .load(foto)
                        .centerCrop()
                        .placeholder(R.drawable.ic_cat_placeholder)
                        .error(R.drawable.ic_cat_placeholder)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivFoto);
            } else {
                ivFoto.setImageResource(R.drawable.ic_cat_placeholder);
            }

            btnEditar.setOnClickListener(v  -> onEditar.onEditar(animal));
            btnExcluir.setOnClickListener(v -> onExcluir.onExcluir(animal));
        }
    }
}