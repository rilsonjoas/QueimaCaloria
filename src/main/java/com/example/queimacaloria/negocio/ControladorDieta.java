
package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioDietasArray;
import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;

import java.util.List;
import java.util.UUID;

public class ControladorDieta {

    private RepositorioDietasArray repositorio;

    // Construtor, inicializa o repositório.
    public ControladorDieta() {
        this.repositorio = RepositorioDietasArray.getInstanciaUnica();
    }

    // Configura uma dieta, atualizando ou adicionando ao repositório.
    public void configurarDieta(Dieta dieta, String nome, Meta.Tipo objetivo, int caloriasDiarias, Usuario usuario) throws DietaNaoEncontradaException {
        dieta.setNome(nome);
        dieta.setObjetivo(objetivo);
        dieta.setCaloriasDiarias(caloriasDiarias);
        dieta.setUsuario(usuario);

        try {
            repositorio.salvar(dieta);
        } catch (DietaNaoEncontradaException e) {
            repositorio.adicionar(dieta);
        }
    }

    // Lista todas as dietas do repositório.
    public List<Dieta> listarDietas() {
        return repositorio.getAll();
    }

    // Remove uma dieta do repositório (usando o ID).
    public void removerDieta(UUID id) throws DietaNaoEncontradaException{
        repositorio.remover(id);
    }

}