package com.example.queimacaloria.negocio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class Usuario {
    private UUID id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private float peso;
    private float altura;
    private float imc;
    private ArrayList<Meta> metas;
    private ArrayList<Treino> treinos;
    private ArrayList<Dieta> dietas;

    // Enum para representar o sexo do usuário
    public enum Sexo {
        MASCULINO,
        FEMININO,
    }

    // Construtor
    public Usuario() {
        this.id = UUID.randomUUID();
        this.metas = new ArrayList<>();
        this.treinos = new ArrayList<>();
        this.dietas = new ArrayList<>();
    }

    // Construtor com parâmetros
    public Usuario(String nome, String email, LocalDate dataNascimento, Sexo sexo, float peso, float altura) {
        this(nome, email, dataNascimento, sexo, peso, altura, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public Usuario(String nome, String email, LocalDate dataNascimento, Sexo sexo, float peso, float altura, ArrayList<Meta> metas, ArrayList<Treino> treinos, ArrayList<Dieta> dietas) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
        this.metas = metas;
        this.treinos = treinos;
        this.dietas = dietas;
    }



    // Getters
    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public float getPeso() {
        return peso;
    }

    public float getAltura() {
        return altura;
    }

    public float getImc() {
        return imc;
    }

    public ArrayList<Meta> getMetas() {
        return metas;
    }

    public ArrayList<Treino> getTreinos() {
        return treinos;
    }

    public ArrayList<Dieta> getDietas() {
        return dietas;
    }

    // Setters
    public void setAltura(float altura) {
        this.altura = altura;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setImc(float imc) {
        this.imc = imc;
    }

    public void setMetas(ArrayList<Meta> metas) {
        this.metas = metas;
    }

    public void setTreinos(ArrayList<Treino> treinos) {
        this.treinos = treinos;
    }

    public void setDietas(ArrayList<Dieta> dietas) {
        this.dietas = dietas;
    }
}