package com.example.petconnect.model;

public class Ongs {

    private int id;
    private String nome;
    private String cnpj;

    private String telefone;
    private String email;
    private String senha;
    private String cep;
    private String estado;
    private String cidade;
    private String endereco;

    public Ongs() {}

    public Ongs(String nome, String cnpj, String telefone ,String email, String senha,
               String cep, String estado, String cidade, String endereco) {

        this.nome = nome;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.cep = cep;
        this.estado = estado;
        this.cidade = cidade;
        this.endereco = endereco;
    }

    // getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }


    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.cnpj = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}