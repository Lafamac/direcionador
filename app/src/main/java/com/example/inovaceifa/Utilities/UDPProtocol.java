package com.example.inovaceifa.Utilities;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * Classe que controla o protocolo de envio UDP, o qual possui uma alta velocidade na entrega dos
 * dados devido à sua "despreocupação" se os dados chegaram ao destino final e/ou se chegaram
 * corretamente. Seu uso é voltado para aplicações que não se importam com a eventual perda de dados.
 *
 * @author Gustavo Henrique Tostes
 * @version 2.0 - feb. 2023
 */
public class UDPProtocol extends AppCompatActivity {

    /**
     * {@link String} - Armazena a mensagem recebida ao utilizar o protocolo UDP.
     */
    private String mensagem_recebida;

    /**
     * {@link String} - TAG para debug do código.
     */
    private final static String TAG = "centralizadorDebug";

    //----------------------------------------------------------------------------------------------
    /**
     * Envio de mensagens, utilizando {@link Thread}.
     * @param mensagem {@link String} contendo a mensagem a ser enviada pelo protocolo.
     * @param UDP_SERVER_PORT {@link Integer} contendo a porta de conexão com o UDP.
     * @param context {@link Context} da aplicação (para encontrar o Gateway).
     */
    public void enviarMensagem(String mensagem, int UDP_SERVER_PORT, Context context) {
        String gateway = Gateway.getGateway(context);
        Log.d(TAG, "enviar Mensagem " + mensagem + " to: " + gateway);

        new Thread(() -> sendUDP(mensagem, UDP_SERVER_PORT, gateway)).start();
    }

    /**
     * Envio de mensagens, utilizando {@link Thread}.
     * @param mensagem {@link String} contendo a mensagem a ser enviada pelo protocolo.
     * @param UDP_SERVER_PORT {@link Integer} contendo a porta de conexão com o UDP.
     * @param context {@link Context} da aplicação (para encontrar o Gateway).
     * @param sufixo {@link Integer} contendo o sufixo a ser utilizado no endereço de IP.
     */
    public void enviarMensagem(String mensagem, int UDP_SERVER_PORT, Context context, int sufixo) {
        String gateway = Gateway.getGateway(context);
        String aux = "";
        for (int i = 0; i < gateway.lastIndexOf('.'); i++) {
            aux += gateway.charAt(i);
        }
        aux += "." + sufixo;

        String newGateway = aux;

        Log.d(TAG, "enviar Mensagem " + mensagem + " to: " + newGateway);

        new Thread(() -> sendUDP(mensagem, UDP_SERVER_PORT, newGateway)).start();
    }

    /**
     * Envio de um pacote via protocolo UDP.
     * @param mensagem {@link String} contendo a mensagem a ser enviada pelo protocolo.
     * @param UDP_SERVER_PORT {@link Integer} contendo a porta de conexão com o UDP.
     * @param gateway {@link String} contendo o endereço de IP do ESP.
     */
    private void sendUDP(String mensagem, int UDP_SERVER_PORT, String gateway) {
        DatagramSocket sds = null;
        try {
            sds = new DatagramSocket();
            sds.setSoTimeout(1000); // Adicionado timeout de 1 segundo
            InetAddress serverAddr = InetAddress.getByName(gateway);
            DatagramPacket sdp = new DatagramPacket(mensagem.getBytes(), mensagem.length(),
                    serverAddr, UDP_SERVER_PORT);
            sds.send(sdp);

            byte[] buf = new byte[1024]; // Aumentado para 1024 para segurança
            DatagramPacket rdp = new DatagramPacket(buf, buf.length);
            sds.receive(rdp);
            
            mensagem_recebida = new String(rdp.getData(), rdp.getOffset(), rdp.getLength(), StandardCharsets.UTF_8);
            Log.d(TAG, "recebido: " + mensagem_recebida);

        } catch (IOException e) {
            Log.e(TAG, "Erro UDP (sendUDP): " + e.getMessage());
            mensagem_recebida = null;
        } catch (Exception e) {
            Log.e(TAG, "Erro inesperado UDP: " + e.getMessage());
            mensagem_recebida = null;
        } finally {
            if (sds != null) {
                sds.close();
            }
        }
    }

    /**
     * Envio e recebimento síncrono de mensagens UDP.
     * @param mensagem Mensagem a ser enviada.
     * @param UDP_SERVER_PORT Porta do servidor.
     * @param context Contexto para obter o gateway.
     * @return Resposta recebida ou null se falhar/timeout.
     */
    public String enviarEReceber(String mensagem, int UDP_SERVER_PORT, Context context) {
        String gateway = Gateway.getGateway(context);
        return sendAndReceiveUDP(mensagem, UDP_SERVER_PORT, gateway);
    }

    private String sendAndReceiveUDP(String mensagem, int UDP_SERVER_PORT, String gateway) {
        DatagramSocket sds = null;
        try {
            sds = new DatagramSocket();
            sds.setSoTimeout(1000); // Timeout de 1 segundo
            InetAddress serverAddr = InetAddress.getByName(gateway);
            
            byte[] sendData = mensagem.getBytes(StandardCharsets.UTF_8);
            DatagramPacket sdp = new DatagramPacket(sendData, sendData.length, serverAddr, UDP_SERVER_PORT);
            sds.send(sdp);

            byte[] buf = new byte[1024];
            DatagramPacket rdp = new DatagramPacket(buf, buf.length);
            sds.receive(rdp);
            
            String resposta = new String(rdp.getData(), rdp.getOffset(), rdp.getLength(), StandardCharsets.UTF_8);
            Log.d(TAG, "UDP Recebido: " + resposta);
            return resposta;
        } catch (IOException e) {
            Log.e(TAG, "Erro UDP: " + e.getMessage());
            return null;
        } finally {
            if (sds != null) {
                sds.close();
            }
        }
    }

    /**
     * Método Get() para obter a mensagem recebida pelo ESP
     * @return {@link String} contendo a mensagem em questão.
     */
    public String getMensagem_recebida() {
        return mensagem_recebida;
    }
}
