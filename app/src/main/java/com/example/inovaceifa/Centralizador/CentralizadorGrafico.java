package com.example.inovaceifa.Centralizador;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import com.example.inovaceifa.R;
import com.example.inovaceifa.Utilities.Arquivo;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Classe responsável por gerenciar o gráfico contendo informações relativas ao sistema de
 * centralizamento da colhedora.
 *
 * NOT USED.
 *
 * @author Gustavo Henrique Tostes
 * @version 1.1 - mar. 2023
 */
public class CentralizadorGrafico extends AppCompatActivity {

    String nomeArquivo = "teste2.txt";  // Nome do arquivo a ser gerado
    GraphView grafico;                              // Objeto do gráfico (tela)
    LineGraphSeries<DataPoint> series;              // Conjunto de dados (grafico)
    Viewport viewport;                              // View relativa ao gráfico
    String diretorio = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS)+"/"+"INOVA CEIFA"+"/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centralizador_grafico);

        grafico = findViewById(R.id.centGraf_graph);

        Button apagarGrafico = findViewById(R.id.centGraf_apagar);
        apagarGrafico.setOnClickListener(v -> {
            Arquivo.deleteFile(this, "teste2.txt");

            Log.i("MSG", "arquivo excluido");
        });

        Button voltar = findViewById(R.id.centGraf_voltar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 0)
        });

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        viewport = grafico.getViewport();
        viewport.setScrollable(true);
        viewport.setXAxisBoundsManual(true);

        //viewport.setMinY(-20);
        //viewport.setMaxY(20);

        viewport.setYAxisBoundsManual(true);

        //graph.setTitle("Deslocamento Diferencial");

        //Seta as linhas dos pontos para a mesma cor dos botões (um verde água)
        series.setColor(Color.argb(255, 0, 200, 135));

        //Adiciona os pontos ao gráfico, tanto os "atuais" quanto os que ainda forem colocados.
        grafico.addSeries(series);

        String[] texto = (Arquivo.readFile(this, nomeArquivo)).split("\n");

        int[] valores;
        try {
            valores = Arrays.stream(texto).mapToInt(Integer::parseInt).toArray();
            System.out.println(Arrays.toString(valores));
        } catch (NumberFormatException e) {
            valores = new int[]{0};
            System.out.println(Arrays.toString(valores));
        }


        //lerArquivo();

        //viewport.setBackgroundColor(Color.argb(255, 222, 222, 222));

        //iniciarLoop();

        graficoSemAnimacao(valores);
    }

    /**
     * Leitura do arquivo que contém os dados necessários para visualização do gráfico.
     */
    /*public void lerArquivo() {
        try {
            File dir = new File(diretorio);
            File arquivo = new File (dir, nomeArquivo);
            if (arquivo.exists()) {
                FileInputStream leitura = new FileInputStream(arquivo);
                char caractereAtual = ((char) leitura.read());
                String medida = "";
                contador = 0;

                while (leitura.available() > 0) {
                    if (caractereAtual != ('\n')) {
                        medida += caractereAtual;
                    } else {
                        valores[contador] = Integer.parseInt(medida);
                        contador++;
                        medida = "";
                    }
                    caractereAtual = ((char) leitura.read());
                }
                leitura.close();
            }
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
    }*/


    /**
     * Monta o gráfico, permitindo que o usuário possa "andar" por ele, vendo intervalos desejados.
     */
    private void graficoSemAnimacao(int[] valores) {
        for (int i = 1; i <= valores.length; i++) {
            series.appendData(new DataPoint(i, valores[i-1]), true, i+1);
            viewport.setMinY(-20);
            viewport.setMaxY(20);
        }
        viewport.setMinX(0);
        viewport.setMaxX(100);
        viewport.setScrollable(true);
        viewport.setScalableY(false);
    }
}