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

    //MUDANÇA CRUCIAL AQUI:
    private DoubleProperty caloriasQueimadas = new SimpleDoubleProperty(0.0); // Agora é uma Property, inicializada.

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
        //Removido, agora é inicializado na declaração
        //this.caloriasQueimadas = 0.0;
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
        this.caloriasQueimadas.set(caloriasQueimadas); //Usa o set para setar o valor na property.
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

    //Não precisa mais criar um objeto novo, já que agora ele é um atributo da classe
    public DoubleProperty caloriasQueimadasProperty() {
        return caloriasQueimadas;
    }

    //Getter e setter para acessar o valor da property.
    public double getCaloriasQueimadas() {
        return caloriasQueimadas.get();
    }

    public void setCaloriasQueimadas(double caloriasQueimadas) {
        this.caloriasQueimadas.set(caloriasQueimadas); //Usa o set para setar o valor na property
    }
}