package com.example.inovaceifa.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.example.inovaceifa.R;

/**
 * Classe que implementa uma caixa de aviso (erro) utilizando um AlertDialog. Contém dois métodos,
 * cuja diferença entre eles reside nos seus parâmetros, onde o primeiro possui apenas uma String,
 * enquanto o segundo possui duas Strings. Isso ocorre devido ao fato de o layout pertencente conter
 * duas TextView, ponderando quando seu uso é necessário.
 *
 * @author Gustavo Henrique Tostes
 * @version 1.0 - 10/01/2023
 */
public class DialogWarning extends AppCompatDialogFragment {

    private DialogWarningListener listener;        // Objeto do listener para uso dos métodos

    @Override
    public Dialog onCreateDialog (Bundle savedInstance) {
        AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.dialog_warning, null);
        ad.setView(subView);

        TextView tituloErro = subView.findViewById(R.id.warnBox_title);
        TextView msg1 = subView.findViewById(R.id.warnBox_msg1);
        TextView msg2 = subView.findViewById(R.id.warnBox_msg2);

        listener.setTextViewTexts(tituloErro, msg1, msg2);
        listener.setTextViewVisibilities(tituloErro, msg1, msg2);

        final Button warnBox_button = subView.findViewById(R.id.warnBox_button);

        warnBox_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClicked();
                ad.dismiss();
            }
        });

        return ad;
    }

    public interface DialogWarningListener {
        /**
         * Configura o que ocorre ao clicar no botão presente na DialogBox em questão.
         */
        void onButtonClicked();

        /**
         * Configura os textos pertencentes às TextViews, sendo o texto de título da mesma, e as
         * mensagens correspondentes.
         *
         * @param title TextView correspondente ao título da DialogBox
         * @param msg1 TextView correspondente à primeira mensagem a ser disposta na DialogBox
         * @param msg2 TextView correspondente à segunda mensagem a ser disposta na DialogBox
         */
        void setTextViewTexts(TextView title, TextView msg1, TextView msg2);

        /**
         * Configura as visibilidades pertencentes às TextViews, sendo o texto de título da mesma,
         * e as mensagens correspondentes.
         * <p>
         *     Default: VISIBLE, VISIBLE, VISIBLE
         * </p>
         * @param title TextView correspondente ao título da DialogBox
         * @param msg1 TextView correspondente à primeira mensagem a ser disposta na DialogBox
         * @param msg2 TextView correspondente à segunda mensagem a ser disposta na DialogBox
         */
        void setTextViewVisibilities(TextView title, TextView msg1, TextView msg2);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogWarningListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + "must implement CaixaAvisoListener");
        }
    }
}
