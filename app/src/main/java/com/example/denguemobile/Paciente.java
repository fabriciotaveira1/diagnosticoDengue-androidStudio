package com.example.denguemobile;

public class Paciente {
    private final int numero;
    private final String nome;
    private final String telefone; // Alterado para String
    private final int idade; // Alterado para int
    private final String regiao;
    private final String sintomas; // Adicionado ponto e vírgula aqui

    public Paciente(int numero, String nome, String telefone, int idade, String regiao, String sintomas) {
        this.numero = numero;
        this.nome = nome;
        this.telefone = telefone;
        this.idade = idade;
        this.regiao = regiao;
        this.sintomas = sintomas;
    }

    public int getNumero() {
        return numero;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public int getIdade() {
        return idade;
    }

    public String getRegiao() {
        return regiao;
    }

    public String getSintomas() {
        return sintomas;
    }
}
