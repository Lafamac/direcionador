package com.example.inovaceifa.Utilities;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.inovaceifa.Centralizador.CentralizadorLEDs;
import com.example.inovaceifa.Connectivity.Connectivity;

import java.io.File;
import java.io.FileOutputStream;

/*
    Classe utilizada para realizar a comunicação com o ESP (seja o ESP32, ESP8266, D1 Mini, etc) no
    envio/recebimento de dados. Primordialmente é utilizada no sistema de centralização de colheita
    (classe "Direcionador"), mas podendo ser extensível à outras classes (como a "Gerenciador").
 */
public class request_data extends AsyncTask<String, Void, String> {

    /*
        Variáveis utilizadas nessa classe:

        ~ Centralizador da Colhedora
            - produtoUsado      -> String com o nome do produto a qual a classe está sendo usada
            - diretorio         -> Diretorio destino para o arquivo que conterá os dados obtidos
            - nomeArquivo       -> String contendo o nome do arquivo destino
            - l                 -> Objeto da classe "Leds" para realizar o controle dos LEDs

        ~ Gerenciador de Colheita
            - dadoRecebido      -> String com o dado recebido através do ESP

        ~ Monitor Animal
            - IDAnimal
            - pesoAnimal
     */

    String produtoUsado;
    String diretorio;
    String nomeArquivo;
    CentralizadorLEDs l;

    /*----------------------------------------------------------------------------------------------
        request_data(ImageView[], String, String, String):
            Construtor da classe (para a criação do objeto a ser usado nas classes principais). É
            composto por quatro parâmetros:
                -> i    = Vetor com os imageView a serem utilizados (voltado para a utilização em
                          "Leds")
                -> pU   = String cotendo o produto usado para ser inserido no atributo da classe
                -> d    = String contendo o diretorio usado para ser inserido no atributo da classe
                -> nA   = String contendo o nome do arquivo para ser inserido no atributo da classe
     ---------------------------------------------------------------------------------------------*/
    public request_data(ImageView[] i, String pU, String d, String nA) {
        produtoUsado = pU;
        diretorio = d;
        nomeArquivo = nA;
        l = new CentralizadorLEDs(i);
    }

    /*----------------------------------------------------------------------------------------------
        request_data(String, String, String)
            Outro construtor para a classe (servindo para uma utilização mais geral). É composto por
            três parâmetros:
                -> pU   = String cotendo o produto usado para ser inserido no atributo da classe
                -> d    = String contendo o diretorio usado para ser inserido no atributo da classe
                -> nA   = String contendo o nome do arquivo para ser inserido no atributo da classe
     ---------------------------------------------------------------------------------------------*/
    public request_data(String pU, String d, String nA) {
        produtoUsado = pU;
        diretorio = d;
        nomeArquivo = nA;
    }

    /*----------------------------------------------------------------------------------------------
        request_data(String):
            Outro construtor para a classe (servindo para uma utilizacao onde não se salva dados em
            um arquivo. É composto por um parâmetro:
                -> pU = String contendo o produto usado para ser inserido no atributo da classe
     ---------------------------------------------------------------------------------------------*/
    public request_data(String pU) {
        produtoUsado = pU;
    }

    @Override
    protected String doInBackground(String... url) {
        return Connectivity.geturl(url[0]);
    }

    /*----------------------------------------------------------------------------------------------
        onPostExecute (String):
            Execução de fato: Se o dado recebido através do ESP não for nulo, verifica qual o
            produto usado no momento.
            -> Caso seja o direcionador de colheita (centralizador de colheita), chama o método que
               controla o acendimento (ou não) dos LEDs. Além disso, grava o valor utilizado no
               gráfico.
            -> Caso seja o Gerenciador de Colheita, atualiza o valor da String na classe
               "Gerenciador.java" para ser utilizado conforme necessário.
            -> Caso seja o Monitor Animal, a String recebida é separada, para comportar tanto a ID
               quanto o peso do animal, e "enviada" para a classe "MonitorAnimal.java". Além disso,
               grava-se um arquivo com os parâmetros do animal.
     ---------------------------------------------------------------------------------------------*/
    @Override
    protected void onPostExecute(String result_data) {
        if(result_data != null) {
            switch (produtoUsado) {
                case "Direcionador":
                    /*try {
                        Direcionador.LEDs_acesos = Integer.parseInt(result_data);
                    } catch (NumberFormatException ex) {
                        Direcionador.LEDs_acesos = 0;
                    }*/
                    l.leds(result_data);
                    gravarArquivo(l.getValorArquivo());
                    break;
            }
        }
    }

    /*----------------------------------------------------------------------------------------------
        gravarArquivo(String) -> Realiza a gravação dos dados em um arquivo. Essa função utiliza o
                                 diretorio e nomeArquivo (parâmetros) para realizar suas funções.
     ---------------------------------------------------------------------------------------------*/
    public void gravarArquivo(String texto) {
        //Criação de um "arquivo" para o diretorio a ser utilizado, definido no escopo
        File dir = new File(diretorio);

        //Verifica se o diretório citado já foi criado. Caso não, cria o mesmo.
        if (!dir.exists()) {
            dir.mkdir();
        }

        //Cria uma variável para se referir ao arquivo a ser utilizado para armazenar os dados
        File arquivo = new File (dir, nomeArquivo);
        try {
            FileOutputStream escrita = new FileOutputStream(arquivo, true);

            //Escreve no arquivo os dados em questão
            escrita.write(texto.getBytes());

            //Fecha o arquivo que está sendo utilizado.
            escrita.close();
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
    }
}
