package com.example.petconnect.model;

public class Solicitacao {
    private int    id;
    private int    idUsuario;
    private String idAnimal;
    private String status;       // "Em análise", "Aprovado", "Recusado"
    private String data;
    private String nomeAnimal;   // preenchido via JOIN
    private String nomeOng;      // preenchido via JOIN

    public int    getId()          { return id; }
    public void   setId(int id)    { this.id = id; }

    public int    getIdUsuario()              { return idUsuario; }
    public void   setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getIdAnimal()              { return idAnimal; }
    public void   setIdAnimal(String id)     { this.idAnimal = id; }

    public String getStatus()               { return status; }
    public void   setStatus(String status)  { this.status = status; }

    public String getData()                 { return data; }
    public void   setData(String data)      { this.data = data; }

    public String getNomeAnimal()                   { return nomeAnimal; }
    public void   setNomeAnimal(String nomeAnimal)  { this.nomeAnimal = nomeAnimal; }

    public String getNomeOng()                { return nomeOng; }
    public void   setNomeOng(String nomeOng)  { this.nomeOng = nomeOng; }
}
