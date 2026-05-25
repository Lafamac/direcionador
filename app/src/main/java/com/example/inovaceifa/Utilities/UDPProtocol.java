package com.example.inovaceifa.Utilities;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * Classe que controla o protocolo de envio UDP.
 * Refatorada para NÃO estender Activity, evitando crashes de Handler/Looper.
 */
public class UDPProtocol {

    private String mensagem_recebida;
    private final static String TAG = "centralizadorDebug";

    public UDPProtocol() {
        // Construtor vazio e limpo
    }

    public void enviarMensagem(String mensagem, int UDP_SERVER_PORT, Context context) {
        String gateway = Gateway.getGateway(context);
        Log.d(TAG, "enviar Mensagem " + mensagem + " to: " + gateway);
        new Thread(() -> sendUDP(mensagem, UDP_SERVER_PORT, gateway)).start();
    }

    public void enviarMensagem(String mensagem, int UDP_SERVER_PORT, Context context, int sufixo) {
        String gateway = Gateway.getGateway(context);
        if (gateway == null) return;
        
        String aux = "";
        int lastDot = gateway.lastIndexOf('.');
        if (lastDot != -1) {
            aux = gateway.substring(0, lastDot);
            String newGateway = aux + "." + sufixo;
            Log.d(TAG, "enviar Mensagem " + mensagem + " to: " + newGateway);
            new Thread(() -> sendUDP(mensagem, UDP_SERVER_PORT, newGateway)).start();
        }
    }

    private void sendUDP(String mensagem, int UDP_SERVER_PORT, String gateway) {
        DatagramSocket sds = null;
        try {
            sds = new DatagramSocket();
            sds.setSoTimeout(1000);
            InetAddress serverAddr = InetAddress.getByName(gateway);
            byte[] data = mensagem.getBytes(StandardCharsets.UTF_8);
            DatagramPacket sdp = new DatagramPacket(data, data.length, serverAddr, UDP_SERVER_PORT);
            sds.send(sdp);

            // Tenta receber uma confirmação opcional
            byte[] buf = new byte[1024];
            DatagramPacket rdp = new DatagramPacket(buf, buf.length);
            try {
                sds.receive(rdp);
                mensagem_recebida = new String(rdp.getData(), rdp.getOffset(), rdp.getLength(), StandardCharsets.UTF_8);
                Log.d(TAG, "Confirmação UDP recebida: " + mensagem_recebida);
            } catch (IOException e) {
                // Timeout no receive do sendUDP é comum se o hardware apenas processa e não responde
                mensagem_recebida = null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Erro UDP (sendUDP): " + e.getMessage());
            mensagem_recebida = null;
        } finally {
            if (sds != null) sds.close();
        }
    }

    public String enviarEReceber(String mensagem, int UDP_SERVER_PORT, Context context) {
        String gateway = Gateway.getGateway(context);
        if (gateway == null) return null;
        return sendAndReceiveUDP(mensagem, UDP_SERVER_PORT, gateway);
    }

    private String sendAndReceiveUDP(String mensagem, int UDP_SERVER_PORT, String gateway) {
        DatagramSocket sds = null;
        try {
            sds = new DatagramSocket();
            sds.setSoTimeout(1000);
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
            if (sds != null) sds.close();
        }
    }

    public String getMensagem_recebida() {
        return mensagem_recebida;
    }
}
