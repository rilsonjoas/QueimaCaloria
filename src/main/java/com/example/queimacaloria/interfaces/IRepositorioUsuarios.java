package com.example.queimacaloria.interfaces;

import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.negocio.Usuario;

import java.util.UUID;

public interface IRepositorioUsuarios {

    public void adicionar(Usuario usuario);

    public void atualizar(Usuario usuario)throws UsuarioNaoEncontradoException;

    public void remover(UUID id)throws UsuarioNaoEncontradoException;

    public Usuario buscar(UUID id)throws UsuarioNaoEncontradoException;

}
