package com.example.inovaceifa.Centralizador;

import android.content.Context;
import android.util.Log;
import com.example.inovaceifa.Utilities.UDPProtocol;
import java.util.Random;
import java.util.Locale;

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
    private int sufixoIP = -1;

    public SensorManager() {
        this.udp = new UDPProtocol();
    }

    public void setSufixoIP(int sufixo) {
        this.sufixoIP = sufixo;
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
            String recebido;
            if (sufixoIP != -1) {
                recebido = udp.enviarEReceber(comando, 1234, context, sufixoIP);
            } else {
                recebido = udp.enviarEReceber(comando, 1234, context);
            }

            if (recebido != null) {
                ultimoValor = recebido;
                return recebido;
            }
            return null;
        }
    }

    /**
     * Envia parâmetros de calibração para o ESP32 usando o novo protocolo robusto.
     */
    public boolean enviarParametros(Context context, int diam, int min, int max, int angulo, int valido) {
        if (modoSimulado) return true;

        String comando = String.format(Locale.US, "params:diametro=%d;minimo=%d;maximo=%d;angulo=%d;valido=%d",
                diam, min, max, angulo, valido);
        
        String resposta = (sufixoIP != -1) 
                ? udp.enviarEReceber(comando, 1234, context, sufixoIP)
                : udp.enviarEReceber(comando, 1234, context);

        boolean sucesso = resposta != null && resposta.contains("OK params");
        if (sucesso) {
            Log.d(TAG, "Parâmetros sincronizados com sucesso");
        } else {
            Log.e(TAG, "Falha na sincronização de parâmetros: " + resposta);
        }
        return sucesso;
    }
}
