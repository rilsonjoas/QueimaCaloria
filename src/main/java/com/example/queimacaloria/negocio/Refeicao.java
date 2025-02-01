package com.example.queimacaloria.negocio;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

@ToString
@Getter
public class Refeicao {

    private final UUID id;
    @Setter
    private String nome;
    @Setter
    private String descricao;
    @Setter
    private int calorias;
    @Setter
    private Map<String, Double> macronutrientes;

    public Refeicao() {
        this.id = UUID.randomUUID();
    }

    public Refeicao(String nome, String descricao, int calorias, Map<String, Double> macronutrientes) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.descricao = descricao;
        this.calorias = calorias;
        this.macronutrientes = macronutrientes;
    }

    public StringProperty nomeProperty() {
        return new SimpleStringProperty(nome);
    }

    public IntegerProperty caloriasProperty() {
        return new SimpleIntegerProperty(calorias);
    }

    public ObjectProperty<Map<String, Double>> macronutrientesProperty() {
        return new SimpleObjectProperty<>(macronutrientes);
    }


}