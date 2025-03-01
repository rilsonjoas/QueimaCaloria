package com.example.queimacaloria.interfaces;

import com.example.queimacaloria.negocio.Usuario;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;

import java.util.List;
import java.util.UUID;

public interface IRepositorioUsuarios {

    void adicionar(Usuario usuario);

    void salvar(Usuario usuario) throws UsuarioNaoEncontradoException;

    void remover(UUID id) throws UsuarioNaoEncontradoException;

    Usuario buscar(UUID id) throws UsuarioNaoEncontradoException;

    List<Usuario> getAll();

    List<Usuario> getAll(Usuario.TipoUsuario tipo);
}