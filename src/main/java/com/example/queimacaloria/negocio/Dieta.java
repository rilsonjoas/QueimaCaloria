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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@ToString
public class Dieta {
    private final UUID id;
    @Setter
    private String nome;
    @Setter
    private Objetivo objetivo;
    @Setter
    private int caloriasDiarias;
    @Setter
    private Map<String, Double> macronutrientes;
    @Setter
    private ArrayList<Refeicao> refeicoes;
    @Setter
    private Usuario usuario;

    public enum Objetivo {
        PERDA_DE_PESO, GANHO_DE_MASSA, MANUTENCAO;
    }

    public Dieta() {
        this.id = UUID.randomUUID();
        this.macronutrientes = new HashMap<>();
        this.refeicoes = new ArrayList<>();
    }

    public Dieta(String nome, Objetivo objetivo, int caloriasDiarias, Map<String, Double> macronutrientes,
                 ArrayList<Refeicao> refeicoes, Usuario usuario) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.objetivo = objetivo;
        this.caloriasDiarias = caloriasDiarias;
        this.macronutrientes = macronutrientes;
        this.refeicoes = refeicoes;
        this.usuario = usuario;
    }

    public StringProperty nomeProperty() {
        return new SimpleStringProperty(nome);
    }

    public ObjectProperty<Objetivo> objetivoProperty() {
        return new SimpleObjectProperty<>(objetivo);
    }

    public IntegerProperty caloriasDiariasProperty() {
        return new SimpleIntegerProperty(caloriasDiarias);
    }

    public DoubleProperty progressProperty() {
        ControladorDieta controladorDieta = new ControladorDieta();
        return new SimpleDoubleProperty(controladorDieta.getProgresso(this));
    }
}