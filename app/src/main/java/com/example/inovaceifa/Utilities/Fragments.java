package com.example.inovaceifa.Utilities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.inovaceifa.R;

/**
 * Classe cujo obetivo é cuidar de tudo aquilo relacionado à configuração de Fragments, como abrir,
 * fechar, etc.
 *
 * @author Gustavo Tostes
 * @version 1.0 - feb. 2023
 */
public class Fragments {

    /**
     * Abre um Fragment, considerando que alguma informação deva ser passada como parâmetro.
     * @param fragment Fragment a ser aberto
     * @param fm Objeto "extraido" do getFragmentManager
     * @param view View a ser substituida pelo Fragment
     * @param bundle Objeto contendo todas as informações a serem passadas para a Fragment
     */
    public static void abrirFragment(Fragment fragment, FragmentManager fm, int view, Bundle bundle) {
        FragmentTransaction ft = fm.beginTransaction();
        fragment.setArguments(bundle);
        ft.replace(view, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Abrir um Fragment, sem passar informações.
     * @param fragment Fragment a ser aberto
     * @param fm Objeto "extraido" do getFragmentManager
     * @param view View a ser substituida pelo Fragment
     */
    public static void abrirFragment(Fragment fragment, FragmentManager fm, int view) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(view, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
