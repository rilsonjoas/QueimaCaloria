package com.example.queimacaloria.negocio;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.UUID;
@ToString
@Getter
public class Treino {

    private final UUID id;
    @Setter private String nome;
    @Setter private String tipoDeTreino;
    @Setter private int duracao;
    @Setter private int nivelDeDificuldade;
    @Setter private ArrayList<Exercicio> exercicios;
    @Setter private double caloriasQueimadas;
    @Setter private double progresso;
    @Setter private boolean concluido;

    public Treino() {
        this.id = UUID.randomUUID();
        this.exercicios = new ArrayList<>();
        this.concluido = false;
        this.caloriasQueimadas = 0.0;
        this.progresso = 0.0;
    }

    public Treino(String nome, String tipoDeTreino, int duracao, int nivelDeDificuldade,
                  ArrayList<Exercicio> exercicios, double caloriasQueimadas, double progresso, boolean concluido) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.tipoDeTreino = tipoDeTreino;
        this.duracao = duracao;
        this.nivelDeDificuldade = nivelDeDificuldade;
        this.exercicios = exercicios;
        this.caloriasQueimadas = caloriasQueimadas;
        this.progresso = progresso;
        this.concluido = concluido;
    }

    public Treino(String nome, String tipoDeTreino, String tipo, int duracao, int nivelDeDificuldade,
            ArrayList<Exercicio> exercicios, double caloriasQueimadas, double progresso, boolean concluido) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.tipoDeTreino = tipoDeTreino;
        this.duracao = duracao;
        this.nivelDeDificuldade = nivelDeDificuldade;
        this.exercicios = (exercicios != null) ? exercicios : new ArrayList<>(); // Importante!
        this.caloriasQueimadas = caloriasQueimadas;
        this.progresso = progresso;
        this.concluido = concluido;
    }


    public StringProperty nomeProperty() {
        return new SimpleStringProperty(nome);
    }

    public StringProperty tipoDeTreinoProperty() {
        return new SimpleStringProperty(tipoDeTreino);
    }

    public IntegerProperty duracaoProperty() {
        return new SimpleIntegerProperty(duracao);
    }

    public IntegerProperty nivelDeDificuldadeProperty() {
        return new SimpleIntegerProperty(nivelDeDificuldade);
    }

    public DoubleProperty progressoProperty() {
        return new SimpleDoubleProperty(progresso);
    }

}