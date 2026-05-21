package com.example.inovaceifa.Utilities;

import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class LayoutElements {

    /**
     * Controla a visibilidade de um Constraint Layout.
     * @param constraintLayout Constraint Layout "alvo".
     * @param visibility Código de visibilidade a ser utilizado (GONE, VISIBLE, etc.).
     */
    public static void constraintVisibility(ConstraintLayout constraintLayout, int visibility) {
        constraintLayout.setVisibility(visibility);
    }

    /**
     * Controla a visibilidade de uma TextView.
     * @param textView TextView "alvo".
     * @param visibility Código de visibilidade a ser utilizado.
     */
    public static void textViewVisibility(TextView textView, int visibility) {
        textView.setVisibility(visibility);
    }

    /**
     * Controla o texto a ser disposto em uma TextView.
     * @param textView TextView "alvo".
     * @param text String contendo o texto a ser disposto
     */
    public static void textViewText(TextView textView, String text) {
        textView.setText(text);
    }

    /**
     * Controla o texto a ser disposto em uma TextView.
     * @param textView TextView "alvo".
     * @param resid Código contendo a String a ser utilizada na TextView (disposta em R.string.x).
     */
    public static void textViewText(TextView textView, int resid) {
        textView.setText(resid);
    }
}
