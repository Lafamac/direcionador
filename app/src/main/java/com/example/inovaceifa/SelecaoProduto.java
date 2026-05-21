package com.example.inovaceifa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.inovaceifa.Centralizador.CentralizadorAjustes;
import com.example.inovaceifa.Centralizador.CentralizadorPrincipal;
import com.example.inovaceifa.Utilities.HelperDatabaseSQL;

/**
 * Implementa uma tela contendo os produtos/serviços disponibilizados pela empresa.
 * <p>
 *     - Clicar no "Ver Detalhes" do Direcionador de Colheita redireciona o usuário à explicação do
 *       produto e consequente uso.
 * </p>
 * <p>
 *     - Clicar no "Ver Detalhes" do Gerenciador de Colheita redireciona o usuário à explicação do
 *       produto e consequente uso.
 * </p>
 * <p>
 *     - Clicar no "Ver Detalhes" do Monitor Animal redireciona o usuário à explicação do produto e
 *       consequente uso.
 * </p>
 * <p>
 *     - Clicar na logomarca da empresa redireciona o usuário para o site da empresa, externo ao app
 *       (sendo o site: http://inovaceifa.com.br/).
 * </p>
 */

public class SelecaoProduto extends AppCompatActivity {

    /**
     * {@link ImageButton} - Botão contendo a logo da CEIFA, o qual redireciona ao site.
     */
    private ImageButton btnCeifa;
    /**
     * {@link ConstraintLayout} - "<i>Botão</i>" do Direcionador de Colheita, o qual redireciona para {@link CentralizadorPrincipal}.
     */
    private ConstraintLayout direcionador;

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao_produto_novo);

        iniciarComponentes();

        btnCeifa.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://inovaceifa.com.br/"))));

        direcionador.setOnClickListener(v -> {
            Intent intent = new Intent(SelecaoProduto.this,
                    CentralizadorPrincipal.class);
            startActivity(intent);
        });
    }

    /**
     * Inicializa os componentes através do método {@link android.app.Activity#findViewById(int)}.
     */
    private void iniciarComponentes() {


        btnCeifa = findViewById(R.id.btn_site);

        direcionador = findViewById(R.id.selProd_block1);
    }
}