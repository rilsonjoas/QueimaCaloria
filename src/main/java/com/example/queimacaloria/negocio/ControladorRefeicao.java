package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioRefeicoesArray;
import com.example.queimacaloria.excecoes.RefeicaoNaoEncontradaException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Classe responsável pela lógica de negócio relacionada a refeições.
public class ControladorRefeicao {

    private RepositorioRefeicoesArray repositorio; // Repositório de refeições.

    // Construtor: Inicializa o repositório de refeições.
    public ControladorRefeicao() {
        this.repositorio = RepositorioRefeicoesArray.getInstanciaUnica();
    }

    // Inicializa uma refeição com os dados fornecidos. Se a refeição já existe, atualiza; senão, cria uma nova.
    public void inicializar(Refeicao refeicao, String nome, String descricao, Map<String, Double> macronutrientes) {
        refeicao.setNome(nome);
        refeicao.setDescricao(descricao);
        refeicao.setMacronutrientes(macronutrientes);
        refeicao.setCalorias(calcularCalorias(refeicao)); // Calcula as calorias da refeição.

        try {
            repositorio.salvar(refeicao); // Tenta salvar. Lança exceção se não encontrar.
        } catch (RefeicaoNaoEncontradaException e) {
            repositorio.adicionar(refeicao); // Se não encontrar, adiciona como nova refeição.
        }
    }

    // Calcula o total de calorias da refeição com base nos macronutrientes.
    public int calcularCalorias(Refeicao refeicao) {
        double totalCalorias = 0;
        if (refeicao.getMacronutrientes() != null) {
            for (Map.Entry<String, Double> entry : refeicao.getMacronutrientes().entrySet()) {
                String macro = entry.getKey();
                Double quantidade = entry.getValue();

                if ("Proteínas".equalsIgnoreCase(macro) || "Carboidratos".equalsIgnoreCase(macro)) {
                    totalCalorias += quantidade * 4; // 4 calorias por grama de proteína ou carboidrato.
                } else if ("Gorduras".equalsIgnoreCase(macro)) {
                    totalCalorias += quantidade * 9; // 9 calorias por grama de gordura.
                }
            }
        }
        return (int) Math.round(totalCalorias);
    }

    // Retorna uma cópia dos macronutrientes para evitar modificações diretas na Refeição.
    public Map<String, Double> calcularMacronutrientes(Refeicao refeicao) {
        return new HashMap<>(refeicao.getMacronutrientes()); // Cria uma nova instância do HashMap.
    }

    // Retorna uma lista de todas as refeições no repositório.
    public List<Refeicao> listarRefeicoes() {
        return repositorio.getAll();
    }
}