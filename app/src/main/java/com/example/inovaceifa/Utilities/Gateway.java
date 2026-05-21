package com.example.inovaceifa.Utilities;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Classe responsável pelas "atividades" relativas ao uso de Gateway.
 *
 * @author Gustavo Henrique Tostes
 * @version 1.0 - feb. 2023
 */
public class Gateway {

    /**
     * Classe para retornar o IP ao qual o dispositivo está conectado no momento da aplicação.
     * @param context Contexto da aplicação atual.
     * @return IP ao qual o dispositivo está conectado.
     */
    public static String getGateway(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifi.getDhcpInfo();

        return MMath.intParaIp(dhcpInfo.gateway);
    }

    public static int getDis(Context context) {
        InetAddress localhost;
        try {
            localhost = InetAddress.getByName(getGateway(context));
        } catch (UnknownHostException e) {
            System.out.println("eh");
            return 0;
        }

        byte[] ip = localhost.getAddress();

        for (int i = 1; i < 254; i++) {
            ip[3] = (byte) i;
            InetAddress address;
            try {
                address = InetAddress.getByAddress(ip);
            } catch (UnknownHostException e) {
                System.out.println("eh2");
                return 0;
            }

            boolean disponivel = false;
            final boolean[] achou = {false};
            String ipp;
            try {
                disponivel = address.isReachable(200);
            } catch (IOException e) {
                System.out.println("eh3");
            }

            if (disponivel) {
                System.out.println(address + " maquina esta ligada e pode ser pingada");
                //Acrescentar um envio de informacao. Caso receber um ok, para instantaneamente!
                //Apos parar, guarda o endereco de IP consigo.
            }
        }
        return 1;
    }
}
