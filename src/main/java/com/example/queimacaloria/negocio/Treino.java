package com.example.queimacaloria.negocio;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;
import java.util.UUID;

public class Treino {

    private final UUID id;
    private String nome;
    private String tipoDeTreino;
    private int duracao;
    private int nivelDeDificuldade;
    private Usuario usuario;
    private ArrayList<Exercicio> exercicios;
    private double caloriasQueimadas;
    private double progresso;
    private boolean concluido;

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

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoDeTreino() {
        return tipoDeTreino;
    }

    public void setTipoDeTreino(String tipoDeTreino) {
        this.tipoDeTreino = tipoDeTreino;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public int getNivelDeDificuldade() {
        return nivelDeDificuldade;
    }

    public void setNivelDeDificuldade(int nivelDeDificuldade) {
        this.nivelDeDificuldade = nivelDeDificuldade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ArrayList<Exercicio> getExercicios() {
        return exercicios;
    }

    public void setExercicios(ArrayList<Exercicio> exercicios) {
        this.exercicios = exercicios;
    }

    public double getCaloriasQueimadas() {
        return caloriasQueimadas;
    }

    public void setCaloriasQueimadas(double caloriasQueimadas) {
        this.caloriasQueimadas = caloriasQueimadas;
    }

    public double getProgresso() {
        return progresso;
    }

    public void setProgresso(double progresso) {
        this.progresso = progresso;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
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

    public DoubleProperty progressProperty() {
        return new SimpleDoubleProperty(progresso);
    }

    @Override
    public String toString() {
        return "Treino{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipoDeTreino='" + tipoDeTreino + '\'' +
                ", duracao=" + duracao +
                ", nivelDeDificuldade=" + nivelDeDificuldade +
                ", usuario=" + usuario +
                ", exercicios=" + exercicios +
                ", caloriasQueimadas=" + caloriasQueimadas +
                ", progresso=" + progresso +
                ", concluido=" + concluido +
                '}';
    }
}