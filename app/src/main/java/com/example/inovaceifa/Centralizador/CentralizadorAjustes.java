package com.example.inovaceifa.Centralizador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inovaceifa.R;
import com.example.inovaceifa.Utilities.DialogWarning;
import com.example.inovaceifa.Utilities.Gateway;
import com.example.inovaceifa.Utilities.HelperDatabaseSQL;
import com.example.inovaceifa.Utilities.LayoutElements;
import com.example.inovaceifa.Utilities.MMath;
import com.example.inovaceifa.Utilities.Teclado;
import com.example.inovaceifa.Utilities.UDPProtocol;

/**
 * Classe responsável pro gerenciar a Activity relativa à setar os parâmetros iniciais do sistema,
 * sendo eles: Diâmetro médio do pé de café, Distância entre as barras da máquina, Distância máxima
 * de medição, Distância mínima de medição, Ângulo de inclinação da caixa de proteção.
 *
 * @author Gustavo Henrique Tostes
 * @version 2.0 - feb. 2022
 */
public class CentralizadorAjustes extends AppCompatActivity implements DialogWarning.DialogWarningListener {

    /**
     * {@link EditText} - Campo de digitação para o ângulo de inclinação das caixas contendo os sensores.
     */
    private EditText campo_angulo;

    /**
     * {@link EditText} - Campo de digitação para a distância entre as barras da máquina.
     */
    private EditText campo_distBarra;

    /**
     * {@link EditText} - Campo de digitação para a menor distância medida pelos sensores.
     */
    private EditText campo_distMin;

    /**
     * {@link EditText} - Campo de digitação para a maior distância medida pelos sensores.
     */
    private EditText campo_distMax;

    /**
     * {@link EditText} - Campo de digitação para o diâmetro médio dos pés de café do local a ser colhido.
     */
    private EditText campo_diam;

    /**
     * {@link EditText} - Campo de digitação para o tempo de atualização do sistema.
     */
    private EditText campo_temp;

    /**
     * {@link Integer} - Armazena o valor do ângulo digitado pelo usuário.
     */
    private int angulo;

    /**
     * {@link Integer} - Armazena o valor da distância entre as barras da máquina, digitado pelo usuário.
     */
    private int distBarra;

    /**
     * {@link Integer} - Armazena o valor da maior distância a ser medida, digitado pelo usuário.
     */
    private int distMax;

    /**
     * {@link Integer} - Armazena o valor da menor distância a ser medida, digitado pelo usuário.
     */
    private int distMin;

    /**
     * {@link Integer} - Aramzena o valor do diâmetro médio dos pés de café, em milímetros, digitado pelo usuário.
     */
    private int diam;

    /**
     * {@link Float} - Armazena o valor do tempo, em segundos, de atualização do sistema, digitado pelo usuário.
     */
    private float temp;

    /**
     * {@link Integer} - Ângulo padrão de inclinação dos sensores.
     */
    private final int anguloDefault = 25;

    /**
     * {@link Float} - Tempo default para atualização do sistema.
     */
    private final float tempoDefault = (float) 1.0;

    /**
     * {@link Integer} - Diâmetro default dos pés de café da gleba.
     */
    private final int diametroDefault = 100;

    /**
     * {@link Integer} - Sufixo utilizado para o endereço de IP, o qual corresponde ao microcontrolador utilziado.
     */
    private final int sufixo_IP = 200;

    /**
     * {@link Button} - Avança para a próxima página {@link CentralizadorSistema}.
     */
    private Button continuar;

    /**
     * {@link Button} - Retorna para a página anterior.
     */
    private Button voltar;

    /**
     * {@link String} - Armazena uma mensagem de erro caso os procedimentos não tenham sido seguidos corretamente.
     */
    private String texto = "";

    /**
     * {@link String} - Armazena o dado recebido através do protocolo de comunicação, obtido através do {@link CentralizadorAjustes#UDP}.
     */
    private String recebido;

    /**
     * {@link String} - TAG para debug do código.
     */
    private final static String TAG = "centralizadorDebug";

    /**
     * {@link UDPProtocol} - Objeto para utilização do protocolo de comunicação para envio/recebimento dos dados.
     */
    private UDPProtocol UDP;

    /**
     * {@link Boolean} - Armazena true caso o TPS esteja sendo utilizado e false caso não esteja.
     */
    private boolean tps_sensor = false;

