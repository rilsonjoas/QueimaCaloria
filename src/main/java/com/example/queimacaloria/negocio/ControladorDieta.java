package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioDietasArray;
import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;

import java.util.*;

// Classe responsável pela lógica de negócio relacionada a dietas.
public class ControladorDieta {

    private RepositorioDietasArray repositorio; // Repositório para armazenar as dietas.

    // Construtor: Inicializa o repositório de dietas.
    public ControladorDieta() {
        this.repositorio = RepositorioDietasArray.getInstanciaUnica();
    }

    // Configura os dados de uma dieta. Se a dieta já existe (tem ID), ela é atualizada. Senão, uma nova dieta é criada.
    public void configurarDieta(Dieta dieta, String nome, Dieta.ObjetivoDieta objetivo, int caloriasDiarias, Usuario usuario) throws DietaNaoEncontradaException {
        dieta.getNome().set(nome);
        dieta.getObjetivo().set(objetivo);
        dieta.getCaloriasDiarias().set(caloriasDiarias);
        dieta.getUsuario().set(usuario);

        try {
            repositorio.salvar(dieta); // Tenta atualizar a dieta. Lança exceção se não encontrar.
        } catch (DietaNaoEncontradaException e) {
            repositorio.adicionar(dieta); // Se não encontrar, adiciona como uma nova dieta.
        }
    }

    // Adiciona uma refeição à dieta.
    public void inserirRefeicao(Dieta dieta, Refeicao refeicao) throws DietaNaoEncontradaException {
        if (refeicao != null && !dieta.getRefeicoes().get().contains(refeicao)) {
            dieta.getRefeicoes().get().add(refeicao);
            repositorio.salvar(dieta); // Salva a dieta após adicionar a refeição.
        }
    }

    // Remove uma refeição da dieta.
    public void excluirRefeicao(Dieta dieta, Refeicao refeicao) throws DietaNaoEncontradaException {
        dieta.getRefeicoes().get().remove(refeicao);
        repositorio.salvar(dieta); // Salva a dieta após remover a refeição.
    }


    public Map<String, Double> calcularTotalMacronutrientes(Dieta dieta) {
        Map<String, Double> totalMacronutrientes = new HashMap<>();
        for (Refeicao refeicao : dieta.getRefeicoes().get()) {
            Map<String, Double> macrosRefeicao = refeicao.getMacronutrientes();

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

    // Calcula o progresso da dieta (em porcentagem).
    public double calcularProgresso(Dieta dieta) {
        if (dieta.getCaloriasDiarias().get() == 0) {
            return 0; // Evita divisão por zero.
        }
        return (double) calcularTotalCalorias(dieta) / dieta.getCaloriasDiarias().get() * 100;
    }

    // Verifica se a dieta foi concluída com base no objetivo e calorias.
    public boolean verificarDietaConcluida(Dieta dieta) {
        if (dieta.getObjetivo().get() == null) {
            return false; // Sem objetivo, a dieta não pode ser concluída.
        }

        switch (dieta.getObjetivo().get()) {
            case PERDA_DE_PESO:
                return calcularTotalCalorias(dieta) <= dieta.getCaloriasDiarias().get();
            case GANHO_DE_MASSA:
                return calcularTotalCalorias(dieta) >= dieta.getCaloriasDiarias().get();
            default: // MANUTENCAO ou outros casos.
                return false; // Considera que a dieta de manutenção nunca é "concluída".
        }
    }

    // Retorna uma lista de todas as dietas no repositório.
    public List<Dieta> listarDietas() {
        return repositorio.getAll();
    }
}
