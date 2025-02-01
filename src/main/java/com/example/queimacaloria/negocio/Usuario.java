package com.example.queimacaloria.negocio;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
@ToString
@Getter

public class Usuario {
    private final UUID id;
    @Setter
    private String nome;
    @Setter
    private String email;
    @Setter
    private String senha;
    @Setter
    private LocalDate dataNascimento;
    @Setter
    private Sexo sexo;
    @Setter
    private float peso;
    @Setter
    private float altura;
    @Setter
    private float imc;
    @Setter
    private ArrayList<Meta> metas;
    @Setter
    private ArrayList<Treino> treinos;
    @Setter
    private ArrayList<Dieta> dietas;

    public enum Sexo {
        MASCULINO,
        FEMININO,
    }

    public Usuario() {
        this.id = UUID.randomUUID();
        this.metas = new ArrayList<>();
        this.treinos = new ArrayList<>();
        this.dietas = new ArrayList<>();
    }

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

}