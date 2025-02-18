package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioUsuariosArray;
import com.example.queimacaloria.excecoes.UsuarioNaoEncontradoException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

public class ControladorUsuario {

    private RepositorioUsuariosArray repositorio;

    public ControladorUsuario() {
        this.repositorio = RepositorioUsuariosArray.getInstanciaUnica();
    }

    // Método de cadastro.
    public void cadastrarUsuario(Usuario novoUsuario) {
        // Verifica se o e-mail já existe
        List<Usuario> usuarios = repositorio.getAll();
        for (Usuario user : usuarios) {
            if (user.getEmail().equals(novoUsuario.getEmail())) {
                throw new IllegalArgumentException("Email já cadastrado.");
            }
        }

        repositorio.adicionar(novoUsuario);
    }

    // Atualiza os dados de um usuário existente
    public void atualizarDados(UUID usuarioId, String nome, String email, String senha, LocalDate dataNascimento,
                               Usuario.Sexo sexo, float peso, float altura) throws UsuarioNaoEncontradoException
    {
        Usuario usuario = repositorio.buscar(usuarioId); // Busca o usuário pelo ID
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException("Usuário com ID " + usuarioId + " não encontrado.");
        }

        if (nome != null && !nome.isEmpty()) {
            usuario.setNome(nome);
        }
        if (email != null && !email.isEmpty()) {
            usuario.setEmail(email);
        }
        if (senha != null && !senha.isEmpty()) {
            usuario.setSenha(senha);
        }
        if (dataNascimento != null) {
            usuario.setDataNascimento(dataNascimento);
        }
        if (sexo != null) {
            usuario.setSexo(sexo);
        }
        if (peso > 0) {
            usuario.setPeso(peso);
        }
        if (altura > 0) {
            usuario.setAltura(altura);
        }

        repositorio.salvar(usuario); // Salva o objeto ATUALIZADO
    }

    public void cadastrarMeta(Usuario usuario, Meta meta) throws UsuarioNaoEncontradoException {
        if (meta != null) {
            usuario.getMetas().add(meta);
            repositorio.salvar(usuario);
        }
    }

    public void adicionarTreino(Usuario usuario, Treino treino) throws UsuarioNaoEncontradoException {
        if (treino != null && !usuario.getTreinos().contains(treino)) {
            usuario.getTreinos().add(treino);
            repositorio.salvar(usuario);
        }
    }

    public void adicionarDieta(Usuario usuario, Dieta dieta) throws UsuarioNaoEncontradoException {
        if (dieta != null && !usuario.getDietas().contains(dieta)) {
            usuario.getDietas().add(dieta);
            repositorio.salvar(usuario);
        }
    }

    public double getProgresso(Usuario usuario) {
        double progressoTotal = 0;
        int contadorAtividades = 0;

        for (Meta meta : usuario.getMetas()) {
            progressoTotal += calcularProgressoMeta(meta);
            contadorAtividades++;
        }
        for (Treino treino : usuario.getTreinos()) {
            progressoTotal += calcularProgressoTreino(treino);
            contadorAtividades++;
        }
        for (Dieta dieta : usuario.getDietas()) {
            progressoTotal += calcularProgressoDieta(dieta);
            contadorAtividades++;
        }

        return contadorAtividades > 0 ? progressoTotal / contadorAtividades : 0;
    }

    public int getIdade(Usuario usuario) {
        if (usuario.getDataNascimento() == null) return 0;
        return Period.between(usuario.getDataNascimento(), LocalDate.now()).getYears();
    }

    public double calcularProgressoMeta(Meta meta) {
        if (meta.getValorAlvo() == 0) return 0;
        return (meta.getProgressoAtual() / meta.getValorAlvo()) * 100;
    }

    public double calcularProgressoTreino(Treino treino) {
        if (treino.getExercicios().isEmpty()) return 0.0;
        long exerciciosConcluidos = treino.getExercicios().stream().filter(Exercicio::isConcluido).count();
        return (exerciciosConcluidos / (double) treino.getExercicios().size()) * 100.0;
    }

    public double calcularProgressoDieta(Dieta dieta) {
        int caloriasDiarias = dieta.getCaloriasDiarias();
        if (caloriasDiarias == 0) return 0;
        return (double) calcularCaloriasTotaisDieta(dieta) / caloriasDiarias * 100;
    }

    public int calcularCaloriasTotaisDieta(Dieta dieta) {
        int totalCalorias = 0;
        for (Refeicao refeicao : dieta.getRefeicoes()) {
            totalCalorias += refeicao.getCalorias();
        }
        return totalCalorias;
    }

    public boolean isMetaConcluida(Meta meta) {
        return meta.getDataConclusao() != null;
    }

    public Usuario buscarPorId(UUID id) throws UsuarioNaoEncontradoException{
        return repositorio.buscar(id);
    }

    public List<Usuario> listarUsuarios() {
        return repositorio.getAll();
    }

    public void remover(UUID id) throws UsuarioNaoEncontradoException {
        repositorio.remover(id);
    }
}