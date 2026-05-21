package com.example.inovaceifa.Utilities;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Classe para gerenciar o funcionamento de Buzzer (ou sons de forma geral).
 *
 * @author Gustavo H. Tostes
 * @version 1.0 - jul. 2023
 */
public class Buzzer {

    /**
     * Inicia uma música/buzzer.
     * @param mp Objeto MediaPlayer, inicializado na classe específica.
     * @param isPlaying Boolean para verificar se o som está tocando no momento.
     * @param context Contexto da aplicação.
     * @param sound Código do som a ser tocado.
     * @return Objeto do tipo mediaPlayer a ser criado (ou não) dentro do método.
     */
    public static MediaPlayer tocarMusica(MediaPlayer mp, boolean isPlaying, Context context, int sound) {
        if (isPlaying) {
            mp.stop();
            mp.release();
            mp = MediaPlayer.create(context, sound);
        }
        mp.start();
        return mp;
    }

    /**
     * Para a execução do objeto do tipo MediaPlayer, de modo a fazer com que um som pare de tocar.
     * @param mp Objeto MediaPlayer, inicializado na classe específica.
     * @param isPlaying Boolean para verificar se o som está tocando no momento.
     * @param context Contexto da aplicação.
     * @param sound Código do som a ser tocado.
     * @return Objeto do tipo mediaPlayer a ser criado (ou não) dentro do método.
     */
    public static MediaPlayer pararMusica(MediaPlayer mp, boolean isPlaying, Context context, int sound) {
        if (isPlaying) {
            mp.stop();
            mp = MediaPlayer.create(context, sound);
        }

        return mp;
    }
}
