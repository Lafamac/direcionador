package com.example.inovaceifa.Utilities;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Classe relativa à conversões envolvendo atributos/variáveis numéricas, tais quais conversões,
 * multiplicações "difíceis", etc.
 *
 * @author Gustavo Henrique Tostes
 * @version 1.0 - 12/01/2023
 */
public class MMath {

    /**
     * Conversão de um numéro do tipo Float para uma String contendo apenas 1 casa decimal.
     * @param num Número a ser convertido;
     * @return Número convertido.
     */
    public static String convert1Dec(float num) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
        nf.setMaximumFractionDigits(1);
        nf.setMinimumFractionDigits(1);
        return nf.format(num);
    }

    /**
     * Conversão de um numéro do tipo {@link Float} para uma {@link String} contendo apenas 2 casas decimais.
     * @param num Número a ser convertido;
     * @return Número convertido.
     */
    public static String converter2Dec(float num) {
        //DecimalFormat df = new DecimalFormat("###,##0.00");
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(num);
    }

    /**
     * Conversão de um numéro do tipo {@link Double} para uma {@link String} contendo apenas 2 casas decimais.
     * @param num Número a ser convertido;
     * @return Número convertido.
     */
    public static String converter2Dec(double num) {
        //DecimalFormat df = new DecimalFormat("###,##0.00");
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(num);
    }
    /**
     * Conversão de inteiro para número de IP (formato)
     * @param addr Inteiro cujo formato deseja-se alterar para IP
     * @return Inteiro convertido para formato de número de IP.
     */
    public static String intParaIp(int addr) {
        return ((addr & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF));
    }

    /**
     * Conversão de um número contido em uma String para um número Inteiro. Utilizado dessa forma
     * para fugir da implementação de try/catch em todas as aplicações de conversão.
     * @param num Número, em formato de String, a ser convertido.
     * @param falha Valor a ser retornado em caso de falha na conversão.
     * @return Número inteiro (ou valor de falha).
     */
    public static int StringParaInt(String num, int falha) {
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException n) {
            Log.e("Exception", "Não foi possivel converter a String");
            return falha;
        }
    }

    /**
     * Conversão de um número contido em uma String para um número Float. Utilizado dessa forma
     * para fugir da implementação de try/catch em todas as aplicações de conversão.
     * @param num Número, em formato de String, a ser convertido.
     * @param falha Valor a ser retornado em caso de falha na conversão.
     * @return Número inteiro (ou valor de falha).
     */
    public static float StringParaFloat(String num, float falha) {
        try {
            return Float.parseFloat(num);
        } catch (Exception e) {
            Log.e("Exception", "Não foi possivel converter a String");
            return falha;
        }
    }

    public static float StringparaFloat(String num) {
        int index = num.indexOf(" ");
        String novo = "";
        for (int i = 0; i < index; i++) {
            novo = novo + num.charAt(i);
        }
        return Float.parseFloat(novo);
    }

    /**
     * Valor absoluto de um número, verificando se o mesmo já é positivo ou não. Caso seja, não faz
     * nada.
     * @param num Número de tipo inteiro ao qual se quer obter o absoluto.
     * @return Valor absoluto do número.
     */
    public static int abs(int num) {
        if (num > 0) {
            return num;
        } else {
            return num*-1;
        }
    }

    /**
     * Valor absoluto de um número, verificando se o mesmo já é positivo ou não. Caso seja, não faz
     * nada.
     * @param num Número de tipo flutuante ao qual se quer obter o absoluto.
     * @return Valor absoluto do número.
     */
    public static float abs(float num) {
        if (num > 0) {
            return num;
        } else {
            return num*-1;
        }
    }

    /**
     * Soma de dois números, considerando a possibilidade de um ser negativo ou n]ap
     * @param num1 Primeiro número da soma
     * @param num2 Segundo número da soma
     * @return Soma dos dois números.
     */
    public static int soma(int num1, int num2) {
        if (num1 < 0) {
            return num1 - num2;
        } else {
            return num1 + num2;
        }
    }

    /**
     * Subtração de dois números, considerando a possibilidade de um ser negativo ou não. Sendo
     * negativo, faz a subtração dos absolutos (Ex: -4 + 2 = -2). Sendo positivo, faz a subtração
     * comum (Ex: 4 - 2 = 2);
     * @param num1 Primeiro número da subtração;
     * @param num2 Segundo número da subtração
     * @return Subtração dos dois números
     */
    public static int subtracao(int num1, int num2) {
        if (num1 < 0) {
            return num1 + num2;
        } else {
            return num1 - num2;
        }
    }

    /**
     * Arredondamento de um número real, obedecendo as seguintes regras:
     * Número Par: qualquer número cuja casa decimal seja 0.5 ou superior é arredondado para cima.
     * Número Ímpar: qualquer número cuja casa decimal seja 0.6 ou superior é arredondado para cima.
     * @param num Número a ser arredondado.
     * @return Número inteiro arredondado.
     */
    public static int round(float num) {
        int aux = (int) num;
        float sub = num-aux;

        if (aux %2 == 0) {
            if (sub > 0.5) {
                return (int) (aux+1);
            } else {
                return (int) aux;
            }
        } else {
            if (sub >= 0.5) {
                return (int) (aux+1);
            } else {
                return (int) aux;
            }
        }
    }
}
