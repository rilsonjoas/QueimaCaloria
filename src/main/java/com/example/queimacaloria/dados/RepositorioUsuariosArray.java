package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.interfaces.IRepositorioUsuarios;
import com.example.queimacaloria.negocio.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioUsuariosArray implements IRepositorioUsuarios {
    private Usuario[] usuarios;
    private int proximoIndice;
    private static RepositorioUsuariosArray instanciaUnica;

    //Construtor privado (padrão Singleton)
    public RepositorioUsuariosArray() {
        usuarios = new Usuario[10];
        proximoIndice = 0;
    }

    //Método estático para obter a instância única (Singleton)
    public static RepositorioUsuariosArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioUsuariosArray();
        }
        return instanciaUnica;
    }

    //Método privado para encontrar o índice de um usuário pelo ID
    private int procurarIndice(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        for (int i = 0; i < proximoIndice; i++) {
            if (id.equals(usuarios[i].getId())) return i;
        }
        return proximoIndice;
    }

    // Adiciona um usuário ao repositório.
    @Override
    public void adicionar(Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException("Usuário não pode ser nulo.");
        if (proximoIndice >= usuarios.length) {
            Usuario[] temp = new Usuario[usuarios.length + 10];
            System.arraycopy(usuarios, 0, temp, 0, usuarios.length);
            usuarios = temp;
        }
        usuarios[proximoIndice++] = usuario;
    }

    // Salva (atualiza) um usuário no repositório.
    @Override
    public void salvar(Usuario usuario) throws UsuarioNaoEncontradoException {
        if (usuario == null) throw new IllegalArgumentException("Usuário não pode ser nulo.");
        int indice = procurarIndice(usuario.getId());
        if (indice < proximoIndice) {
            usuarios[indice] = usuario;
        } else {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        }
    }

    // Remove um usuário do repositório pelo ID.
    @Override
    public void remover(UUID id) throws UsuarioNaoEncontradoException {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            usuarios[indice] = usuarios[proximoIndice - 1];
            usuarios[--proximoIndice] = null;
        } else {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        }
    }

    // Busca um usuário pelo ID.
    @Override
    public Usuario buscar(UUID id) throws UsuarioNaoEncontradoException {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        int indice = procurarIndice(id);
        if (indice < proximoIndice) {
            return usuarios[indice];
        } else {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        }
    }

    // Retorna todos os usuários do repositório.
    @Override
    public List<Usuario> getAll() {
        List<Usuario> lista = new ArrayList<>();
        for (int i = 0; i < proximoIndice; i++) {
            if (usuarios[i] != null) {
                lista.add(usuarios[i]);
            }
        }
        return lista;
    }
}