package com.example.inovaceifa.Centralizador;

import android.view.View;
import android.widget.ImageView;
import com.example.inovaceifa.R;
import com.example.inovaceifa.Utilities.MMath;

/**
 * Classe utilizada para realizar o controle dos LEDs e setas de direção.
 * Revisada para consistência com o novo layout e protocolo operacional (ETAPA 9).
 */
public class CentralizadorLEDs {
    private ImageView[] imagens;
    private ImageView setaD; // Seta que manda ir para a Direita (Correção)
    private ImageView setaE; // Seta que manda ir para a Esquerda (Correção)
    private String valorArquivo = "";
    
    private final static int NULL_CODE = -40;

    public CentralizadorLEDs(ImageView[] i, ImageView sD, ImageView sE) {
        this.imagens = i;
        this.setaD = sD;
        this.setaE = sE;
    }

    /**
     * Atualiza o painel de LEDs e as setas baseado no valor recebido.
     * @param valor String representando o desalinhamento (-6 a 6).
     */
    public void leds(String valor) {
        int valor_recebido = MMath.StringParaInt(valor, NULL_CODE);

        if (valor_recebido == NULL_CODE) {
            apagaLEDs();
            apagaSetas();
            valorArquivo = "";
            return;
        }

        // Normalização de segurança
        if (valor_recebido < -6) valor_recebido = -6;
        if (valor_recebido > 6) valor_recebido = 6;

        valorArquivo = valor_recebido + "\n";

        if (valor_recebido == 0) {
            apagaLEDs();
            apagaSetas();
            // Mantém apenas o LED central (marco zero) aceso
            if (imagens != null && imagens.length > 6 && imagens[6] != null) {
                imagens[6].setVisibility(View.VISIBLE);
            }
        } else if (valor_recebido > 0) {
            // Máquina deslocada para a DIREITA -> Acende blocos à direita do centro
            // Instrução: Corrigir para a ESQUERDA
            acende_direita(valor_recebido);
            acendeSetaEsquerda(valor_recebido);
        } else {
            // Máquina deslocada para a ESQUERDA -> Acende blocos à esquerda do centro
            // Instrução: Corrigir para a DIREITA
            acende_esquerda(valor_recebido);
            acendeSetaDireita(valor_recebido);
        }
    }

    public void apagaLEDs() {
        if (imagens == null) return;
        for (ImageView img : imagens) {
            if (img != null) img.setVisibility(View.INVISIBLE);
        }
    }

    public void apagaSetas() {
        if (setaE != null) setaE.setVisibility(View.INVISIBLE);
        if (setaD != null) setaD.setVisibility(View.INVISIBLE);
    }

    private void acende_direita(int valor) {
        apagaLEDs();
        int limite = 6 + valor;
        for (int i = 6; i <= limite && i < imagens.length; i++) {
            if (imagens[i] != null) imagens[i].setVisibility(View.VISIBLE);
        }
    }

    private void acende_esquerda(int valor) {
        apagaLEDs();
        int limite = 6 + valor; // valor é negativo
        for (int i = 6; i >= limite && i >= 0; i--) {
            if (imagens[i] != null) imagens[i].setVisibility(View.VISIBLE);
        }
    }

    private void acendeSetaDireita(int valor) {
        apagaSetas();
        if (setaD == null) return;
        setaD.setVisibility(View.VISIBLE);
        int absValor = Math.abs(valor);
        // Cores baseadas na gravidade do desalinhamento
        if (absValor <= 2) setaD.setImageResource(R.drawable.vd);      // Verde
        else if (absValor <= 4) setaD.setImageResource(R.drawable.am); // Amarelo
        else setaD.setImageResource(R.drawable.vm);                    // Vermelho
    }

    private void acendeSetaEsquerda(int valor) {
        apagaSetas();
        if (setaE == null) return;
        setaE.setVisibility(View.VISIBLE);
        int absValor = Math.abs(valor);
        if (absValor <= 2) setaE.setImageResource(R.drawable.vd);
        else if (absValor <= 4) setaE.setImageResource(R.drawable.am);
        else setaE.setImageResource(R.drawable.vm);
    }

    public String getValorArquivo() {
        return valorArquivo;
    }
}
