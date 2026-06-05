package com.example.petconnect.model;

public class Animal {

    private int id;
    private String nome;
    private String especie;
    private int idade;
    private String porte;
    private String sexo;
    private String descricao;
    private int idOng;
    private String fotoUrl; // ← campo novo

    public Animal() {
    }

    public Animal(String nome, String especie, int idade,
                  String porte, String sexo,
                  String descricao, int idOng) {
        this.nome = nome;
        this.especie = especie;
        this.idade = idade;
        this.porte = porte;
        this.sexo = sexo;
        this.descricao = descricao;
        this.idOng = idOng;
    }

    public int getId()             { return id; }
    public void setId(int id)      { this.id = id; }

    public String getNome()              { return nome; }
    public void setNome(String nome)     { this.nome = nome; }

    public String getEspecie()               { return especie; }
    public void setEspecie(String especie)   { this.especie = especie; }

    public int getIdade()            { return idade; }
    public void setIdade(int idade)  { this.idade = idade; }

    public String getPorte()             { return porte; }
    public void setPorte(String porte)   { this.porte = porte; }

    public String getSexo()            { return sexo; }
    public void setSexo(String sexo)   { this.sexo = sexo; }

    public String getDescricao()                 { return descricao; }
    public void setDescricao(String descricao)   { this.descricao = descricao; }

    public int getIdOng()              { return idOng; }
    public void setIdOng(int idOng)    { this.idOng = idOng; }

    // ── Foto ──────────────────────────────────────────────────
    public String getFotoUrl()             { return fotoUrl != null ? fotoUrl : ""; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
}
