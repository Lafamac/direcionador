package com.example.inovaceifa.Centralizador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inovaceifa.R;
import com.example.inovaceifa.Utilities.Arquivo;
import com.example.inovaceifa.Utilities.DialogWarning;
import com.example.inovaceifa.Utilities.UDPProtocol;
import com.example.inovaceifa.Utilities.request_data;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class CentralizadorSistema2 extends AppCompatActivity implements Runnable, DialogWarning.DialogWarningListener {

    ImageView[] imagens = {null, null, null, null, null, null, null, null, null, null,
            null, null, null};

    String recebido;
    String descricao;
    String comando = "";
    String nomeArquivo = "Dados_Deslocamento.txt";
    String diretorio = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS)+"/"+"INOVA CEIFA"+"/";

    TextView sensibilidade_usuario;
    Button start, parar, acessarGrafico;
    ImageButton voltar;

    private final AtomicBoolean controle_Thread = new AtomicBoolean(false);
    private Thread thread;
    static boolean rodando = false;
    boolean reiniciar = true;

    CentralizadorLEDs l;
    UDPProtocol UDP;
    float tempo;

    final int sufixo_IP = 200;
    int angulo, distBarra, distMax, distMin, diam;
    boolean tps;

    int contaPes = 0;
    final String separa_dados = "\n--------------------------------------------------\n";

    //----------------------------------------------------------------------------------------------

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centralizador_sistema2);

        //Pega os dados advindos da pagina anterior
        Intent intent2 = getIntent();
        tempo = (float) intent2.getSerializableExtra("tempo") * 1000;
        angulo = (int) intent2.getSerializableExtra("angulo");
        distBarra = (int) intent2.getSerializableExtra("distBarra");
        distMax = (int) intent2.getSerializableExtra("distMax");
        distMin = (int) intent2.getSerializableExtra("distMin");
        diam = (int) intent2.getSerializableExtra("diam");
        tps = (boolean) intent2.getSerializableExtra("tps");

        SwitchCompat media = findViewById(R.id.centSist_media);
        SwitchCompat moda = findViewById(R.id.centSist_moda);
        SwitchCompat modaMovel = findViewById(R.id.centSist_movel);

        //Configuração do Switch de Média
        media.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (media.isChecked()) {
                moda.setChecked(false);
                modaMovel.setChecked(false);
            }
        });

        //Configuração do Switch de Moda
        moda.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (moda.isChecked()) {
                media.setChecked(false);
                modaMovel.setChecked(false);
            }
        });

        //Configuração do Switch de Moda Móvel
        modaMovel.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (modaMovel.isChecked()) {
                media.setChecked(false);
                moda.setChecked(false);
            }
        });

        UDP = new UDPProtocol();

        //Thread para enviar o comando de restart ao ESP32, iniciada logo ao chegar na tela!
        /*Handler reenviar_dados = new Handler();
        Thread restart_esp = new Thread(() -> {
            while(reiniciar) {
                try {
                    Thread.sleep(600000);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }

                if (reiniciar) {
                    System.out.println("fez isso :)))");
                    UDP.enviarMensagem("restart", 1234,
                            CentralizadorSistema.this, sufixo_IP);

                    reenviar_dados.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enviar("angulo:", angulo);
                            pausarThread();

                            enviar("maximo:", distBarra);
                            pausarThread();

                            enviar("minimo:", distMin);
                            pausarThread();

                            if (!tps) {
                                enviar("valido:", distMax);
                                pausarThread();

                                enviar("diametro:", diam);
                                pausarThread();
                            }

                            enviar("confirmado");
                            pausarThread();
                        }
                    }, 30000/2);
                }
            }
        });
        restart_esp.start();*/

        //Configuração do Botão de Iniciar
        start = findViewById(R.id.centSist_start);
        start.setOnClickListener(v -> {
            if (!media.isChecked() && !moda.isChecked() && !modaMovel.isChecked()) {
                DialogWarning dialogWarning = new DialogWarning();
                dialogWarning.show(getSupportFragmentManager(), "Warning missing parameters");
            } else {
                start.setVisibility(View.GONE);
                parar.setVisibility(View.VISIBLE);
                acessarGrafico.setVisibility(View.GONE);

                if (media.isChecked()) {
                    comando = "iniciar:Media";
                } else if (moda.isChecked()) {
                    comando = "iniciar:Moda";
                } else if (modaMovel.isChecked()) {
                    comando = "iniciar:Movel";
                }

                rodando = true;
                startThread();
            }
        });

        //Configuração do Botão de Parar (Pausar)
        parar = findViewById(R.id.centSist_stop);
        parar.setOnClickListener(v -> {
            contaPes = 0;
            parar.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);
            acessarGrafico.setVisibility(View.VISIBLE);
            l.apagaLEDs2();
            l.apagaSetas();
            stopThread();
        });

        //Configuração do Botão de Acesso ao Gráfico (Sendo usado como acesso aos dados com tempo)
        acessarGrafico = findViewById(R.id.centSist_grafico);
        acessarGrafico.setOnClickListener(v -> {
            //gravarArquivo("20\n15\n-15\n8\n\n");

            Intent intent = new Intent(CentralizadorSistema2.this, CentralizadorContaPes.class);
            startActivity(intent);
        });

        //Configuração do Botão de Voltar
        voltar = findViewById(R.id.centSist_voltar);
        voltar.setOnClickListener(v -> {
            reiniciar = false;
            //restart_esp.interrupt();
            finish();
        });

        //Configuração do Botão de Marcar dados (Com tempo)
        Button marcar = findViewById(R.id.centSist_marcarPe);
        marcar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe01fedaa, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        marcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contaPes++;
                DateFormat formato_hora = DateFormat.getTimeInstance();
                String hora = formato_hora.format(Calendar.getInstance().getTime());
                String texto = "        Hora: " + hora + "\n        Número do Pé: " + contaPes + separa_dados;
                Arquivo.writeFile(texto, CentralizadorSistema2.this, "contaPes.txt");
            }
        });



        //sensibilidade_usuario = findViewById(R.id.descricao_sens);
        //setarSensibilidades();

        configuracaoInicial();                      // Seta as visibilidades iniciais

        ImageView setaD = findViewById(R.id.centSist_setaDireita2);
        ImageView setaE = findViewById(R.id.centSist_setaEsquerda2);
        ImageView leddd = findViewById(R.id.centSist_ledsP);

        l = new CentralizadorLEDs(leddd, setaD, setaE);
        l.apagaLEDs2();                              // Apaga todos os LEDs
        l.apagaSetas();
    }

    /**
     * Envia uma mensagem ao ESP32 via protocolo UDP
     * @param prefixo Mensagem a ser enviada juntamente com um dado
     * @param dado Dado a ser enviado pelo protocolo
     */
    private void enviar(String prefixo, int dado) {
        UDP.enviarMensagem(prefixo + dado, 1234, this, sufixo_IP);
    }

    /**
     * Envia uma mensagem ao ESP32 via protocolo UDP
     * @param mensagem Mensagem a ser enviada
     */
    private void enviar(String mensagem) {
        UDP.enviarMensagem(mensagem, 1234,
                this, sufixo_IP);
    }

    /**
     * Pausa uma Thread em execução por alguns segundos.
     */
    private void pausarThread() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            //run();
            System.out.println("Thread was interrupted, Failed to complete operation");
        }
    }

    /**
     * Configurações iniciais da Activity em questão, tais quais visibilidade, texto e afins.
     */
    private void configuracaoInicial() {
        start.setVisibility(View.VISIBLE);
        parar.setVisibility(View.GONE);
        acessarGrafico.setVisibility(View.VISIBLE);
    }

    /**
     * Seta as variáveis relativas às sensibilidades, pegando o valor em questão da classe
     * OBSERVAÇÃO: SERÁ SUBSTITUIDO POR UM intent.putExtra();
     */
    private void setarSensibilidades() {

        Intent intent = getIntent();

        int sens_pot = (Integer) intent.getSerializableExtra("sens1");
        int sens_ult = (Integer) intent.getSerializableExtra("sens2");
        int sens_vl = (Integer) intent.getSerializableExtra("sens3");

        String texto = "Sensibilidades utilizadas:\n\n" +
                "Lidar: " + sens_vl + "%\n" +
                "Ultrassonico: " + sens_ult + "%\n" +
                "Potenciometro: " + sens_pot + "%";

        sensibilidade_usuario.setText(texto);

        descricao = "/_" + sens_pot + "%$" + sens_vl + "%&" + sens_ult + "}";
    }

    /**
     * Envio de dados via HTTPs
     * @param dado Dado a ser enviado pelo protocolo em questão
     */
    public void request_to_url (String dado) {
        // Configurações de conectividade com o ESP
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //Utilizado para enviar o endereço ao ESP
        request_data ab = new request_data(imagens, "Direcionador", diretorio, nomeArquivo);

        //Se tudo estiver nos conformes, envia o endereço ao ESP. Caso o aparelho não esteja
        //conectado, aparece uma mensagem na tela (Toast) que indica que o meso não está conectado.
        if(networkInfo != null && networkInfo.isConnected()) {
            ab.execute("http://" + "192.168.4.1" + dado);
        } else {
            Toast.makeText(CentralizadorSistema2.this, "Not connected  ", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Inicia a execução da Thread, chamado quando se deseja que informações sejam recebidas do
     * ESP32.
     */
    public void startThread() {
        thread = new Thread(this);            // "Criação" da thread
        thread.start();                             // Inicia a Thread
    }

    /**
     * Finaliza a execução da Thread criada, sendo chamado quando se deseja que o recebimento de
     * dados do ESP32 pare.
     */
    public void stopThread() {
        rodando = false;
        controle_Thread.set(false);
        thread.interrupt();
    }

    /**
     * Sobrescrita do método run(), advindo da classe Runnable, configurando o que ocorre após iniciar
     * a execução da Thread.
     */
    @Override
    public void run() {
        controle_Thread.set(true);
        while (controle_Thread.get()) {
            try {
                Thread.sleep((long)tempo);     // Tempo de inatividade
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                //run();
                System.out.println("Thread was interrupted, Failed to complete operation");
            }

            //SystemClock.sleep(100);
            if (rodando) {
                //request_to_url(descricao);
                executarUDP();
            } else {
                l.apagaLEDs2();
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * Execução do UDP, recebendo dados do ESP32 e realizando alguma ação com os mesmos
     */
    private void executarUDP() {
        //UDP.enviarMensagem(comando, 1234, this, sufixo_IP);
        UDP.enviarMensagem(comando, 1234, this);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            //run();
            System.out.println("Thread was interrupted, Failed to complete operation");
        }

        recebido = UDP.getMensagem_recebida();
        System.out.println(recebido);

        //Execução de uma Thread em background UI.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //recebido = "" + (new Random().nextInt(12) - 6);
                //System.out.println(" valor base: " + recebido);
                l.leds2(recebido);
                Arquivo.writeFile(l.getValorArquivo(), CentralizadorSistema2.this,
                        "teste2.txt");
                //System.out.println(Arquivo.readFile(CentralizadorSistema.this, "teste2.txt"));
                //gravarArquivo(l.getValorArquivo());
            }
        });
    }

    /**
     * Gravação de dados num arquivo com extensão .txt
     * @param texto Conteúdo a ser inserido no arquivo de texto.
     */
    public void gravarArquivo(String texto) {
        //Criação de um "arquivo" para o diretorio a ser utilizado, definido no escopo
        File diretorio = new File(this.diretorio);

        //Verifica se o diretório citado já foi criado. Caso não, cria o mesmo.
        if (!diretorio.exists()) {
            diretorio.mkdir();
        }

        //Cria uma variável para se referir ao arquivo a ser utilizado para armazenar os dados
        File arquivo = new File (diretorio, nomeArquivo);
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

    @Override
    public void onButtonClicked() {

    }

    @Override
    public void setTextViewTexts(TextView title, TextView msg1, TextView msg2) {
        title.setText(R.string.tituloErrosDetectados);
        msg1.setText(R.string.erroMediaModa);
    }

    @Override
    public void setTextViewVisibilities(TextView title, TextView msg1, TextView msg2) {
        msg2.setVisibility(View.GONE);
    }
}