package com.example.queimacaloria.negocio;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
    @Setter private Exercicio.TipoExercicio tipoDeTreino;
    @Setter private int duracao;
    @Setter private int nivelDeDificuldade;
    @Setter private ArrayList<Exercicio> exercicios;
    @Setter private double caloriasQueimadas;
    @Setter private double progresso;
    @Setter private boolean concluido;

    // Adicionado: Campo Usuario
    @Setter private ObjectProperty<Usuario> usuario = new SimpleObjectProperty<>();

    public Treino() {
        this.id = UUID.randomUUID();
        this.exercicios = new ArrayList<>();
        this.concluido = false;
        this.caloriasQueimadas = 0.0;
        this.progresso = 0.0;
    }


    public Treino(String nome, Exercicio.TipoExercicio tipoDeTreino, int duracao, int nivelDeDificuldade,
                  ArrayList<Exercicio> exercicios, double caloriasQueimadas, double progresso, boolean concluido) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.tipoDeTreino = tipoDeTreino;
        this.duracao = duracao;
        this.nivelDeDificuldade = nivelDeDificuldade;
        this.exercicios = (exercicios != null) ? exercicios : new ArrayList<>();
        this.caloriasQueimadas = caloriasQueimadas;
        this.progresso = progresso;
        this.concluido = concluido;
    }



    public double calcularCaloriasTotais() {
        double caloriasTotais = 0;
        for (Exercicio exercicio : this.getExercicios()) {
            caloriasTotais += exercicio.getCaloriasQueimadas() * exercicio.getTempo();
        }
        return caloriasTotais;
    }

    // Métodos Property
    public StringProperty nomeProperty() {
        return new SimpleStringProperty(nome);
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

    // Adicionado: Getter e Property para Usuario
    public Usuario getUsuario() { return usuario.get(); }
    public ObjectProperty<Usuario> usuarioProperty() { return usuario; }
}