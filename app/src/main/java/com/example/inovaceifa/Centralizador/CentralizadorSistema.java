package com.example.inovaceifa.Centralizador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inovaceifa.R;
import com.example.inovaceifa.Utilities.Buzzer;
import com.example.inovaceifa.Utilities.DialogWarning;
import com.example.inovaceifa.Utilities.HelperDatabaseSQL;
import com.example.inovaceifa.Utilities.MMath;
import com.example.inovaceifa.Utilities.UDPProtocol;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Classe que comanda a Activity relativa ao funcionamento do sistema.
 * Alterada para travar o tempo de atualização em 1.3s fixos.
 */
public class CentralizadorSistema extends AppCompatActivity implements Runnable, DialogWarning.DialogWarningListener {

    private ImageView[] imagens = {null, null, null, null, null, null, null, null, null, null, null, null, null};
    private Button start, parar, marcarPe;
    private ImageButton voltar;
    private SwitchCompat random, buzzer, manobra, media, moda, modaMovel;
    private TextView texto_segundos, textoAlerta;
    private SeekBar barraTempo;
    private Thread thread;
    private CentralizadorLEDs l;
    private SensorManager sensorManager;
    private MediaPlayer mp;
    private final AtomicBoolean controle_Thread = new AtomicBoolean(false);
    public static boolean rodando = false;
    private boolean reiniciar = true;
    private boolean tps;
    private float tempo;
    private int habilitaScroll;
    private float tempoFixo;
    private final int sufixo_IP = 200;
    private int angulo, distBarra, distMax, distMin, diam;
    private final int som = R.raw.sound;
    private int totalReset, totalManobra;
    private int cont = 0, desalinhadoE = 0, desalinhadoD = 0, centralizado = 0;
    private int contaPes = 0;
    private final static String TAG = "centralizadorDebug";
    private final String separa_dados = "\n--------------------------------------------------\n";
    private String recebido, comando = "";
    private HelperDatabaseSQL helper;
    private final static int ERROR_CODE = -20;
    private Operador operador;
    private String numeroOperador;
    private int numOpe;
    private int tempoDeTrabalho;
    private ImageView contagemAlturaEsquerdo, contagemAlturaDireito;
    private boolean onAlertEsquerdo, onAlertDireito;
    private View setaEsquerda, setaDireita;
    private final float maxAltura = 800, minAltura = 50;
    private long tempoInicioMillis = 0, tempoDecorridoMillis = 0;
    private boolean estaRodando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centralizador_sistema);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("numeroOperador")) {
            Object extra = intent.getSerializableExtra("numeroOperador");
            numeroOperador = String.valueOf(extra);

            try {
                numOpe = Integer.parseInt(numeroOperador);
            } catch (NumberFormatException e) {
                numOpe = 0;
                Log.e(TAG, "Erro ao converter numeroOperador para int");
            }
        }

        iniciarComponentes();
        sensorManager = new SensorManager();
        sensorManager.setSufixoIP(sufixo_IP);
        helper = HelperDatabaseSQL.getInstance(this);

        if (numeroOperador != null) {
            operador = helper.searchOperadorByNumber(numeroOperador);
        }

        CentralizadorParametros params = helper.searchAjustes(1);
        inicializaVariaveis(params);

        // Tempo de atualização inicializado a partir do banco de dados via inicializaVariaveis(params)

        media.setChecked(true);
        configMedia();
        configModa();
        configModaMovel();
        configManobra();
        configRestartESP();
        configStart();
        configParar();
        configVoltar();
        configMarcarPe();

        mp = MediaPlayer.create(this, som);
        configuracaoInicial();
        configLEDs();
        configTextoSegundos();
        configBarraTempo();    // O método agora trava a barra visualmente

        estaRodando = false;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void inicializaVariaveis(CentralizadorParametros cent) {
        if (cent != null) {
            habilitaScroll = cent.getHabilitaScrollTempo();
            tempoFixo = cent.getTempoFixo();
            
            if (habilitaScroll == 1) {
                tempo = cent.getTempoAtt() * 1000;
            } else {
                tempo = tempoFixo * 1000;
            }
            
            angulo = cent.getAngulo();
            distBarra = cent.getDistBarra();
            distMax = cent.getDistMax();
            distMin = cent.getDistMin();
            diam = cent.getDiametroMedio();
        } else {
            tempo = 1000;
            angulo = 0;
            distBarra = 480;
            distMax = 230;
            distMin = 60;
            diam = 30;
        }

        tps = false;
        totalReset = MMath.round(60 * 1000 / tempo);
        totalManobra = MMath.round(15 * 1000 / tempo);

        new Thread(this::enviaParams).start();
    }

    private void iniciarComponentes() {
        textoAlerta = findViewById(R.id.textoAlerta);
        media = findViewById(R.id.centSist_media);
        moda = findViewById(R.id.centSist_moda);
        modaMovel = findViewById(R.id.centSist_movel);
        random = findViewById(R.id.random);
        manobra = findViewById(R.id.manobra);
        start = findViewById(R.id.centSist_start);
        parar = findViewById(R.id.centSist_stop);
        marcarPe = findViewById(R.id.centSist_marcarPe);
        voltar = findViewById(R.id.centSist_voltar);
        texto_segundos = findViewById(R.id.centSist_segundos);
        barraTempo = findViewById(R.id.centSist_barraTempo);
        contagemAlturaEsquerdo = findViewById(R.id.centSist_ledAltura1D);
        contagemAlturaDireito = findViewById(R.id.centSist_ledAltura1E);
        setaEsquerda = findViewById(R.id.imageView2);
        setaDireita = findViewById(R.id.imageView3);

        imagens = new ImageView[]{
                findViewById(R.id.centSist_led1), findViewById(R.id.centSist_led2),
                findViewById(R.id.centSist_led3), findViewById(R.id.centSist_led4),
                findViewById(R.id.centSist_led5), findViewById(R.id.centSist_led6),
                findViewById(R.id.centSist_ledCentral),
                findViewById(R.id.centSist_led7), findViewById(R.id.centSist_led8),
                findViewById(R.id.centSist_led9), findViewById(R.id.centSist_led10),
                findViewById(R.id.centSist_led11), findViewById(R.id.centSist_led12)
        };
        ImageView setaE = findViewById(R.id.centSist_setaEsquerda);
        ImageView setaD = findViewById(R.id.centSist_setaDireita);

        l = new CentralizadorLEDs(imagens, setaD, setaE);
    }

    private void configRestartESP() {
        if (start != null) {
            // Um clique longo no botão Iniciar envia o comando de restart ao ESP32
            start.setOnLongClickListener(v -> {
                new Thread(() -> {
                    String resp = new UDPProtocol().enviarEReceber("restart", 1234, this, sufixo_IP);
                    runOnUiThread(() -> {
                        if (resp != null) {
                            Toast.makeText(this, "Comando de Reinício enviado ao ESP32", Toast.LENGTH_SHORT).show();
                            enviaParams(); // Re-envia os parâmetros após o restart
                        } else {
                            Toast.makeText(this, "Falha ao alcançar o ESP32 para Reinício", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
                return true;
            });
        }
    }

    private void configParar() {
        parar.setOnClickListener(v -> {
            parar.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);
            manobra.setVisibility(View.GONE);
            l.apagaLEDs();
            l.apagaSetas();
            stopThread();
        });
    }

    private void configVoltar() {
        voltar.setOnClickListener(v -> {
            reiniciar = false;
            stopThread();
            Intent i = new Intent(this, CentralizadorPrincipal.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
    }

    private void configuracaoInicial() { }

    private void configLEDs() { }

    private void configTextoSegundos() {
        // Exibirá o tempo atual (ex: "1.3 segundo(s)")
        texto_segundos.setText((tempo / 1000) + " segundo(s)");
    }

    /**
     * Configura a barra de tempo para permitir a seleção pelo usuário.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void configBarraTempo() {
        // Calcula o progresso inicial baseado no tempo atual (ex: 1300ms -> progress 3)
        int progress = (int) ((tempo - 1000) / 100);
        barraTempo.setProgress(progress);

        // Habilita ou desabilita a barra conforme configuração de parâmetros
        if (habilitaScroll == 1) {
            barraTempo.setEnabled(true);
            barraTempo.setAlpha(1.0f);
        } else {
            barraTempo.setEnabled(false);
            barraTempo.setAlpha(0.5f); // Deixa visualmente "apagada"
        }

        barraTempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Atualiza o tempo baseado no progresso (1.0s a 1.6s)
                tempo = 1000 + (progress * 100);
                configTextoSegundos();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void configMarcarPe() {
        if (marcarPe != null) {
            marcarPe.setOnClickListener(v -> {
                contaPes++;
                
                // Registrar no banco de dados (Profissional)
                helper.registrarEvento("MARCAR_PE", String.valueOf(contaPes), numeroOperador);
                
                // Manter log em arquivo para compatibilidade
                java.text.DateFormat formato_hora = java.text.DateFormat.getTimeInstance();
                String hora = formato_hora.format(java.util.Calendar.getInstance().getTime());
                String texto = "        Hora: " + hora + "\n        Número do Pé: " + contaPes + separa_dados;
                com.example.inovaceifa.Utilities.Arquivo.writeFile(texto, this, "contaPes_operational.txt");
                
                Toast.makeText(this, "Pé #" + contaPes + " registrado no banco!", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void enviaParams() {
        // Usa o sensorManager para enviar parâmetros no formato robusto esperado pelo firmware
        sensorManager.enviarParametros(this, diam, distMin, distBarra, angulo, distMax);
    }

    private void configStart() {
        start.setOnClickListener(v -> {
            if (!media.isChecked() && !moda.isChecked() && !modaMovel.isChecked()) {
                DialogWarning dw = new DialogWarning();
                dw.show(getSupportFragmentManager(), "Warning");
            } else {
                start.setVisibility(View.GONE);
                parar.setVisibility(View.VISIBLE);
                manobra.setVisibility(View.VISIBLE);

                if (media.isChecked()) comando = "iniciar:Media";
                else if (moda.isChecked()) comando = "iniciar:Moda";
                else comando = "iniciar:Movel";

                rodando = true;
                startThread();
            }
        });
    }

    private void startThread() {
        if (!controle_Thread.get()) {
            controle_Thread.set(true);
            thread = new Thread(this);
            thread.start();
        }
    }

    private void stopThread() {
        controle_Thread.set(false);
        rodando = false;
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    @Override
    public void run() {
        while (controle_Thread.get()) {
            if (rodando) {
                executarUDP();
            }
            try {
                Thread.sleep((long) tempo);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void executarUDP() {
        sensorManager.setModoSimulado(random.isChecked());
        
        // Se estiver em manobra, apenas mantém a interface limpa
        if (manobra.isChecked()) {
            runOnUiThread(() -> {
                l.apagaLEDs();
                l.apagaSetas();
            });
            return;
        }

        recebido = sensorManager.getDesalinhamento(comando, this);

        runOnUiThread(() -> {
            if (recebido != null && !recebido.isEmpty()) {
                try {
                    // Esconde o alerta em caso de sucesso
                    textoAlerta.setVisibility(View.GONE);
                    
                    // Atualiza os LEDs com o valor recebido
                    l.leds(recebido);
                    
                    // Salva log de deslocamento se estiver rodando
                    if (rodando) {
                        com.example.inovaceifa.Utilities.Arquivo.writeFile(l.getValorArquivo(), this, "log_deslocamento.txt");
                    }

                    Log.d(TAG, "Interface atualizada: " + recebido);
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao atualizar interface: " + e.getMessage());
                }
            } else if (!random.isChecked()) {
                // Em caso de falha de comunicação (Timeout) e não estiver em simulação
                Log.w(TAG, "Falha de comunicação com ESP32");
                textoAlerta.setVisibility(View.VISIBLE);
                l.apagaLEDs();
                l.apagaSetas();
            }
        });
    }

    private void configMedia() {
        media.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) { moda.setChecked(false); modaMovel.setChecked(false); }
        });
    }

    private void configModa() {
        moda.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) { media.setChecked(false); modaMovel.setChecked(false); }
        });
    }

    private void configModaMovel() {
        modaMovel.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) { media.setChecked(false); moda.setChecked(false); }
        });
    }

    private void configManobra() {
        manobra.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) { l.apagaSetas(); l.apagaLEDs(); }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThread();
    }

    @Override
    public void onButtonClicked() { }

    @Override
    public void onBackPressed() {
        // Bloqueia o botão voltar do Android para evitar saídas acidentais durante a colheita
        Toast.makeText(this, "Utilize o botão de retorno da interface para sair.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTextViewVisibilities(TextView tv1, TextView tv2, TextView tv3) {
        if (tv1 != null) tv1.setVisibility(View.VISIBLE);
        if (tv2 != null) tv2.setVisibility(View.GONE);
        if (tv3 != null) tv3.setVisibility(View.GONE);
    }

    @Override
    public void setTextViewTexts(TextView tv1, TextView tv2, TextView tv3) {
        if (tv1 != null) {
            tv1.setText("Selecione pelo menos um modo de operação antes de iniciar.");
        }
    }
}