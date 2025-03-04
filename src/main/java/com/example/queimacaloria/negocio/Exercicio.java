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

    private UUID id;
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
    private DoubleProperty caloriasQueimadas = new SimpleDoubleProperty(0.0);
    @Setter
    private boolean concluido;


    // Enum para os tipos de exercício.
    public enum TipoExercicio {
        FORCA,
        CARDIO,
        FLEXIBILIDADE,
        EQUILIBRIO,
        AQUATICO,
        RESISTENCIA, OUTRO
    }

    // Construtor padrão da classe Exercicio.
    public Exercicio() {
        this.id = UUID.randomUUID();
        this.musculosTrabalhados = new ArrayList<>();
        this.concluido = false;
    }

    // Construtor da classe Exercicio (simplificado).
    public Exercicio(String nome, String descricao, ArrayList<String> musculosTrabalhados, TipoExercicio tipo,
                     int tempo, double caloriasQueimadas, boolean concluido) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.descricao = descricao;
        this.musculosTrabalhados = (musculosTrabalhados != null) ? musculosTrabalhados : new ArrayList<>();
        this.tipo = tipo;
        this.tempo = tempo;
        this.caloriasQueimadas.set(caloriasQueimadas);  // Taxa por minuto
        this.concluido = concluido;
    }

    // Métodos Property
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
        return caloriasQueimadas;
    }

    public double getCaloriasQueimadas() {
        return caloriasQueimadas.get();
    }

    public void setCaloriasQueimadas(double caloriasQueimadas) {
        this.caloriasQueimadas.set(caloriasQueimadas);
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
}