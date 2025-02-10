package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioDietasArray;
import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorDieta {

    private RepositorioDietasArray repositorio;

    public ControladorDieta() {
        this.repositorio = RepositorioDietasArray.getInstanciaUnica();
    }

    // Configura (cria ou atualiza) uma dieta.
    public void configurarDieta(Dieta dieta, String nome, Dieta.ObjetivoDieta objetivo, int caloriasDiarias, Usuario usuario) throws DietaNaoEncontradaException {
        dieta.setNome(nome);
        dieta.setObjetivo(objetivo);
        dieta.setCaloriasDiarias(caloriasDiarias);
        dieta.setUsuario(usuario);

        try {
            repositorio.salvar(dieta); // Tenta atualizar.
        } catch (DietaNaoEncontradaException e) {
            repositorio.adicionar(dieta); // Se não existir, adiciona.
        }
    }

    // Insere uma refeição na dieta.
    public void inserirRefeicao(Dieta dieta, Refeicao refeicao) throws DietaNaoEncontradaException {
        if (refeicao != null && !dieta.getRefeicoes().contains(refeicao)) {
            dieta.getRefeicoes().add(refeicao);
            repositorio.salvar(dieta); // Salva após adicionar.
        }
    }

    // Exclui uma refeição da dieta.
    public void excluirRefeicao(Dieta dieta, Refeicao refeicao) throws DietaNaoEncontradaException {
        dieta.getRefeicoes().remove(refeicao);
        repositorio.salvar(dieta); // Salva após remover.
    }

    // Calcula o total de macronutrientes da dieta.
    public Map<String, Double> calcularTotalMacronutrientes(Dieta dieta) {
        Map<String, Double> totalMacronutrientes = new HashMap<>();
        ObservableList<Refeicao> refeicoes = dieta.getRefeicoes(); // Obtenha a lista observável

        for (Refeicao refeicao : refeicoes) { // Agora você pode iterar
            Map<String, Double> macrosRefeicao = refeicao.getMacronutrientes();
            for (Map.Entry<String, Double> entry : macrosRefeicao.entrySet()) {
                totalMacronutrientes.put(entry.getKey(),
                        totalMacronutrientes.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
            }
        }
        return totalMacronutrientes;
    }

    // Calcula o total de calorias da dieta.
    public int calcularTotalCalorias(Dieta dieta) {
        int totalCalorias = 0;
        for (Refeicao refeicao : dieta.getRefeicoes()) {
            totalCalorias += refeicao.getCalorias();
        }
        return totalCalorias;
    }

    // Calcula o progresso da dieta (em porcentagem).
    public double calcularProgresso(Dieta dieta) {
        if (dieta.getCaloriasDiarias() == 0) {
            return 0; // Evita divisão por zero.
        }
        return (double) calcularTotalCalorias(dieta) / dieta.getCaloriasDiarias() * 100;
    }

    // Verifica se a dieta foi concluída.
    public boolean verificarDietaConcluida(Dieta dieta) {
        if (dieta.getObjetivo() == null) {
            return false;
        }
        switch (dieta.getObjetivo()) {
            case PERDA_DE_PESO:
                return calcularTotalCalorias(dieta) <= dieta.getCaloriasDiarias();
            case GANHO_DE_MASSA:
                return calcularTotalCalorias(dieta) >= dieta.getCaloriasDiarias();
            default: // MANUTENCAO
                return false;
        }
    }

    // Lista todas as dietas do repositório.
    public List<Dieta> listarDietas() {
        return repositorio.getAll();
    }
}