package com.example.queimacaloria.negocio;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.*;

@Getter
@ToString
public class Exercicio {
    private final UUID id;
    @Setter private String nome;
    @Setter private String descricao;
    @Setter private ArrayList<String> musculosTrabalhados;
    @Setter private TipoExercicio tipo;
    @Setter private int tempo;
    @Setter private double caloriasQueimadasPorMinuto;
    @Setter private boolean concluido;
    @Setter private double caloriasQueimadas;

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

    public Exercicio(String nome, String descricao, ArrayList<String> musculosTrabalhados, TipoExercicio tipo,
            int tempo, double caloriasQueimadasPorMinuto, boolean concluido, double caloriasQueimadas) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.descricao = descricao;
        this.musculosTrabalhados = musculosTrabalhados;
        this.tipo = tipo;
        this.tempo = tempo;
        this.caloriasQueimadasPorMinuto = caloriasQueimadasPorMinuto;
        this.concluido = concluido;
        this.caloriasQueimadas = caloriasQueimadas;
    }
}