    /**
     * {@link HelperDatabaseSQL} - Helper do banco de dados para utilização dos métodos inerentes à ele.
     */
    private HelperDatabaseSQL centAjusHelper;

    /**
     * {@link CentralizadorParametros} - Objeto contendo os valores padrão do Centralizador da Colhedora.
     */
    private CentralizadorParametros cent;

    /**
     * {@link ImageButton} - Confirma o envio do valor digitado pelo usuário em {@link CentralizadorAjustes#campo_angulo}.
     */
    private ImageButton ok_angulo;

    /**
     * {@link ImageButton} - Confirma o envio do valor digitado pelo usuário em {@link CentralizadorAjustes#campo_distBarra}.
     */
    private ImageButton ok_distBarra;

    /**
     * {@link ImageButton} - Confirma o envio do valor digitado pelo usuário em
     * {@link CentralizadorAjustes#campo_distMin}.
     */
    private ImageButton ok_distMin;

    /**
     * {@link ImageButton} - Confirma o envio do valor digitado pelo usuário em
     * {@link CentralizadorAjustes#campo_distMax}.
     */
    private ImageButton ok_distMax;

    /**
     * {@link ImageButton} - Confirma o envio do valor digitado pelo usuário em
     * {@link CentralizadorAjustes#campo_diam}.
     */
    private ImageButton ok_diam;

    /**
     * {@link ImageButton} - Confirma o envio do valor digitado pelo usuário em
     * {@link CentralizadorAjustes#campo_temp}.
     */
    private ImageButton ok_temp;

    /**
     * {@link SwitchCompat} - Utilizado para o usuário confirmar se o TPS (sensor) está sendo utilizado ou não.
     */
    private SwitchCompat tps;

    /**
     * {@link SwitchCompat} - Define se o operador pode mudar o tempo pela barra de rolagem.
     */
    private SwitchCompat sw_habilitaScroll;

