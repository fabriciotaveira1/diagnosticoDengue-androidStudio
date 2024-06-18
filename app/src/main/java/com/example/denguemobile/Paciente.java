package com.example.denguemobile;

public class Paciente {
    private final int numero;
    private final String nome;
    private final int telefone;
    private final int idade;
    private final String regiao;
    private final String sintomas.

    public Paciente(int numero, String nome, int telefone,int idade, String regiao, String sintomas){
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

    public int getTelefone() {
        return telefone;
    }

    public int getIdade(){
        return idade;
    }

    public String getRegiao(){
        return regiao;
    }

    public String getSintomas() {
        return sintomas;
    }
}
