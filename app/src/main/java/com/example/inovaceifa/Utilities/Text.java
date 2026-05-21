package com.example.inovaceifa.Utilities;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

/**
 *  Classe cuja finalidade é ser responsável pelas mudanças de texto, tais quais implementação de
 *  negrito, itálico, cores etc.
 *
 * @author Gustavo Henrique Tostes
 * @version 1.1 - mar. 2023
 */
public class Text {
    private static final StyleSpan bold = new StyleSpan(Typeface.BOLD);
    private static final StyleSpan italic = new StyleSpan(Typeface.ITALIC);
    private static final StyleSpan boldItalic = new StyleSpan(Typeface.BOLD_ITALIC);

    /**
     * Método cuja finalidade é atribuir "sublinhado" ("underline") a toda uma String.
     * @param text Texto de entrada (sem sublinhado)
     * @return Texto de saída (sublinhado)
     */
    public static SpannableString underline(String text) {
        SpannableString texto = new SpannableString(text);
        texto.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        return texto;
    }

    /**
     * Método cuja finalidade é atribuir "sublinhado" ("underline") a um trecho de uma String (Ex:
     * na frase "Eu gosto de laranjas" apenas a palavra laranjas ser sublinhada).
     * @param texto String toda, contendo o trecho a ser utilizado (Ex: Eu gosto de laranjas).
     * @param trecho Trecho percentente à String, a ser re-caracterizado (Ex: laranjas).
     * @return Texto contendo o trecho re-caracterizado.
     */
    public static SpannableString underline(String texto, String trecho) {
        SpannableString spannableString = new SpannableString(texto);
        int percurso = texto.indexOf(trecho) + trecho.length();
        spannableString.setSpan(new UnderlineSpan(), texto.indexOf(trecho), percurso,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir "bold" e uma cor à apenas uma parcela de uma String.
     * @param text String toda, contendo o trecho a ser utilizado (Ex: Eu gosto de laranjas).
     * @param trecho Trecho percentente à String, a ser re-caracterizado (Ex: laranjas).
     * @param cor Numero hexadecimal representando a cor a ser atribuída.
     * @return Texto com as novas características.
     */
    public static SpannableString bold(String text, String trecho, int cor) {
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(cor);
        int percurso = text.indexOf(trecho.charAt(0)) + trecho.length();
        spannableString.setSpan(foregroundColorSpan, text.indexOf(trecho.charAt(0)),
                percurso, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(bold, text.indexOf(trecho.charAt(0)),
                percurso, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir "bold" a um trecho de uma String (Ex: na frase "Eu gosto de
     * laranjas" apenas a palavra laranjas ser "bold").
     * @param texto String toda, contendo o trecho a ser utilizado (Ex: Eu gosto de laranjas).
     * @param trecho Trecho percentente à String, a ser re-caracterizado (Ex: laranjas).
     * @return Texto contendo o trecho re-caracterizado.
     */
    public static SpannableString bold (String texto, String trecho) {
        SpannableString spannableString = new SpannableString(texto);
        int percurso = texto.indexOf(trecho) + trecho.length();
        spannableString.setSpan(bold, texto.indexOf(trecho), percurso, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir "bold" e uma cor à toda uma String.
     * @param texto String toda, contendo o trecho a ser utilizado (Ex: Eu gosto de laranjas).
     * @param cor Numero hexadecimal representando a cor a ser atribuída.
     * @return Texto com as novas características
     */
    public static SpannableString bold (String texto, int cor) {
        SpannableString spannableString = new SpannableString(texto);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(cor);
        spannableString.setSpan(foregroundColorSpan, 0, texto.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(bold, 0, texto.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir "bold" a toda uma String.
     * @param texto String a ser colocada em "bold" (Ex: Eu gosto de laranjas).
     * @return String re-caracterizada com "bold" em toda sua extensão.
     */
    public static SpannableString bold (String texto) {
        SpannableString spannableString = new SpannableString(texto);
        spannableString.setSpan(bold, 0, texto.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir "italic" e uma cor à apenas uma parcela de uma String.
     * @param text String toda, contendo o trecho a ser utilizado (Ex: Eu gosto de laranjas).
     * @param trecho Trecho percentente à String, a ser re-caracterizado (Ex: laranjas).
     * @param cor Numero hexadecimal representando a cor a ser atribuída.
     * @return Texto com as novas características.
     */
    public static SpannableString italic(String text, String trecho, int cor) {
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(cor);
        int percurso = text.indexOf(trecho.charAt(0)) + trecho.length();
        spannableString.setSpan(foregroundColorSpan, text.indexOf(trecho.charAt(0)),
                percurso, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(italic, text.indexOf(trecho.charAt(0)),
                percurso, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir "italic" a um trecho de uma String (Ex: na frase "Eu gosto de
     * laranjas" apenas a palavra laranjas ser "bold").
     * @param texto String toda, contendo o trecho a ser utilizado (Ex: Eu gosto de laranjas).
     * @param trecho Trecho percentente à String, a ser re-caracterizado (Ex: laranjas).
     * @return Texto contendo o trecho re-caracterizado.
     */
    public static SpannableString italic(String texto, String trecho) {
        SpannableString spannableString = new SpannableString(texto);
        int percurso = texto.indexOf(trecho) + trecho.length();
        spannableString.setSpan(italic, texto.indexOf(trecho), percurso, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir "italic" e uma cor à toda uma String.
     * @param texto String toda, contendo o trecho a ser utilizado (Ex: Eu gosto de laranjas).
     * @param cor Numero hexadecimal representando a cor a ser atribuída.
     * @return Texto com as novas características
     */
    public static SpannableString italic(String texto, int cor) {
        SpannableString spannableString = new SpannableString(texto);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(cor);
        spannableString.setSpan(foregroundColorSpan, 0, texto.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(italic, 0, texto.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir "italic" a toda uma String.
     * @param texto String a ser colocada em "italic" (Ex: Eu gosto de laranjas).
     * @return String re-caracterizada com "italic" em toda sua extensão.
     */
    public static SpannableString italic(String texto) {
        SpannableString spannableString = new SpannableString(texto);
        spannableString.setSpan(italic, 0, texto.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir "bold" e "italic e uma cor à apenas uma parcela de uma String.
     * @param text String toda, contendo o trecho a ser utilizado (Ex: Eu gosto de laranjas).
     * @param trecho Trecho percentente à String, a ser re-caracterizado (Ex: laranjas).
     * @param cor Numero hexadecimal representando a cor a ser atribuída.
     * @return Texto com as novas características.
     */
    public static SpannableString boldItalic(String text, String trecho, int cor) {
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(cor);
        int percurso = text.indexOf(trecho.charAt(0)) + trecho.length();
        spannableString.setSpan(foregroundColorSpan, text.indexOf(trecho.charAt(0)),
                percurso, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(boldItalic, text.indexOf(trecho.charAt(0)),
                percurso, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir "bold" e "italic" a um trecho de uma String (Ex: na frase "Eu gosto de
     * laranjas" apenas a palavra laranjas ser "bold" e "italic").
     * @param texto String toda, contendo o trecho a ser utilizado (Ex: Eu gosto de laranjas).
     * @param trecho Trecho percentente à String, a ser re-caracterizado (Ex: laranjas).
     * @return Texto contendo o trecho re-caracterizado.
     */
    public static SpannableString boldItalic(String texto, String trecho) {
        SpannableString spannableString = new SpannableString(texto);
        int percurso = texto.indexOf(trecho) + trecho.length();
        spannableString.setSpan(boldItalic, texto.indexOf(trecho), percurso, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir "bold" e "italic" e uma cor à toda uma String.
     * @param texto String toda, contendo o trecho a ser utilizado (Ex: Eu gosto de laranjas).
     * @param cor Numero hexadecimal representando a cor a ser atribuída.
     * @return Texto com as novas características
     */
    public static SpannableString boldItalic(String texto, int cor) {
        SpannableString spannableString = new SpannableString(texto);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(cor);
        spannableString.setSpan(foregroundColorSpan, 0, texto.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(boldItalic, 0, texto.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir "bold" e "italic" a toda uma String.
     * @param texto String a ser colocada em "bold" e "italic" (Ex: Eu gosto de laranjas).
     * @return String re-caracterizada com "bold" e "italic" em toda sua extensão.
     */
    public static SpannableString boldItalic(String texto) {
        SpannableString spannableString = new SpannableString(texto);
        spannableString.setSpan(boldItalic, 0, texto.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir uma cor à apenas uma parcela de uma String.
     * @param text String toda, contendo o trecho a ser utilizado (Ex: Eu gosto de laranjas).
     * @param trecho Trecho percentente à String, a ser re-caracterizado (Ex: laranjas).
     * @param cor Numero hexadecimal representando a cor a ser atribuída.
     * @return Texto com as novas características.
     */
    public static SpannableString cor(String text, String trecho, int cor) {
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(cor);
        int percurso = text.indexOf(trecho.charAt(0)) + trecho.length();
        spannableString.setSpan(foregroundColorSpan, text.indexOf(trecho.charAt(0)),
                percurso, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Método cuja finalidade é atribuir uma cor à toda uma String.
     * @param texto String toda, contendo o trecho a ser utilizado (Ex: Eu gosto de laranjas).
     * @param cor Numero hexadecimal representando a cor a ser atribuída.
     * @return Texto com as novas características
     */
    public static SpannableString cor(String texto, int cor) {
        SpannableString spannableString = new SpannableString(texto);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(cor);
        spannableString.setSpan(foregroundColorSpan, 0, texto.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
