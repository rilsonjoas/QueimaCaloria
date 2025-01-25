package com.example.queimacaloria.negocio;

import java.util.*;

public class Dieta {
    private UUID id;
    private String nome;
    private Objetivo objetivo;
    private int caloriasDiarias;
    private Map<String, Double> macronutrientes;
    private ArrayList<Refeicao> refeicoes;
    private Usuario usuario;

    // Enum para representar os objetivos da dieta
    public enum Objetivo {
        PERDA_DE_PESO, GANHO_DE_MASSA, MANUTENCAO;
    }

    // Construtor
    public Dieta() {
        this.id = UUID.randomUUID();
        this.macronutrientes = new HashMap<>(); // or whatever Map implementation you prefer
        this.refeicoes = new ArrayList<>();
    }

    public Dieta(UUID id, String nome, Objetivo objetivo, int caloriasDiarias, Map<String, Double> macronutrientes,
            ArrayList<Refeicao> refeicoes, Usuario usuario) {
        this.id = id;
        this.nome = nome;
        this.objetivo = objetivo;
        this.caloriasDiarias = caloriasDiarias;
        this.macronutrientes = macronutrientes;
        this.refeicoes = refeicoes;
        this.usuario = usuario;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public int getCaloriasDiarias() {
        return caloriasDiarias;
    }

    public Map<String, Double> getMacronutrientes() {
        return macronutrientes;
    }

    public ArrayList<Refeicao> getRefeicoes() {
        return refeicoes;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    // Setters
    public void setId(UUID id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public void setCaloriasDiarias(int caloriasDiarias) {
        this.caloriasDiarias = caloriasDiarias;
    }

    public void setMacronutrientes(Map<String, Double> macronutrientes) {
        this.macronutrientes = macronutrientes;
    }

    public void setRefeicoes(ArrayList<Refeicao> refeicoes) {
        this.refeicoes = refeicoes;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}