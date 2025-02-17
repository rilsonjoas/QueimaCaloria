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
        this.repositorio = RepositorioRefeicoesArray.getInstanciaUnica();
    }

    // Inicializa uma refeição.
    public void inicializar(Refeicao refeicao, String nome, String descricao, Map<String, Double> macronutrientes) {
        refeicao.setNome(nome);
        refeicao.setDescricao(descricao);
        refeicao.setMacronutrientes(macronutrientes);
        refeicao.setCalorias(calcularCalorias(refeicao)); // Calcula e define as calorias.

        try {
            repositorio.salvar(refeicao); // Tenta atualizar.
        } catch (RefeicaoNaoEncontradaException e) {
            repositorio.adicionar(refeicao); // Se não existir, adiciona.
        }
    }

    // Calcula o total de calorias da refeição.
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

    // Calcula os macronutrientes da refeição (retorna uma cópia para evitar modificações externas).
    public Map<String, Double> calcularMacronutrientes(Refeicao refeicao) {
        return new HashMap<>(refeicao.getMacronutrientes()); // Retorna uma cópia.
    }

    // Lista todas as refeições do repositório.
    public List<Refeicao> listarRefeicoes() {
        return repositorio.getAll();
    }

    // Adiciona o método de remoção
    public void remover(UUID id) throws RefeicaoNaoEncontradaException {
        repositorio.remover(id);
    }
}