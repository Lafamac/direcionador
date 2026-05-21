package com.example.inovaceifa.Connectivity;

import java.io.IOException;
import java.net.Socket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Connectivity {
    public static String geturl (String url_esp32){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url_esp32)
                .build();

        return correcaoConexao(client, request);
    }

    private static String correcaoConexao(OkHttpClient c, Request r) {
        try  {
            Response response = c.newCall(r).execute();
            return response.body().string();

        } catch (IOException error) {
            return correcaoConexao(c, r);
        }
    }
}