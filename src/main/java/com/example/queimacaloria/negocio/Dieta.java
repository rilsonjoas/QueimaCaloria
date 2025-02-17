package com.example.queimacaloria.negocio;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
public class Dieta {

    private final UUID id = UUID.randomUUID();
    private StringProperty nome = new SimpleStringProperty("");
    private ObjectProperty<ObjetivoDieta> objetivo = new SimpleObjectProperty<>();
    private IntegerProperty caloriasDiarias = new SimpleIntegerProperty(0);

    // Usando MapProperty e ListProperty para coleções observáveis:
    private MapProperty<String, Double> macronutrientes = new SimpleMapProperty<>(FXCollections.observableHashMap());
    private ListProperty<Refeicao> refeicoes = new SimpleListProperty<>(FXCollections.observableArrayList());

    private ObjectProperty<Usuario> usuario = new SimpleObjectProperty<>();

    public enum ObjetivoDieta {
        PERDA_DE_PESO, GANHO_DE_MASSA, MANUTENCAO
    }

    public Dieta(String nome, ObjetivoDieta objetivo, int caloriasDiarias,
                 Map<String, Double> macronutrientes, ArrayList<Refeicao> refeicoes, Usuario usuario) {
        this.nome.set(nome);
        this.objetivo.set(objetivo);
        this.caloriasDiarias.set(caloriasDiarias);
        this.usuario.set(usuario);

        // Copiando os dados para evitar compartilhamento acidental:
        if (macronutrientes != null) {
            this.macronutrientes.putAll(macronutrientes);
        }
        if (refeicoes != null) {
            this.refeicoes.addAll(refeicoes);
        }
    }

    // Método para calcular o total de calorias dentro da classe Dieta:
    public int calcularTotalCalorias() {
        int total = 0;
        for (Refeicao refeicao : this.getRefeicoes()) { //Usa o getRefeicoes()
            total += refeicao.getCalorias();
        }
        return total;
    }


    // Método para calcular o progresso dentro da classe Dieta:
    public double calcularProgresso() {
        // Exemplo simples de cálculo de progresso (adapte à sua lógica):
        if (refeicoes.isEmpty()) {
            return 0.0;
        }

        int totalCaloriasConsumidas = 0;
        for (Refeicao refeicao : refeicoes) {
            totalCaloriasConsumidas += refeicao.getCalorias();
        }

        // O progresso é a porcentagem de calorias consumidas em relação às calorias diárias:
        return (double) totalCaloriasConsumidas / caloriasDiarias.get() * 100.0;
    }

    // Getters:
    public UUID getId() { return id; }

    public String getNome() { return nome.get(); }
    public void setNome(String nome) { this.nome.set(nome); }

    public ObjetivoDieta getObjetivo() { return objetivo.get(); }
    public void setObjetivo(ObjetivoDieta objetivo) { this.objetivo.set(objetivo); }

    public int getCaloriasDiarias() { return caloriasDiarias.get(); }
    public void setCaloriasDiarias(int caloriasDiarias) { this.caloriasDiarias.set(caloriasDiarias); }

    public ObservableMap<String, Double> getMacronutrientes() { return macronutrientes.get(); }
    public void setMacronutrientes(Map<String, Double> macronutrientes) { this.macronutrientes.putAll(macronutrientes); }

    public ObservableList<Refeicao> getRefeicoes() { return refeicoes.get(); }
    public void setRefeicoes(ArrayList<Refeicao> refeicoes) { this.refeicoes.setAll(refeicoes); }

    public Usuario getUsuario() { return usuario.get(); }
    public void setUsuario(Usuario usuario) { this.usuario.set(usuario); }


    // Métodos Property da classe Dieta
    public StringProperty nomeProperty() { return nome; }
    public ObjectProperty<ObjetivoDieta> objetivoProperty() { return objetivo; }
    public IntegerProperty caloriasDiariasProperty() { return caloriasDiarias; }
    public ObjectProperty<Usuario> usuarioProperty() { return usuario; }
    public MapProperty<String, Double> macronutrientesProperty(){ return macronutrientes;}
    public ListProperty<Refeicao> refeicoesProperty() { return refeicoes;}


    // equals() e hashCode() baseados no ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dieta dieta = (Dieta) o;
        return id.equals(dieta.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}