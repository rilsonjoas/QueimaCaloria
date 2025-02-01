package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioDietasArray;
import com.example.queimacaloria.dados.RepositorioExerciciosArray;
import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;

import java.util.*;

public class ControladorDieta {

    RepositorioDietasArray repositorio;

    public ControladorDieta() {
        repositorio = RepositorioDietasArray.getInstanciaUnica();
    }

    // Construtor com parâmetros
    public void inicializar(Dieta dieta, String nome, Dieta.Objetivo objetivo, int caloriasDiarias, Usuario usuario) throws DietaNaoEncontradaException {
        dieta.setNome(nome);
        dieta.setObjetivo(objetivo);
        dieta.setCaloriasDiarias(caloriasDiarias);
        dieta.setUsuario(usuario);
        repositorio.adicionar(dieta);
    }

    // Adiciona uma refeição à lista de refeições
    public void adicionarRefeicao(Dieta dieta, Refeicao refeicao) throws DietaNaoEncontradaException {
        if (refeicao != null && !dieta.getRefeicoes().contains(refeicao)) {
            dieta.getRefeicoes().add(refeicao);
            repositorio.salvar(dieta);
        }
    }

    // Remove uma refeição da lista de refeições
    public void removerRefeicao(Dieta dieta, Refeicao refeicao) throws DietaNaoEncontradaException {
        dieta.getRefeicoes().remove(refeicao);
        repositorio.salvar(dieta);
    }

    // Calcula o total de macronutrientes da dieta com base nas refeições que a compõem
    public Map<String, Double> calcularMacronutrientes(Dieta dieta) {
        Map<String, Double> totalMacronutrientes = new HashMap<>();
        for (Refeicao refeicao : dieta.getRefeicoes()) {
            Map<String, Double> macrosRefeicao = refeicao.getMacronutrientes();

            for (Map.Entry<String, Double> entry : macrosRefeicao.entrySet()) {
                totalMacronutrientes.put(entry.getKey(),
                        totalMacronutrientes.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
            }
        }
        return totalMacronutrientes;
    }

    // Calcula o total de calorias da dieta com base nas refeições
    public int calcularCaloriasTotais(Dieta dieta) {
        int totalCalorias = 0;
        for (Refeicao refeicao : dieta.getRefeicoes()) {
            totalCalorias += refeicao.getCalorias();
        }
        return totalCalorias;
    }

    // Calcula o progresso da dieta em relação à meta de calorias diárias
    public double getProgresso(Dieta dieta) {

        if (dieta.getCaloriasDiarias() == 0) {
            return 0;
        }
        return (double) calcularCaloriasTotais(dieta) / dieta.getCaloriasDiarias() * 100;
    }

    // Verifica se a dieta foi concluída com base no objetivo e nas calorias consumidas
    public boolean isConcluido(Dieta dieta) {
        if (dieta.getObjetivo() == null) {
            return false;
        }

        switch (dieta.getObjetivo()) {
            case PERDA_DE_PESO:
                return calcularCaloriasTotais(dieta) <= dieta.getCaloriasDiarias();
            case GANHO_DE_MASSA:
                return calcularCaloriasTotais(dieta) >= dieta.getCaloriasDiarias();
            default:
                return false;
        }
    }

}