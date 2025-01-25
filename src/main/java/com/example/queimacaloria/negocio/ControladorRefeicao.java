package com.example.queimacaloria.negocio;

import java.util.*;

public class ControladorRefeicao {

    // Construtor com parâmetros
    public void inicializar(Refeicao refeicao, String nome, String descricao, Map<String, Double> macronutrientes) {
        refeicao.setNome(nome);
        refeicao.setDescricao(descricao);
        refeicao.setMacronutrientes(macronutrientes);
        refeicao.setCalorias(calcularCalorias(refeicao));
    }

    // Método para calcular o total de calorias da refeição com base nos macronutrientes
    public int calcularCalorias(Refeicao refeicao) {
        double totalCalorias = 0;
        if (refeicao.getMacronutrientes() != null) {
            for (Map.Entry<String, Double> entry : refeicao.getMacronutrientes().entrySet()) {
                String macro = entry.getKey();
                Double quantidade = entry.getValue();

                // Considera o fator de conversão calórica para cada macronutriente
                if ("Proteínas".equalsIgnoreCase(macro) || "Carboidratos".equalsIgnoreCase(macro)) {
                    totalCalorias += quantidade * 4; // 4 calorias por grama de proteína ou carboidrato
                } else if ("Gorduras".equalsIgnoreCase(macro)) {
                    totalCalorias += quantidade * 9; // 9 calorias por grama de gordura
                }
            }
        }
        return (int) Math.round(totalCalorias);
    }

    // Retorna uma cópia dos macronutrientes para evitar modificações diretas em
    // Refeição
    public Map<String, Double> calcularMacronutrientes(Refeicao refeicao) {
        return new HashMap<>(refeicao.getMacronutrientes()); // Retorna uma cópia
    }

}