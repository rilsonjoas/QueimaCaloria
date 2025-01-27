package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.RepositorioUsuariosArray;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

// ControladorUsuário
public class ControladorUsuario {

    RepositorioUsuariosArray repositorio;

    public ControladorUsuario() {
        repositorio = RepositorioUsuariosArray.getInstanciaUnica();
    }

    // Método para atualizar os dados do usuário, ele testa com um if se os dados
    // fornecidos mudaram
    public void atualizarDados(Usuario usuario, String nome, String email, LocalDate dataNascimento,
            Usuario.Sexo sexo, float peso, float altura) {
        if (nome != null && !nome.isEmpty()) {
            usuario.setNome(nome);
        }

        if (email != null && !email.isEmpty()) {
            usuario.setEmail(email);
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

        calcularIMC(usuario); // Recalcula o IMC após alterar peso e altura
    }

    // Método para calcular o IMC do usuário
    public float calcularIMC(Usuario usuario) {
        if (usuario.getAltura() > 0 && usuario.getPeso() > 0) {
            float imc = usuario.getPeso() / (usuario.getAltura() * usuario.getAltura()); // IMC = peso / altura²
            usuario.setImc(imc);
            return imc;
        } else {
            throw new IllegalArgumentException("Altura e peso devem ser maiores que zero.");
        }
    }

    // Método para cadastrar uma meta para o usuário
    public void cadastrarMeta(Usuario usuario, Meta meta) {
        if (meta != null) {
            usuario.getMetas().add(meta);
        }
    }

    // Método para adicionar um treino para o usuário
    public void adicionarTreino(Usuario usuario, Treino treino) {
        // Adiciona o treino à lista se ele não for nulo e não existir na lista
        if (treino != null && !usuario.getTreinos().contains(treino)) {
            usuario.getTreinos().add(treino);
        }
    }

    // Método para adicionar uma dieta para o usuário
    public void adicionarDieta(Usuario usuario, Dieta dieta) {
        if (dieta != null && !usuario.getDietas().contains(dieta)) {
            usuario.getDietas().add(dieta);
        }
    }

    // Método para calcular o progresso geral do usuário
    public double getProgresso(Usuario usuario) {
        double progressoTotal = 0;
        int contadorAtividades = 0;

        // Calcula o progresso das metas
        for (Meta meta : usuario.getMetas()) {
            progressoTotal += calcularProgressoMeta(meta);
            contadorAtividades++;
        }

        // Calcula o progresso dos treinos
        for (Treino treino : usuario.getTreinos()) {
            progressoTotal += calcularProgressoTreino(treino);
            contadorAtividades++;
        }

        // Calcula o progresso das dietas
        for (Dieta dieta : usuario.getDietas()) {
            progressoTotal += calcularProgressoDieta(dieta);
            contadorAtividades++;
        }

        // Calcula a média do progresso e retorna
        return contadorAtividades > 0 ? progressoTotal / contadorAtividades : 0;
    }

    // Método para calcular a idade do usuário
    public int getIdade(Usuario usuario) {
        if (usuario.getDataNascimento() == null) {
            return 0;
        }
        return Period.between(usuario.getDataNascimento(), LocalDate.now()).getYears();
    }

    // Método para calcular o progresso da meta em porcentagem
    public double calcularProgressoMeta(Meta meta) {
        if (meta.getValorAlvo() == 0) {
            return 0;
        }
        return (meta.getProgressoAtual() / meta.getValorAlvo()) * 100;
    }

    // Método para calcular o progresso do treino
    public double calcularProgressoTreino(Treino treino) {

        if (treino.getExercicios().isEmpty()) {
            return 0.0;
        }

        long exerciciosConcluidos = treino.getExercicios().stream().filter(Exercicio::isConcluido).count();
        return (exerciciosConcluidos / (double) treino.getExercicios().size()) * 100.0;

    }

    // Método para calcular o progresso da dieta em relação à meta de calorias
    // diárias
    public double calcularProgressoDieta(Dieta dieta) {
        int caloriasDiarias = dieta.getCaloriasDiarias();
        if (caloriasDiarias == 0) {
            return 0;
        }
        return (double) calcularCaloriasTotaisDieta(dieta) / caloriasDiarias * 100;
    }

    // Calcula o total de calorias da dieta com base nas refeições
    public int calcularCaloriasTotaisDieta(Dieta dieta) {
        int totalCalorias = 0;
        for (Refeicao refeicao : dieta.getRefeicoes()) {
            totalCalorias += refeicao.getCalorias();
        }
        return totalCalorias;
    }

    // Verifica se a meta foi concluída
    public boolean isMetaConcluida(Meta meta) {
        return meta.getDataConclusao() != null;
    }

    // Define a data de conclusão para a data atual, marcando a meta como concluída
    public void concluirMeta(Meta meta) {
        meta.setDataConclusao(new Date());
    }

}
