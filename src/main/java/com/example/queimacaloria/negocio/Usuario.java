package com.example.queimacaloria.negocio;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Usuario {
    private final UUID id;
    private StringProperty nome = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty senha = new SimpleStringProperty();
    private ObjectProperty<LocalDate> dataNascimento = new SimpleObjectProperty<>();
    private ObjectProperty<Sexo> sexo = new SimpleObjectProperty<>();
    private FloatProperty peso = new SimpleFloatProperty();
    private FloatProperty altura = new SimpleFloatProperty();
    private FloatProperty imc = new SimpleFloatProperty();
    private IntegerProperty aguaConsumida = new SimpleIntegerProperty(0);
    private StringProperty tipo = new SimpleStringProperty(TipoUsuario.USUARIO_COMUM.getDescricao()); //MODIFICADO

    @Setter
    private ObservableList<Meta> metas = FXCollections.observableArrayList();
    @Setter
    private ObservableList<Treino> treinos = FXCollections.observableArrayList();
    @Setter
    private ObservableList<Dieta> dietas = FXCollections.observableArrayList();
    @Setter
    private ObservableList<Exercicio> exercicios = FXCollections.observableArrayList();

    private ObjectProperty<Dieta> dietaAtiva = new SimpleObjectProperty<>();

    private IntegerProperty pontuacao = new SimpleIntegerProperty(0);

    // Adicionando o histórico de peso
    private ListProperty<PesoRegistro> historicoPeso = new SimpleListProperty<>(FXCollections.observableArrayList());

    // NOVOS CAMPOS PARA MEDIDAS CORPORAIS
    private DoubleProperty cintura = new SimpleDoubleProperty();
    private DoubleProperty biceps = new SimpleDoubleProperty();
    private DoubleProperty coxa = new SimpleDoubleProperty();
    private DoubleProperty quadril = new SimpleDoubleProperty();
    // Adicione outros campos conforme necessário (peito, panturrilha, etc.)


    // Enum para o sexo do usuário.
    public enum Sexo {
        Masculino,
        Feminino,
    }

    //ADICIONADO - Enum para os tipos de Usuário
    public enum TipoUsuario {
        USUARIO_COMUM("Usuário Comum"),
        ADMINISTRADOR("Administrador");

        private final String descricao;

        TipoUsuario(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
        //Aqui fica só a descrição do enum
        @Override
        public String toString() {
            return descricao;
        }
    }

    // Construtor padrão.
    public Usuario() {
        this.id = UUID.randomUUID();
        this.metas = FXCollections.observableArrayList();
        this.treinos = FXCollections.observableArrayList();
        this.dietas = FXCollections.observableArrayList();
        this.exercicios = FXCollections.observableArrayList();
    }

    public Usuario(String nome, String email, LocalDate dataNascimento, Sexo sexo, float peso, float altura, TipoUsuario tipo) {
        this.id = UUID.randomUUID();
        this.nome.set(nome);
        this.email.set(email);
        this.senha.set(String.valueOf(senha));
        this.dataNascimento.set(dataNascimento);
        this.sexo.set(sexo);
        this.peso.set(peso);
        this.altura.set(altura);
        this.metas = FXCollections.observableArrayList();
        this.treinos = FXCollections.observableArrayList();
        this.dietas =  FXCollections.observableArrayList();
        this.exercicios = FXCollections.observableArrayList();
        this.pontuacao.set(0);
        this.dietaAtiva.set(null);
        this.tipo.set(tipo.getDescricao());
        calcularEAtualizarIMC();
        adicionarPesoAoHistorico(peso);
    }


    // Getters, Setters e Properties gerais

    public String getNome() {
        return nome.get();
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getSenha() {
        return senha.get();
    }

    public StringProperty senhaProperty() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha.set(senha);
    }

    public LocalDate getDataNascimento() {
        return dataNascimento.get();
    }

    public ObjectProperty<LocalDate> dataNascimentoProperty() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento.set(dataNascimento);
    }

    public Sexo getSexo() {
        return sexo.get();
    }

    public ObjectProperty<Sexo> sexoProperty() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo.set(sexo);
    }

    public float getPeso() {
        return peso.get();
    }

    public FloatProperty pesoProperty() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso.set(peso);
        calcularEAtualizarIMC();  // Importante: recalcula o IMC sempre que o peso muda.
        adicionarPesoAoHistorico(peso); // Adiciona o novo peso ao histórico.
    }

    public float getAltura() {
        return altura.get();
    }

    public FloatProperty alturaProperty() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura.set(altura);
        calcularEAtualizarIMC(); // Importante: recalcula o IMC sempre que a altura muda
    }

    public float getImc() {
        return imc.get();
    }

    public FloatProperty imcProperty() {
        return imc;
    }


    private void calcularEAtualizarIMC() {
        if (this.getAltura() > 0 && this.getPeso() > 0) {
            float alturaEmMetros = this.getAltura() / 100.0f;
            float imc = this.getPeso() / (alturaEmMetros * alturaEmMetros);
            this.imc.set(imc);
        } else {
            this.imc.set(0);
        }
    }


    // Getters, Setters e Properties para Dieta Ativa
    public Dieta getDietaAtiva() {
        return dietaAtiva.get();
    }

    public ObjectProperty<Dieta> dietaAtivaProperty() {
        return dietaAtiva;
    }

    public void setDietaAtiva(Dieta dietaAtiva) {
        this.dietaAtiva.set(dietaAtiva);
    }

    public String getTipo() { //ADICIONADO
        return tipo.get();
    }

    public StringProperty tipoProperty() { //ADICIONADO
        return tipo;
    }

    public void setTipo(String tipo) { //ADICIONADO
        this.tipo.set(tipo);
    }


    // Getters, Setters e Properties para Água
    public int getAguaConsumida() {
        return aguaConsumida.get();
    }

    public IntegerProperty aguaConsumidaProperty() {
        return aguaConsumida;
    }

    public void setAguaConsumida(int aguaConsumida) {
        this.aguaConsumida.set(aguaConsumida);
    }

    // Adiciona água consumida.
    public void beberAgua(int ml) {
        this.aguaConsumida.set(this.aguaConsumida.get() + ml);
    }

    // Getters, Setters e Properties para Pontuação

    public int getPontuacao() {
        return pontuacao.get();
    }

    public IntegerProperty pontuacaoProperty() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao.set(pontuacao);
    }

    // Adiciona pontuação.
    public void adicionarPontuacao(int pontos) {
        this.pontuacao.set(this.pontuacao.get() + pontos);
    }

    //Getter da lista de treinos.
    public ObservableList<Treino> getTreinos() {
        return this.treinos;
    }
    // Métodos para o histórico de peso
    public void adicionarPesoAoHistorico(float peso) {
        historicoPeso.get().add(new PesoRegistro(peso, LocalDate.now()));
    }

    public ObservableList<PesoRegistro> getHistoricoPeso() {
        return historicoPeso.get();
    }

    public ListProperty<PesoRegistro> historicoPesoProperty() {
        return historicoPeso;
    }

    public void setHistoricoPeso(ObservableList<PesoRegistro> historicoPeso) {
        this.historicoPeso.set(historicoPeso);
    }

    public ObservableList<Exercicio> getExercicios() {
        return this.exercicios;
    }

    //Getters e Setters para as novas medidas (adicionado)
    public double getCintura(){
        return cintura.get();
    }

    public DoubleProperty cinturaProperty(){
        return cintura;
    }

    public void setCintura(double cintura){
        this.cintura.set(cintura);
    }

    public double getBiceps(){
        return biceps.get();
    }

    public DoubleProperty bicepsProperty(){
        return biceps;
    }

    public void setBiceps(double biceps){
        this.biceps.set(biceps);
    }

    public double getCoxa(){
        return coxa.get();
    }

    public DoubleProperty coxaProperty(){
        return coxa;
    }

    public void setCoxa(double coxa){
        this.coxa.set(coxa);
    }

    public double getQuadril(){
        return quadril.get();
    }

    public DoubleProperty quadrilProperty(){
        return quadril;
    }

    public void setQuadril(double quadril){
        this.quadril.set(quadril);
    }

    // Método toString na classe Usuario (fora do enum)
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome=" + nome.get() +
                ", email=" + email.get() +
                ", tipo=" + tipo.get() +
                '}';
    }
}