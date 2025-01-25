package com.example.queimacaloria.negocio;

import java.util.*;

public class Exercicio {
    private UUID id;
    private String nome;
    private String descricao;
    private ArrayList<String> musculosTrabalhados;
    private TipoExercicio tipo;
    private int tempo;
    private double caloriasQueimadasPorMinuto;
    private boolean concluido;
    private double caloriasQueimadas;

    // Enum para representar os tipos de exercício
    public enum TipoExercicio {
        FORCA,
        CARDIO,
        FLEXIBILIDADE,
        EQUILIBRIO,
        AQUATICO,
        OUTRO
    }

    // Construtor
    public Exercicio() {
        this.id = UUID.randomUUID();
        this.musculosTrabalhados = new ArrayList<>();
        this.concluido = false;
    }

    public Exercicio(UUID id, String nome, String descricao, ArrayList<String> musculosTrabalhados, TipoExercicio tipo,
            int tempo, double caloriasQueimadasPorMinuto, boolean concluido, double caloriasQueimadas) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.musculosTrabalhados = musculosTrabalhados;
        this.tipo = tipo;
        this.tempo = tempo;
        this.caloriasQueimadasPorMinuto = caloriasQueimadasPorMinuto;
        this.concluido = concluido;
        this.caloriasQueimadas = caloriasQueimadas;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public ArrayList<String> getMusculosTrabalhados() {
        return musculosTrabalhados;
    }

    public TipoExercicio getTipo() {
        return tipo;
    }

    public int getTempo() {
        return tempo;
    }

    public double getCaloriasQueimadasPorMinuto() {
        return caloriasQueimadasPorMinuto;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public double getCaloriasQueimadas() {
        return caloriasQueimadas;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setTipo(TipoExercicio tipo) {
        this.tipo = tipo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public void setCaloriasQueimadasPorMinuto(double caloriasQueimadasPorMinuto) {
        this.caloriasQueimadasPorMinuto = caloriasQueimadasPorMinuto;
    }

    public void setMusculosTrabalhados(ArrayList<String> musculosTrabalhados) {
        this.musculosTrabalhados = musculosTrabalhados;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public void setCaloriasQueimadas(double caloriasQueimadas) {
        this.caloriasQueimadas = caloriasQueimadas;
    }

}