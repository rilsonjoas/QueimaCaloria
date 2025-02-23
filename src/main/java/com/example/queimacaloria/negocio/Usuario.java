package com.example.queimacaloria.negocio;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
// Importe a classe correta
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@ToString
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

    @Setter
    // Use ObservableList desde o início!
    private ObservableList<Meta> metas = FXCollections.observableArrayList();
    @Setter
    private ObservableList<Treino> treinos = FXCollections.observableArrayList();
    @Setter
    private ObservableList<Dieta> dietas = FXCollections.observableArrayList();

    public enum Sexo {
        MASCULINO,
        FEMININO,
    }

    public Usuario() {
        this.id = UUID.randomUUID();
        // Não precisa mais inicializar aqui, já foi feito na declaração
    }

    public Usuario(String nome, String email, LocalDate dataNascimento, Sexo sexo, float peso, float altura) {
        this(nome, email, dataNascimento, sexo, peso, altura, FXCollections.observableArrayList(), FXCollections.observableArrayList(), FXCollections.observableArrayList()); // Use listas observáveis
    }

    public Usuario(String nome, String email, LocalDate dataNascimento, Sexo sexo, float peso, float altura, ObservableList<Meta> metas, ObservableList<Treino> treinos, ObservableList<Dieta> dietas) { //CORRETO
        this.id = UUID.randomUUID();
        this.nome.set(nome);
        this.email.set(email);
        this.dataNascimento.set(dataNascimento);
        this.sexo.set(sexo);
        this.peso.set(peso);
        this.altura.set(altura);
        // Corrigido: Use setAll para adicionar os elementos, não substitua a lista
        this.metas.setAll(metas);
        this.treinos.setAll(treinos);
        this.dietas.setAll(dietas);
    }
    //Restante do código
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
        calcularEAtualizarIMC(); // Chama o método para calcular o IMC
    }

    public float getAltura() {
        return altura.get();
    }

    public FloatProperty alturaProperty() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura.set(altura);
        calcularEAtualizarIMC(); // Chama o método para calcular o IMC
    }

    public float getImc() {
        return imc.get();
    }

    public FloatProperty imcProperty() {
        return imc;
    }

    public void setImc(float imc) {
        this.imc.set(imc);
    }

    private void calcularEAtualizarIMC(){
        if (this.getAltura() > 0 && this.getPeso() > 0) {
            float imc = this.getPeso() / (this.getAltura() * this.getAltura());
            this.imc.set(imc);
        } else {
            this.imc.set(0); // Mantém 0 se altura ou peso forem inválidos.
        }
    }

}