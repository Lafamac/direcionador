package com.example.inovaceifa.Centralizador;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.example.inovaceifa.Utilities.HelperDatabaseSQL;
import com.example.inovaceifa.Utilities.Imagem;
import com.example.inovaceifa.Utilities.Text;
import com.example.inovaceifa.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Adaptador para ser utilizado em conjunto com o RecyclerView.
 *
 * @version 1.1 - 09/01/2022
 * @author Gustavo Henrique Tostes
 */
public class AdapterOperadorBox extends RecyclerView.Adapter<AdapterOperadorBox.WordViewHolder> implements Filterable {

    /**
     * Classe para o Holder, sendo que a mesma contem os itens relativos à "box" que aparece no
     * RecyclerView
     *
     * @author Gustavo Henrique Tostes
     * @version 1.0 - 20/12/2022
     */
    class WordViewHolder extends RecyclerView.ViewHolder {
        /**
         * {@link TextView} - Marca o número da Operador (<i>Ex: Operador 01 // Operador 02...</i>)
         */
        TextView nome;
        /**
         * {@link TextView} - Marca o local da gleba, a ser inserido pelo usuário.
         */
        TextView local;
        /**
         * {@link TextView} - Marca a data que deve ser realizada uma próxima medição.
         */
        TextView data;
        /**
         * {@link Button} - Permite acesso do usuário aos detalhes da Operador (Activity: {@link CentralizadorTelaOperador}).
         */
        Button detalhes;
        /**
         * {@link ImageButton} - Permite deletar a gleba do sistema.
         */
        ImageButton apagar;
        /**
         * {@link ShapeableImageView} - Mostra a imagem associada à gleba (<i>Caso o usuário não tenha inserido alguma.
         * uma imagem default é apresentada</i>).
         */
        ShapeableImageView foto;

