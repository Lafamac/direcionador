package com.example.inovaceifa.Centralizador;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.inovaceifa.Utilities.HelperDatabaseSQL;
import com.example.inovaceifa.Utilities.Imagem;
import com.example.inovaceifa.Utilities.DialogWarning;
import com.example.inovaceifa.R;

public class CentralizadorTelaOperador extends AppCompatActivity implements DialogWarning.DialogWarningListener {

    /**
     * {@link Operador} - Objeto da operador a ser utilizada.
     */
    private Operador operador;

    /**
     * {@link HelperDatabaseSQL} - Helper para utilização dos métodos do banco de dados.
     */
    private HelperDatabaseSQL helper;

    /**
     * {@link String} - Número da operador cujas informações estão sendo analisadas na Activity.
     */
    private String numOperador;

    /**
     * {@link Button} - Responsável por passar para a próxima página (Activity), podendo ser a
     * {@link } ou {@link CentralizadorParametros}.
     */
    private Button medicao;

    /**
     * {@link FloatingActionButton} - Retorna à página anterior.
     */
    private FloatingActionButton voltar;

    /**
     * {@link Button} - Permite editar as informações referentes à operador.
     */
    private Button editar;

    private boolean admin;

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DEBUG", "OPERADOR:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centralizador_tela_operador);
        helper = HelperDatabaseSQL.getInstance(this);

        Intent intent = getIntent();
        numOperador = (String) intent.getSerializableExtra("numeroOperador");
        admin = !(Boolean) intent.getSerializableExtra("cadastro");

        iniciarComponentes();

        operador = helper.searchOperadorByNumber(numOperador);

        atualizarTextos();

        atualizarImagem();

        configMedicao();

        configVoltar();

        configEditar();

        setarVisibilidade();

        Button under_button = findViewById(R.id.gerTela_button);
        under_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(admin)
                {
                    iniciarResultados();
                } else {
                    iniciarMedicao();
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.gerTela_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button edit_button = findViewById(R.id.gerTela_edit);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CentralizadorTelaOperador.this, CentralizadorEdicaoDados.class);
                intent1.putExtra("numeroOperador", numOperador);
                startActivity(intent1);
            }
        });
    }

    private void setarVisibilidade() {
        if (admin) {
            medicao.setText("Ver Resultados");
        } else {
            medicao.setText("Utilizar Sistema");
        }

        if(admin){
            editar.setVisibility(View.VISIBLE);
        } else {
            editar.setVisibility(View.GONE);
        }
    }

    /**
     * Inicializa os componentes através do método {@link android.app.Activity#findViewById(int)}.
     */
    private void iniciarComponentes() {
        medicao = findViewById(R.id.gerTela_button);
        voltar = findViewById(R.id.gerTela_fab);
        editar = findViewById(R.id.gerTela_edit);
    }

    /**
     * Configuração dos parâmetros referentes ao {@link CentralizadorTelaOperador#medicao}.
     */
    private void configMedicao() {
        medicao.setOnClickListener(v -> {
                    iniciarMedicao();
        });
    }

    /**
     * Configuração dos parâmetros referentes ao {@link CentralizadorTelaOperador#voltar}.
     */
    private void configVoltar() {
        voltar.setOnClickListener(v -> finish());
    }

    /**
     * Configuração dos parâmetros referentes ao {@link CentralizadorTelaOperador#editar}.
     */
    private void configEditar() {
        editar.setOnClickListener(v -> {
            Intent intent1 = new Intent(CentralizadorTelaOperador.this, CentralizadorEdicaoDados.class);
            intent1.putExtra("numeroOperador", numOperador);
            startActivity(intent1);
        });
    }

    /**
     * Seta os textos das TextView baseado no que foi cadastrado no banco de dados. Caso nada tenha
     * sido cadastrado, seta o texto para "".
     */
    private void atualizarTextos() {
        String texto = "Operador " + operador.getNumero();
        TextView textView;

        textView = findViewById(R.id.gerTela_num);
        textView.setText(texto);

        textView = findViewById(R.id.gerTela_local);
        if (operador.getLocal() != null) {
            textView.setText(operador.getLocal());
        } else {
            textView.setText("");
        }
        textView.setMovementMethod(new ScrollingMovementMethod());

    }

    /**
     * Seta a imagem, em caso de já existir uma imagem prévia no banco de dados, na tela do usuário
     */
    private void atualizarImagem() {
        if (operador.getImagemPath() != null) {
            ImageView imagem = findViewById(R.id.gerTela_imagem);
            Imagem image = new Imagem();
            imagem.setImageBitmap(image.pathToImage(operador.getImagemPath()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        operador = helper.searchOperadorByNumber(numOperador);
        atualizarTextos();
        atualizarImagem();
    }

    /**
     * Inicia a Activity relativa aos resultados obtidos após realizar medições de uma operador.
     */
    private void iniciarMedicao() {
       Intent intent1 = new Intent(CentralizadorTelaOperador.this, CentralizadorSistema.class);
       intent1.putExtra("numeroOperador", numOperador);
       startActivity(intent1);
    }

    /**
     * Inicia a Activity relativa aos resultados obtidos após realizar medições de uma operador.
     */
    private void iniciarResultados() {
        Intent intent1 = new Intent(CentralizadorTelaOperador.this, CentralizadorResultados.class);
        intent1.putExtra("numeroOperador", numOperador);
        startActivity(intent1);
    }

    //--------------------------- CaixaAviso.java - Métodos Implementados --------------------------
    @Override
    public void onButtonClicked() {

    }

    //@Override
    public void editButton(Button text) {

    }

    @Override
    public void setTextViewTexts(TextView title, TextView msg1, TextView msg2) {
        title.setText(R.string.tituloMedicaoNaoRealizada);
        msg1.setText(R.string.medicaoNaoRealizada);
    }

    @Override
    public void setTextViewVisibilities(TextView title, TextView msg1, TextView msg2) {
        msg2.setVisibility(View.GONE);
    }
}