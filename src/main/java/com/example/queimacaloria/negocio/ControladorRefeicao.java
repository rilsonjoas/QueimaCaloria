package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioRefeicoesArray;
import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ControladorRefeicao {

    private RepositorioRefeicoesArray repositorio;

    public ControladorRefeicao() {
        this.repositorio = RepositorioRefeicoesArray.getInstanciaUnica(); // Singleton!
    }

    public void inicializar(Refeicao refeicao, String nome, String descricao, Map<String, Double> macronutrientes) {
        refeicao.setNome(nome);
        refeicao.setDescricao(descricao);
        refeicao.setMacronutrientes(macronutrientes);
        refeicao.setCalorias(calcularCalorias(refeicao));

        try {
            repositorio.salvar(refeicao);
        } catch (RefeicaoNaoEncontradaException e) {
            repositorio.adicionar(refeicao);
        }
    }

    public int calcularCalorias(Refeicao refeicao) {
        double totalCalorias = 0;
        if (refeicao.getMacronutrientes() != null) {
            for (Map.Entry<String, Double> entry : refeicao.getMacronutrientes().entrySet()) {
                String macro = entry.getKey();
                Double quantidade = entry.getValue();

                if ("Proteínas".equalsIgnoreCase(macro) || "Carboidratos".equalsIgnoreCase(macro)) {
                    totalCalorias += quantidade * 4;
                } else if ("Gorduras".equalsIgnoreCase(macro)) {
                    totalCalorias += quantidade * 9;
                }
            }
        }
        return (int) Math.round(totalCalorias);
    }

    public Map<String, Double> calcularMacronutrientes(Refeicao refeicao) {
        return new HashMap<>(refeicao.getMacronutrientes());
    }

    public List<Refeicao> listarRefeicoes() {
        if (repositorio == null) {
            System.err.println("ERRO CRÍTICO: Repositório nulo em ControladorRefeicao.listarRefeicoes()!");
        }
        List<Refeicao> refeicoes = repositorio.getAll();
        System.out.println("ControladorRefeicao.listarRefeicoes(): Refeições retornadas: " + refeicoes); // LOG
        return refeicoes;
    }

    public void remover(UUID id) throws RefeicaoNaoEncontradaException {
        repositorio.remover(id);
    }
}