        public WordViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.gerCad_num);
            local = itemView.findViewById(R.id.centSens_ajuste);
            detalhes = itemView.findViewById(R.id.gerCad_detalhes);
            apagar = itemView.findViewById(R.id.gerCad_apagar);
            foto = itemView.findViewById(R.id.gerCad_foto);
        }
    }

    /**
     * {@link LayoutInflater} - <i>Infla</i> o layout definido para a classe em questão.
     */
    private final LayoutInflater inflater;

    /**
     * {@link HelperDatabaseSQL} - Helper do banco de dados para utilização dos métodos inerentes ao mesmo.
     */
    HelperDatabaseSQL helper;

    /**
     * {@link Context} - Contexto da aplicação (<i>Página onde o usuário se encontra</i>).
     */
    Context context;

    /**
     * {@link ArrayList<Operador>} - Contém todas as glebas cadastradas no sistema.
     */
    ArrayList<Operador> listGlebas;

    /**
     * {@link HelperDatabaseSQL} - Contém todas as glebas cadastradas no sistema (cópia).
     */
    ArrayList<Operador> mArrayList;

    /**
     * {@link FragmentManager} - Manager da Fragment para devidas mudanças de tela e afins.
     */
    FragmentManager fm;

    /**
     * {@link Operador} - Objeto da gleba manipulada durante a operação.
     */
    Operador gleba;

    boolean deleteButtonVisible;

    private final String TAG = "gerDebug";

    /**
     * Construtor para o adaptador.
     * @param context Contexto da aplicação (de onde o construtor é chamado).
     * @param help Instância do helper, dado que o mesmo é criado apenas uma vez.
     * @param listGlebas Lista de glebas, podendo ser a filtrada ou a completa.
     * @param fm Objeto do FragmentManager, para gerenciar a aplicação
     * @param deleteButtonVisible Verifica se o botão de deletar gleba deve estar presente ou não
     */
    public AdapterOperadorBox(Context context, HelperDatabaseSQL help, ArrayList<Operador> listGlebas,
                           FragmentManager fm, boolean deleteButtonVisible) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listGlebas = listGlebas;
        this.mArrayList = listGlebas;
        helper = help;
        this.fm = fm;
        this.deleteButtonVisible = deleteButtonVisible;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.activity_adapter_operador_box, parent, false);

        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        gleba = listGlebas.get(position);

        holder.apagar.setVisibility(deleteButtonVisible ? View.GONE : View.VISIBLE);
        
        String texto_textView;

        texto_textView = "Operador " + gleba.getNumero();
        holder.nome.setText(Text.underline(texto_textView));

        texto_textView = gleba.getLocal();
        holder.local.setText(texto_textView);

        Imagem image = new Imagem();
        if (gleba.getImagemPath() != null) {
            holder.foto.setImageBitmap(image.pathToImage(gleba.getImagemPath()));
        }

        holder.detalhes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                String numeroOperador = (listGlebas.get(holder.getAbsoluteAdapterPosition())).getNumero();
                Intent intent = new Intent(context, CentralizadorTelaOperador.class);
                intent.putExtra("numeroOperador", numeroOperador);
                intent.putExtra("cadastro", deleteButtonVisible);
                intent.putExtra("activity", context.getClass().getSimpleName());
                ((Activity) context).startActivity(intent);
            }
        });

        holder.nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numeroOperador = (listGlebas.get(holder.getAbsoluteAdapterPosition())).getNumero();
                Intent intent = new Intent(context, CentralizadorTelaOperador.class);
                intent.putExtra("numeroOperador", numeroOperador);
                intent.putExtra("cadastro", deleteButtonVisible);
                ((Activity) context).startActivity(intent);
            }
        });

        holder.apagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gleba = listGlebas.get(holder.getAdapterPosition());

                showDialogDelete(gleba, holder.getAbsoluteAdapterPosition());
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    listGlebas = mArrayList;
                } else {
                    ArrayList<Operador> lista_filtrada = new ArrayList<>();

                    for (Operador gleba : mArrayList) {
                        if ((gleba.getLocal().toLowerCase().contains(charString.toLowerCase())) ||
                                (gleba.getNumero().toLowerCase().contains(charString.toLowerCase())) ||
                                ("Operador " + (gleba.getNumero())).toLowerCase().contains(charString.toLowerCase())){
                            lista_filtrada.add(gleba);
                        }
                    }

                    listGlebas = lista_filtrada;
                }
                FilterResults resultado = new FilterResults();
                resultado.values = listGlebas;
                return resultado;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listGlebas = (ArrayList<Operador>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    /**
     * Conta a quantidade de glebas presente na <i>tela</i> do usuário (variando devido o recyclerView).
     * @return {@link Integer} contendo o número de glebas.
     */
    @Override
    public int getItemCount() {
        return listGlebas.size();
    }

    /**
     * Implementa uma AlertDialog para confirmar se o usuário realmente deseja excluir a gleba em
     * questão.
     * @param glebaDeletar Operador a ser deletada;
     * @param position Posição atual do adapter (no momento do click no botão).
     */
    private void showDialogDelete(Operador glebaDeletar, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.dialog_confirm, null);

        final TextView titulo_Dialog = subView.findViewById(R.id.tituloConf);
        final TextView descricao_Dialog = subView.findViewById(R.id.mensagemConfirmacao);
        final Button cancel_Dialog = subView.findViewById(R.id.cancelConfirmacao);
        final Button confirm_Dialog = subView.findViewById(R.id.confConfirmacao);

        titulo_Dialog.setText(R.string.tituloDeletarDados);
        descricao_Dialog.setText(R.string.deletarDados);

        AlertDialog dialog2 = new AlertDialog.Builder(context).create();
        dialog2.setView(subView);

        cancel_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });

        confirm_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.deleteOneOperador(glebaDeletar);

                listGlebas.remove(glebaDeletar);
                mArrayList.remove(glebaDeletar);

                notifyItemRemoved(position);
                CentralizadorCadastro.changeScreen();

                dialog2.dismiss();
            }
        });

        dialog2.show();
    }

    /**
     * Verifica a data da próxima medição caso já tenham sido feitas medições e o resultado nao foi
     * colheita imediatada. Caso nenhuma medição tenha sido feita ou não seja colheita imediata, não
     * há retorno.
     * @param quando {@link String} contendo o atributo da classe {@link Operador} de quando colher.
     * @return {@link String} contendo a data da proxima colheita.
     */
    private String verificaData(String quando) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(Objects.requireNonNull(sdf.parse(date)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        calendar.add(Calendar.DATE, 21);

        Log.d(TAG, sdf.format(calendar.getTime()));

        if (quando == null) {
            return "";
        } else if (quando.equals("0")) {
            return "";
        } else {
            switch (quando) {
                case "7":
                    calendar.add(Calendar.DATE, 7);
                    break;
                case "14":
                    calendar.add(Calendar.DATE, 14);
                    break;
                case "21":
                    calendar.add(Calendar.DATE, 21);
                    break;
            }

            return sdf.format(calendar.getTime());
        }
    }
}
