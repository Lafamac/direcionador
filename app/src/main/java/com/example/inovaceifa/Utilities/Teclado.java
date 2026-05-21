package com.example.inovaceifa.Utilities;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Classe responsável por gerenciar o teclado (digitação de dados)
 */
public class Teclado {
    /**
     * Fecha o teclado presente em uma Activity.
     * @param activity Activity na qual o teclado está sendo mostrado.
     */
    public static void fecharTeclado(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }
}