    /**
     * {@link EditText} - Campo de digitação para o tempo fixo.
     */
    private EditText campo_tempoFixo;

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centralizador_ajustes);
        
        UDP = new UDPProtocol();

        centAjusHelper = HelperDatabaseSQL.getInstance(this);

        iniciarComponentes();

        carregarDados();

        configTps();

        configOkAngulo();

        configOkDistBarra();

        configOkDistMin();

        configOkDistMax();

        configOkDiam();

        configOkTemp();

        configContinuar();

        configVoltar();
    }

    /**
     * Inicializa os componentes através do método {@link android.app.Activity#findViewById(int)}.
     */
    private void iniciarComponentes() {
        continuar = findViewById(R.id.centSens_avancar);
        voltar = findViewById(R.id.centSens_voltar);

        campo_angulo = findViewById(R.id.centAjus_fieldAngulo);
        campo_distBarra = findViewById(R.id.centAjus_fieldDistBarra);
        campo_distMin = findViewById(R.id.centAjus_fieldDistMin);
        campo_distMax = findViewById(R.id.centAjus_fieldDistMax);
        campo_diam = findViewById(R.id.centAjus_fieldDiam);
        campo_temp = findViewById(R.id.centAjus_fieldTemp);

        ok_angulo = findViewById(R.id.centAjus_okAngulo);
        ok_distBarra = findViewById(R.id.centAjus_okDistBarra);
        ok_distMin = findViewById(R.id.centAjus_okDistMin);
        ok_distMax = findViewById(R.id.centAjus_okDistMax);
        ok_diam = findViewById(R.id.centAjus_okDiam);
        ok_temp = findViewById(R.id.centAjus_okTempo);

        tps = findViewById(R.id.centSens_tps);
        sw_habilitaScroll = findViewById(R.id.centAjus_swHabilitaScroll);
        campo_tempoFixo = findViewById(R.id.centAjus_fieldTempoFixo);
    }

    private void carregarDados() {
        cent = centAjusHelper.searchAjustes(1);
        if (cent != null) {
            campo_angulo.setText(String.valueOf(cent.getAngulo()));
            campo_distBarra.setText(String.valueOf(cent.getDistBarra()));
            campo_distMin.setText(String.valueOf(cent.getDistMin()));
            campo_distMax.setText(String.valueOf(cent.getDistMax()));
            campo_diam.setText(String.valueOf(cent.getDiametroMedio()));
            campo_temp.setText(String.valueOf(cent.getTempoAtt()));
            sw_habilitaScroll.setChecked(cent.getHabilitaScrollTempo() == 1);
            campo_tempoFixo.setText(String.valueOf(cent.getTempoFixo()));
            
            angulo = cent.getAngulo();
            distBarra = cent.getDistBarra();
            distMin = cent.getDistMin();
            distMax = cent.getDistMax();
            diam = cent.getDiametroMedio();
            temp = cent.getTempoAtt();
        }
    }

    /**
     * Configura o switch relacionado ao TPS ({@link CentralizadorAjustes#tps}), tais como a troca de estado.
     */
    private void configTps() {
        tps.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tps_sensor = tps.isChecked();
            tpsField(tps_sensor);
        });
    }

    /**
     * Configura os parâmetros do botão {@link CentralizadorAjustes#ok_angulo}.
     */
    private void configOkAngulo() {
        ok_angulo.setOnClickListener(view -> {
            angulo = conferirNumero(campo_angulo);

            Thread t = new Thread(() -> {
                enviar("angulo:", angulo);
                pausarThread();
                recebido = UDP.getMensagem_recebida();
                System.out.println(recebido);
            });
            t.start();

            //Fecha o teclado após clicar no botão de ok;
            Teclado.fecharTeclado(CentralizadorAjustes.this);
        });
    }

    /**
     * Configura os parâmetros do botão {@link CentralizadorAjustes#ok_distBarra}.
     */
    private void configOkDistBarra() {
        ok_distBarra.setOnClickListener(view -> {
            distBarra = conferirNumero(campo_distBarra);

            Thread t = new Thread(() -> {
                enviar("maximo:", distBarra);
                pausarThread();
                recebido = UDP.getMensagem_recebida();
                System.out.println(recebido);
            });
            t.start();

            System.out.println(Gateway.getGateway(CentralizadorAjustes.this));

            //Fecha o teclado após clicar no botão de ok;
            Teclado.fecharTeclado(CentralizadorAjustes.this);
        });
    }

    /**
     * Configura os parâmetros do botão {@link CentralizadorAjustes#ok_distMin}.
     */
    private void configOkDistMin() {
        ok_distMin.setOnClickListener(view -> {
            distMin = conferirNumero(campo_distMin);

            Thread t = new Thread(() -> {
                enviar("minimo:", distMin);
                pausarThread();
                recebido = UDP.getMensagem_recebida();
                System.out.println(recebido);
            });
            t.start();


            //Fecha o teclado após clicar no botão de ok;
            Teclado.fecharTeclado(CentralizadorAjustes.this);
        });
    }

    /**
     * Configura os parâmetros do botão {@link CentralizadorAjustes#ok_distMax}.
     */
    private void configOkDistMax() {
        ok_distMax.setOnClickListener(view -> {
            distMax = conferirNumero(campo_distMax);

            Thread t = new Thread(() -> {
                enviar("valido:", distMax);
                pausarThread();
                recebido = UDP.getMensagem_recebida();
                System.out.println(recebido);
            });
            t.start();


            //Fecha o teclado após clicar no botão de ok;
            Teclado.fecharTeclado(CentralizadorAjustes.this);
        });
    }

    /**
     * Configura os parâmetros do botão {@link CentralizadorAjustes#ok_diam}.
     */
    private void configOkDiam() {
        ok_diam.setOnClickListener(view -> {
            diam = conferirNumero(campo_diam);
            Thread t = new Thread(() -> {
                enviar("diametro:", diam);
                pausarThread();
                recebido = UDP.getMensagem_recebida();
                System.out.println(recebido);
            });
            t.start();


            //Fecha o teclado após clicar no botão de ok;
            Teclado.fecharTeclado(CentralizadorAjustes.this);
        });
    }

    /**
     * Configura os parâmetros do botão {@link CentralizadorAjustes#ok_temp}.
     */
    private void configOkTemp() {
        ok_temp.setOnClickListener(v -> {
            //temp = conferirNumero(campo_temp);
            temp = MMath.StringParaFloat(campo_temp.getText().toString(), 0.0f);
            System.out.println(temp);
        });
    }

    /**
     * Configura os parâmetros do botão {@link CentralizadorAjustes#continuar}.
     */
    private void configContinuar() {
        continuar.setOnClickListener(view -> {

            angulo = conferirNumero(campo_angulo);
            distBarra = conferirNumero(campo_distBarra);
            distMin = conferirNumero(campo_distMin);
            distMax = conferirNumero(campo_distMax);
            diam = conferirNumero(campo_diam);
            temp = MMath.StringParaFloat(campo_temp.getText().toString(), 0.0f);
            float tempoFixo = MMath.StringParaFloat(campo_tempoFixo.getText().toString(), 1.3f);

            Log.d(TAG, "Iniciando processo de salvamento e sincronização");

            if (tudo_preenchido()) {
                if (tempoFixo < 1.0f || tempoFixo > 1.5f) {
                    texto = "O tempo fixo deve estar entre 1.0 e 1.5 segundos.";
                    caixaAviso();
                    return;
                }

                // Sincronização robusta com ESP32 (Etapa de Segurança)
                new Thread(() -> {
                    SensorManager sm = new SensorManager();
                    sm.setSufixoIP(sufixo_IP);
                    // O valor 'valido' no protocolo do ESP32 parece corresponder ao distMax ou um parâmetro de corte
                    boolean sincronizado = sm.enviarParametros(this, diam, distMin, distBarra, angulo, distMax);
                    
                    runOnUiThread(() -> {
                        if (!sincronizado) {
                            Toast.makeText(this, "Aviso: ESP32 não confirmou os parâmetros, mas os dados foram salvos localmente.", Toast.LENGTH_LONG).show();
                        }
                    });
                }).start();

                Intent intent;
                SwitchCompat tela2 = findViewById(R.id.centSens_pagina);
                if (tela2.isChecked()) {
                    intent = new Intent(CentralizadorAjustes.this, CentralizadorSistema2.class);
                } else {
                    intent = new Intent(CentralizadorAjustes.this, CentralizadorSistema.class);
                }

                try {
                    salvarDados();
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao salvar dados: " + e);
                }

                startActivity(intent);
            } else {
                caixaAviso();
            }
        });
    }

    /**
     * Configura os parâmetros do botão {@link CentralizadorAjustes#voltar}.
     */
    private void configVoltar() {
        voltar.setOnClickListener(view -> {
            finish();           // Termina a página (destroi, apaga)
        });
    }

    /**
     * Configuração dos elementos da tela quando o {@link SwitchCompat} relativo ao uso do TPS está ativo e quando
     * não está ativo.
     * @param tps_sensor {@link Boolean} que identifica se o campo foi marcado como ativo ou não, para devidas
     *                   alterações de visibilidade, texto, etc.
     */
    private void tpsField(boolean tps_sensor) {
        if (tps_sensor) {
            LayoutElements.constraintVisibility(findViewById(R.id.centAjus_diam), View.GONE);
            LayoutElements.constraintVisibility(findViewById(R.id.centAjus_block5), View.GONE);

            LayoutElements.textViewText(findViewById(R.id.centAjus_textoDistBarra), R.string.anguloMax);
            LayoutElements.textViewText(findViewById(R.id.centAjus_unidDistBarra), R.string.graus);

            LayoutElements.textViewText(findViewById(R.id.centAjus_textoDistMin), R.string.anguloMin);
            LayoutElements.textViewText(findViewById(R.id.centAjus_unidDistMin), R.string.graus);

            LayoutElements.constraintVisibility(findViewById(R.id.centAjus_distMax), View.GONE);
            LayoutElements.constraintVisibility(findViewById(R.id.centAjus_block4), View.GONE);

        } else {

            LayoutElements.constraintVisibility(findViewById(R.id.centAjus_diam), View.VISIBLE);
            LayoutElements.constraintVisibility(findViewById(R.id.centAjus_block5), View.VISIBLE);

            LayoutElements.textViewText(findViewById(R.id.centAjus_textoDistBarra), R.string.distanciaBarras);
            LayoutElements.textViewText(findViewById(R.id.centAjus_unidDistBarra), R.string.milimetros);

            LayoutElements.textViewText(findViewById(R.id.centAjus_textoDistMin), R.string.menorDistancia);
            LayoutElements.textViewText(findViewById(R.id.centAjus_unidDistMin), R.string.milimetros);

            LayoutElements.constraintVisibility(findViewById(R.id.centAjus_distMax), View.VISIBLE);
            LayoutElements.constraintVisibility(findViewById(R.id.centAjus_block4), View.VISIBLE);
        }
    }

    /**
     * Pausa uma {@link Thread} em execução por alguns segundos.
     */
    private void pausarThread() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            //run();
            System.out.println("Thread Interrompida!");
        }
    }

    /**
     * Envia uma mensagem ao ESP32 via protocolo UDP
     * @param prefixo Mensagem a ser enviada juntamente com um dado
     * @param dado Dado a ser enviado pelo protocolo
     */
    private void enviar(String prefixo, int dado) {
        UDP.enviarMensagem(prefixo + dado, 1234, this, sufixo_IP);
        //UDP.enviarMensagem(prefixo + dado, 1234, this);
    }

    /**
     * Envia uma mensagem ao ESP32 via protocolo UDP
     * @param mensagem Mensagem a ser enviada
     */
    private void enviar(String mensagem) {
        UDP.enviarMensagem(mensagem, 1234, this, sufixo_IP);
        //UDP.enviarMensagem(mensagem, 1234, this);
    }

    /**
     * Verifica se todos os campos foram preenchidos corretamente.
     * @return true - Se tudo está de acordo // false - Se algo errado ocorreu;
     */
    private boolean tudo_preenchido() {
        texto = "";
        if (tps_sensor) {
            if ((campo_angulo.getText().toString().equals(""))
                    || (campo_distBarra.getText().toString().equals(""))
                    || (campo_distMin.getText().toString().equals(""))
                    || (campo_temp.getText().toString().equals(""))){
                texto = "Todos os campos precisam ser preenchidos.\n";
                return false;
            }
        } else {
            if (//(campo_angulo.getText().toString().equals(""))
                    //||
                    (campo_distBarra.getText().toString().equals(""))
                    || (campo_distMin.getText().toString().equals(""))
                    || (campo_distMax.getText().toString().equals(""))
                    || (campo_diam.getText().toString().equals(""))
                    || (campo_temp.getText().toString().equals(""))
                    || (campo_tempoFixo.getText().toString().equals(""))
                    ){
                texto = "Todos os campos precisam ser preenchidos.\n";
                return false;
            }
        }
        return true;
    }

    /**
     * Confere se algo foi digitado na caixa de Input ({@link EditText}) / se o que foi digitado é possível de se
     * converter para inteiro.
     * @param editText {@link EditText} "alvo".
     * @return A {@link String} convertida em inteiro.
     */
    private int conferirNumero(EditText editText) {
        return MMath.StringParaInt(editText.getText().toString(), 0);
    }

    /**
     * Método para exibição da caix de aviso, quando o usuário digitar entradas inválidas.
     */
    private void caixaAviso() {
        DialogWarning dialogWarning = new DialogWarning();
        dialogWarning.show(getSupportFragmentManager(), "Warning missing sensibilities");
    }

    /**
     * Método para salvar os dados obtidos pela inserção do usuário no banco de dados. Método a ser
     * utilizado na versão final.
     */
    private void salvarDados() {
        CentralizadorParametros cent1 = new CentralizadorParametros();

        cent1.setID(1);
        cent1.setAngulo(angulo);
        cent1.setDiametroMedio(diam);
        cent1.setTempoAtt(temp);
        cent1.setDistBarra(distBarra);
        cent1.setDistMin(distMin);
        cent1.setDistMax(distMax);
        cent1.setHabilitaScrollTempo(sw_habilitaScroll.isChecked() ? 1 : 0);
        cent1.setTempoFixo(MMath.StringParaFloat(campo_tempoFixo.getText().toString(), 1.3f));

        if (centAjusHelper.searchAjustes(1) == null) {
            centAjusHelper.addCent(cent1);
        } else {
            centAjusHelper.updateParametrosCentralizador(cent1);
        }
    }

    @Override
    public void onButtonClicked() {

    }

    @Override
    public void setTextViewTexts(TextView title, TextView msg1, TextView msg2) {
        title.setText(R.string.tituloErrosDetectados);
        msg1.setText(texto);
    }

    @Override
    public void setTextViewVisibilities(TextView title, TextView msg1, TextView msg2) {

        if (texto.equals("")) {
            msg1.setVisibility(View.GONE);
        } else {
            msg1.setVisibility(View.VISIBLE);
        }

        msg2.setVisibility(View.GONE);
    }
}