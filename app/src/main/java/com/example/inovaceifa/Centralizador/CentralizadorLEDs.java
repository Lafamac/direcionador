package com.example.inovaceifa.Centralizador;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.inovaceifa.R;
import com.example.inovaceifa.Utilities.MMath;

/**
 * Classe utilizada para realizar o controle dos LEDs, juntamente com a utilização do sistema
 * desenvolvido. É utilizada juntamente com a Activity contida na classe CentralizadorSistema.java.
 *
 * @author Gustavo Henrique Tostes
 * @version 1.1 - nov. 2022
 */
public class CentralizadorLEDs {
    /**
     * {@link ImageView}[] - Vetor com os LEDs a serem acesos e/ou apagados
     */
    ImageView[] imagens;

    /**
     * {@link ImageView} - Seta apontando para a direita (>), indicando que o usuário deve deslocar a máquina para tal direçãp
     */
    ImageView setaD;

    /**
     * {@link ImageView} - Seta apontando para a esquerda (<), indicando que o usuário deve deslocar a máquina para tal direçãp
     */
    ImageView setaE;

    /**
     * {@link ImageView} - LEDs acesos no momento, considerando uso na Activity ({@link CentralizadorSistema2}).
     */
    ImageView leds;

    /**
     * {@link String} - Valor a ser inserido no arquivo.
     */
    String valorArquivo;

    /**
     * {@link Integer} - Número máximo de LEDs (somando esquerda + direita).
     */
    int limite = 12;

    /**
     * {@link Integer} - Código de erro, para o caso em que o número excedeu o limite (> 6 ou < -6).
     */
    private final static int ERROR_CODE = -20;

    /**
     * {@link Integer} - Código de nulo, para o caso em que o número recebido é nulo.
     */
    public final static int NULL_CODE = -40;

    private Operador operador;

    /**
     * {@link Integer}[] - Vetor com os LEDs a serem acesos e/ou apagados, utilizado na Activity {@link CentralizadorSistema2}.
     */
    int[] ledss = {R.drawable.e66, R.drawable.e55, R.drawable.e44,
            R.drawable.e33, R.drawable.e22, R.drawable.e11,
            R.drawable.c1,
            R.drawable.d11, R.drawable.d22, R.drawable.d33,
            R.drawable.d44, R.drawable.d55, R.drawable.d66};

    //----------------------------------------------------------------------------------------------
    /**
     * Construtor da classe em questão, utilizado na Activity {@link CentralizadorSistema}.
     * @param i Vetor contendo as imagens (utilizando o findViewById()) para identificá-las no sistema.
     */
    public CentralizadorLEDs(ImageView[] i, ImageView sD, ImageView sE) {
        imagens = i;
        setaD = sD;
        setaE = sE;
    }

    /**
     * Construtor da classe em questão, utilizado na Activity {@link CentralizadorSistema2}.
     * @param i Vetor contendo as imagens (utilizando o findViewById()) para identificá-las no sistema.
     */
    public CentralizadorLEDs(ImageView i, ImageView sD, ImageView sE) {
        leds = i;
        setaD = sD;
        setaE = sE;
    }

    public CentralizadorLEDs(ImageView[] i) {
        imagens = i;
    }

    /**
     * Configura a manipulação dos LEDs, sendo assim o ato de "acender" ou "apagar" os LEDs.
     * @param valor Valor de LEDs a serem manipulados, sendo positivo para os LEDs do lado direito e
     *              negativo para os LEDs do lado esquerdo.
     */
    public void leds(String valor) {
        int valor_recebido = MMath.StringParaInt(valor, ERROR_CODE);

        contabilizarLedRecebido(valor_recebido);

        Log.e("valor", Integer.toString(valor_recebido));

        if (valor_recebido != NULL_CODE) {
            if (valor_recebido == ERROR_CODE) {
                valor_recebido = 0;
            } else if (valor_recebido < -6) {
                valor_recebido = -6;
            } else if (valor_recebido > 6) {
                valor_recebido = 6;
            }

            if (CentralizadorSistema.rodando) {
                if(valor_recebido == 0) {
                    apagaLEDs();
                    apagaSetas();
                    imagens[6].setVisibility((View.VISIBLE));
                } else if(valor_recebido > 0) {
                    acende_direita(valor_recebido);
                    acendeSetaEsquerda(valor_recebido); // Invertido: se está na direita, manda ir para a esquerda
                } else {
                    acende_esquerda(valor_recebido);
                    acendeSetaDireita(valor_recebido); // Invertido: se está na esquerda, manda ir para a direita
                }
                valorArquivo = (""+valor_recebido+"\n");
            }
        } else {
            valorArquivo = "";
        }
    }

    private void contabilizarLedRecebido(int valorRecebido) {
        switch (valorRecebido) {
            case -6:
        }
    }

