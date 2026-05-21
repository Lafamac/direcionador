package com.example.inovaceifa.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.inovaceifa.R;

public class DialogInsert extends AppCompatDialogFragment {

    private DialogInsertListener listener;          // Objeto do listener para uso dos métodos

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.dialog_insert, null);
        ad.setView(subView);

        EditText campo1 = subView.findViewById(R.id.insercao_campo1);
        EditText campo2 = subView.findViewById(R.id.insercao_campo2);
        EditText campo3 = subView.findViewById(R.id.insercao_campo3);

        listener.setTitleInsertDialog(ad);

        listener.setVisibilitiesInsertDialog(campo1, campo2, campo3);
        listener.setHintsInsertDialog(campo1, campo2, campo3);
        listener.setInputTypesInsertDialog(campo1, campo2, campo3);

        ad.setButton(DialogInterface.BUTTON_POSITIVE, "Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String texto1 = "";
                String texto2 = "";
                String texto3 = "";

                if (campo1.getVisibility() == View.VISIBLE) {
                    texto1 = campo1.getText().toString();
                    System.out.println(texto1);
                }

                if (campo2.getVisibility() == View.VISIBLE) {
                    texto2 = campo2.getText().toString();
                }

                if (campo3.getVisibility() == View.VISIBLE) {
                    texto3 = campo3.getText().toString();
                }

                listener.onConfirmClickedInsertDialog(texto1, texto2, texto3);

                ad.dismiss();
            }
        });

        ad.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onCancelClickedInsertDialog();
                ad.dismiss();
            }
        });


        return ad;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogInsert.DialogInsertListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement CaixaConfirmacaoListener");
        }
    }

    public interface DialogInsertListener {
        /**
         * Chamado quando se deseja utilizar os dados digitados pelo usuário nas caixas de seleção
         * @param textDialog1 Texto da EditText1, digitado pelo usuário;
         * @param textDialog2 Texto da EditText2, digitado pelo usuário;
         * @param textDialog3 Texto da EditText3, digitado pelo usuário;
         * @// FIXME: 17/01/2023
         */
        void onConfirmClickedInsertDialog(String textDialog1, String textDialog2, String textDialog3);

        /**
         * Chamado quando se deseja configurar o clique para o botão de cancelar da AlertDialog
         */
        void onCancelClickedInsertDialog();

        /**
         * Configuração da visibilidade das EditText presentes na AlertDialog em questão.
         * <p>
         *     Default: VISIBLE, VISIBLE, VISIBLE
         * </p>
         * @param editText1 primeira EditText;
         * @param editText2 segunda EditText;
         * @param editText3 terceira EditText;
         */
        void setVisibilitiesInsertDialog(EditText editText1, EditText editText2, EditText editText3);

        /**
         * Configuração das Hints das EditText, ou seja, o texto que irá ser mostrado para o usuário
         * quando a AlertDialog não possui conteúdo.
         * @param editText1 primeira EditText;
         * @param editText2 segunda EditText;
         * @param editText3 terceira EditText;
         */
        void setHintsInsertDialog(EditText editText1, EditText editText2, EditText editText3);

        /**
         * Configuração do tipo de entrada de dados das EditText a serem utilizadas.
         * <p>
         *     Default: Text
         * </p>
         *
         * @param editText1 primeira EditText;
         * @param editText2 segunda EditText;
         * @param editText3 terceira EditText;
         */
        void setInputTypesInsertDialog(EditText editText1, EditText editText2, EditText editText3);

        /**
         * Configuração do título da AlertDialog a ser utilizada.
         * @param ad Objeto da AlertDialog para configuração.
         */
        void setTitleInsertDialog(AlertDialog ad);
    }
}
