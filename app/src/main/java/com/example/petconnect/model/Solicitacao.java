package com.example.petconnect.model;

public class Solicitacao {

    private int    id;
    private String status;
    private String data;
    private String nomeAnimal;
    private String nomeOng;

    // Dados do solicitante
    private String emailUsuario;
    private String cpfUsuario;
    private String enderecoUsuario;
    private String cepUsuario;
    private String estadoUsuario;
    private String cidadeUsuario;

    // ── Getters e Setters ──────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getNomeAnimal() { return nomeAnimal; }
    public void setNomeAnimal(String nomeAnimal) { this.nomeAnimal = nomeAnimal; }

    public String getNomeOng() { return nomeOng; }
    public void setNomeOng(String nomeOng) { this.nomeOng = nomeOng; }

    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }

    public String getCpfUsuario() { return cpfUsuario; }
    public void setCpfUsuario(String cpfUsuario) { this.cpfUsuario = cpfUsuario; }

    public String getEnderecoUsuario() { return enderecoUsuario; }
    public void setEnderecoUsuario(String enderecoUsuario) { this.enderecoUsuario = enderecoUsuario; }

    public String getCepUsuario() { return cepUsuario; }
    public void setCepUsuario(String cepUsuario) { this.cepUsuario = cepUsuario; }

    public String getEstadoUsuario() { return estadoUsuario; }
    public void setEstadoUsuario(String estadoUsuario) { this.estadoUsuario = estadoUsuario; }

    public String getCidadeUsuario() { return cidadeUsuario; }
    public void setCidadeUsuario(String cidadeUsuario) { this.cidadeUsuario = cidadeUsuario; }
}