package com.example.inovaceifa.Centralizador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.inovaceifa.R;
import com.example.inovaceifa.Utilities.Arquivo;
import com.example.inovaceifa.Utilities.HelperDatabaseSQL;
import com.example.inovaceifa.Utilities.MMath;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity para ações destinadas à coleta e demonstração de dados referentes ao "tempo" de descentralização
 * da máquina enquanto o sistema estava em uso.
 *
 * @author Gustavo Henrique Tostes
 * @version 1.1
 * @since 20/09/2023
 */
public class CentralizadorRelatorio extends AppCompatActivity {

    /**
     * {@link TextView} - Texto referente à porcentagem de descentralização para a direita.
     */
    private TextView desD;

    /**
     * {@link TextView} - Texto referente à porcentagem de descentralização para a esquerda.
     */
    private TextView desE;

    /**
     * {@link TextView} - Texto referente à porcentagem de centralização.
     */
    private TextView cent;

    /**
     * {@link Button} - Botão para finalizar a análise de relatórios.
     */
    private Button finalizar;

    /**
     * {@link PowerSpinnerView} - Contém as 7 últimas datas que contenham dados salvos, ou seja, quando o aplicativo foi utilizado.
     */
    private PowerSpinnerView datas;

    /**
     * {@link FloatingActionButton} - Botão para retornar à página anterior.
     */
    private FloatingActionButton voltar;

    /**
     * {@link String} - TAG para debug.
     */
    private final String TAG = "centralizadorDebug";

    /**
     * {@link String} - Última data registrada nos dados do banco de dados, referindo-se então ao último dia salvo de
     * coleta de dados.
     */
    private String lastDate;

    /**
     * {@link List<String>} - Lista contendo os nomes válidos para os arquivos.
     */
    private List<String> files;

    /**
     * {@link HelperDatabaseSQL} - Helper para ações do banco de dados SQLite
     */
    private HelperDatabaseSQL helper;

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centralizador_relatorio);

        helper = HelperDatabaseSQL.getInstance(this);

        iniciaComponentes();

        setarSpinner();

        configVoltar();

        configFinalizar();

        atualizaDados(verificaNulo());

        datas.setText(lastDate);

        datas.setSpinnerOutsideTouchListener((view, motionEvent) -> datas.dismiss());

        datas.setOnSpinnerItemSelectedListener((oldIndex, oldItem, newIndex, newObject) -> {
            atualizaDados(receberDados(files.get(newIndex)));
        });
    }

    /**
     * Inicializa os componentes através do método {@link android.app.Activity#findViewById(int)}.
     */
    private void iniciaComponentes() {
        desD = findViewById(R.id.centRel_porcentagemD);
        desE = findViewById(R.id.centRel_porcentagemE);
        cent = findViewById(R.id.centRel_porcentagemC);
        datas = findViewById(R.id.centRel_spinnerDatas);
        voltar = findViewById(R.id.centRel_voltar);
        finalizar = findViewById(R.id.centRel_finalizar);
    }

    /**
     * Seta os dados a serem utilizados dentro do Spinner {@link CentralizadorRelatorio#datas}, ou seja,
     * as datas específicas que contenham dados salvos.
     */
    private void setarSpinner() {
        IconSpinnerAdapter adapter;
        adapter = new IconSpinnerAdapter(datas);
        datas.setSpinnerAdapter(adapter);
        //datas.setItems(setarVetor());
    }

    /**
     * Configura os parâmetros referentes ao {@link CentralizadorRelatorio#voltar}.
     */
    private void configVoltar() {
        voltar.setOnClickListener(v -> finish());
    }

    /**
     * Configura os parâmetros referentes ao {@link CentralizadorRelatorio#finalizar}.
     */
    private void configFinalizar() {
        finalizar.setOnClickListener(v -> {
            finish(); // Apenas fecha a tela para voltar para a tela de Administração
        });
    }


    /**
     * Recupera os dados provenientes do arquivo, lembrando que os dados são separados por quebra de linha ("\n").
     * @param file_name {@link String} contendo o nome do arquivo
     * @return {@link String}[], sendo um vetor contendo os três dados de centralização, na seguinte sequência:
     * Descentralização para a Esquerda, Centralização, Descentralização para a Direita.
     */
    private String[] receberDados(String file_name) {
        String texto = Arquivo.readFile(this, file_name);

        return texto.split("\n");
    }

    private String[] verificaNulo() {
        if (files.isEmpty()) {
            String texto = "0\n0\n0";
            return texto.split("\n");
        } else {
            return receberDados(files.get(files.size()-1));
        }
    }

    /**
     * Atualiza os dados da tela, baseado no arquivo escolhido para análise, sendo que o resultado "visual" é
     * dado em percentual.
     * @param dados {@link String}[], sendo um array contendo os dados.
     */
    private void atualizaDados(String[] dados) {
        int desaE = Integer.parseInt(dados[0]);
        int centra = Integer.parseInt(dados[1]);
        int desaD = Integer.parseInt(dados[2]);

        double total = (desaD + desaE + centra) * 1.0;

        double porcD, porcE, porcC;
        if (total == 0) {
            porcD = 0;
            porcE = 0;
            porcC = 0;
        } else {
            porcD = (desaD/total)*100;
            porcE = (desaE/total)*100;
            porcC = (centra/total)*100;
        }

        if (this.getResources().getConfiguration().orientation == 2) {
            atualizaTextos("Desalinhado - Esquerda: " + "" + MMath.converter2Dec(porcE) + "%",
                    "Desalinhado - Direita: " + "" + MMath.converter2Dec(porcD) + "%",
                    "Centralizado: " + "" + MMath.converter2Dec(porcC) + "%");
        } else {
            atualizaTextos("Desalinhado - Esquerda: " + "" + MMath.converter2Dec(porcE) + "%",
                    "Desalinhado - Direita: " + "" + MMath.converter2Dec(porcD) + "%",
                    "Centralizado: " + "" + MMath.converter2Dec(porcC) + "%");
        }
    }

    /**
     * Atualiza os textos referentes ás TextViews presentes na Activity em questão.
     * @param texto1 {@link String} contendo o texto a ser inserido na {@link CentralizadorRelatorio#desD}.
     * @param texto2 {@link String} contendo o texto a ser inserido na {@link CentralizadorRelatorio#desE}.
     * @param texto3 {@link String} contendo o texto a ser inserido na {@link CentralizadorRelatorio#cent}.
     */
    private void atualizaTextos(String texto1, String texto2, String texto3) {
        desE.setText(texto1);

        desD.setText(texto2);

        cent.setText(texto3);
    }

}