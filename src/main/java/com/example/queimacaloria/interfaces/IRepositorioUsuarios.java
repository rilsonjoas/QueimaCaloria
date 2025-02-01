package com.example.queimacaloria.interfaces;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Meta;
import com.example.queimacaloria.negocio.Treino;
import com.example.queimacaloria.negocio.Usuario;

import java.util.UUID;

public interface IRepositorioUsuarios {

    public void adicionar(Usuario usuario) throws UsuarioNaoEncontradoException;

    public void salvar(Usuario usuario)throws UsuarioNaoEncontradoException;

    public void remover(UUID id)throws UsuarioNaoEncontradoException;

    public Usuario buscar(UUID id)throws UsuarioNaoEncontradoException;

}
