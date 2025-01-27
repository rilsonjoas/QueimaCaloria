package com.example.queimacaloria.negocio;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Map;
import java.util.UUID;

public class Refeicao {

    private final UUID id;
    private String nome;
    private String descricao;
    private int calorias;
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

    public int getCalorias() {
        return calorias;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public Map<String, Double> getMacronutrientes() {
        return macronutrientes;
    }

    public void setMacronutrientes(Map<String, Double> macronutrientes) {
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

    @Override
    public String toString() {
        return "Refeicao{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", calorias=" + calorias +
                ", macronutrientes=" + macronutrientes +
                '}';
    }
}