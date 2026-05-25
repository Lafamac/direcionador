package com.example.inovaceifa.Centralizador;

import android.content.Context;
import android.util.Log;
import com.example.inovaceifa.Utilities.UDPProtocol;
import java.util.Random;

/**
 * Gerenciador de Sensores (PROBLEMA 6 - agents.md)
 * Responsável por obter dados (UDP ou Simulação) e manter o estado atual.
 */
public class SensorManager {
    private static final String TAG = "SensorManager";
    private UDPProtocol udp;
    private boolean modoSimulado = false;
    private String ultimoValor = "0";
    private Random random = new Random();

    public SensorManager() {
        this.udp = new UDPProtocol();
    }

    public void setModoSimulado(boolean simulado) {
        this.modoSimulado = simulado;
    }

    /**
     * Obtém o próximo valor de desalinhamento.
     */
    public String getDesalinhamento(String comando, Context context) {
        if (modoSimulado) {
            // Simulação: gera valores entre -6 e 6 com tendência a manter o valor anterior (suavização)
            int alvo = random.nextInt(13) - 6;
            int atual = Integer.parseInt(ultimoValor);
            
            if (alvo > atual) atual++;
            else if (alvo < atual) atual--;
            
            ultimoValor = String.valueOf(atual);
            return ultimoValor;
        } else {
            // Real: Busca do ESP32 via UDP
            String recebido = udp.enviarEReceber(comando, 1234, context);
            if (recebido != null) {
                ultimoValor = recebido;
                return recebido;
            }
            return null;
        }
    }

    public String getUltimoValor() {
        return ultimoValor;
    }
}
