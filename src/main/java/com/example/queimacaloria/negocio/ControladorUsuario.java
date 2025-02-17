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

    // Método de cadastro
    public void cadastrarUsuario(String nome, String email, String senha, LocalDate dataNascimento,
                                 Usuario.Sexo sexo, float peso, float altura) {

        // Verifica se o e-mail já existe (importante!)
        List<Usuario> usuarios = repositorio.getAll();
        for (Usuario user : usuarios) {
            if (user.getEmail().equals(email)) {
                throw new IllegalArgumentException("Email já cadastrado."); // Use IllegalArgumentException, mais apropriado
            }
        }

        // Cria e adiciona o novo usuário diretamente.  NÃO USA atualizarDados aqui.
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senha);
        novoUsuario.setDataNascimento(dataNascimento);
        novoUsuario.setSexo(sexo);
        novoUsuario.setPeso(peso); // Define peso PRIMEIRO
        novoUsuario.setAltura(altura); //Define altura DEPOIS
        // novoUsuario.setImc(calcularIMC(novoUsuario)); // Remove daqui, o setPeso/setAltura já calculam
        repositorio.adicionar(novoUsuario); // Adiciona diretamente


        try {
            repositorio.salvar(novoUsuario); //  ISSO É CRUCIAL! Salva o usuário *DEPOIS* de todas as modificações.
            System.out.println("DEBUG: Usuário salvo no repositório após cadastro. IMC: " + novoUsuario.getImc()); // Log para depuração
        } catch (UsuarioNaoEncontradoException e) {
            System.err.println("Erro ao salvar usuário após cadastro: " + e.getMessage()); // Log de erro
            throw new RuntimeException("Erro interno ao cadastrar usuário.", e); // Relança como RuntimeException
        }

    }


    // ... restante da classe ControladorUsuario (sem alterações) ...
    // Atualiza os dados de um usuário existente.  Mantém a exceção.
    public void atualizarDados(Usuario usuario, String nome, String email, String senha, LocalDate dataNascimento,
                               Usuario.Sexo sexo, float peso, float altura) throws UsuarioNaoEncontradoException {

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
        System.out.println("DEBUG: Dentro de atualizarDados.  IMC antes de salvar: " + usuario.getImc()); // Log
        repositorio.salvar(usuario); // Salva as alterações no repositório.
        System.out.println("DEBUG: Dentro de atualizarDados.  IMC depois de salvar: " + usuario.getImc());//Log

    }


    // Cadastra uma meta para o usuário (adiciona à lista de metas do usuário).
    public void cadastrarMeta(Usuario usuario, Meta meta) throws UsuarioNaoEncontradoException {
        if (meta != null) {
            usuario.getMetas().add(meta);
            repositorio.salvar(usuario); // Salva o usuário com a nova meta.
        }
    }

    // Adiciona um treino à lista de treinos do usuário.
    public void adicionarTreino(Usuario usuario, Treino treino) throws UsuarioNaoEncontradoException {
        if (treino != null && !usuario.getTreinos().contains(treino)) {
            usuario.getTreinos().add(treino);
            repositorio.salvar(usuario); // Salva o usuário com o novo treino.
        }
    }

    // Adiciona uma dieta à lista de dietas do usuário.
    public void adicionarDieta(Usuario usuario, Dieta dieta) throws UsuarioNaoEncontradoException {
        if (dieta != null && !usuario.getDietas().contains(dieta)) {
            usuario.getDietas().add(dieta);
            repositorio.salvar(usuario);  // Salva o usuário com a nova dieta.
        }
    }

    // Calcula o progresso geral do usuário (combina progresso de metas, treinos e dietas).
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

    // Calcula a idade do usuário.
    public int getIdade(Usuario usuario) {
        if (usuario.getDataNascimento() == null) {
            return 0;
        }
        return Period.between(usuario.getDataNascimento(), LocalDate.now()).getYears();
    }

    // Calcula o progresso de uma meta específica.
    public double calcularProgressoMeta(Meta meta) {
        if (meta.getValorAlvo() == 0) {
            return 0;
        }
        return (meta.getProgressoAtual() / meta.getValorAlvo()) * 100;
    }

    // Calcula o progresso de um treino específico.
    public double calcularProgressoTreino(Treino treino) {
        if (treino.getExercicios().isEmpty()) {
            return 0.0;
        }
        long exerciciosConcluidos = treino.getExercicios().stream().filter(Exercicio::isConcluido).count();
        return (exerciciosConcluidos / (double) treino.getExercicios().size()) * 100.0;
    }

    // Calcula o progresso de uma dieta específica.
    public double calcularProgressoDieta(Dieta dieta) {
        int caloriasDiarias = dieta.getCaloriasDiarias();
        if (caloriasDiarias == 0) {
            return 0;
        }
        return (double) calcularCaloriasTotaisDieta(dieta) / caloriasDiarias * 100;
    }

    // Calcula o total de calorias consumidas em uma dieta.
    public int calcularCaloriasTotaisDieta(Dieta dieta) {
        int totalCalorias = 0;
        for (Refeicao refeicao : dieta.getRefeicoes()) {
            totalCalorias += refeicao.getCalorias();
        }
        return totalCalorias;
    }

    // Verifica se uma meta foi concluída.
    public boolean isMetaConcluida(Meta meta) {
        return meta.getDataConclusao() != null;
    }

    // Lista todos os usuários do repositório.
    public List<Usuario> listarUsuarios() {
        return repositorio.getAll();
    }

    // Corrigido: Método de remoção
    public void remover(UUID id) throws UsuarioNaoEncontradoException {
        try {
            repositorio.remover(id); // Tenta remover
        } catch (UsuarioNaoEncontradoException e) {
            // Aqui você decide o que fazer se o usuário não for encontrado: Relançar
            throw e;
        }
    }
}