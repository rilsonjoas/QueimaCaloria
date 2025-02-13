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

@Getter
@ToString
public class Exercicio {

    private final UUID id;
    @Setter
    private String nome;
    @Setter
    private String descricao;
    @Setter
    private ArrayList<String> musculosTrabalhados;
    @Setter
    private TipoExercicio tipo;
    @Setter
    private int tempo;
    @Setter
    private double caloriasQueimadasPorMinuto;
    @Setter
    private boolean concluido;
    @Setter
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

    // Construtor que trata corretamente o caso de musculosTrabalhados ser nulo.
    public Exercicio(String nome, String descricao, ArrayList<String> musculosTrabalhados, TipoExercicio tipo,
                     int tempo, double caloriasQueimadasPorMinuto, boolean concluido, double caloriasQueimadas) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.descricao = descricao;
        this.musculosTrabalhados = (musculosTrabalhados != null) ? musculosTrabalhados : new ArrayList<>(); // IMPORTANTE!
        this.tipo = tipo;
        this.tempo = tempo;
        this.caloriasQueimadasPorMinuto = caloriasQueimadasPorMinuto;
        this.concluido = concluido;
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

}