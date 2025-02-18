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
@Setter
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

    // Construtor sem as calorias
    public Refeicao(String nome, String descricao, Map<String, Double> macronutrientes) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.descricao = descricao;
        this.macronutrientes = macronutrientes;
    }

    public StringProperty nomeProperty() {
        return new SimpleStringProperty(nome);
    }

    public IntegerProperty caloriasProperty() {
        return new SimpleIntegerProperty(calorias);
    }

    public String getMacronutrientesFormatados() {
        if (macronutrientes == null || macronutrientes.isEmpty()) {
            return ""; // Ou "N/A", ou o que você preferir para indicar ausência de dados.
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> entry : macronutrientes.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(String.format("%.1f", entry.getValue())).append("g, ");
        }

        // Remove a última vírgula e espaço:
        if (sb.length() > 2) {
            sb.delete(sb.length() - 2, sb.length());
        }

        return sb.toString();
    }

    public ObjectProperty<Map<String, Double>> macronutrientesProperty() {
        return new SimpleObjectProperty<>(macronutrientes);
    }
}