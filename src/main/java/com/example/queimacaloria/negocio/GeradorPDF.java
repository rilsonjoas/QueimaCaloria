package com.example.queimacaloria.negocio;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.IOException;
import java.util.List;

public class GeradorPDF {

    // Método genérico para adicionar cabeçalho (YouFit + Nome do Usuário)
    private static void adicionarCabecalho(Document document, String nomeUsuario) {
        document.add(new Paragraph("YouFit").setFontSize(24).setBold().setFontColor(ColorConstants.BLUE));
        document.add(new Paragraph("Relatório para: " + nomeUsuario).setFontSize(16).setFontColor(ColorConstants.BLACK));
        document.add(new Paragraph("\n")); // Linha em branco para espaçamento.
    }

    // Métodos para gerar relatórios
    public static void gerarRelatorioDietas(List<Dieta> dietas, String caminhoArquivo, String nomeUsuario) throws IOException {
        try (PdfWriter writer = new PdfWriter(caminhoArquivo);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            adicionarCabecalho(document, nomeUsuario);
            document.add(new Paragraph("Relatório de Dietas").setFontSize(18));

            if (dietas != null && !dietas.isEmpty()) {
                adicionarSecaoDietas(document, dietas);
            } else {
                document.add(new Paragraph("Nenhuma dieta registrada.").setFontColor(ColorConstants.GRAY));
            }
        }
    }

    public static void gerarRelatorioExercicios(List<Exercicio> exercicios, String caminhoArquivo, String nomeUsuario) throws IOException {
        try (PdfWriter writer = new PdfWriter(caminhoArquivo);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            adicionarCabecalho(document, nomeUsuario);
            document.add(new Paragraph("Relatório de Exercícios").setFontSize(18));

            if (exercicios != null && !exercicios.isEmpty()) {
                adicionarSecaoExercicios(document, exercicios);
            } else {
                document.add(new Paragraph("Nenhum exercício registrado.").setFontColor(ColorConstants.GRAY));
            }
        }
    }


    public static void gerarRelatorioMetas(List<Meta> metas, String caminhoArquivo, String nomeUsuario) throws IOException {
        try (PdfWriter writer = new PdfWriter(caminhoArquivo);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            adicionarCabecalho(document, nomeUsuario);
            document.add(new Paragraph("Relatório de Metas").setFontSize(18));

            if (metas != null && !metas.isEmpty()) {
                adicionarSecaoMetas(document, metas);
            } else {
                document.add(new Paragraph("Nenhuma meta registrada.").setFontColor(ColorConstants.GRAY));
            }
        }
    }


    public static void gerarRelatorioRefeicoes(List<Refeicao> refeicoes, String caminhoArquivo, String nomeUsuario) throws IOException {
        try (PdfWriter writer = new PdfWriter(caminhoArquivo);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            adicionarCabecalho(document, nomeUsuario);
            document.add(new Paragraph("Relatório de Refeições").setFontSize(18));

            if (refeicoes != null && !refeicoes.isEmpty()) {
                adicionarSecaoRefeicoes(document, refeicoes);

            } else {
                document.add(new Paragraph("Nenhuma refeição registrada.").setFontColor(ColorConstants.GRAY));
            }
        }
    }

    public static void gerarRelatorioTreinos(List<Treino> treinos, String caminhoArquivo, String nomeUsuario) throws IOException {
        try (PdfWriter writer = new PdfWriter(caminhoArquivo);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            adicionarCabecalho(document, nomeUsuario);
            document.add(new Paragraph("Relatório de Treinos").setFontSize(18));

            if (treinos != null && !treinos.isEmpty()) {
                adicionarSecaoTreinos(document, treinos);
            } else{
                document.add(new Paragraph("Nenhum treino registrado.").setFontColor(ColorConstants.GRAY));
            }
        }
    }


    // Métodos auxiliares para criar as seções
    private static void adicionarSecaoMetas(Document document, List<Meta> metas) {
        document.add(new Paragraph("Metas:").setFontSize(14));

        Table tabela = new Table(UnitValue.createPercentArray(new float[]{50, 30, 20}))
                .useAllAvailableWidth();

        tabela.addHeaderCell(new Paragraph("Descrição").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Tipo").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Progresso").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);


