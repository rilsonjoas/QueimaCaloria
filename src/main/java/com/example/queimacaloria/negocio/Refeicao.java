package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioRefeicoesArray;
import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

@ToString
@Getter
public class Refeicao {

    private UUID id;
    private StringProperty nome;
    private StringProperty descricao;
    private IntegerProperty calorias;
    private Map<String, Double> macronutrientes;

    // Construtor padrão da classe Refeicao.
    public Refeicao() {
        this.id = UUID.randomUUID();
        this.nome = new SimpleStringProperty("");
        this.descricao = new SimpleStringProperty("");
        this.calorias = new SimpleIntegerProperty(0);
    }

    // Construtor da classe Refeicao.
    public Refeicao(String nome, String descricao, int calorias, Map<String, Double> macronutrientes) {
        this.id = UUID.randomUUID();
        this.nome = new SimpleStringProperty(nome);
        this.descricao = new SimpleStringProperty(descricao);
        this.calorias = new SimpleIntegerProperty(calorias);
        this.macronutrientes = macronutrientes;
    }

    // Construtor sem as calorias.
    public Refeicao(String nome, String descricao, Map<String, Double> macronutrientes) {
        this.id = UUID.randomUUID();
        this.nome =  new SimpleStringProperty(nome);
        this.descricao = new SimpleStringProperty(descricao);
        this.macronutrientes = macronutrientes;
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    // Métodos Property
    public StringProperty nomeProperty() {
        return nome;
    }

    public StringProperty descricaoProperty() {
        return descricao;
    }

    public IntegerProperty caloriasProperty() {
        return calorias;
    }

    public Map<String, Double> getMacronutrientes() {
        return macronutrientes;
    }

    public void setMacronutrientes(Map<String, Double> macronutrientes) {
        this.macronutrientes = macronutrientes;
    }


    // Formata os macronutrientes para exibição.
    public String getMacronutrientesFormatados() {
        if (macronutrientes == null || macronutrientes.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> entry : macronutrientes.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(String.format("%.1f", entry.getValue())).append("g, ");
        }

        if (sb.length() > 2) {
            sb.delete(sb.length() - 2, sb.length());
        }

        return sb.toString();
    }

    public ObjectProperty<Map<String, Double>> macronutrientesProperty() {
        return new SimpleObjectProperty<>(macronutrientes);
    }
    public String getNome() {
        return nome.get();
    }

    public String getDescricao() {
        return descricao.get();
    }
    public int getCalorias() {
        return calorias.get();
    }
    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }

    public void setCalorias(int calorias) {
        if (this.calorias == null) {
            this.calorias = new SimpleIntegerProperty(calorias);
        } else {
            this.calorias.set(calorias);
        }
    }

}