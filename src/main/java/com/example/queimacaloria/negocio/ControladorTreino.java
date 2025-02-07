package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioTreinosArray;
import com.example.queimacaloria.excecoes.ExercicioNaoEncontradoException;
import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;

import java.util.List;

// Classe responsável pela lógica de negócio relacionada a treinos.
public class ControladorTreino {

    private RepositorioTreinosArray repositorio;  // Repositório de treinos.


    public ControladorTreino() {
        this.repositorio = RepositorioTreinosArray.getInstanciaUnica();
    }


    public void inicializar(Treino treino, String nome, String tipoDeTreino, int duracao, int nivelDeDificuldade) throws TreinoNaoEncontradoException {

        treino.setNome(nome);
        treino.setTipoDeTreino(tipoDeTreino);
        treino.setDuracao(duracao);
        treino.setNivelDeDificuldade(nivelDeDificuldade);


        try {
            repositorio.salvar(treino);
        } catch (TreinoNaoEncontradoException e) {
            repositorio.adicionar(treino);
        }
    }



    public void adicionarExercicio(Treino treino, Exercicio exercicio) throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        if (exercicio != null && !treino.getExercicios().contains(exercicio)) {
            treino.getExercicios().add(exercicio);
            calcularCaloriasQueimadas(treino);
            atualizarProgresso(treino);
        }
    }



    public void removerExercicio(Treino treino, Exercicio exercicio) throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        if (exercicio != null && treino.getExercicios().contains(exercicio)) {
            treino.getExercicios().remove(exercicio);
            calcularCaloriasQueimadas(treino);
            atualizarProgresso(treino);
        }
    }




    public double calcularCaloriasQueimadas(Treino treino) throws TreinoNaoEncontradoException, ExercicioNaoEncontradoException {
        double caloriasQueimadas = 0;
        for (Exercicio exercicio : treino.getExercicios()) {
            ControladorExercicio ce = new ControladorExercicio();
            caloriasQueimadas += ce.calcularCaloriasQueimadas(exercicio);
        }
        treino.setCaloriasQueimadas(caloriasQueimadas);
        repositorio.salvar(treino);
        return caloriasQueimadas;
    }





    public void atualizarProgresso(Treino treino) throws TreinoNaoEncontradoException {
        if (treino.getExercicios().isEmpty()) {
            treino.setProgresso(0.0);
            treino.setConcluido(false);
            repositorio.salvar(treino);
            return;
        }

        long exerciciosConcluidos = treino.getExercicios().stream().filter(Exercicio::isConcluido).count();
        double progresso = (exerciciosConcluidos / (double) treino.getExercicios().size()) * 100.0;
        treino.setProgresso(progresso);
        treino.setConcluido(progresso == 100.0);
        repositorio.salvar(treino);
    }




    public List<Treino> listarTreinos() {
        return repositorio.getAll();
    }
}