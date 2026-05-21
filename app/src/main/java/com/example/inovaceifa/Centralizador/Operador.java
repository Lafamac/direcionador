package com.example.inovaceifa.Centralizador;

import java.time.ZonedDateTime;

/**
 * Classe referente às Glebas a serem cadastradas no banco de dados. Contém todos os os seus
 * parâmetros, sendo eles:
 * <p>
 * - Número, Local, Descrição;
 * <p>
 * - Cultivar, Espaçamento entre plantas, Espaçamento entre Ruas, Altura de plantas, Número de ramos
 *   por planta, Porcentagem de grãos cereja, Porcentagem de grãos verdes, Renda esperada, Modelo da
 *   colhedora, Carga pendente;
 * <p>
 * - Plantas por hectare, Produtividade, Litros de café por metro;
 * <p>
 * - Força Média de Desprendimento dos grãos cereja, Força Média de Desprendimento dos grãos verdes;
 * <p>
 * - Vibração da máquina na hora da colheita, Velocidade da máquina na hora da colheita.
 * @author Gustavo Henrique Tostes
 * @version 1.1 - 09/01/2023
 */
public class Operador {
    /**
     * {@link String} - Número identificador da Gleba, seguindo o formato: 01, 02, 03...
     */
    private String numero;

    /**
     * {@link String} - Local da gleba, a ser inserido pelo usuário durante o cadastro ({@link CentralizadorCadastro} ou por edição {@link CentralizadorEdicaoDados}.     */
    private String local;

    /**
     * {@link String} - Descrição da gleba, a ser inserida pelo usuário através da Activity: {@link CentralizadorEdicaoDados}
     */
    private String descricao;

    /**
     * {@link String} - Path da imagem associado à gleba, considerando seu Bitmap.
     */
    private String imagemPath;

    private int numeroLedsVerdeClaros;

    private int numeroLedsVerdeEscurosEsquerda;

    private int numeroLedsAmarelosEsquerda;

    private int numeroLedsVermelhosEsquerda;

    private int numeroLedsVerdeEscurosDireita;

    private int numeroLedsAmarelosDireita;

    private int numeroLedsVermelhosDireita;

    private int tempoDiarioTrabalhado;

    private String diaAtual;

    //----------------------------------------------------------------------------------------------

    /**
     * Método Get para o número de uma gleba.
     *
     * @return Número da gleba.
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Seta o número de uma gleba.
     * @param numero String contendo o número que se deseja inserir no objeto.
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * Método Get para o local de uma gleba.
     *
     * @return Local da gleba.
     */
    public String getLocal() {
        return local;
    }

    /**
     * Seta o local de uma gleba.
     * @param local String contendo o local que se deseja inserir no objeto.
     */
    public void setLocal(String local) {
        this.local = local;
    }

    /**
     * Método Get para a descrição de uma gleba.
     *
     * @return Descrição da gleba.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Seta a descrição de uma gleba.
     * @param descricao String contendo a descrição que se deseja inserir no objeto.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Método Get para o path (caminho) da imagem de uma gleba.
     * @return Caminho para a imagem da gleba.
     */
    public String getImagemPath() {
        return imagemPath;
    }

    /**
     * Seta o path da imagem de uma gleba.
     * @param imagemPath - String contendo o path (transformado a partir de Uri) da imagem.
     */
    public void setImagemPath(String imagemPath) {
        this.imagemPath = imagemPath;
    }

    public void setNumeroLedsAmarelosDireita(int numeroLedsAmarelosDireita) {
        this.numeroLedsAmarelosDireita = numeroLedsAmarelosDireita;
    }

    public int getNumeroLedsAmarelosDireita() {
        return numeroLedsAmarelosDireita;
    }

    public void setNumeroLedsAmarelosEsquerda(int numeroLedsAmarelosEsquerda) {
        this.numeroLedsAmarelosEsquerda = numeroLedsAmarelosEsquerda;
    }

    public int getNumeroLedsAmarelosEsquerda() {
        return numeroLedsAmarelosEsquerda;
    }

    public void setNumeroLedsVerdeClaros(int numeroLedsVerdeClaros) {
        this.numeroLedsVerdeClaros = numeroLedsVerdeClaros;
    }

    public int getNumeroLedsVerdeClaros() {
        return numeroLedsVerdeClaros;
    }

    public void setNumeroLedsVerdeEscurosDireita(int numeroLedsVerdeEscurosDireita) {
        this.numeroLedsVerdeEscurosDireita = numeroLedsVerdeEscurosDireita;
    }

    public int getNumeroLedsVerdeEscurosDireita() {
        return numeroLedsVerdeEscurosDireita;
    }

    public void setNumeroLedsVerdeEscurosEsquerda(int numeroLedsVerdeEscurosEsquerda) {
        this.numeroLedsVerdeEscurosEsquerda = numeroLedsVerdeEscurosEsquerda;
    }

    public int getNumeroLedsVerdeEscurosEsquerda() {
        return numeroLedsVerdeEscurosEsquerda;
    }

    public void setNumeroLedsVermelhosDireita(int numeroLedsVermelhosDireita) {
        this.numeroLedsVermelhosDireita = numeroLedsVermelhosDireita;
    }

    public int getNumeroLedsVermelhosDireita() {
        return numeroLedsVermelhosDireita;
    }

    public void setNumeroLedsVermelhosEsquerda(int numeroLedsVermelhosEsquerda) {
        this.numeroLedsVermelhosEsquerda = numeroLedsVermelhosEsquerda;
    }

    public int getNumeroLedsVermelhosEsquerda() {
        return numeroLedsVermelhosEsquerda;
    }

    public void setTempoDiarioTrabalhado(int tempoDiarioTrabalhado) {
        this.tempoDiarioTrabalhado = tempoDiarioTrabalhado;
    }

    public int getTempoDiarioTrabalhado() {
        return tempoDiarioTrabalhado;
    }

    public void setDiaAtual(String diaAtual) {
    }

    public String getDiaAtual() {
        return diaAtual;
    }
}