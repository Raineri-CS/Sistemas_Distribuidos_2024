package com.sisdist.server;

public class Candidato {
    private int id;
    private String nome;
    private String email;
    private String senha;

    public Candidato(int id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha(){
        return senha;
    }
}