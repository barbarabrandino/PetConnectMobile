package com.example.petconnect.repository;

import com.example.petconnect.model.Pet;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class PetRepository {

    private static final String COLLECTION = "pets";

    private final CollectionReference petsRef;


    public interface OnPetsLoadedListener {
        void onSuccess(List<Pet> pets);
        void onFailure(Exception e);
    }

    public PetRepository() {
        petsRef = FirebaseFirestore.getInstance().collection(COLLECTION);
    }

    public com.google.firebase.firestore.ListenerRegistration observarTodos(
            OnPetsLoadedListener listener) {

        return petsRef
                .orderBy("nome")
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        listener.onFailure(error);
                        return;
                    }
                    if (snapshots == null) return;

                    List<Pet> pets = new ArrayList<>();
                    for (var doc : snapshots.getDocuments()) {
                        Pet pet = doc.toObject(Pet.class);
                        if (pet != null) pets.add(pet);
                    }
                    listener.onSuccess(pets);
                });
    }

    public com.google.firebase.firestore.ListenerRegistration observarComFiltros(
            String tipo,
            String tamanho,
            String idade,
            OnPetsLoadedListener listener) {

        Query query = petsRef;

        if (tipo != null && !tipo.isEmpty()) {
            query = query.whereEqualTo("tipo", tipo);
        }
        if (tamanho != null && !tamanho.isEmpty()) {
            query = query.whereEqualTo("tamanho", tamanho);
        }
        if (idade != null && !idade.isEmpty()) {
            query = query.whereEqualTo("idade", idade);
        }

        return query.addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                listener.onFailure(error);
                return;
            }
            if (snapshots == null) return;

            List<Pet> pets = new ArrayList<>();
            for (var doc : snapshots.getDocuments()) {
                Pet pet = doc.toObject(Pet.class);
                if (pet != null) pets.add(pet);
            }
            listener.onSuccess(pets);
        });
    }

    public com.google.firebase.firestore.ListenerRegistration observarPorNome(
            String busca,
            OnPetsLoadedListener listener) {


        String fim = busca + "\uf8ff";

        return petsRef
                .whereGreaterThanOrEqualTo("nome", busca)
                .whereLessThanOrEqualTo("nome", fim)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        listener.onFailure(error);
                        return;
                    }
                    if (snapshots == null) return;

                    List<Pet> pets = new ArrayList<>();
                    for (var doc : snapshots.getDocuments()) {
                        Pet pet = doc.toObject(Pet.class);
                        if (pet != null) pets.add(pet);
                    }
                    listener.onSuccess(pets);
                });
    }
}
