package com.example.inovaceifa.Centralizador;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.example.inovaceifa.R;
import com.example.inovaceifa.Utilities.HelperDatabaseSQL;
import com.example.inovaceifa.Utilities.Imagem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.Toast;




public class CentralizadorResultados extends AppCompatActivity {
    private String numeroOperador;
    private HelperDatabaseSQL helper;
    private Operador operador;
    private int ledSum;
    private ImageButton voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centralizador_resultados);
        helper = HelperDatabaseSQL.getInstance(this);
        Log.d("DEBUG", "AQUI:");


       // atualizarImagem();

        // Enable EdgeToEdge functionality if required
        // EdgeToEdge.enable(this);

        // Retrieve numeroOperador from Intent extra
        Intent intent = getIntent(); // Obtém o Intent que foi usado para abrir esta Activity
        String numeroOperador = intent.getStringExtra("numeroOperador"); // Recupera o valor de "numeroOperador"

// Verifica se o valor foi passado corretamente
        if (numeroOperador != null) {
            Log.d("DEBUG", "numeroOperador passado: " + numeroOperador); // Exibe no Logcat se o valor foi encontrado
        } else {
            Log.e("DEBUG", "numeroOperador não foi passado."); // Exibe erro se o valor não foi encontrado
        }



        numeroOperador = intent.getStringExtra("numeroOperador");
        operador = helper.searchOperadorByNumber(numeroOperador);

        configVoltar();
        atualizarTextos();
        atualizarImagem();

        // Define total number of columns (bars)
        int totalColumns = 7; // Example total number of columns

        // Arrays to hold references to all ImageViews for bars and TextViews for percentages
        ImageView[] bars = new ImageView[totalColumns];
        TextView[] percentages = new TextView[totalColumns];

        // Initialize references for each bar ImageView and corresponding TextView
        for (int i = 0; i < totalColumns; i++) {
            int barResId = getResources().getIdentifier("barra" + (i + 1), "id", getPackageName());
            int percentageResId = getResources().getIdentifier("percentage" + (i + 1), "id", getPackageName());
            bars[i] = findViewById(barResId);
            percentages[i] = findViewById(percentageResId);
        }

        // Example: Get values from Operador and set heights accordingly
        for (int i = 0; i < totalColumns; i++) {
            int ledCount = 0; // Default value if getter method is not applicable

            switch (i) {
                case 0:
                    ledCount = operador.getNumeroLedsVermelhosEsquerda();
                    percentages[i].setTextColor(getResources().getColor(R.color.red));
                    break;
                case 1:
                    ledCount = operador.getNumeroLedsAmarelosEsquerda();
                    percentages[i].setTextColor(getResources().getColor(R.color.yellow));
                    break;
                case 2:
                    ledCount = operador.getNumeroLedsVerdeEscurosEsquerda();
                    percentages[i].setTextColor(getResources().getColor(R.color.dark_green));
                    break;
                case 3:
                    ledCount = operador.getNumeroLedsVerdeClaros();
                    percentages[i].setTextColor(getResources().getColor(R.color.bright_green));
                    break;
                case 4:
                    ledCount = operador.getNumeroLedsVerdeEscurosDireita();
                    percentages[i].setTextColor(getResources().getColor(R.color.dark_green));
                    break;
                case 5:
                    ledCount = operador.getNumeroLedsAmarelosDireita();
                    percentages[i].setTextColor(getResources().getColor(R.color.yellow));
                    break;
                case 6:
                    ledCount = operador.getNumeroLedsVermelhosDireita();
                    percentages[i].setTextColor(getResources().getColor(R.color.red));
                    break;
            }

            ledSum += ledCount;

            // Calculate height percentage based on ledCount
            if (ledSum == 0) {
                ledSum++;
            }
            if (ledCount == 0) {
                ledCount++;
            }
            int heightPercentage = (ledCount * 100) / ledSum;

            // Set height for the corresponding bar ImageView
            bars[i].getLayoutParams().height = heightPercentage;
            bars[i].requestLayout(); // Refresh layout to apply changes

            // Set percentage text for the corresponding TextView
            percentages[i].setText(heightPercentage + "%");
        }


    }

    private void configVoltar() {
        voltar = findViewById(R.id.resulSist_voltar); // Inicializa o botão pelo ID
        voltar.setOnClickListener(v -> finish()); // Configura a ação de voltar
    }

    private void atualizarTextos() {
        String texto = "Operador " + operador.getNumero();
        TextView numOperador;

        numOperador = findViewById(R.id.numOp_resul);
        numOperador.setText(texto);

        TextView textView = findViewById(R.id.nomeOp_resul);
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
            ImageView imagem = findViewById(R.id.resul_imagem);
            Imagem image = new Imagem();
            imagem.setImageBitmap(image.pathToImage(operador.getImagemPath()));
        }
    }

}


