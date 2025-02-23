package com.example.queimacaloria.negocio;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.FileNotFoundException;
import java.io.IOException; // Importante: Importar IOException
import java.util.List;

public class GeradorPDF {

    // Método para gerar relatório de DIETAS
    public static void gerarRelatorioDietas(List<Dieta> dietas, String caminhoArquivo) throws IOException { // Adicionada IOException
        try (PdfWriter writer = new PdfWriter(caminhoArquivo);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Relatório de Dietas").setFontSize(18).setBold());

            if (dietas != null && !dietas.isEmpty()) {
                adicionarSecaoDietas(document, dietas);
            } else {
                document.add(new Paragraph("Nenhuma dieta registrada.").setFontColor(ColorConstants.GRAY));
            }
        } // Não precisa de catch aqui, pois a exceção é propagada
    }

    // Método para gerar relatório de EXERCÍCIOS
    public static void gerarRelatorioExercicios(List<Exercicio> exercicios, String caminhoArquivo) throws IOException { // Adicionada IOException
        try (PdfWriter writer = new PdfWriter(caminhoArquivo);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Relatório de Exercícios").setFontSize(18).setBold());

            if (exercicios != null && !exercicios.isEmpty()) {
                adicionarSecaoExercicios(document, exercicios);
            } else {
                document.add(new Paragraph("Nenhum exercício registrado.").setFontColor(ColorConstants.GRAY));
            }
        }
    }

    // Método para gerar relatório de METAS
    public static void gerarRelatorioMetas(List<Meta> metas, String caminhoArquivo) throws IOException { // Adicionada IOException
        try (PdfWriter writer = new PdfWriter(caminhoArquivo);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Relatório de Metas").setFontSize(18).setBold());

            if (metas != null && !metas.isEmpty()) {
                adicionarSecaoMetas(document, metas);
            } else {
                document.add(new Paragraph("Nenhuma meta registrada.").setFontColor(ColorConstants.GRAY));
            }
        }
    }
    // Método para gerar relatório de Refeições
    public static void gerarRelatorioRefeicoes(List<Refeicao> refeicoes, String caminhoArquivo) throws IOException { // Adicionada IOException
        try (PdfWriter writer = new PdfWriter(caminhoArquivo);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Relatório de Refeições").setFontSize(18).setBold());

            if (refeicoes != null && !refeicoes.isEmpty()) {
                adicionarSecaoRefeicoes(document, refeicoes);

            } else {
                document.add(new Paragraph("Nenhuma refeição registrada.").setFontColor(ColorConstants.GRAY));
            }
        }
    }

    // Método para gerar relatório de TREINOS
    public static void gerarRelatorioTreinos(List<Treino> treinos, String caminhoArquivo) throws IOException { // Adicionada IOException
        try (PdfWriter writer = new PdfWriter(caminhoArquivo);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Relatório de Treinos").setFontSize(18).setBold());

            if (treinos != null && !treinos.isEmpty()) {
                adicionarSecaoTreinos(document, treinos);
            } else{
                document.add(new Paragraph("Nenhum treino registrado.").setFontColor(ColorConstants.GRAY));
            }
        }
    }

    // Métodos auxiliares para criar as seções (já existiam, mas precisam ser adaptados/chamados)
    private static void adicionarSecaoMetas(Document document, List<Meta> metas) {
        document.add(new Paragraph("Metas:").setFontSize(14).setBold());

        Table tabela = new Table(UnitValue.createPercentArray(new float[]{50, 30, 20}))
                .useAllAvailableWidth();

        tabela.addHeaderCell(new Paragraph("Descrição").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Tipo").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Progresso").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);


        for (Meta meta : metas) {
            tabela.addCell(new Paragraph(meta.getDescricao()));
            tabela.addCell(new Paragraph(meta.getTipo().toString()));

            // Cálculo e formatação corretos do progresso *dentro* do addCell:
            double progresso = 0.0;
            if (meta.getValorAlvo() > 0) { // Evita divisão por zero
                progresso = (meta.getProgressoAtual() / meta.getValorAlvo()) * 100.0;
            }
            tabela.addCell(new Paragraph(String.format("%.1f%%", progresso))); // Formata aqui!
        }

        document.add(tabela);
    }


    private static void adicionarSecaoDietas(Document document, List<Dieta> dietas) {
        document.add(new Paragraph("Dietas:").setFontSize(14).setBold());

        Table tabela = new Table(UnitValue.createPercentArray(new float[]{50, 30, 20}))
                .useAllAvailableWidth();

        tabela.addHeaderCell(new Paragraph("Nome").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Objetivo").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Calorias Diárias").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);

        for (Dieta dieta : dietas) {
            tabela.addCell(new Paragraph(dieta.getNome()));
            tabela.addCell(new Paragraph(dieta.getObjetivo().toString()));
            tabela.addCell(new Paragraph(String.valueOf(dieta.getCaloriasDiarias())));
        }

        document.add(tabela);
    }
    private static void adicionarSecaoExercicios(Document document, List<Exercicio> exercicios) {
        document.add(new Paragraph("Exercícios:").setFontSize(14).setBold());

        Table tabela = new Table(UnitValue.createPercentArray(new float[]{40, 30, 15, 15}))
                .useAllAvailableWidth();

        tabela.addHeaderCell(new Paragraph("Nome").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Tipo").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Tempo (min)").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Calorias Queimadas/min").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);

        for (Exercicio exercicio : exercicios) {
            tabela.addCell(new Paragraph(exercicio.getNome()));
            tabela.addCell(new Paragraph(exercicio.getTipo().toString()));
            tabela.addCell(new Paragraph(String.valueOf(exercicio.getTempo())));
            tabela.addCell(new Paragraph(String.valueOf(exercicio.getCaloriasQueimadas())));
        }

        document.add(tabela);
    }

    private static void adicionarSecaoRefeicoes(Document document, List<Refeicao> refeicoes) {
        document.add(new Paragraph("Refeições:").setFontSize(14).setBold());

        Table tabela = new Table(UnitValue.createPercentArray(new float[]{40, 40, 20})) // Ajuste as larguras conforme necessário
                .useAllAvailableWidth();

        tabela.addHeaderCell(new Paragraph("Nome").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Descrição").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Calorias").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);

        for (Refeicao refeicao : refeicoes) {
            tabela.addCell(new Paragraph(refeicao.getNome()));
            tabela.addCell(new Paragraph(refeicao.getDescricao()));
            tabela.addCell(new Paragraph(String.valueOf(refeicao.getCalorias())));
        }

        document.add(tabela);
    }
    private static void adicionarSecaoTreinos(Document document, List<Treino> treinos) {
        document.add(new Paragraph("Treinos:").setFontSize(14).setBold());

        Table tabela = new Table(UnitValue.createPercentArray(new float[]{40, 30, 15, 15}))
                .useAllAvailableWidth();

        tabela.addHeaderCell(new Paragraph("Nome").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Tipo").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Duração (min)").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Dificuldade").setBold().setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);

        for (Treino treino : treinos) {
            tabela.addCell(new Paragraph(treino.getNome()));
            tabela.addCell(new Paragraph(treino.getTipoDeTreino()));
            tabela.addCell(new Paragraph(String.valueOf(treino.getDuracao())));
            tabela.addCell(new Paragraph(String.valueOf(treino.getNivelDeDificuldade())));
        }

        document.add(tabela);
    }

    // Outros métodos (adicionarSecaoExercicios, adicionarSecaoRefeicoes) seriam similares.
}