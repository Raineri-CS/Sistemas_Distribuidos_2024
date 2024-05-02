package com.sisdist.server;

public class Candidato {
    private final int id;
    private final String nome;
    private final String email;
    private final String senha;

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