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
        System.out.println("ControladorDieta.configurarDieta: Iniciando...");
        System.out.println("ControladorDieta.configurarDieta: Usuário recebido: " + (usuario != null ? usuario.getEmail() : "NULO")); // PRINT
        dieta.setNome(nome);
        dieta.setObjetivo(objetivo);
        dieta.setCaloriasDiarias(caloriasDiarias);
        dieta.setUsuario(usuario); // <- A associação crucial

        System.out.println("ControladorDieta.configurarDieta: Dieta antes de salvar: " + dieta);

        try {
            repositorio.salvar(dieta); // Tenta atualizar.
        } catch (DietaNaoEncontradaException e) {
            System.out.println("ControladorDieta.configurarDieta: Dieta não encontrada, adicionando..."); // PRINT
            repositorio.adicionar(dieta); // Se não existir, adiciona.
        }
        System.out.println("ControladorDieta.configurarDieta: Dieta após salvar/adicionar" + dieta); //PRINT
    }

    public List<Dieta> listarDietas() {
        System.out.println("ControladorDieta.listarDietas - Aqui estão as dietas: "+ repositorio.getAll());
        return repositorio.getAll();
    }

    // Insere uma refeição na dieta.
    public void inserirRefeicao(Dieta dieta, Refeicao refeicao) throws DietaNaoEncontradaException {
        System.out.println("ControladorDieta.inserirRefeicao() chamado. Dieta: " + dieta.getNome() + ", Refeicao: " + refeicao.getNome()); //PRINT
        if (refeicao != null && !dieta.getRefeicoes().contains(refeicao)) {
            dieta.getRefeicoes().add(refeicao);
            System.out.println("Refeicao adicionada. Nova lista de refeições: " + dieta.getRefeicoes()); // PRINT
            repositorio.salvar(dieta); // Salva após adicionar.
        }
    }

    public void excluirRefeicao(Dieta dieta, Refeicao refeicao) throws DietaNaoEncontradaException {
        System.out.println("ControladorDieta.excluirRefeicao() chamado. Dieta: " + dieta.getNome() + ", Refeicao: " + refeicao.getNome()); //PRINT
        dieta.getRefeicoes().remove(refeicao);
        System.out.println("Refeicao removida. Nova lista de refeições: " + dieta.getRefeicoes()); // PRINT
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
        System.out.println("ControladorDieta.calcularProgresso() chamado para dieta: " + dieta.getNome()); // PRINT
        System.out.println("  Calorias Diárias: " + dieta.getCaloriasDiarias()); // PRINT
        System.out.println("  Refeições: " + dieta.getRefeicoes()); //PRINT

        if (dieta.getCaloriasDiarias() == 0) {
            System.out.println("  Calorias Diárias = 0, retornando 0"); // PRINT
            return 0; // Evita divisão por zero.
        }
        double progresso = (double) calcularTotalCalorias(dieta) / dieta.getCaloriasDiarias() * 100;
        System.out.println("  Progresso calculado: " + progresso); // PRINT
        return progresso;
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


}