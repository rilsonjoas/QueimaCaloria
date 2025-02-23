package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioDietasArray;
import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;

import java.util.List;
import java.util.UUID;

public class ControladorDieta {

    private RepositorioDietasArray repositorio;

    public ControladorDieta() {
        this.repositorio = RepositorioDietasArray.getInstanciaUnica();
    }

    public void configurarDieta(Dieta dieta, String nome, Dieta.ObjetivoDieta objetivo, int caloriasDiarias, Usuario usuario) throws DietaNaoEncontradaException {
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

    public List<Dieta> listarDietas() {
        return repositorio.getAll();
    }

    public void removerDieta(Dieta dieta) throws DietaNaoEncontradaException{
        repositorio.remover(dieta.getId());
    }

    public void removerDieta(UUID id) throws DietaNaoEncontradaException{
        repositorio.remover(id);
    }
}
