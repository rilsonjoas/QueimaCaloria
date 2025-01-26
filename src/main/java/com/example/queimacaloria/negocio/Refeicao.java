package com.example.queimacaloria.negocio;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.*;

@Getter
@ToString
public class Refeicao {
    // Getters
    private final UUID id;
    @Setter private String nome;
    @Setter private String descricao;
    @Setter private int calorias;
    @Setter private Map<String, Double> macronutrientes;

    // Construtor
    public Refeicao() {
        this.id = UUID.randomUUID();
    }

    public Refeicao(String nome, String descricao, int calorias, Map<String, Double> macronutrientes) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.descricao = descricao;
        this.calorias = calorias;
        this.macronutrientes = macronutrientes;
    }
}