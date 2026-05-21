package com.example.inovaceifa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.inovaceifa.Centralizador.CentralizadorPrincipal;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementa uma tela inicial para o aplicativo, contendo a logo da empresa e o nome da mesma.
 * Possui uma animação de, aproximadamente, 3 segundos. Após essa, passa para a tela que contém
 * os produtos/serviços
 *
 * @author Leomar Santos Marques
 * @version 1.0 - 10/05/2022
 */

public class Inicial extends AppCompatActivity {
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_inicial);

        loop1();
        loop2();
        loop1();

        //tempo para abrir a nova guia
        timer =new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Inicial.this, CentralizadorPrincipal.class);
                startActivity(intent);
            }
        },2900);
        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                //startActivity(new Intent(this, SelecaoEquipamento.class));
                finish();
            }
        };
        h.postDelayed(r,3000);
        //startActivity(new Intent(this, SelecaoEquipamento.class));
        //esconder ActionBar
        //getSupportActionBar().hide();
        //finish();
    }

    public void loop2() {
        ImageView logo = findViewById(R.id.imagelogo);
        // code here
        logo.animate()
                .scaleX(0.5f)
                .scaleY(0.5f)
                .alpha(.6f)
                .setDuration(1950);

    }
    public void loop1() {
        ImageView logo = findViewById(R.id.imagelogo);
        // code here
        logo.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(1950);
    }
}