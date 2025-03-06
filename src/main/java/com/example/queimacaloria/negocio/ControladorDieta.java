package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioDietasArray;
import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ControladorDieta {

    private RepositorioDietasArray repositorio;

    // Construtor, inicializa o repositório.
    public ControladorDieta() {
        this.repositorio = RepositorioDietasArray.getInstanciaUnica();
    }

    public void configurarDieta(Dieta dieta, String nome, Meta.Tipo objetivo, int caloriasDiarias, Usuario usuario, Usuario.TipoDieta tipoDieta) throws DietaNaoEncontradaException { // Usuario adicionado
        dieta.setNome(nome);
        dieta.setObjetivo(objetivo);
        dieta.setCaloriasDiarias(caloriasDiarias);
        dieta.setUsuario(usuario);
        dieta.setTipoDieta(tipoDieta);

        try {
            repositorio.salvar(dieta);
        } catch (DietaNaoEncontradaException e) {
            repositorio.adicionar(dieta);
        }
    }

    // Lista todas as dietas do repositório.
    public List<Dieta> listarDietas() {
        if (repositorio == null) {
            System.err.println("ERRO CRÍTICO: Repositório nulo em ControladorDieta.listarDietas()!");
        }

        List<Dieta> dietas = repositorio.getAll();
        System.out.println("ControladorDieta.listarDietas(): Dietas retornadas: " + dietas);
        return dietas;
    }

    public List<Dieta> listarDietas(Usuario.TipoDieta tipo) {
        return repositorio.getAll().stream()
                .filter(dieta -> dieta.getTipoDieta() == tipo)
                .collect(Collectors.toList());
    }

    // Remove uma dieta do repositório (usando o ID).
    public void removerDieta(UUID id) throws DietaNaoEncontradaException{
        repositorio.remover(id);
    }

}