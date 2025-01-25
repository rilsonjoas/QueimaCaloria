package com.example.queimacaloria;

import com.example.queimacaloria.negocio.*;
import com.example.queimacaloria.dados.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        System.out.println("Teste");

        // Criando um usuário usando LocalDate
//        Usuario usuario = new Usuario(
//                "João Silva",
//                "joao.silva@email.com",
//                LocalDate.of(1990, 6, 15),  // Ano, Mês, Dia
//                Usuario.Sexo.MASCULINO,
//                80f,
//                1.75f
//        );
//        System.out.println("Usuário criado: " + usuario);

        // Criando um treino
        // Treino treinoMusculacao = new Treino("Musculação", "Força", 60, 3, usuario);

        // // Criando um exercício
        // Exercicio exercicioAgachamento = new Exercicio("Agachamento", "Trabalha pernas e glúteos", Exercicio.TipoExercicio.FORCA, 60, 5.5);
        // exercicioAgachamento.adicionarMusculoTrabalhado("Quadríceps");
        // exercicioAgachamento.adicionarMusculoTrabalhado("Glúteos");
        // exercicioAgachamento.concluir();

        // treinoMusculacao.adicionarExercicio(exercicioAgachamento);

        // System.out.println("Exercício adicionado: " + exercicioAgachamento);
        // System.out.println("Calorias queimadas no exercício: " + exercicioAgachamento.getCaloriasQueimadas());
        // System.out.println("Treino: " + treinoMusculacao);

        // //Adicionando mais um exercício (dessa vez não concluído)
        // Exercicio exercicioCorrida = new Exercicio("Corrida", "Exercício cardiovascular", Exercicio.TipoExercicio.CARDIO, 300, 10);
        // exercicioCorrida.adicionarMusculoTrabalhado("Perna");
        // treinoMusculacao.adicionarExercicio(exercicioCorrida);
        // System.out.println("Treino em andamento: " + treinoMusculacao);

        // //Atualizando o progresso do treino
        // treinoMusculacao.atualizarProgresso();
        // System.out.println("Progresso do treino: " + treinoMusculacao.getProgresso() + "%");

        // // Criando um repositório de usuários
        // RepositorioUsuariosArray repositorioUsuariosArray = new RepositorioUsuariosArray();
        // repositorioUsuariosArray.adicionar(usuario);

        // //Exemplo de criação de refeição
        // Map<String, Double> macronutrientes = new HashMap<>();
        // macronutrientes.put("Proteínas", 20.0);
        // macronutrientes.put("Carboidratos", 30.0);
        // macronutrientes.put("Gorduras", 10.0);

        // Refeicao refeicao = new Refeicao("Almoço", "Frango com arroz e salada", macronutrientes);
        // System.out.println("Refeição criada: " + refeicao.getNome() + ", Calorias: " + refeicao.getCalorias());
    }
}