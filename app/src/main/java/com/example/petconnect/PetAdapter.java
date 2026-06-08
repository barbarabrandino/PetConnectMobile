package com.example.petconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.petconnect.R;
import com.example.petconnect.model.Pet;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class PetAdapter extends ListAdapter<Pet, PetAdapter.PetViewHolder> {

    private final Set<String> favoritos = new HashSet<>();

    public interface OnPetClickListener {
        void onVerPerfil(Pet pet);
        void onFavoritarToggle(Pet pet, boolean favoritado);
    }

    private final OnPetClickListener listener;

    public PetAdapter(OnPetClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Pet> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Pet>() {
                @Override
                public boolean areItemsTheSame(@NonNull Pet a, @NonNull Pet b) {
                    return a.getId().equals(b.getId());
                }
                @Override
                public boolean areContentsTheSame(@NonNull Pet a, @NonNull Pet b) {
                    return a.getNome().equals(b.getNome())
                            && a.getIdade().equals(b.getIdade())
                            && a.getFotoUrl().equals(b.getFotoUrl())
                            && a.isVacinado() == b.isVacinado()
                            && a.isCastrado() == b.isCastrado();
                }
            };

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pet_card, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Pet pet = getItem(position);
        holder.bind(pet, favoritos.contains(pet.getId()), listener);
    }


    public void marcarFavorito(String petId) {
        favoritos.add(petId);
    }


    public void setFavorito(String petId, boolean favoritado) {
        if (favoritado) {
            favoritos.add(petId);
        } else {
            favoritos.remove(petId);
        }
        for (int i = 0; i < getCurrentList().size(); i++) {
            if (getCurrentList().get(i).getId().equals(petId)) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    static class PetViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivPhoto, ivFavorite;
        private final TextView tvName, tvAge, tvBreed;
        private final TextView tagVacinado, tagTamanho, tagCastrado;
        private final TextView tvShelter, tvDescription;
        private final Button btnVerPerfil;

        PetViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto       = itemView.findViewById(R.id.ivPetPhoto);
            ivFavorite    = itemView.findViewById(R.id.ivFavorite);
            tvName        = itemView.findViewById(R.id.tvPetName);
            tvAge         = itemView.findViewById(R.id.tvPetAge);
            tvBreed       = itemView.findViewById(R.id.tvPetBreed);
            tagVacinado   = itemView.findViewById(R.id.tagVacinado);
            tagTamanho    = itemView.findViewById(R.id.tagTamanho);
            tagCastrado   = itemView.findViewById(R.id.tagCastrado);
            tvShelter     = itemView.findViewById(R.id.tvShelter);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnVerPerfil  = itemView.findViewById(R.id.btnVerPerfil);
        }

        void bind(Pet pet, boolean favoritado, OnPetClickListener listener) {
            Context ctx = itemView.getContext();

            tvName.setText(pet.getNome());
            tvAge.setText(pet.getIdade());
            tvBreed.setText(pet.getRaca());
            tvShelter.setText(pet.getAbrigo());
            tvDescription.setText(pet.getDescricao());
            tagTamanho.setText(pet.getTamanho());

            tagVacinado.setVisibility(pet.isVacinado() ? View.VISIBLE : View.GONE);
            tagCastrado.setVisibility(pet.isCastrado() ? View.VISIBLE : View.GONE);
            if (pet.isVacinado()) tagVacinado.setText("Vacinado");
            if (pet.isCastrado()) tagCastrado.setText("Castrado");


            String foto = pet.getFotoUrl();
            if (foto != null && foto.startsWith("/")) {
                Glide.with(ctx).load(new File(foto)).centerCrop()
                        .placeholder(R.drawable.ic_cat_placeholder)
                        .error(R.drawable.ic_cat_placeholder)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivPhoto);
            } else {
                Glide.with(ctx).load(foto).centerCrop()
                        .placeholder(R.drawable.ic_cat_placeholder)
                        .error(R.drawable.ic_cat_placeholder)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivPhoto);
            }

            atualizarIconeFavorito(favoritado);


            ivFavorite.setOnClickListener(v -> {
                boolean novoEstado = !favoritado;
                listener.onFavoritarToggle(pet, novoEstado);
            });

            btnVerPerfil.setOnClickListener(v -> listener.onVerPerfil(pet));
        }

        private void atualizarIconeFavorito(boolean favoritado) {
            Context ctx = itemView.getContext();
            if (favoritado) {
                ivFavorite.setImageResource(R.drawable.ic_heart_filled);
                ivFavorite.setColorFilter(ctx.getColor(android.R.color.holo_red_light));
            } else {
                ivFavorite.setImageResource(R.drawable.ic_heart_outline);
                ivFavorite.setColorFilter(ctx.getColor(android.R.color.white));
            }
        }
    }
}
