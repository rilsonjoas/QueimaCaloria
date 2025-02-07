package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioDietasArray;
import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;

import java.util.*;

public class ControladorDieta {

    RepositorioDietasArray repositorio;

    public ControladorDieta() {
        repositorio = RepositorioDietasArray.getInstanciaUnica();
    }

    public void configurarDieta(Dieta dieta, String nome, Dieta.ObjetivoDieta objetivo, int caloriasDiarias,
            Usuario usuario) throws DietaNaoEncontradaException {
        dieta.getNome().set(nome);
        dieta.getObjetivo().set(objetivo);
        dieta.getCaloriasDiarias().set(caloriasDiarias);
        dieta.getUsuario().set(usuario);

        // Importante: Se a dieta já existe, usa salvar. Se não existe, adiciona.
        try {
            repositorio.salvar(dieta);
        } catch (DietaNaoEncontradaException e) {
            repositorio.adicionar(dieta);
        }
    }

    public void inserirRefeicao(Dieta dieta, Refeicao refeicao) throws DietaNaoEncontradaException {
        if (refeicao != null && !dieta.getRefeicoes().get().contains(refeicao)) {
            dieta.getRefeicoes().get().add(refeicao);
            repositorio.salvar(dieta);
        }
    }

    public void excluirRefeicao(Dieta dieta, Refeicao refeicao) throws DietaNaoEncontradaException {
        dieta.getRefeicoes().get().remove(refeicao);
        repositorio.salvar(dieta);
    }

    public Map<String, Double> calcularTotalMacronutrientes(Dieta dieta) {
        Map<String, Double> totalMacronutrientes = new HashMap<>();
        for (Refeicao refeicao : dieta.getRefeicoes().get()) {
            Map<String, Double> macrosRefeicao = refeicao.getMacronutrientes(); // Sem .get() aqui

            for (Map.Entry<String, Double> entry : macrosRefeicao.entrySet()) {
                totalMacronutrientes.put(entry.getKey(),
                        totalMacronutrientes.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
            }
        }
        return totalMacronutrientes;
    }

    public int calcularTotalCalorias(Dieta dieta) {
        int totalCalorias = 0;
        for (Refeicao refeicao : dieta.getRefeicoes().get()) {
            totalCalorias += refeicao.getCalorias();
        }
        return totalCalorias;
    }

    public double calcularProgresso(Dieta dieta) {
        // Usar .get() para obter o valor int da IntegerProperty
        if (dieta.getCaloriasDiarias().get() == 0) {
            return 0;
        }
        return (double) calcularTotalCalorias(dieta) / dieta.getCaloriasDiarias().get() * 100; // .get() aqui
    }

    public boolean verificarDietaConcluida(Dieta dieta) {
        // Usar .get() para obter o valor do ObjectProperty e IntegerProperty
        if (dieta.getObjetivo().get() == null) {
            return false;
        }

        switch (dieta.getObjetivo().get()) { // .get() aqui também
            case PERDA_DE_PESO:
                return calcularTotalCalorias(dieta) <= dieta.getCaloriasDiarias().get(); // .get() aqui
            case GANHO_DE_MASSA:
                return calcularTotalCalorias(dieta) >= dieta.getCaloriasDiarias().get(); // .get() aqui
            default:
                return false;
        }
    }

    // Método para listar todas as dietas
    public List<Dieta> listarDietas() {
        return repositorio.getAll();
    }
}