        for (Meta meta : metas) {
            tabela.addCell(new Paragraph(meta.getDescricao()));
            tabela.addCell(new Paragraph(meta.getTipo().toString()));

            // Cálculo e formatação corretos do progresso *dentro* do addCell:
            double progresso = 0.0;
            if (meta.getValorAlvo() > 0) { // Evita divisão por zero
                progresso = (meta.getProgressoAtual() / meta.getValorAlvo()) * 100.0;
            }
            tabela.addCell(new Paragraph(String.format("%.1f%%", progresso)));
        }

        document.add(tabela);
    }


    private static void adicionarSecaoDietas(Document document, List<Dieta> dietas) {
        document.add(new Paragraph("Dietas:").setFontSize(14));

        Table tabela = new Table(UnitValue.createPercentArray(new float[]{50, 30, 20}))
                .useAllAvailableWidth();

        tabela.addHeaderCell(new Paragraph("Nome").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Objetivo").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Calorias Diárias").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);

        for (Dieta dieta : dietas) {
            tabela.addCell(new Paragraph(dieta.getNome()));
            tabela.addCell(new Paragraph(dieta.getObjetivo().toString()));
            tabela.addCell(new Paragraph(String.valueOf(dieta.getCaloriasDiarias())));
        }

        document.add(tabela);
    }
    private static void adicionarSecaoExercicios(Document document, List<Exercicio> exercicios) {
        document.add(new Paragraph("Exercícios:").setFontSize(14));

        Table tabela = new Table(UnitValue.createPercentArray(new float[]{40, 30, 15, 15}))
                .useAllAvailableWidth();

        tabela.addHeaderCell(new Paragraph("Nome").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Tipo").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Tempo (min)").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Calorias Queimadas/min").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);

        for (Exercicio exercicio : exercicios) {
            tabela.addCell(new Paragraph(exercicio.getNome()));
            tabela.addCell(new Paragraph(exercicio.getTipo().toString()));
            tabela.addCell(new Paragraph(String.valueOf(exercicio.getTempo())));
            tabela.addCell(new Paragraph(String.valueOf(exercicio.getCaloriasQueimadas())));
        }

        document.add(tabela);
    }

    private static void adicionarSecaoRefeicoes(Document document, List<Refeicao> refeicoes) {
        document.add(new Paragraph("Refeições:").setFontSize(14));

        Table tabela = new Table(UnitValue.createPercentArray(new float[]{40, 40, 20})) // Ajuste as larguras conforme necessário
                .useAllAvailableWidth();

        tabela.addHeaderCell(new Paragraph("Nome").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Descrição").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Calorias").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);

        for (Refeicao refeicao : refeicoes) {
            tabela.addCell(new Paragraph(refeicao.getNome()));
            tabela.addCell(new Paragraph(refeicao.getDescricao()));
            tabela.addCell(new Paragraph(String.valueOf(refeicao.getCalorias())));
        }

        document.add(tabela);
    }
    private static void adicionarSecaoTreinos(Document document, List<Treino> treinos) {
        document.add(new Paragraph("Treinos:").setFontSize(14));

        Table tabela = new Table(UnitValue.createPercentArray(new float[]{40, 30, 15, 15}))
                .useAllAvailableWidth();

        tabela.addHeaderCell(new Paragraph("Nome").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Tipo").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Duração (min)").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);
        tabela.addHeaderCell(new Paragraph("Dificuldade").setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY);

        for (Treino treino : treinos) {
            tabela.addCell(new Paragraph(treino.getNome()));
            tabela.addCell(new Paragraph(treino.getTipoDeTreino()));
            tabela.addCell(new Paragraph(String.valueOf(treino.getDuracao())));
            tabela.addCell(new Paragraph(String.valueOf(treino.getNivelDeDificuldade())));
        }

        document.add(tabela);
    }

}