package com.example.petconnect.model;

public class Solicitacao {
    private int    id;
    private String status, data, nomeAnimal, nomeOng;
    private String emailUsuario, cpfUsuario, enderecoUsuario;
    private String cepUsuario, estadoUsuario, cidadeUsuario;
    private String nomeSolicitante, telefone, moradia;
    private String outrosAnimais, experiencia, observacoes;

    public int    getId()                      { return id; }
    public void   setId(int id)                { this.id = id; }
    public String getStatus()                  { return status; }
    public void   setStatus(String v)          { this.status = v; }
    public String getData()                    { return data; }
    public void   setData(String v)            { this.data = v; }
    public String getNomeAnimal()              { return nomeAnimal; }
    public void   setNomeAnimal(String v)      { this.nomeAnimal = v; }
    public String getNomeOng()                 { return nomeOng; }
    public void   setNomeOng(String v)         { this.nomeOng = v; }
    public String getEmailUsuario()            { return emailUsuario; }
    public void   setEmailUsuario(String v)    { this.emailUsuario = v; }
    public String getCpfUsuario()              { return cpfUsuario; }
    public void   setCpfUsuario(String v)      { this.cpfUsuario = v; }
    public String getEnderecoUsuario()         { return enderecoUsuario; }
    public void   setEnderecoUsuario(String v) { this.enderecoUsuario = v; }
    public String getCepUsuario()              { return cepUsuario; }
    public void   setCepUsuario(String v)      { this.cepUsuario = v; }
    public String getEstadoUsuario()           { return estadoUsuario; }
    public void   setEstadoUsuario(String v)   { this.estadoUsuario = v; }
    public String getCidadeUsuario()           { return cidadeUsuario; }
    public void   setCidadeUsuario(String v)   { this.cidadeUsuario = v; }
    public String getNomeSolicitante()         { return nomeSolicitante; }
    public void   setNomeSolicitante(String v) { this.nomeSolicitante = v; }
    public String getTelefone()                { return telefone; }
    public void   setTelefone(String v)        { this.telefone = v; }
    public String getMoradia()                 { return moradia; }
    public void   setMoradia(String v)         { this.moradia = v; }
    public String getOutrosAnimais()           { return outrosAnimais; }
    public void   setOutrosAnimais(String v)   { this.outrosAnimais = v; }
    public String getExperiencia()             { return experiencia; }
    public void   setExperiencia(String v)     { this.experiencia = v; }
    public String getObservacoes()             { return observacoes; }
    public void   setObservacoes(String v)     { this.observacoes = v; }
}