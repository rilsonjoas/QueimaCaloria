package com.example.queimacaloria.dados;

import com.example.queimacaloria.excecoes.DietaNaoEncontradaException;
import com.example.queimacaloria.excecoes.MetaNaoEncontradaException;
import com.example.queimacaloria.excecoes.TreinoNaoEncontradoException;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;
import com.example.queimacaloria.interfaces.IRepositorioUsuarios;
import com.example.queimacaloria.negocio.Dieta;
import com.example.queimacaloria.negocio.Meta;
import com.example.queimacaloria.negocio.Treino;
import com.example.queimacaloria.negocio.Usuario;

import java.util.UUID;

public class RepositorioUsuariosArray implements IRepositorioUsuarios {

    private Usuario[] usuarios;
    private int proximoIndice;

    private static RepositorioUsuariosArray instanciaUnica;


    public RepositorioUsuariosArray() {
        usuarios = new Usuario[10];
        proximoIndice = 0;
    }

    public static RepositorioUsuariosArray getInstanciaUnica() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioUsuariosArray();
        }
        return instanciaUnica;
    }

    private int procurarIndice(UUID id) {
        int i = 0;
        boolean achou = false;
        while ((!achou) && (i < this.proximoIndice)) {
            if (id.equals(this.usuarios[i].getId())) {
                achou = true;
            } else {
                i = i + 1;
            }
        }
        return i;
    }

    @Override
    public void adicionar(Usuario usuario) throws UsuarioNaoEncontradoException {
        if(proximoIndice > usuarios.length - 1) {
          int novoTam = proximoIndice + 10;
          Usuario[] arrayTemporario = new Usuario[novoTam];

          for (int i = 0; i < proximoIndice; i++) {
              arrayTemporario[i] = usuarios[i];
          }
          usuarios = arrayTemporario;
        }

        usuarios[proximoIndice] = usuario;
        proximoIndice++;

    }

    @Override
    public void salvar(Usuario usuario) throws UsuarioNaoEncontradoException {
       if(usuario != null) {
           int indice = this.procurarIndice(usuario.getId());
           if (indice != proximoIndice) {
               usuarios[indice] = usuario;
           }

       }else{
           throw new IllegalArgumentException("Usuário inválido.");
       }

    }

    @Override
    public void remover(UUID id) throws UsuarioNaoEncontradoException {
        int i = this.procurarIndice(id);
        if (i != this.proximoIndice) {
            this.usuarios[i] = this.usuarios[this.proximoIndice - 1];
            this.usuarios[this.proximoIndice - 1] = null;
            this.proximoIndice = this.proximoIndice - 1;
        } else {
            throw  new UsuarioNaoEncontradoException("Usuario Nao encontrado");
        }
    }

    @Override
    public Usuario buscar(UUID id) throws UsuarioNaoEncontradoException {
        int indice = procurarIndice(id);
        if ( indice < proximoIndice) {
            return usuarios[indice];

        }else{
            throw new UsuarioNaoEncontradoException("Usuario Nao encontrado");
        }

    }
}
