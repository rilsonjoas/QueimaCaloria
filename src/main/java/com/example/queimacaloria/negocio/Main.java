package com.example.queimacaloria.negocio;

import com.example.queimacaloria.excecoes.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    // Método principal para testes (pode ser removido ou adaptado).
    public static void main(String[] args) {
        Fachada fachada = Fachada.getInstanciaUnica();

        //Vamos criar um usuário de teste para usar nos testes abaixo.
        Usuario usuario = new Usuario("Teste", "teste@email.com", LocalDate.of(1995, 8, 22), Usuario.Sexo.Masculino,
                75.0f, 180.0f, Usuario.TipoUsuario.USUARIO_COMUM);

        try {
            //Testando atualizar um usuário que não existe
            fachada.atualizarDadosUsuario(usuario, "João Silva", "joao@email.com", "senha123",
                    LocalDate.of(1990, 5, 15), Usuario.Sexo.Masculino, 80.5f, 1.75f, usuario.getTipo(), 20, 20, 20, 20);

        } catch (UsuarioNaoEncontradoException e){
            System.err.println("Teste atualizarDadosUsuario() - Erro esperado: " + e.getMessage());
            //OK.
        }

        try {
            fachada.cadastrarUsuario(
                    "João Silva",
                    "joao@email.com",
                    "senha123",
                    LocalDate.of(1990, 5, 15),
                    Usuario.Sexo.Masculino,
                    80.5f,
                    175f,  // Agora em centímetros.
                    Usuario.TipoUsuario.USUARIO_COMUM.getDescricao()
            );
            System.out.println("Usuário cadastrado com sucesso.");

            //Agora vamos buscar o usuário recém-criado.
            List<Usuario> usuarios = fachada.listarUsuarios();
            usuario = usuarios.get(0); //Pega o primeiro, já que é pra ser o único.

            System.out.println("IMC do usuário: " + usuario.getImc());

            //Testando configurar a meta
            Meta meta = new Meta();
            //Passando usuário
            fachada.configurarMeta(meta, "Perder 5kg", Meta.Tipo.PERDA_DE_PESO, 5.0, 0.0, LocalDate.of(2024, 12, 31), usuario);
            System.out.println("Meta cadastrada para o usuário.");

            //Testando configurar o treino
            Treino treino = new Treino();
            //Passando usuário
            fachada.configurarTreino(treino, "Treino de Força", Exercicio.TipoExercicio.FORCA, 60, 3,  usuario, Usuario.NivelExperiencia.INTERMEDIARIO);
            System.out.println("Treino adicionado ao usuário.");

            //Testando configurar a dieta.
            Dieta dieta = new Dieta();
            //Passando usuário e tipo de dieta.
            fachada.configurarDieta(dieta, "Dieta Low Carb", Meta.Tipo.PERDA_DE_PESO, 1800, usuario, Usuario.TipoDieta.LOW_CARB);
            System.out.println("Dieta adicionada ao usuário.");

            int idadeUsuario = fachada.calcularIdadeUsuario(usuario);
            System.out.println("Idade do usuário: " + idadeUsuario);

        } catch (MetaNaoEncontradaException | TreinoNaoEncontradoException | DietaNaoEncontradaException e) {
            System.err.println("Erro no usuário: " + e.getMessage());
        }


        Dieta dieta2 = new Dieta();
        //Usuario userDieta = new Usuario();  Não precisa mais, a dieta é associada a um usuário existente.

        try {
            //Aqui não precisamos mais de um usuário separado.
            fachada.configurarDieta(dieta2, "Dieta de Teste", Meta.Tipo.GANHO_DE_MASSA, 2500, usuario, Usuario.TipoDieta.ONIVORO);

            Refeicao refeicao1 = new Refeicao();
            Map<String, Double> macros1 = new HashMap<>();
            macros1.put("Proteínas", 50.0);
            macros1.put("Carboidratos", 100.0);
            macros1.put("Gorduras", 30.0);
            fachada.configurarRefeicao(refeicao1, "Café da Manhã", "Ovos mexidos com bacon", macros1, usuario, Usuario.TipoDieta.ONIVORO);

            Refeicao refeicao2 = new Refeicao();
            Map<String, Double> macros2 = new HashMap<>();
            macros2.put("Proteínas", 60.0);
            macros2.put("Carboidratos", 80.0);
            macros2.put("Gorduras", 40.0);

            fachada.configurarRefeicao(refeicao2, "Almoço", "Frango grelhado com batata doce", macros2,  usuario, Usuario.TipoDieta.ONIVORO);


        } catch (DietaNaoEncontradaException e) {
            System.err.println("Erro na dieta: " + e.getMessage());
        }

        Exercicio exercicio = new Exercicio();
        try {
            fachada.configurarExercicio(exercicio, "Corrida", "Corrida leve", Exercicio.TipoExercicio.CARDIO, 30, 10.0,  usuario, Usuario.NivelExperiencia.INICIANTE);
            fachada.adicionarMusculoExercicio(exercicio, "Pernas");
            System.out.println("Exercício configurado.");

            fachada.concluirExercicio(exercicio);
            System.out.println("Exercício concluído.");

            fachada.removerMusculoExercicio(exercicio, "Pernas");
            System.out.println("Músculo removido do exercício(Teste).");
        } catch (ExercicioNaoEncontradoException e) {
            System.err.println("Erro no exercício: " + e.getMessage());
        }

        Meta meta = new Meta();
        try {
            fachada.configurarMeta(meta, "Ganhar 2kg de massa", Meta.Tipo.GANHO_DE_MASSA, 2.0, 0.0, LocalDate.of(2024, 10, 15), usuario);
            System.out.println("Meta configurada.");

            boolean metaConcluida = fachada.verificarMetaConcluida(meta);
            System.out.println("Meta concluída? " + metaConcluida);

            double progressoMeta = fachada.calcularProgressoMeta(meta);
            System.out.println("Progresso da meta: " + progressoMeta + "%");

            fachada.concluirMeta(meta);
            System.out.println("Meta marcada como concluída (teste).");

        } catch (MetaNaoEncontradaException e) {
            System.err.println("Erro na Meta: " + e.getMessage());
        }

        Refeicao refeicao = new Refeicao();
        Map<String, Double> macronutrientesRefeicao = new HashMap<>();
        macronutrientesRefeicao.put("Proteínas", 40.0);
        macronutrientesRefeicao.put("Carboidratos", 60.0);
        macronutrientesRefeicao.put("Gorduras", 20.0);
        fachada.configurarRefeicao(refeicao, "Jantar", "Salmão com legumes", macronutrientesRefeicao,  usuario, Usuario.TipoDieta.ONIVORO);
        System.out.println("Refeição configurada.");

        int caloriasRefeicao = fachada.calcularCaloriasRefeicao(refeicao);
        System.out.println("Calorias da refeição: " + caloriasRefeicao);

        Map<String, Double> macrosCalculados = fachada.calcularMacronutrientesRefeicao(refeicao);
        System.out.println("Macronutrientes da refeição: " + macrosCalculados);

        Treino treino = new Treino();
        Exercicio exercicio1 = new Exercicio();
        Exercicio exercicio2 = new Exercicio();

        try {

            fachada.configurarExercicio(exercicio1, "Supino", "Peito", Exercicio.TipoExercicio.FORCA, 45, 8.0,  usuario, Usuario.NivelExperiencia.INTERMEDIARIO);
            fachada.configurarExercicio(exercicio2, "Agachamento", "Pernas", Exercicio.TipoExercicio.FORCA, 60, 9.0, usuario, Usuario.NivelExperiencia.INTERMEDIARIO);
            fachada.configurarTreino(treino, "Treino Completo", Exercicio.TipoExercicio.FORCA, 90, 4,  usuario, Usuario.NivelExperiencia.INTERMEDIARIO);

            System.out.println("Treino configurado.");
            fachada.inserirExercicioTreino(treino, exercicio1);
            fachada.inserirExercicioTreino(treino, exercicio2);
            System.out.println("Exercícios inseridos no treino.");
            double caloriasQueimadasTreino = fachada.calcularCaloriasQueimadasTreino(treino);
            System.out.println("Calorias queimadas no treino: " + caloriasQueimadasTreino);
            fachada.atualizarProgressoTreino(treino);
            System.out.println("Progresso do treino atualizado.");

            fachada.excluirExercicioTreino(treino, exercicio1);
            System.out.println("Exercício excluído do treino (teste).");

        } catch (TreinoNaoEncontradoException | ExercicioNaoEncontradoException e) {
            System.err.println("Erro no treino: " + e.getMessage());
        }

        System.out.println("\nListando Dietas:");
        List<Dieta> dietas = fachada.listarDietas();
        for (Dieta d : dietas) {
            System.out.println(d.getNome());
        }

        System.out.println("\nListando Exercícios:");
        List<Exercicio> exercicios = fachada.listarExercicios();
        for (Exercicio e : exercicios) {
            System.out.println(e.getNome());
        }

        System.out.println("\nListando Metas:");
        List<Meta> metas = fachada.listarMetas();
        for (Meta m : metas) {
            System.out.println(m.getDescricao());
        }

        System.out.println("\nListando Refeições:");
        List<Refeicao> refeicoes = fachada.listarRefeicoes();
        for (Refeicao r : refeicoes) {
            System.out.println(r.getNome());
        }

        System.out.println("\nListando Treinos:");
        List<Treino> treinos = fachada.listarTreinos();
        for (Treino t : treinos) {
            System.out.println(t.getNome());
        }
    }
}