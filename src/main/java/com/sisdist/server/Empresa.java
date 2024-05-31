package com.sisdist.server;

public class Empresa implements Cliente{
    private final int id;
    private final String nome;
    private final String email;
    private final String senha;
    private final String ramo;
    private final String descricao;

    public Empresa(int id, String nome, String email, String senha, String ramo, String descricao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ramo = ramo;
        this.descricao = descricao;
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

    public String getRamo() {
        return ramo;
    }

    public String getDescricao() {
        return descricao;
    }
}