package com.example.queimacaloria.controllers;

import com.example.queimacaloria.negocio.PesoRegistro;
import com.example.queimacaloria.negocio.Usuario;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoricoController {

    @FXML private LineChart<String, Number> graficoPeso;
    @FXML private LineChart<String, Number> graficoIMC;
    @FXML private LineChart<String, Number> graficoCintura;
    @FXML private LineChart<String, Number> graficoBiceps;
    @FXML private LineChart<String, Number> graficoCoxa;
    @FXML private LineChart<String, Number> graficoQuadril;
    @FXML private CategoryAxis xAxisPeso;
    @FXML private NumberAxis yAxisPeso;
    @FXML private CategoryAxis xAxisIMC;
    @FXML private NumberAxis yAxisIMC;
    @FXML private CategoryAxis xAxisCintura;
    @FXML private NumberAxis yAxisCintura;
    @FXML private CategoryAxis xAxisBiceps;
    @FXML private NumberAxis yAxisBiceps;
    @FXML private CategoryAxis xAxisCoxa;
    @FXML private NumberAxis yAxisCoxa;
    @FXML private CategoryAxis xAxisQuadril;
    @FXML private NumberAxis yAxisQuadril;
    private Usuario usuarioLogado;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        if (usuarioLogado != null) {
            atualizarGraficos();
        }
    }

    @FXML
    public void initialize() {
        if(xAxisPeso != null){
            xAxisPeso.setTickLabelRotation(45);
            xAxisPeso.setLabel("Data");
        }
        if(yAxisPeso != null){
            yAxisPeso.setLabel("Peso (kg)");
        }

        if(xAxisIMC != null){
            xAxisIMC.setTickLabelRotation(45);
            xAxisIMC.setLabel("Data");
        }
        if(yAxisIMC != null){
            yAxisIMC.setLabel("IMC");
        }

        if(xAxisCintura != null){
            xAxisCintura.setTickLabelRotation(45);
            xAxisCintura.setLabel("Data");
        }
        if(yAxisCintura != null){
            yAxisCintura.setLabel("Cintura (cm)");
        }

        if(xAxisBiceps != null){
            xAxisBiceps.setTickLabelRotation(45);
            xAxisBiceps.setLabel("Data");
        }
        if(yAxisBiceps != null){
            yAxisBiceps.setLabel("Bíceps (cm)");
        }

        if(xAxisCoxa != null){
            xAxisCoxa.setTickLabelRotation(45);
            xAxisCoxa.setLabel("Data");
        }
        if(yAxisCoxa != null){
            yAxisCoxa.setLabel("Coxa (cm)");
        }

        if(xAxisQuadril != null){
            xAxisQuadril.setTickLabelRotation(45);
            xAxisQuadril.setLabel("Data");
        }
        if(yAxisQuadril != null){
            yAxisQuadril.setLabel("Quadril (cm)");
        }

    }

    private void atualizarGraficos() {
        if (usuarioLogado != null) {
            atualizarGraficoPeso();
            atualizarGraficoIMC();
            atualizarGraficoMedida(graficoCintura, xAxisCintura, usuarioLogado.cinturaProperty(), "Cintura (cm)");
            atualizarGraficoMedida(graficoBiceps, xAxisBiceps, usuarioLogado.bicepsProperty(), "Bíceps (cm)");
            atualizarGraficoMedida(graficoCoxa, xAxisCoxa, usuarioLogado.coxaProperty(), "Coxa (cm)");
            atualizarGraficoMedida(graficoQuadril, xAxisQuadril, usuarioLogado.quadrilProperty(), "Quadril (cm)");
        }
    }

    private void atualizarGraficoPeso() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Peso");

        // Ordena o histórico por data
        List<PesoRegistro> historicoOrdenado = new ArrayList<>(usuarioLogado.getHistoricoPeso());
        historicoOrdenado.sort((r1, r2) -> r1.getData().compareTo(r2.getData()));

        for (PesoRegistro registro : historicoOrdenado) {
            String dataFormatada = registro.getData().format(dateFormatter);
            series.getData().add(new XYChart.Data<>(dataFormatada, registro.getPeso()));
        }

        graficoPeso.getData().clear();
        graficoPeso.getData().add(series);
        graficoPeso.setLegendVisible(false);
    }
    private void atualizarGraficoIMC() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("IMC");

        List<PesoRegistro> historicoOrdenado = new ArrayList<>(usuarioLogado.getHistoricoPeso());
        historicoOrdenado.sort((r1, r2) -> r1.getData().compareTo(r2.getData()));
        double alturaEmMetros = usuarioLogado.getAltura() / 100.0;

        for (PesoRegistro registro : historicoOrdenado) {
            String dataFormatada = registro.getData().format(dateFormatter);
            // Calcular o IMC
            double imc = registro.getPeso() / (alturaEmMetros * alturaEmMetros);
            series.getData().add(new XYChart.Data<>(dataFormatada, imc));
        }

        graficoIMC.getData().clear();
        graficoIMC.getData().add(series);
        graficoIMC.setLegendVisible(false);
    }

    private void atualizarGraficoMedida(LineChart<String, Number> grafico, CategoryAxis xAxis, DoubleProperty medidaProperty, String labelEixoY) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(labelEixoY);

        if (usuarioLogado.getHistoricoPeso().isEmpty()) {
            // Se não houver histórico de peso, limpa o gráfico e retorna.
            grafico.getData().clear();
            return;
        }

        List<PesoRegistro> historicoOrdenado = new ArrayList<>(usuarioLogado.getHistoricoPeso());
        historicoOrdenado.sort((r1, r2) -> r1.getData().compareTo(r2.getData()));


        for (PesoRegistro registro : historicoOrdenado) {
            String dataFormatada = registro.getData().format(dateFormatter);
            double valorMedida = obterValorMedida(registro.getData(), medidaProperty);
            series.getData().add(new XYChart.Data<>(dataFormatada, valorMedida));
        }

        grafico.getData().clear();
        grafico.getData().add(series);
        grafico.setLegendVisible(false);
        xAxis.setLabel("Data");

    }

    //Método auxiliar para obter o valor da medida com base na data
    private double obterValorMedida(LocalDate data, DoubleProperty medidaProperty) {

        if (data.equals(usuarioLogado.getDataNascimento())) {
            return 0.0; // Valor inicial de 0
        }
        else{
            return medidaProperty.get();
        }
    }
}