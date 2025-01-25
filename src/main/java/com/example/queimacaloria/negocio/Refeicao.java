package com.example.queimacaloria.negocio;

import java.util.*;

public class Refeicao {
    private UUID id;
    private String nome;
    private String descricao;
    private int calorias;
    private Map<String, Double> macronutrientes;

    // Construtor
    public Refeicao() {
        this.id = UUID.randomUUID();
    }

    public Refeicao(UUID id, String nome, String descricao, int calorias, Map<String, Double> macronutrientes) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.calorias = calorias;
        this.macronutrientes = macronutrientes;
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

    public int getCalorias() {
        return calorias;
    }

    public Map<String, Double> getMacronutrientes() {
        return macronutrientes;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public void setMacronutrientes(Map<String, Double> macronutrientes) {
        this.macronutrientes = macronutrientes;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}