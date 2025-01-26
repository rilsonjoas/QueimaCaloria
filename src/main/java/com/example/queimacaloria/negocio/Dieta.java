package com.example.queimacaloria.negocio;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.*;

@Getter
@ToString
public class Dieta {
    private final UUID id;
    @Setter private String nome;
    @Setter private Objetivo objetivo;
    @Setter private int caloriasDiarias;
    @Setter private Map<String, Double> macronutrientes;
    @Setter private ArrayList<Refeicao> refeicoes;
    @Setter private Usuario usuario;

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

    public Dieta(String nome, Objetivo objetivo, int caloriasDiarias, Map<String, Double> macronutrientes,
            ArrayList<Refeicao> refeicoes, Usuario usuario) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.objetivo = objetivo;
        this.caloriasDiarias = caloriasDiarias;
        this.macronutrientes = macronutrientes;
        this.refeicoes = refeicoes;
        this.usuario = usuario;
    }
}