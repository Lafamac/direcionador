package com.example.inovaceifa.Centralizador;

/**
 * Representa um evento de produção salvo no banco de dados.
 */
public class EventoProducao {
    private int id;
    private String tipo;
    private String hora;
    private String data;
    private String valor;
    private String operadorID;

    public EventoProducao() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public String getOperadorID() { return operadorID; }
    public void setOperadorID(String operadorID) { this.operadorID = operadorID; }
}
