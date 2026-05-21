package com.example.inovaceifa.Centralizador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inovaceifa.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Classe para lidar com uma Activity de introdução à configuração de parâmetros (ajustes) do Centralizador
 * da Colhedora. Sendo assim exibe uma mensagem ao usuário para que o mesmo esteja ciente do que está
 * por vir na próxima tela.
 *
 * @author Gustavo Henrique Tostes
 * @version 1.0
 * @since 26/09/2023
 */
public class CentralizadorAjustesPrincipal extends AppCompatActivity {

    /**
     * {@link FloatingActionButton} - Botão para retornar à página inicial.
     */
    private FloatingActionButton voltar;

    /**
     * {@link Button} - Botão para iniciar a configuração de parâmetros ({@link CentralizadorAjustes}).
     */
    private Button iniciarParam;

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centralizador_ajustes_principal);

        iniciarComponentes();

        configVoltar();

        configIniciarParam();
    }

    /**
     * Inicializa os componentes através do método {@link android.app.Activity#findViewById(int)}.
     */
    private void iniciarComponentes() {
        voltar = findViewById(R.id.centAjusPrin_voltar);
        iniciarParam = findViewById(R.id.centAjusPrin_continuar);
    }

    /**
     * Configura os parâmetros referentes ao {@link CentralizadorAjustesPrincipal#voltar}.
     */
    private void configVoltar() {
        voltar.setOnClickListener(v -> finish());
    }

    /**
     * Configura os parâmetros referentes ao {@link CentralizadorAjustesPrincipal#iniciarParam}.
     */
    private void configIniciarParam() {
        iniciarParam.setOnClickListener(v -> startActivity(new Intent(CentralizadorAjustesPrincipal.this, CentralizadorAjustes.class)));
    }
}