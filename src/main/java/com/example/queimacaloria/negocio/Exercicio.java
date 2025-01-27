package com.example.queimacaloria.negocio;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;
import java.util.UUID;

public class Exercicio {

    private final UUID id;
    private String nome;
    private String descricao;
    private ArrayList<String> musculosTrabalhados;
    private TipoExercicio tipo;
    private int tempo;
    private double caloriasQueimadasPorMinuto;
    private boolean concluido;
    private double caloriasQueimadas;

    public enum TipoExercicio {
        FORCA,
        CARDIO,
        FLEXIBILIDADE,
        EQUILIBRIO,
        AQUATICO,
        OUTRO
    }

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

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ArrayList<String> getMusculosTrabalhados() {
        return musculosTrabalhados;
    }

    public void setMusculosTrabalhados(ArrayList<String> musculosTrabalhados) {
        this.musculosTrabalhados = musculosTrabalhados;
    }

    public TipoExercicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoExercicio tipo) {
        this.tipo = tipo;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public double getCaloriasQueimadasPorMinuto() {
        return caloriasQueimadasPorMinuto;
    }

    public void setCaloriasQueimadasPorMinuto(double caloriasQueimadasPorMinuto) {
        this.caloriasQueimadasPorMinuto = caloriasQueimadasPorMinuto;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public double getCaloriasQueimadas() {
        return caloriasQueimadas;
    }

    public void setCaloriasQueimadas(double caloriasQueimadas) {
        this.caloriasQueimadas = caloriasQueimadas;
    }

    public StringProperty nomeProperty() {
        return new SimpleStringProperty(nome);
    }

    public ObjectProperty<TipoExercicio> tipoProperty() {
        return new SimpleObjectProperty<>(tipo);
    }

    public IntegerProperty tempoProperty() {
        return new SimpleIntegerProperty(tempo);
    }

    public DoubleProperty caloriasQueimadasProperty() {
        return new SimpleDoubleProperty(caloriasQueimadas);
    }

    @Override
    public String toString() {
        return "Exercicio{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", musculosTrabalhados=" + musculosTrabalhados +
                ", tipo=" + tipo +
                ", tempo=" + tempo +
                ", caloriasQueimadasPorMinuto=" + caloriasQueimadasPorMinuto +
                ", concluido=" + concluido +
                ", caloriasQueimadas=" + caloriasQueimadas +
                '}';
    }
}