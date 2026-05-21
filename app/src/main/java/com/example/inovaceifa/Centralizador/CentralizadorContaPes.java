package com.example.inovaceifa.Centralizador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.inovaceifa.R;
import com.example.inovaceifa.Utilities.Arquivo;

/**
 * NOT USED.
 */
public class CentralizadorContaPes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centralizador_conta_pes);

        String dados = Arquivo.readFile(this, "contaPes.txt");
        TextView texto = findViewById(R.id.centCont_dados);
        texto.setText(dados);
        texto.setMovementMethod(new ScrollingMovementMethod());

        Button apagar = findViewById(R.id.centCont_apagarDados);
        apagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Arquivo.deleteFile(CentralizadorContaPes.this, "contaPes.txt");
            }
        });

        Button grafico = findViewById(R.id.centCont_grafico);
        grafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CentralizadorContaPes.this, CentralizadorGrafico.class);
                startActivity(intent);
            }
        });
    }
}