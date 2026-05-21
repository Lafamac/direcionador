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
 * Classe que implementa uma AlertDialog cuja finalidade é servir de confirmação para o usuário, tal
 * qual confirmação para saber se o usuário quer realmente sair da página, confirmação para saber se
 * o usuário quer realmente deletar algo, etc..
 *
 * @author Gustavo Henrique Tostes
 * @version 1.0 - 16/01/2023
 */

public class DialogConfirm extends AppCompatDialogFragment {

    private DialogConfirmListener listener;          // Objeto do listener para uso dos métodos

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.dialog_confirm, null);
        ad.setView(subView);

        TextView titulo_Dialog = subView.findViewById(R.id.tituloConf);
        TextView descricao_Dialog = subView.findViewById(R.id.mensagemConfirmacao);
        final Button cancel_Dialog = subView.findViewById(R.id.cancelConfirmacao);
        final Button confirm_Dialog = subView.findViewById(R.id.confConfirmacao);

        listener.setTextViewTexts(titulo_Dialog, descricao_Dialog);
        listener.setTextViewVisibilities(titulo_Dialog, descricao_Dialog);

        cancel_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelClickedConfirmDialog();
                ad.dismiss();
            }
        });

        confirm_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirmClickedConfirmDialog();
            }
        });

        return ad;
    }

    public interface DialogConfirmListener {
        /**
         * Chamado quando se deseja atribuir uma funcionalidade ao botão cujo id é "confConfirmacao",
         * podendo o mesmo ser reconhecido como um positive button.
         */
        void onCancelClickedConfirmDialog();

        /**
         * Chamado quando se deseja atribuir uma funcionalidade ao botão cujo id é "cancelConfirmacao",
         * podendo o mesmo ser reconhecido como um negative button.
         */
        void onConfirmClickedConfirmDialog();

        /**
         *Configura os textos pertencentes às TextViews, sendo o texto de título da mesma, e a
         * mensagem correspondente.
         * @param title TextView correspondente ao título da DialogBox
         * @param msg TextView correspondente à mensagem a ser disposta na DialogBox
         */
        void setTextViewTexts(TextView title, TextView msg);

        /**
         * Configura as visibilidades pertencentes às TextViews, sendo o texto de título da mesma, e
         * a mensagem correspondente.
         * <p>
         *     Default: VISIBLE, VISIBLE
         * </p>
         * @param title TextView correspondente ao título da DialogBox
         * @param msg TextView correspondente à mensagem a ser disposta na DialogBox
         */
        void setTextViewVisibilities(TextView title, TextView msg);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogConfirmListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement CaixaConfirmacaoListener");
        }
    }
}