    /**
     * Configura a manipulação dos LEDs, sendo assim o ato de "acender" ou "apagar" os LEDs. Utilizado
     * na Activity {@link CentralizadorSistema2}.
     * @param valor Valor de LEDs a serem manipulados, sendo positivo para os LEDs do lado direito e
     *              negativo para os LEDs do lado esquerdo.
     */
    public void leds2(String valor) {
        int valor_recebido = MMath.StringParaInt(valor, ERROR_CODE);

        Log.e("valor", Integer.toString(valor_recebido));

        if (valor_recebido != NULL_CODE) {
            if (valor_recebido == ERROR_CODE) {
                valor_recebido = 0;
            } else if (valor_recebido < -6) {
                valor_recebido = -6;
            } else if (valor_recebido > 6) {
                valor_recebido = 6;
            }

            if (CentralizadorSistema2.rodando) {
                if(valor_recebido == 0) {
                    apagaLEDs2();
                    apagaSetas();
                    leds.setImageResource(R.drawable.c1);
                } else {
                    if (valor_recebido > 0) {
                        acendeSetaEsquerda(valor_recebido);
                    } else {
                        acendeSetaDireita(valor_recebido);
                    }
                    leds.setImageResource(ledss[valor_recebido+6]);
                }
                //adequaValorSetas(valor_recebido);

                //Grava o valor recebido no arquivo. Primeiro coloca em uma string o valor e uma quebra
                //de linha, para facilitar.
                valorArquivo = (""+valor_recebido+"\n");
            }
        } else {
            valorArquivo = "";
        }
    }

    /**
     * "Apaga" todos os LEDs, configurando a visibilidade de todos para INVISIBLE,
     * exceto o LED central (Marco Zero) que deve permanecer VISIBLE.
     */
    public void apagaLEDs() {
        for(int i = 0; i < imagens.length; i++) {
            if (i == 6) {
                imagens[i].setVisibility(View.VISIBLE);
            } else {
                imagens[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * "Apaga" todos os LEDs, configurando a visibilidade de todos para INVISIBLE. Utilizado na Activity {@link CentralizadorSistema2}.
     */
    public void apagaLEDs2() {
        leds.setImageResource(ledss[6]);
    }

    /**
     * Acender os LEDs do lado direito
     * @param valor_recebido Valor advindo da comunicação via UDP - Valores positivos
     */
    private void acende_direita(int valor_recebido) {
        apagaLEDs(); //Apaga todos os LEDs para nao haver conflito, mantendo o central aceso

        int limiteLocal = valor_recebido + 6;
        for(int i = 6; i <= limiteLocal; i++) {
            if (i < imagens.length) {
                imagens[i].setVisibility(View.VISIBLE); //Seta os LEDs como visiveis
            }
        }
        //Apaga os LEDs do lado esquerdo (0 a 5)
        for(int i = 0; i < 6; i++) {
            imagens[i].setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Acender os LEDs do lado esquerdo
     * @param valor_recebido Valor advindo da comunicação via UDP - Valores negativos
     */
    private void acende_esquerda(int valor_recebido) {
        apagaLEDs(); //Apaga todos os LEDs para nao haver conflito, mantendo o central aceso

        int limiteLocal = valor_recebido + 6; // Ex: -6 + 6 = 0
        for(int i = 6; i >= limiteLocal; i--) {
            if (i >= 0) {
                imagens[i].setVisibility(View.VISIBLE);
            }
        }
        //Apaga os LEDs do lado direito (7 a 12)
        for(int i = 7; i < imagens.length; i++) {
            imagens[i].setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Apaga todas as setas de direção
     */
    public void apagaSetas() {
        setaE.setVisibility(View.INVISIBLE);
        setaD.setVisibility(View.INVISIBLE);
    }

    /**
     * Acende as setas da esquerda baseado em uma quantidade específica
     * @param valor Quantas setas devem ser acesas.
     */
    private void acendeSetaDireita(int valor) {
        apagaSetas();
        setaD.setVisibility(View.VISIBLE);
        
        // valor é negativo aqui (máquina na esquerda, blocos na esquerda)
        // A seta DIREITA (setaD) aparece para mandar corrigir para a direita.
        int absValor = Math.abs(valor);
        if (absValor <= 2) {
            setaD.setImageResource(R.drawable.vd); // Verde
        } else if (absValor <= 4) {
            setaD.setImageResource(R.drawable.am); // Amarelo
        } else {
            setaD.setImageResource(R.drawable.vm); // Vermelho
        }
    }

    /**
     * Acende as setas da direita baseado em uma quantidade específica
     * @param valor Quantas setas devem ser acesas.
     */
    private void acendeSetaEsquerda(int valor) {
        apagaSetas();
        setaE.setVisibility(View.VISIBLE);
        
        // valor é positivo aqui (máquina na direita, blocos na direita)
        // A seta ESQUERDA (setaE) aparece para mandar corrigir para a esquerda.
        int absValor = Math.abs(valor); 
        if (absValor <= 2) {
            setaE.setImageResource(R.drawable.vd); // Verde
        } else if (absValor <= 4) {
            setaE.setImageResource(R.drawable.am); // Amarelo
        } else {
            setaE.setImageResource(R.drawable.vm); // Vermelho
        }
    }

    public String getValorArquivo() {
        return valorArquivo;
    }
}
