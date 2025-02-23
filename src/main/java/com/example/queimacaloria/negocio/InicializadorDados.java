package com.example.queimacaloria.negocio;

import com.example.queimacaloria.dados.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InicializadorDados {

    public static void inicializarDados() {

    }

    public static List<Dieta> inicializarDietas() {
        List<Dieta> dietas = new ArrayList<>();
        dietas.add(new Dieta("Dieta para Emagrecimento Rápido", Dieta.ObjetivoDieta.PERDA_DE_PESO, 1200,  null));
        dietas.add(new Dieta("Dieta Low Carb Moderada", Dieta.ObjetivoDieta.PERDA_DE_PESO, 1600, null));
        dietas.add(new Dieta("Dieta da Proteína", Dieta.ObjetivoDieta.GANHO_DE_MASSA, 2500, null));
        dietas.add(new Dieta("Dieta Vegana para Atletas", Dieta.ObjetivoDieta.GANHO_DE_MASSA, 2700,  null));
        dietas.add(new Dieta("Dieta Mediterrânea", Dieta.ObjetivoDieta.MANUTENCAO, 2000, null));
        dietas.add(new Dieta("Dieta sem Glúten e Lactose", Dieta.ObjetivoDieta.MANUTENCAO, 2200, null));
        dietas.add(new Dieta("Dieta para Ganho de Massa Muscular", Dieta.ObjetivoDieta.GANHO_DE_MASSA, 3000, null));
        dietas.add(new Dieta("Dieta Flexível para Definição", Dieta.ObjetivoDieta.PERDA_DE_PESO, 1700, null));
        dietas.add(new Dieta("Dieta para Iniciantes", Dieta.ObjetivoDieta.MANUTENCAO, 1900, null));
        dietas.add(new Dieta("Dieta Personalizada do Cliente", Dieta.ObjetivoDieta.MANUTENCAO, 2400, null));
        return dietas;
    }

    public static List<Exercicio> inicializarExercicios() {
        List<Exercicio> exercicios = new ArrayList<>();
        try {
            exercicios.add(new Exercicio("Agachamento Livre", "Exercício básico para pernas e glúteos com barra nas costas.", new ArrayList<>(), Exercicio.TipoExercicio.FORCA, 30, 9.0, false));
            exercicios.add(new Exercicio("Supino Reto com Barra", "Exercício para peitoral e tríceps com barra.", new ArrayList<>(), Exercicio.TipoExercicio.FORCA, 30, 8.5, false));
            exercicios.add(new Exercicio("Remada Curvada com Barra", "Exercício para costas e bíceps com barra.", new ArrayList<>(), Exercicio.TipoExercicio.FORCA, 30, 7.0, false));
            exercicios.add(new Exercicio("Desenvolvimento Militar com Halteres", "Exercício para ombros com halteres.", new ArrayList<>(), Exercicio.TipoExercicio.FORCA, 30, 7.0, false));
            exercicios.add(new Exercicio("Flexão de Braço", "Exercício para peitoral, ombros e tríceps.", new ArrayList<>(), Exercicio.TipoExercicio.FORCA, 30, 6.5, false));
            exercicios.add(new Exercicio("Barra Fixa Pegada Pronada", "Exercício para costas e bíceps com barra fixa.", new ArrayList<>(), Exercicio.TipoExercicio.FORCA, 30, 8.0, false));
            exercicios.add(new Exercicio("Prancha Abdominal", "Exercício para fortalecimento do core.", new ArrayList<>(), Exercicio.TipoExercicio.FORCA, 60, 4.0, false));
            exercicios.add(new Exercicio("Caminhada na Esteira Inclinada", "Exercício aeróbico de caminhada com inclinação.", new ArrayList<>(), Exercicio.TipoExercicio.CARDIO, 20, 6.0, false));
            exercicios.add(new Exercicio("Corrida na Rua", "Exercício aeróbico de corrida ao ar livre.", new ArrayList<>(), Exercicio.TipoExercicio.CARDIO, 30, 9.0, false));
            exercicios.add(new Exercicio("Pular Corda", "Exercício aeróbico e de coordenação com corda.", new ArrayList<>(), Exercicio.TipoExercicio.CARDIO, 15, 10.0, false));
            exercicios.add(new Exercicio("Natação Livre", "Exercício aeróbico de natação em piscina.", new ArrayList<>(), Exercicio.TipoExercicio.AQUATICO, 40, 10.0, false));
            exercicios.add(new Exercicio("Yoga para Flexibilidade", "Exercício para melhorar a flexibilidade e o equilíbrio.", new ArrayList<>(), Exercicio.TipoExercicio.FLEXIBILIDADE, 40, 4.0, false));
            exercicios.add(new Exercicio("Pilates para Core", "Exercício para fortalecer o core e melhorar a postura.", new ArrayList<>(), Exercicio.TipoExercicio.EQUILIBRIO, 45, 5.0, false));
            exercicios.add(new Exercicio("Spinning", "Exercício aeróbico em bicicleta estacionária.", new ArrayList<>(), Exercicio.TipoExercicio.CARDIO, 45, 8.0, false));
            exercicios.add(new Exercicio("Elíptico", "Exercício aeróbico de baixo impacto em elíptico.", new ArrayList<>(), Exercicio.TipoExercicio.CARDIO, 30, 7.0, false));
            exercicios.add(new Exercicio("Treino Funcional", "Treino que combina diferentes exercícios em circuito.", new ArrayList<>(), Exercicio.TipoExercicio.OUTRO, 60, 6.0, false));
            exercicios.add(new Exercicio("Burpees", "Exercício que trabalha o corpo todo.", new ArrayList<>(), Exercicio.TipoExercicio.FORCA, 20, 10.0, false));
            exercicios.add(new Exercicio("Polichinelo", "Exercício aeróbico simples.", new ArrayList<>(), Exercicio.TipoExercicio.CARDIO, 15, 5.0, false));
            exercicios.add(new Exercicio("Corda Naval", "Exercício de força e resistência com corda naval.", new ArrayList<>(), Exercicio.TipoExercicio.FORCA, 20, 9.0, false));
            exercicios.add(new Exercicio("Abdominal Supra", "Exercício para abdominais superiores.", new ArrayList<>(), Exercicio.TipoExercicio.FORCA, 20, 3.0, false));
        }  catch (Exception e) {
            System.err.println("Erro ao adicionar exercicio pré-definidos: " + e.getMessage());
        }
        return exercicios;
    }

    public static List<Meta> inicializarMetas() {
        List<Meta> metas = new ArrayList<>();
        try{
            metas.add(new Meta("Perder 5kg de gordura corporal em 2 meses", Meta.Tipo.PERDA_DE_PESO, 5.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Ganhar 4kg de massa muscular em 3 meses", Meta.Tipo.PERDA_DE_PESO, 4.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Reduzir medidas da cintura em 4cm em 1 mês", Meta.Tipo.GANHO_DE_MASSA, 4.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Aumentar carga no agachamento em 10kg em 2 meses", Meta.Tipo.GANHO_DE_MASSA, 10.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Correr 5km em 25 minutos em 3 meses", Meta.Tipo.GANHO_DE_MASSA, 5.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Melhorar a flexibilidade das pernas em 2 meses", Meta.Tipo.GANHO_DE_MASSA, 2.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Aumentar a resistência física em 1 mês", Meta.Tipo.GANHO_DE_MASSA, 1.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Concluir 10 treinos por semana", Meta.Tipo.GANHO_DE_MASSA, 10.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Controlar as calorias diarias em 2 meses", Meta.Tipo.GANHO_DE_MASSA, 1.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Melhorar a ingestão de líquidos em 3 meses", Meta.Tipo.GANHO_DE_MASSA, 1.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Dormir 8h por dia por 2 meses", Meta.Tipo.GANHO_DE_MASSA, 1.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Ingerir uma quantidade certa de proteínas em 3 meses", Meta.Tipo.GANHO_DE_MASSA, 1.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Ter uma alimentação balanceada em 2 meses", Meta.Tipo.GANHO_DE_MASSA, 1.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Controlar os níveis de açúcar em 1 mês", Meta.Tipo.GANHO_DE_MASSA, 1.0, 0, LocalDate.now(), null));
            metas.add(new Meta("Aumentar a massa muscular em 2 meses", Meta.Tipo.PERDA_DE_PESO, 2.0, 0, LocalDate.now(), null));
        } catch (Exception e) {
            System.err.println("Erro ao adicionar meta pré-definida: " + e.getMessage());
        }
        return metas;
    }

    public static List<Refeicao> inicializarRefeicoes() {
        List<Refeicao> refeicoes = new ArrayList<>();
        ControladorRefeicao controladorRefeicao = new ControladorRefeicao();
        try {
            //Café da Manhã
            Refeicao refeicao1 = new Refeicao("Iogurte com Granola Caseira e Frutas Tropicais", "Iogurte natural integral com granola feita em casa (aveia, castanhas e sementes) e uma seleção de frutas tropicais como manga, mamão e kiwi.",  Map.of("Proteínas", 14.0, "Gorduras", 10.0, "Carboidratos", 38.0));
            refeicao1.setCalorias(controladorRefeicao.calcularCalorias(refeicao1)); //Calcula e adiciona
            refeicoes.add(refeicao1);
            Refeicao refeicao2 = new Refeicao("Tapioca com Ovo Mexido e Queijo Coalho", "Tapioca feita na frigideira com dois ovos mexidos e fatias de queijo coalho.",  Map.of("Proteínas", 18.0, "Gorduras", 16.0, "Carboidratos", 30.0));
            refeicao2.setCalorias(controladorRefeicao.calcularCalorias(refeicao2));
            refeicoes.add(refeicao2);
            Refeicao refeicao3 = new Refeicao("Cuscuz com Sardinha e Tomate", "Cuscuz nordestino cozido no vapor, acompanhado de sardinha em lata conservada em azeite e rodelas de tomate fresco.",  Map.of("Proteínas", 20.0, "Gorduras", 15.0, "Carboidratos", 40.0));
            refeicao3.setCalorias(controladorRefeicao.calcularCalorias(refeicao3));
            refeicoes.add(refeicao3);
            Refeicao refeicao4 = new Refeicao("Pão Francês com Requeijão e Café com Leite", "Um pão francês com requeijão cremoso e uma xícara de café com leite integral.", Map.of("Proteínas", 12.0, "Gorduras", 14.0, "Carboidratos", 45.0));
            refeicao4.setCalorias(controladorRefeicao.calcularCalorias(refeicao4));
            refeicoes.add(refeicao4);
            Refeicao refeicao5 = new Refeicao("Vitamina de Banana com Aveia e Leite", "Vitamina feita com banana madura, farelo de aveia e leite integral ou vegetal.",  Map.of("Proteínas", 8.0, "Gorduras", 6.0, "Carboidratos", 42.0));
            refeicao5.setCalorias(controladorRefeicao.calcularCalorias(refeicao5));
            refeicoes.add(refeicao5);

            //Almoço
            Refeicao refeicao6 = new Refeicao("Salada de Grãos com Frango Desfiado", "Salada colorida com quinoa, grão de bico, lentilha, pedaços de frango desfiado, folhas verdes e um molho de limão com azeite.",  Map.of("Proteínas", 32.0, "Gorduras", 18.0, "Carboidratos", 50.0));
            refeicao6.setCalorias(controladorRefeicao.calcularCalorias(refeicao6));
            refeicoes.add(refeicao6);
            Refeicao refeicao7 = new Refeicao("Arroz com Feijão, Bife Acebolado e Salada", "Arroz branco, feijão carioca, um bife de carne bovina acebolado e salada de alface com tomate.",  Map.of("Proteínas", 35.0, "Gorduras", 22.0, "Carboidratos", 65.0));
            refeicao7.setCalorias(controladorRefeicao.calcularCalorias(refeicao7));
            refeicoes.add(refeicao7);
            Refeicao refeicao8 = new Refeicao("Macarrão Integral com Molho de Tomate e Carne Moída", "Macarrão integral com molho de tomate caseiro e carne moída magra.",  Map.of("Proteínas", 30.0, "Gorduras", 15.0, "Carboidratos", 60.0));
            refeicao8.setCalorias(controladorRefeicao.calcularCalorias(refeicao8));
            refeicoes.add(refeicao8);
            Refeicao refeicao9 = new Refeicao("Frango Grelhado com Purê de Mandioquinha", "Filé de frango grelhado com um purê cremoso de mandioquinha.", Map.of("Proteínas", 40.0, "Gorduras", 16.0, "Carboidratos", 48.0));
            refeicao9.setCalorias(controladorRefeicao.calcularCalorias(refeicao9));
            refeicoes.add(refeicao9);
            Refeicao refeicao10 = new Refeicao("Peixe Assado com Legumes Salteados", "Filé de peixe branco assado com ervas finas e legumes salteados na frigideira com azeite (brócolis, cenoura e abobrinha).",  Map.of("Proteínas", 35.0, "Gorduras", 14.0, "Carboidratos", 30.0));
            refeicao10.setCalorias(controladorRefeicao.calcularCalorias(refeicao10));
            refeicoes.add(refeicao10);

            //Jantar
            Refeicao refeicao11 = new Refeicao("Omelete com Queijo e Tomate", "Omelete feita com três ovos, queijo minas e tomate picado.",  Map.of("Proteínas", 20.0, "Gorduras", 15.0, "Carboidratos", 5.0));
            refeicao11.setCalorias(controladorRefeicao.calcularCalorias(refeicao11));
            refeicoes.add(refeicao11);
            Refeicao refeicao12 = new Refeicao("Hambúrguer Caseiro de Frango com Pão Integral", "Hambúrguer caseiro de frango grelhado, servido em um pão integral com folhas verdes e tomate.",  Map.of("Proteínas", 30.0, "Gorduras", 18.0, "Carboidratos", 40.0));
            refeicao12.setCalorias(controladorRefeicao.calcularCalorias(refeicao12));
            refeicoes.add(refeicao12);
            Refeicao refeicao13 = new Refeicao("Sopa de Legumes com Carne Desfiada", "Sopa nutritiva feita com diversos legumes cozidos e carne bovina desfiada.", Map.of("Proteínas", 25.0, "Gorduras", 12.0, "Carboidratos", 35.0));
            refeicao13.setCalorias(controladorRefeicao.calcularCalorias(refeicao13));
            refeicoes.add(refeicao13);
            Refeicao refeicao14 = new Refeicao("Escondidinho de Carne Seca com Purê de Batata Doce", "Escondidinho de carne seca desfiada com purê de batata doce gratinado.",  Map.of("Proteínas", 28.0, "Gorduras", 20.0, "Carboidratos", 55.0));
            refeicao14.setCalorias(controladorRefeicao.calcularCalorias(refeicao14));
            refeicoes.add(refeicao14);
            Refeicao refeicao15 = new Refeicao("Wrap de Frango com Pasta de Abacate", "Wrap de frango grelhado, pasta de abacate, tomate e alface.",  Map.of("Proteínas", 25.0, "Gorduras", 22.0, "Carboidratos", 30.0));
            refeicao15.setCalorias(controladorRefeicao.calcularCalorias(refeicao15));
            refeicoes.add(refeicao15);
            Refeicao refeicao16 = new Refeicao("Torta de Frango com Massa Integral", "Torta de frango caseira com massa integral e recheio cremoso.",  Map.of("Proteínas", 22.0, "Gorduras", 18.0, "Carboidratos", 45.0));
            refeicao16.setCalorias(controladorRefeicao.calcularCalorias(refeicao16));
            refeicoes.add(refeicao16);
            Refeicao refeicao17 = new Refeicao("Crepioca com Frango e Requeijão", "Crepioca feita com ovo e tapioca, recheada com frango desfiado e requeijão cremoso.", Map.of("Proteínas", 24.0, "Gorduras", 17.0, "Carboidratos", 25.0));
            refeicao17.setCalorias(controladorRefeicao.calcularCalorias(refeicao17));
            refeicoes.add(refeicao17);
            Refeicao refeicao18 = new Refeicao("Salada de Atum com Grãos e Vegetais", "Salada com atum, grãos cozidos (quinoa ou grão de bico), tomate, pepino, cebola roxa e azeitonas.", Map.of("Proteínas", 30.0, "Gorduras", 18.0, "Carboidratos", 35.0));
            refeicao18.setCalorias(controladorRefeicao.calcularCalorias(refeicao18));
            refeicoes.add(refeicao18);
            Refeicao refeicao19 = new Refeicao("Açaí com Banana e Granola", "Tigela de açaí com rodelas de banana e granola crocante.",  Map.of("Proteínas", 5.0, "Gorduras", 10.0, "Carboidratos", 50.0));
            refeicao19.setCalorias(controladorRefeicao.calcularCalorias(refeicao19));
            refeicoes.add(refeicao19);
            Refeicao refeicao20 = new Refeicao("Mandioca Cozida com Carne de Sol", "Mandioca cozida servida com carne de sol desfiada.",  Map.of("Proteínas", 30.0, "Gorduras", 20.0, "Carboidratos", 45.0));
            refeicao20.setCalorias(controladorRefeicao.calcularCalorias(refeicao20));
            refeicoes.add(refeicao20);
            Refeicao refeicao21 = new Refeicao("Pão Integral com Pasta de Amendoim e Mel", "Duas fatias de pão integral com pasta de amendoim e um fio de mel.",  Map.of("Proteínas", 10.0, "Gorduras", 16.0, "Carboidratos", 35.0));
            refeicao21.setCalorias(controladorRefeicao.calcularCalorias(refeicao21));
            refeicoes.add(refeicao21);
            Refeicao refeicao22 = new Refeicao("Salada de Palmito com Tomate e Azeitona", "Salada refrescante com palmito, tomate cereja e azeitonas pretas.", Map.of("Proteínas", 4.0, "Gorduras", 8.0, "Carboidratos", 15.0));
            refeicao22.setCalorias(controladorRefeicao.calcularCalorias(refeicao22));
            refeicoes.add(refeicao22);
            Refeicao refeicao23 = new Refeicao("Mingau de Aveia com Frutas e Canela", "Mingau de aveia preparado com leite (integral ou vegetal), frutas picadas e canela em pó.",  Map.of("Proteínas", 12.0, "Gorduras", 8.0, "Carboidratos", 40.0));
            refeicao23.setCalorias(controladorRefeicao.calcularCalorias(refeicao23));
            refeicoes.add(refeicao23);
            Refeicao refeicao24 = new Refeicao("Queijo Coalho com Mel", "Cubos de queijo coalho grelhado com um fio de mel.", Map.of("Proteínas", 15.0, "Gorduras", 18.0, "Carboidratos", 8.0));
            refeicao24.setCalorias(controladorRefeicao.calcularCalorias(refeicao24));
            refeicoes.add(refeicao24);
            Refeicao refeicao25 = new Refeicao("Bolinho de Chuva com Café", "Bolinhos de chuva fritos acompanhados de uma xícara de café preto.", Map.of("Proteínas", 5.0, "Gorduras", 15.0, "Carboidratos", 40.0));
            refeicao25.setCalorias(controladorRefeicao.calcularCalorias(refeicao25));
            refeicoes.add(refeicao25);
            Refeicao refeicao26 = new Refeicao("Canjica com Amendoim", "Canjica cremosa feita com leite, amendoim e canela.",  Map.of("Proteínas", 10.0, "Gorduras", 12.0, "Carboidratos", 45.0));
            refeicao26.setCalorias(controladorRefeicao.calcularCalorias(refeicao26));
            refeicoes.add(refeicao26);
            Refeicao refeicao27 = new Refeicao("Panqueca de Banana com Aveia e Canela", "Panquecas feitas com banana amassada, aveia em flocos, ovo e um toque de canela.",  Map.of("Proteínas", 12.0, "Gorduras", 9.0, "Carboidratos", 40.0));
            refeicao27.setCalorias(controladorRefeicao.calcularCalorias(refeicao27));
            refeicoes.add(refeicao27);
            Refeicao refeicao28 = new Refeicao("Pão de Batata com Frango Desfiado", "Pão de batata recheado com frango desfiado e temperado.", Map.of("Proteínas", 18.0, "Gorduras", 14.0, "Carboidratos", 48.0));
            refeicao28.setCalorias(controladorRefeicao.calcularCalorias(refeicao28));
            refeicoes.add(refeicao28);
            Refeicao refeicao29 = new Refeicao("Salada Caprese com Pão Integral", "Salada com tomate, mussarela de búfala, manjericão fresco, azeite e uma fatia de pão integral.", Map.of("Proteínas", 15.0, "Gorduras", 19.0, "Carboidratos", 25.0));
            refeicao29.setCalorias(controladorRefeicao.calcularCalorias(refeicao29));
            refeicoes.add(refeicao29);
            Refeicao refeicao30 = new Refeicao("Suco Verde com Gengibre e Chia", "Suco feito com couve, limão, maçã, pepino, gengibre e sementes de chia.",  Map.of("Proteínas", 3.0, "Gorduras", 5.0, "Carboidratos", 20.0));
            refeicao30.setCalorias(controladorRefeicao.calcularCalorias(refeicao30));
            refeicoes.add(refeicao30);
            Refeicao refeicao31 = new Refeicao("Wrap de Atum com Abacate e Alface", "Wrap com atum, abacate amassado, alface e um toque de limão.", Map.of("Proteínas", 20.0, "Gorduras", 20.0, "Carboidratos", 28.0));
            refeicao31.setCalorias(controladorRefeicao.calcularCalorias(refeicao31));
            refeicoes.add(refeicao31);
            Refeicao refeicao32 = new Refeicao("Arroz de Couve-Flor com Frango e Castanhas", "Arroz de couve-flor temperado com frango desfiado, castanhas picadas e especiarias.",  Map.of("Proteínas", 30.0, "Gorduras", 20.0, "Carboidratos", 40.0));
            refeicao32.setCalorias(controladorRefeicao.calcularCalorias(refeicao32));
            refeicoes.add(refeicao32);
            Refeicao refeicao33 = new Refeicao("Feijoada Light com Arroz Integral e Couve Refogada", "Versão mais leve da feijoada com carnes magras, arroz integral e couve refogada.", Map.of("Proteínas", 35.0, "Gorduras", 25.0, "Carboidratos", 55.0));
            refeicao33.setCalorias(controladorRefeicao.calcularCalorias(refeicao33));
            refeicoes.add(refeicao33);
            Refeicao refeicao34 = new Refeicao("Bobó de Camarão com Arroz Branco", "Bobó de camarão cremoso feito com aipim, leite de coco e azeite de dendê, servido com arroz branco.", Map.of("Proteínas", 30.0, "Gorduras", 20.0, "Carboidratos", 50.0));
            refeicao34.setCalorias(controladorRefeicao.calcularCalorias(refeicao34));
            refeicoes.add(refeicao34);
            Refeicao refeicao35 = new Refeicao("Lasanha de Berinjela com Ricota e Espinafre", "Lasanha feita com camadas de berinjela, ricota, espinafre e molho de tomate.", Map.of("Proteínas", 20.0, "Gorduras", 16.0, "Carboidratos", 30.0));
            refeicao35.setCalorias(controladorRefeicao.calcularCalorias(refeicao35));
            refeicoes.add(refeicao35);
            Refeicao refeicao36 = new Refeicao("Moqueca de Peixe com Pirão e Arroz", "Moqueca capixaba feita com peixe branco, tomate, pimentões, cebola, azeite de dendê, acompanhada de pirão e arroz.",  Map.of("Proteínas", 32.0, "Gorduras", 22.0, "Carboidratos", 45.0));
            refeicao36.setCalorias(controladorRefeicao.calcularCalorias(refeicao36));
            refeicoes.add(refeicao36);
            Refeicao refeicao37 = new Refeicao("Creme de Abóbora com Frango Desfiado e Sementes de Girassol", "Creme de abóbora com frango desfiado, gengibre e sementes de girassol.", Map.of("Proteínas", 20.0, "Gorduras", 15.0, "Carboidratos", 30.0));
            refeicao37.setCalorias(controladorRefeicao.calcularCalorias(refeicao37));
            refeicoes.add(refeicao37);
            Refeicao refeicao38 = new Refeicao("Bolo de Fubá com Café Preto", "Fatia de bolo de fubá caseiro acompanhada de uma xícara de café preto.",  Map.of("Proteínas", 8.0, "Gorduras", 12.0, "Carboidratos", 42.0));
            refeicao38.setCalorias(controladorRefeicao.calcularCalorias(refeicao38));
            refeicoes.add(refeicao38);
            Refeicao refeicao39 = new Refeicao("Salmão Assado com Legumes Rústicos", "Filé de salmão assado com batata doce, cebola roxa e pimentões.",  Map.of("Proteínas", 38.0, "Gorduras", 25.0, "Carboidratos", 35.0));
            refeicao39.setCalorias(controladorRefeicao.calcularCalorias(refeicao39));
            refeicoes.add(refeicao39);
            Refeicao refeicao40 = new Refeicao("Frango à Parmegiana com Purê de Batata", "Filé de frango à parmegiana com molho de tomate, queijo e purê de batata.",  Map.of("Proteínas", 35.0, "Gorduras", 28.0, "Carboidratos", 50.0));
            refeicao40.setCalorias(controladorRefeicao.calcularCalorias(refeicao40));
            refeicoes.add(refeicao40);
            Refeicao refeicao41 = new Refeicao("Torta de Palmito com Massa Caseira", "Fatia de torta de palmito com massa caseira e recheio cremoso.", Map.of("Proteínas", 15.0, "Gorduras", 20.0, "Carboidratos", 35.0));
            refeicao41.setCalorias(controladorRefeicao.calcularCalorias(refeicao41));
            refeicoes.add(refeicao41);
            Refeicao refeicao42 = new Refeicao("Polenta Cremosa com Ragu de Cogumelos", "Polenta cremosa com ragu de cogumelos frescos e ervas.",  Map.of("Proteínas", 18.0, "Gorduras", 14.0, "Carboidratos", 45.0));
            refeicao42.setCalorias(controladorRefeicao.calcularCalorias(refeicao42));
            refeicoes.add(refeicao42);
            Refeicao refeicao43 = new Refeicao("Pamonha com Queijo Coalho", "Pamonha cozida com queijo coalho grelhado.", Map.of("Proteínas", 12.0, "Gorduras", 16.0, "Carboidratos", 35.0));
            refeicao43.setCalorias(controladorRefeicao.calcularCalorias(refeicao43));
            refeicoes.add(refeicao43);
            Refeicao refeicao44 = new Refeicao("Tacos com Carne Moída e Guacamole", "Tacos com carne moída temperada, guacamole, tomate e alface picados.", Map.of("Proteínas", 28.0, "Gorduras", 22.0, "Carboidratos", 30.0));
            refeicao44.setCalorias(controladorRefeicao.calcularCalorias(refeicao44));
            refeicoes.add(refeicao44);
            Refeicao refeicao45 = new Refeicao("Espetinho de Frango com Vegetais Grelhados", "Espetinhos de frango com pimentões, cebola e abobrinha grelhados.",  Map.of("Proteínas", 30.0, "Gorduras", 18.0, "Carboidratos", 25.0));
            refeicao45.setCalorias(controladorRefeicao.calcularCalorias(refeicao45));
            refeicoes.add(refeicao45);
            Refeicao refeicao46 = new Refeicao("Salada de Quinoa com Legumes Assados e Frango", "Salada com quinoa, legumes assados (abóbora, cenoura, couve-flor) e cubos de frango grelhado.",  Map.of("Proteínas", 30.0, "Gorduras", 15.0, "Carboidratos", 40.0));
            refeicao46.setCalorias(controladorRefeicao.calcularCalorias(refeicao46));
            refeicoes.add(refeicao46);
            Refeicao refeicao47 = new Refeicao("Sanduíche de Pasta de Grão de Bico com Rúcula e Tomate", "Sanduíche com pasta de grão de bico (homus), rúcula e tomate em pão integral.",  Map.of("Proteínas", 12.0, "Gorduras", 14.0, "Carboidratos", 30.0));
            refeicao47.setCalorias(controladorRefeicao.calcularCalorias(refeicao47));
            refeicoes.add(refeicao47);
            Refeicao refeicao48 = new Refeicao("Misto Quente com Suco de Laranja", "Misto quente com queijo e presunto em pão de forma, acompanhado de suco de laranja natural.", Map.of("Proteínas", 10.0, "Gorduras", 15.0, "Carboidratos", 35.0));
            refeicao48.setCalorias(controladorRefeicao.calcularCalorias(refeicao48));
            refeicoes.add(refeicao48);
            Refeicao refeicao49 = new Refeicao("Chips de Batata Doce Assada com Especiarias", "Chips de batata doce assada com azeite, sal, páprica e pimenta do reino.",  Map.of("Proteínas", 3.0, "Gorduras", 8.0, "Carboidratos", 30.0));
            refeicao49.setCalorias(controladorRefeicao.calcularCalorias(refeicao49));
            refeicoes.add(refeicao49);
            Refeicao refeicao50 = new Refeicao("Iogurte com Frutas Vermelhas e Sementes", "Iogurte natural com mix de frutas vermelhas e sementes de chia e linhaça.", Map.of("Proteínas", 14.0, "Gorduras", 10.0, "Carboidratos", 30.0));
            refeicao50.setCalorias(controladorRefeicao.calcularCalorias(refeicao50));
            refeicoes.add(refeicao50);
            Refeicao refeicao51 = new Refeicao("Cachorro Quente com Pão Integral e Salada", "Cachorro quente com salsicha de frango, pão integral e salada de repolho com cenoura.",  Map.of("Proteínas", 18.0, "Gorduras", 16.0, "Carboidratos", 40.0));
            refeicao51.setCalorias(controladorRefeicao.calcularCalorias(refeicao51));
            refeicoes.add(refeicao51);
            Refeicao refeicao52 = new Refeicao("Quibe Assado com Salada de Pepino", "Quibe assado com carne moída e trigo para quibe, acompanhado de salada de pepino com hortelã.",  Map.of("Proteínas", 22.0, "Gorduras", 18.0, "Carboidratos", 40.0));
            refeicao52.setCalorias(controladorRefeicao.calcularCalorias(refeicao52));
            refeicoes.add(refeicao52);
            Refeicao refeicao53 = new Refeicao("Sopa de Ervilha com Pão Integral Torrado", "Sopa de ervilha nutritiva com pão integral torrado.", Map.of("Proteínas", 15.0, "Gorduras", 10.0, "Carboidratos", 38.0));
            refeicao53.setCalorias(controladorRefeicao.calcularCalorias(refeicao53));
            refeicoes.add(refeicao53);
            Refeicao refeicao54 = new Refeicao("Salada de Grão de Bico com Bacalhau e Pimentões", "Salada com grão de bico, bacalhau desfiado, pimentões coloridos e azeitonas.", Map.of("Proteínas", 28.0, "Gorduras", 16.0, "Carboidratos", 30.0));
            refeicao54.setCalorias(controladorRefeicao.calcularCalorias(refeicao54));
            refeicoes.add(refeicao54);
            Refeicao refeicao55 = new Refeicao("Mousse de Maracujá com Calda de Chocolate Amargo", "Mousse de maracujá caseiro com uma pequena porção de calda de chocolate amargo.", Map.of("Proteínas", 5.0, "Gorduras", 12.0, "Carboidratos", 25.0));
            refeicao55.setCalorias(controladorRefeicao.calcularCalorias(refeicao55));
            refeicoes.add(refeicao55);
            Refeicao refeicao56 = new Refeicao("Chá Mate com Limão e Biscoito de Polvilho", "Chá mate com limão acompanhado de biscoitos de polvilho.",  Map.of("Proteínas", 2.0, "Gorduras", 4.0, "Carboidratos", 20.0));
            refeicao56.setCalorias(controladorRefeicao.calcularCalorias(refeicao56));
            refeicoes.add(refeicao56);
            Refeicao refeicao57 = new Refeicao("Omelete Mediterrâneo com Queijo Feta e Azeitonas", "Omelete com ovos, queijo feta, azeitonas pretas, tomate e orégano.",  Map.of("Proteínas", 18.0, "Gorduras", 16.0, "Carboidratos", 8.0));
            refeicao57.setCalorias(controladorRefeicao.calcularCalorias(refeicao57));
            refeicoes.add(refeicao57);
            Refeicao refeicao58 = new Refeicao("Wrap Grego de Frango com Tzatziki", "Wrap com frango grelhado, molho tzatziki (iogurte, pepino e alho), tomate e cebola roxa.",  Map.of("Proteínas", 25.0, "Gorduras", 18.0, "Carboidratos", 30.0));
            refeicao58.setCalorias(controladorRefeicao.calcularCalorias(refeicao58));
            refeicoes.add(refeicao58);
            Refeicao refeicao59 = new Refeicao("Salada Niçoise com Atum e Ovos", "Salada com atum, ovos cozidos, batata, azeitonas, tomate, alface e um molho de mostarda e limão.",  Map.of("Proteínas", 28.0, "Gorduras", 20.0, "Carboidratos", 25.0));
            refeicao59.setCalorias(controladorRefeicao.calcularCalorias(refeicao59));
            refeicoes.add(refeicao59);
            Refeicao refeicao60 = new Refeicao("Sopa de Tomate com Manjericão e Croutons", "Sopa cremosa de tomate com manjericão fresco e croutons integrais.",  Map.of("Proteínas", 8.0, "Gorduras", 10.0, "Carboidratos", 35.0));
            refeicao60.setCalorias(controladorRefeicao.calcularCalorias(refeicao60));
            refeicoes.add(refeicao60);
            Refeicao refeicao61 = new Refeicao("Curry de Grão de Bico com Arroz Basmati", "Curry indiano de grão de bico com especiarias e leite de coco, servido com arroz basmati.", Map.of("Proteínas", 15.0, "Gorduras", 18.0, "Carboidratos", 45.0));
            refeicao61.setCalorias(controladorRefeicao.calcularCalorias(refeicao61));
            refeicoes.add(refeicao61);
            Refeicao refeicao62 = new Refeicao("Tacos de Peixe com Repolho Roxo e Molho de Coentro", "Tacos de milho com peixe grelhado ou empanado, repolho roxo picado e molho de coentro e limão.",  Map.of("Proteínas", 22.0, "Gorduras", 16.0, "Carboidratos", 30.0));
            refeicao62.setCalorias(controladorRefeicao.calcularCalorias(refeicao62));
            refeicoes.add(refeicao62);
            Refeicao refeicao63 = new Refeicao("Pad Thai com Frango e Tofu", "Macarrão de arroz salteado com frango, tofu, ovo, amendoim e molho agridoce.", Map.of("Proteínas", 25.0, "Gorduras", 15.0, "Carboidratos", 50.0));
            refeicao63.setCalorias(controladorRefeicao.calcularCalorias(refeicao63));
            refeicoes.add(refeicao63);
            Refeicao refeicao64 = new Refeicao("Frittata Italiana com Legumes e Queijo", "Frittata italiana com ovos, abobrinha, pimentões, cebola e queijo parmesão.",  Map.of("Proteínas", 20.0, "Gorduras", 18.0, "Carboidratos", 12.0));
            refeicao64.setCalorias(controladorRefeicao.calcularCalorias(refeicao64));
            refeicoes.add(refeicao64);
            Refeicao refeicao65 = new Refeicao("Burrito Bowl com Carne e Abacate", "Bowl com carne grelhada ou desfiada, arroz integral, feijão, abacate, milho e salsa.",  Map.of("Proteínas", 30.0, "Gorduras", 22.0, "Carboidratos", 40.0));
            refeicao65.setCalorias(controladorRefeicao.calcularCalorias(refeicao65));
            refeicoes.add(refeicao65);
            Refeicao refeicao66 = new Refeicao("Sopa de Lentilha Marroquina com Especiarias", "Sopa de lentilha com especiarias, tomate, cenoura e coentro.", Map.of("Proteínas", 18.0, "Gorduras", 12.0, "Carboidratos", 35.0));
            refeicao66.setCalorias(controladorRefeicao.calcularCalorias(refeicao66));
            refeicoes.add(refeicao66);
            Refeicao refeicao67 = new Refeicao("Falafel com Molho Tahine e Salada", "Falafel assado ou frito, servido com molho tahine e salada de pepino, tomate e alface.",  Map.of("Proteínas", 15.0, "Gorduras", 20.0, "Carboidratos", 30.0));
            refeicao67.setCalorias(controladorRefeicao.calcularCalorias(refeicao67));
            refeicoes.add(refeicao67);
            Refeicao refeicao68 = new Refeicao("Sushi Variado com Salmão, Atum e Legumes", "Seleção de sushi com salmão, atum, pepino e abacate, acompanhado de molho shoyu.", Map.of("Proteínas", 20.0, "Gorduras", 15.0, "Carboidratos", 40.0));
            refeicao68.setCalorias(controladorRefeicao.calcularCalorias(refeicao68));
            refeicoes.add(refeicao68);
            Refeicao refeicao69 = new Refeicao("Enchiladas de Frango com Molho Vermelho e Queijo", "Tortillas de milho recheadas com frango desfiado, molho vermelho e queijo gratinado.",  Map.of("Proteínas", 28.0, "Gorduras", 20.0, "Carboidratos", 45.0));
            refeicao69.setCalorias(controladorRefeicao.calcularCalorias(refeicao69));
            refeicoes.add(refeicao69);
            Refeicao refeicao70 = new Refeicao("Ceviche Peruano com Batata Doce", "Ceviche feito com peixe branco marinado em limão, cebola roxa, coentro e pimenta, servido com batata doce cozida.", Map.of("Proteínas", 25.0, "Gorduras", 10.0, "Carboidratos", 30.0));
            refeicao70.setCalorias(controladorRefeicao.calcularCalorias(refeicao70));
            refeicoes.add(refeicao70);
            Refeicao refeicao71 = new Refeicao("Ratatouille com Pão Rústico", "Ratatouille com berinjela, abobrinha, tomate, pimentão e ervas, acompanhado de pão rústico.", Map.of("Proteínas", 5.0, "Gorduras", 12.0, "Carboidratos", 35.0));
            refeicao71.setCalorias(controladorRefeicao.calcularCalorias(refeicao71));
            refeicoes.add(refeicao71);
            Refeicao refeicao72 = new Refeicao("Moussaka Grega com Carne Moída e Berinjela", "Moussaka com camadas de berinjela, carne moída, molho bechamel e queijo.",  Map.of("Proteínas", 25.0, "Gorduras", 25.0, "Carboidratos", 40.0));
            refeicao72.setCalorias(controladorRefeicao.calcularCalorias(refeicao72));
            refeicoes.add(refeicao72);
            Refeicao refeicao73 = new Refeicao("Pho Vietnamita com Carne e Legumes", "Sopa vietnamita com macarrão de arroz, carne bovina, broto de feijão, ervas e caldo aromático.", Map.of("Proteínas", 22.0, "Gorduras", 15.0, "Carboidratos", 40.0));
            refeicao73.setCalorias(controladorRefeicao.calcularCalorias(refeicao73));
            refeicoes.add(refeicao73);
            Refeicao refeicao74 = new Refeicao("Quiche Lorraine com Salada Verde", "Quiche Lorraine com ovos, creme de leite, bacon e queijo, servida com salada verde.", Map.of("Proteínas", 18.0, "Gorduras", 25.0, "Carboidratos", 20.0));
            refeicao74.setCalorias(controladorRefeicao.calcularCalorias(refeicao74));
            refeicoes.add(refeicao74);
            Refeicao refeicao75 = new Refeicao("Paella de Frutos do Mar com Arroz e Açafrão", "Paella espanhola com arroz, camarões, mexilhões, lula, ervilha e açafrão.", Map.of("Proteínas", 30.0, "Gorduras", 20.0, "Carboidratos", 45.0));
            refeicao75.setCalorias(controladorRefeicao.calcularCalorias(refeicao75));
            refeicoes.add(refeicao75);
            Refeicao refeicao76 = new Refeicao("Hummus com Cenoura e Pepino", "Pasta de grão de bico (hummus) acompanhada de palitos de cenoura e pepino.", Map.of("Proteínas", 10.0, "Gorduras", 15.0, "Carboidratos", 25.0));
            refeicao76.setCalorias(controladorRefeicao.calcularCalorias(refeicao76));
            refeicoes.add(refeicao76);
            Refeicao refeicao77 = new Refeicao("Salada de Quinoa com Romã e Hortelã", "Salada fresca com quinoa, romã, pepino, hortelã e um molho de limão.", Map.of("Proteínas", 12.0, "Gorduras", 10.0, "Carboidratos", 35.0));
            refeicao77.setCalorias(controladorRefeicao.calcularCalorias(refeicao77));
            refeicoes.add(refeicao77);
            Refeicao refeicao78 = new Refeicao("Lassi de Manga com Cardamomo", "Bebida indiana feita com iogurte, manga, cardamomo e um toque de mel.", Map.of("Proteínas", 8.0, "Gorduras", 6.0, "Carboidratos", 35.0));
            refeicao78.setCalorias(controladorRefeicao.calcularCalorias(refeicao78));
            refeicoes.add(refeicao78);
            Refeicao refeicao79 = new Refeicao("Arepas com Queijo e Abacate", "Arepas venezuelanas recheadas com queijo e fatias de abacate.", Map.of("Proteínas", 10.0, "Gorduras", 18.0, "Carboidratos", 30.0));
            refeicao79.setCalorias(controladorRefeicao.calcularCalorias(refeicao79));
            refeicoes.add(refeicao79);
            Refeicao refeicao80 = new Refeicao("Goulash Húngaro com Pão de Centeio", "Goulash feito com carne bovina, páprica e legumes, servido com pão de centeio.",  Map.of("Proteínas", 30.0, "Gorduras", 20.0, "Carboidratos", 40.0));
            refeicao80.setCalorias(controladorRefeicao.calcularCalorias(refeicao80));
            refeicoes.add(refeicao80);
            Refeicao refeicao81 = new Refeicao("Bibimbap Coreano com Carne e Vegetais", "Bibimbap com arroz, carne bovina, ovo frito, legumes variados e molho gochujang.", Map.of("Proteínas", 28.0, "Gorduras", 18.0, "Carboidratos", 48.0));
            refeicao81.setCalorias(controladorRefeicao.calcularCalorias(refeicao81));
            refeicoes.add(refeicao81);
            Refeicao refeicao82 = new Refeicao("Cachapas com Queijo de Minas", "Cachapas venezuelanas (panquecas de milho) com queijo de minas.",  Map.of("Proteínas", 12.0, "Gorduras", 15.0, "Carboidratos", 45.0));
            refeicao82.setCalorias(controladorRefeicao.calcularCalorias(refeicao82));
            refeicoes.add(refeicao82);
            Refeicao refeicao83 = new Refeicao("Dolma com Iogurte e Hortelã", "Dolma (folhas de uva recheadas com arroz) acompanhado de iogurte e hortelã.",  Map.of("Proteínas", 8.0, "Gorduras", 10.0, "Carboidratos", 30.0));
            refeicao83.setCalorias(controladorRefeicao.calcularCalorias(refeicao83));
            refeicoes.add(refeicao83);
            Refeicao refeicao84 = new Refeicao("Bruschetta com Tomate e Manjericão", "Fatias de pão italiano torrado com tomate fresco picado, alho, manjericão e azeite.",  Map.of("Proteínas", 5.0, "Gorduras", 10.0, "Carboidratos", 25.0));
            refeicao84.setCalorias(controladorRefeicao.calcularCalorias(refeicao84));
            refeicoes.add(refeicao84);
            Refeicao refeicao85 = new Refeicao("Tabbouleh com Frango Grelhado", "Salada Tabbouleh com trigo para quibe, tomate, pepino, salsa, hortelã e frango grelhado.",  Map.of("Proteínas", 22.0, "Gorduras", 12.0, "Carboidratos", 30.0));
            refeicao85.setCalorias(controladorRefeicao.calcularCalorias(refeicao85));
            refeicoes.add(refeicao85);
            Refeicao refeicao86 = new Refeicao("Pão Pita com Falafel e Molho Tahine", "Pão pita integral com falafel, molho tahine, alface, tomate e cebola roxa.", Map.of("Proteínas", 18.0, "Gorduras", 20.0, "Carboidratos", 35.0));
            refeicao86.setCalorias(controladorRefeicao.calcularCalorias(refeicao86));
            refeicoes.add(refeicao86);

        } catch (Exception e) {
            System.err.println("Erro ao adicionar refeição pré-definida: " + e.getMessage());
        }
        return refeicoes;
    }

    public static List<Treino> inicializarTreinos() {
        List<Treino> treinos = new ArrayList<>();

        Treino treino1 = new Treino();
        treino1.setNome("Treino de Força (A) - Completo");
        treino1.setTipoDeTreino("FORCA");
        treino1.setDuracao(45);
        treino1.setNivelDeDificuldade(4);
        treinos.add(treino1);

        Treino treino2 = new Treino();
        treino2.setNome("Treino de Força (B) - Inferior");
        treino2.setTipoDeTreino("FORCA");
        treino2.setDuracao(40);
        treino2.setNivelDeDificuldade(4);
        treinos.add(treino2);

        Treino treino3 = new Treino();
        treino3.setNome("Treino de Resistência (A)");
        treino3.setTipoDeTreino("RESISTENCIA");
        treino3.setDuracao(40);
        treino3.setNivelDeDificuldade(3);
        treinos.add(treino3);


        Treino treino4 = new Treino();
        treino4.setNome("Treino de Resistência (B)");
        treino4.setTipoDeTreino("RESISTENCIA");
        treino4.setDuracao(40);
        treino4.setNivelDeDificuldade(3);
        treinos.add(treino4);

        Treino treino5 = new Treino();
        treino5.setNome("Treino de Core e Funcional Completo");
        treino5.setTipoDeTreino("CORE");
        treino5.setDuracao(50);
        treino5.setNivelDeDificuldade(4);
        treinos.add(treino5);

        Treino treino6 = new Treino();
        treino6.setNome("Treino de Core (A)");
        treino6.setTipoDeTreino("CORE");
        treino6.setDuracao(40);
        treino6.setNivelDeDificuldade(4);
        treinos.add(treino6);

        Treino treino7 = new Treino();
        treino7.setNome("Treino Aeróbico - Corrida");
        treino7.setTipoDeTreino("AEROBICO");
        treino7.setDuracao(50);
        treino7.setNivelDeDificuldade(3);
        treinos.add(treino7);

        Treino treino8 = new Treino();
        treino8.setNome("Treino Aeróbico - Caminhada");
        treino8.setTipoDeTreino("AEROBICO");
        treino8.setDuracao(40);
        treino8.setNivelDeDificuldade(2);
        treinos.add(treino8);

        Treino treino9 = new Treino();
        treino9.setNome("Treino de Flexibilidade");
        treino9.setTipoDeTreino("FLEXIBILIDADE");
        treino9.setDuracao(40);
        treino9.setNivelDeDificuldade(2);
        treinos.add(treino9);

        Treino treino10 = new Treino();
        treino10.setNome("Treino de Equilíbrio");
        treino10.setTipoDeTreino("EQUILIBRIO");
        treino10.setDuracao(30);
        treino10.setNivelDeDificuldade(3);
        treinos.add(treino10);

        Treino treino11 = new Treino();
        treino11.setNome("Treino Aquático Completo");
        treino11.setTipoDeTreino("AQUATICO");
        treino11.setDuracao(50);
        treino11.setNivelDeDificuldade(4);
        treinos.add(treino11);

        Treino treino12 = new Treino();
        treino12.setNome("Treino HIIT");
        treino12.setTipoDeTreino("CARDIO");
        treino12.setDuracao(30);
        treino12.setNivelDeDificuldade(5);
        treinos.add(treino12);

        Treino treino13 = new Treino();
        treino13.setNome("Treino Personalizado do Cliente");
        treino13.setTipoDeTreino("OUTRO");
        treino13.setDuracao(60);
        treino13.setNivelDeDificuldade(5);
        treinos.add(treino13);

        Treino treino14 = new Treino();
        treino14.setNome("Treino Completo");
        treino14.setTipoDeTreino("COMPLETO");
        treino14.setDuracao(45);
        treino14.setNivelDeDificuldade(3);
        treinos.add(treino14);

        Treino treino15 = new Treino();
        treino15.setNome("Treino de Cardio para Iniciantes");
        treino15.setTipoDeTreino("CARDIO");
        treino15.setDuracao(30);
        treino15.setNivelDeDificuldade(1);
        treinos.add(treino15);

        return treinos;
    }
}