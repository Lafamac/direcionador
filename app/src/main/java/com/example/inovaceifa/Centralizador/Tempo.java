package com.example.inovaceifa.Centralizador;

import java.time.ZonedDateTime;


public class Tempo {
    /*** { - Identificador único do tempo no banco de dados.*/
    private int tempoID;
    /*** ID do operador relacionado ao tempo (chave estrangeira).*/
    private int operadorID;
    /**- Hora de início do trabalho.*/
    private String tempoInicio;
    /** - Hora de término do trabalho.*/
    private String tempoFinal;
    private String tempoTotal;
    /** - Data do registro do trabalho.*/
    private String tempoData;

    //----------------------------------------------------------------------------------------------

    public int getTempoID() {
        return tempoID;
    }

    /**Método Set para o ID do tempo.@param tempoID Identificador único do tempo.*/
    public void setTempoID(int tempoID) {
        this.tempoID = tempoID;
    }

    /** Método Get para o ID do operador relacionado. @return ID do operador. */
    public int getOperadorID() {
        return operadorID;
    }

    public void setOperadorID(int operadorID) {
        this.operadorID = operadorID;
    }

    public String getTempoInicio() {
        return tempoInicio;
    }

    public void setTempoInicio(String tempoInicio) {
        this.tempoInicio = tempoInicio;
    }

    public String getTempoFinal() {
        return tempoFinal;
    }

    public void setTempoFinal(String tempoFinal) {
        this.tempoFinal = tempoFinal;
    }

    public String getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(String tempoFinal) {
        this.tempoTotal = tempoTotal;
    }

    public String getTempoData() {
        return tempoData;
    }

    public void setTempoData(String tempoData) {
        this.tempoData = tempoData;
    }
}
