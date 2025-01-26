package com.example.queimacaloria.negocio;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.*;

@Getter
@ToString
public class Treino {

    private final UUID id;
    @Setter private String nome;
    @Setter private String tipoDeTreino;
    @Setter private int duracao;
    @Setter private int nivelDeDificuldade;
    @Setter private Usuario usuario;
    @Setter private ArrayList<Exercicio> exercicios;
    @Setter private double caloriasQueimadas;
    @Setter private double progresso;
    @Setter private boolean concluido;

    // Construtor
    public Treino() {
        this.id = UUID.randomUUID();
        this.exercicios = new ArrayList<>();
        this.concluido = false;
        this.caloriasQueimadas = 0.0;
        this.progresso = 0.0;
    }

    public Treino(String nome, String tipoDeTreino, int duracao, int nivelDeDificuldade, Usuario usuario,
            ArrayList<Exercicio> exercicios, double caloriasQueimadas, double progresso, boolean concluido) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.tipoDeTreino = tipoDeTreino;
        this.duracao = duracao;
        this.nivelDeDificuldade = nivelDeDificuldade;
        this.usuario = usuario;
        this.exercicios = exercicios;
        this.caloriasQueimadas = caloriasQueimadas;
        this.progresso = progresso;
        this.concluido = concluido;
    }

}
