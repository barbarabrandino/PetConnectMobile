package com.example.petconnect.model;

import com.google.firebase.firestore.DocumentId;

/**
 * Modelo que representa um animal disponível para adoção.
 *
 * Estrutura esperada no Firestore (coleção "pets"):
 * {
 *   "nome":        "Pulga",
 *   "idade":       "3 anos",
 *   "raca":        "Vira-lata",
 *   "abrigo":      "Lar dos Felinos",
 *   "descricao":   "Gata muito meiga e carinhosa...",
 *   "fotoUrl":     "https://...",
 *   "vacinado":    true,
 *   "castrado":    true,
 *   "tamanho":     "Médio",   // "Pequeno" | "Médio" | "Grande"
 *   "tipo":        "Gato",    // "Gato" | "Cachorro" | "Outro"
 *   "sexo":        "Fêmea"    // "Macho" | "Fêmea"  ← NOVO CAMPO
 * }
 */
public class Pet {

    @DocumentId
    private String id;

    private String nome;
    private String idade;
    private String raca;
    private String abrigo;
    private String descricao;
    private String fotoUrl;
    private boolean vacinado;
    private boolean castrado;
    private String tamanho;
    private String tipo;
    private String sexo; // ← NOVO: "Macho" | "Fêmea"

    /** Construtor vazio obrigatório para o Firestore desserializar */
    public Pet() {}

    public Pet(String id, String nome, String idade, String raca,
               String abrigo, String descricao, String fotoUrl,
               boolean vacinado, boolean castrado,
               String tamanho, String tipo, String sexo) { // ← sexo adicionado
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.raca = raca;
        this.abrigo = abrigo;
        this.descricao = descricao;
        this.fotoUrl = fotoUrl;
        this.vacinado = vacinado;
        this.castrado = castrado;
        this.tamanho = tamanho;
        this.tipo = tipo;
        this.sexo = sexo; // ← NOVO
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getId()        { return id; }
    public String getNome()      { return nome; }
    public String getIdade()     { return idade; }
    public String getRaca()      { return raca; }
    public String getAbrigo()    { return abrigo; }
    public String getDescricao() { return descricao; }
    public String getFotoUrl()   { return fotoUrl; }
    public boolean isVacinado()  { return vacinado; }
    public boolean isCastrado()  { return castrado; }
    public String getTamanho()   { return tamanho; }
    public String getTipo()      { return tipo; }
    public String getSexo()      { return sexo; } // ← NOVO

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setId(String id)             { this.id = id; }
    public void setNome(String nome)         { this.nome = nome; }
    public void setIdade(String idade)       { this.idade = idade; }
    public void setRaca(String raca)         { this.raca = raca; }
    public void setAbrigo(String abrigo)     { this.abrigo = abrigo; }
    public void setDescricao(String d)       { this.descricao = d; }
    public void setFotoUrl(String fotoUrl)   { this.fotoUrl = fotoUrl; }
    public void setVacinado(boolean v)       { this.vacinado = v; }
    public void setCastrado(boolean c)       { this.castrado = c; }
    public void setTamanho(String tamanho)   { this.tamanho = tamanho; }
    public void setTipo(String tipo)         { this.tipo = tipo; }
    public void setSexo(String sexo)         { this.sexo = sexo; } // ← NOVO
}
