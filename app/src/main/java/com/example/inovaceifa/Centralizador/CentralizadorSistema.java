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
    private Button start, parar;
    private ImageButton voltar;
    private SwitchCompat random, buzzer, manobra, media, moda, modaMovel;
    private TextView texto_segundos, textoAlerta;
    private SeekBar barraTempo;
    private Thread thread;
    private CentralizadorLEDs l;
    private UDPProtocol UDP;
    private MediaPlayer mp;
    private final AtomicBoolean controle_Thread = new AtomicBoolean(false);
    public static boolean rodando = false;
    private boolean reiniciar = true;
    private boolean tps;
    private float tempo;
    private final int sufixo_IP = 200;
    private int angulo, distBarra, distMax, distMin, diam;
    private final int som = R.raw.sound;
    private int totalReset, totalManobra;
    private int cont = 0, desalinhadoE = 0, desalinhadoD = 0, centralizado = 0;
    private final static String TAG = "centralizadorDebug";
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
        UDP = new UDPProtocol();
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
            tempo = cent.getTempoAtt() * 1000;
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
        voltar = findViewById(R.id.centSist_voltar);
        texto_segundos = findViewById(R.id.centSist_segundos);
        barraTempo = findViewById(R.id.centSist_barraTempo);
        contagemAlturaEsquerdo = findViewById(R.id.centSist_ledAltura1D);
        contagemAlturaDireito = findViewById(R.id.centSist_ledAltura1E);
        setaEsquerda = findViewById(R.id.imageView2);
        setaDireita = findViewById(R.id.imageView3);

        l = new CentralizadorLEDs(imagens);
    }

    private void configRestartESP() { }

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

        // Habilita a barra para que o usuário consiga interagir
        barraTempo.setEnabled(true);

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

    private void enviaParams() {
        String msg = "params:" + angulo + ":" + distBarra + ":" + distMax + ":" + distMin + ":" + diam;
        UDP.enviarMensagem(msg, 1234, this, sufixo_IP);
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
    }

    @Override
    public void run() {
        while (controle_Thread.get()) {
            try {
                Thread.sleep((long) tempo);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
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
    public void onButtonClicked() { }

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