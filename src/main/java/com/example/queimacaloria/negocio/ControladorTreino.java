package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioTreinosArray;
import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;

import java.util.List;

public class ControladorTreino {

    private RepositorioTreinosArray repositorio;

    public ControladorTreino() {
        this.repositorio = RepositorioTreinosArray.getInstanciaUnica();
    }

    // Inicializa (cria ou atualiza) um treino.
    public void inicializar(Treino treino, String nome, String tipoDeTreino, int duracao, int nivelDeDificuldade) throws TreinoNaoEncontradoException {
        treino.setNome(nome);
        treino.setTipoDeTreino(tipoDeTreino);
        treino.setDuracao(duracao);
        treino.setNivelDeDificuldade(nivelDeDificuldade);

        try {
            repositorio.salvar(treino); // Tenta atualizar.
        } catch (TreinoNaoEncontradoException e) {
            repositorio.adicionar(treino); // Se não existir, adiciona.
        }
    }

    // Adiciona um exercício ao treino.
    public void adicionarExercicio(Treino treino, Exercicio exercicio) throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        if (exercicio != null && !treino.getExercicios().contains(exercicio)) {
            treino.getExercicios().add(exercicio);
            calcularCaloriasQueimadas(treino); // Recalcula calorias.
            atualizarProgresso(treino);      // Atualiza o progresso.
            repositorio.salvar(treino); //Salva as mudanças.
        }
    }

    // Remove um exercício do treino.
    public void removerExercicio(Treino treino, Exercicio exercicio) throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        if (exercicio != null && treino.getExercicios().contains(exercicio)) {
            treino.getExercicios().remove(exercicio);
            calcularCaloriasQueimadas(treino); // Recalcula calorias.
            atualizarProgresso(treino);      // Atualiza o progresso.
            repositorio.salvar(treino); //Salva as mudanças.
        }
    }

    // Calcula o total de calorias queimadas no treino.
    public double calcularCaloriasQueimadas(Treino treino) throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        double caloriasQueimadas = 0;
        for (Exercicio exercicio : treino.getExercicios()) {
            ControladorExercicio ce = new ControladorExercicio(); // Use o controlador existente.
            caloriasQueimadas += ce.calcularCaloriasQueimadas(exercicio);
        }
        treino.setCaloriasQueimadas(caloriasQueimadas);
        repositorio.salvar(treino); // Salva as calorias calculadas.
        return caloriasQueimadas;
    }

    // Atualiza o progresso do treino.
    public void atualizarProgresso(Treino treino) throws TreinoNaoEncontradoException {
        if (treino.getExercicios().isEmpty()) {
            treino.setProgresso(0.0);
            treino.setConcluido(false);
            repositorio.salvar(treino); // Salva o progresso.
            return;
        }

        long exerciciosConcluidos = treino.getExercicios().stream().filter(Exercicio::isConcluido).count();
        double progresso = (exerciciosConcluidos / (double) treino.getExercicios().size()) * 100.0;
        treino.setProgresso(progresso);
        treino.setConcluido(progresso == 100.0);
        repositorio.salvar(treino); // Salva o progresso.
    }

    // Lista todos os treinos do repositório.
    public List<Treino> listarTreinos() {
        return repositorio.getAll();
    }
}