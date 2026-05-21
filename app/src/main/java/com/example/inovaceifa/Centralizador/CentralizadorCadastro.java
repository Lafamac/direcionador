package com.example.inovaceifa.Centralizador;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inovaceifa.Utilities.DialogWarning;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.inovaceifa.Utilities.HelperDatabaseSQL;
import com.example.inovaceifa.Utilities.DialogInsert;
import com.example.inovaceifa.Utilities.Teclado;
import com.example.inovaceifa.R;

import java.util.ArrayList;

/**
 * Activity onde ocorre o cadastro de glebas no sistema (banco de dados).
 *
 * @version 1.0 - 02/01/2022
 * @author Gustavo Tostes
 */
public class CentralizadorCadastro extends AppCompatActivity implements DialogInsert.DialogInsertListener {

    /**
     * {@link ArrayList<Operador>} - Array contendo todas as glebas já cadastradas no sistema.
     */
    private static ArrayList<Operador> allGlebas = new ArrayList<>();

    /**
     * {@link Integer} - Número máximo de glebas a serem cadastradas no sistema.
     */
    private static final int maximo_Glebas = 50;

    /**
     * {@link HelperDatabaseSQL} - Objeto da database para utilização dos métodos inerentes à mesma.
     */
    private HelperDatabaseSQL helper;

    /**
     * {@link AdapterOperadorBox} - Adapter para utilização dos cards das glebas cadastradas.
     */
    private AdapterOperadorBox adapter;

    /**
     * {@link RecyclerView} - View para inserção/visualização dos cards das glebas já cadastradas.
     */
    private static RecyclerView recyclerView;

    /**
     * {@link TextView} - Texto indicando ao usuário quando nenhuma gleba está cadastrada e o que o mesmo
     * deve fazer na situação em questão.
     */
    private static TextView nenhuma_gleba;

    /**
     * {@link FloatingActionButton} - Botão para retornar à pàgina anterior.
     */
    private FloatingActionButton voltar;

    /**
     * {@link Button} - Botão para a realização do cadastro de uma nova gleba no sistema.
     */
    private Button cadastro;

    /**
     * {@link SearchView} - Campo de busca para o usuário encontrar uma gleba requerida.
     */
    private SearchView searchView;

    /**
     * {@link FragmentManager} - Objeto para utilização da fragment dentro da {@link CentralizadorCadastro#recyclerView}.
     */
    private FragmentManager fragmentManager;

    private String formaDeAcesso;

    private ImageButton botaoApagar;

    private View operadorBox;

    private View textoCadastro;

    private View textoOperador;

    private boolean deletaBotao;

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centralizador_cadastro);

        iniciaComponentes();

        fragmentManager = getSupportFragmentManager();

        helper = HelperDatabaseSQL.getInstance(this);

        configRecyclerView();

        allGlebas = helper.listarTodasOperadors();

        configInicial();

        configCadastro();

        configSearchView();

        configVoltar();
    }

    /**
     * Inicializa os componentes através do método {@link android.app.Activity#findViewById(int)}.
     */
    private void iniciaComponentes() {
        recyclerView = findViewById(R.id.listaOperadores);
        nenhuma_gleba = findViewById(R.id.gerCad_txt);
        cadastro = findViewById(R.id.cadastrarOperador);
        searchView = findViewById(R.id.campoBuscaGer);
        voltar = findViewById(R.id.gerCad_fab);
        botaoApagar = findViewById(R.id.gerCad_apagar);
        textoCadastro = findViewById(R.id.txt_gerCad1);
        textoOperador = findViewById(R.id.txt_operador);

        formaDeAcesso = (String) getIntent().getSerializableExtra("formaAcesso");

        assert formaDeAcesso != null;
        if(formaDeAcesso.equals("admin"))
        {
            deletaBotao = false;
        } else if(formaDeAcesso.equals("operador"))
        {
            deletaBotao = true;
        }
    }

    /**
     * Configura os parâmetros referentes à {@link CentralizadorCadastro#recyclerView}.
     */
    private void configRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    /**
     * Configurações iniciais da Activity, englobando visibilidades, textos, etc.
     */
    private void configInicial() {
        if (!allGlebas.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            nenhuma_gleba.setVisibility(View.GONE);
            adapter = new AdapterOperadorBox(this, helper, allGlebas, fragmentManager, deletaBotao);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            nenhuma_gleba.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Configura os parâmetros referentes ao {@link CentralizadorCadastro#cadastro}.
     */
    private void configCadastro() {
        if(formaDeAcesso.equals("admin"))
        {
            cadastro.setVisibility(View.VISIBLE);
            textoCadastro.setVisibility(View.VISIBLE);
            textoOperador.setVisibility(View.GONE);
            cadastro.setOnClickListener(v -> {
                DialogInsert di = new DialogInsert();
                di.show(getSupportFragmentManager(), "Add operador");
            });
        }
        else
        {
            cadastro.setVisibility(View.GONE);
            textoCadastro.setVisibility(View.GONE);
            textoOperador.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Configura os parâmetros referentes ao {@link CentralizadorCadastro#searchView}.
     */
    private void configSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Teclado.fecharTeclado(CentralizadorCadastro.this);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Configura os parâmetros referentes ao {@link CentralizadorCadastro#voltar}.
     */
    private void configVoltar() {
        voltar.setOnClickListener(v -> finish());
    }

    /**
     * Verificação para saber o próximo número a ser inserido como "título" da gleba. Atenta-se ao limite
     * estabelecido, definido por {@link CentralizadorCadastro#maximo_Glebas}.
     * @return {@link Integer} contendo o próximo número a ser inserido.
     */
    private int verificarNumero() {
        int numero = 1;

        while (numero <= maximo_Glebas) {
            Cursor cursor = helper.searchNumber(""+numero);
            cursor.moveToFirst();

            if (cursor.getCount() == 0) {
                cursor.close();
                break;
            }

            cursor.close();
            numero++;
        }

        return numero;
    }

    /**
     * Método para realizar a "troca de tela". Consiste em uma mensagem diferente ao usuário
     * para quando não há glebas cadastradas, representado por {@link CentralizadorCadastro#nenhuma_gleba}.
     */
    public static void changeScreen() {
        if (allGlebas.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            nenhuma_gleba.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            nenhuma_gleba.setVisibility(View.VISIBLE);
        }
    }

    //-------------------------------- Insert Dialog Methods ---------------------------------------
    @Override
    public void onConfirmClickedInsertDialog(String textDialog1, String textDialog2, String textDialog3) {
        int numero = verificarNumero();
        if (numero > maximo_Glebas) {
            Toast.makeText(CentralizadorCadastro.this, "Limite atingido", Toast.LENGTH_LONG).show();
        } else {
            Operador novoOperador = new Operador();

            if (numero < 10) {
                novoOperador.setNumero("0"+numero);
            } else {
                novoOperador.setNumero(""+numero);
            }
            novoOperador.setLocal(textDialog1);

            helper.addOperador(novoOperador);

            allGlebas = helper.listarTodasOperadors();
            if (!allGlebas.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                nenhuma_gleba.setVisibility(View.GONE);
                adapter = new AdapterOperadorBox(CentralizadorCadastro.this, helper, allGlebas, fragmentManager, true);
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                nenhuma_gleba.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCancelClickedInsertDialog() {
        Toast.makeText(CentralizadorCadastro.this, "Cancelado", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setVisibilitiesInsertDialog(EditText editText1, EditText editText2, EditText editText3) {
        editText2.setVisibility(View.GONE);
        editText3.setVisibility(View.GONE);
    }

    @Override
    public void setHintsInsertDialog(EditText editText1, EditText editText2, EditText editText3) {
        editText1.setHint("Digite o nome do operador");
    }

    @Override
    public void setInputTypesInsertDialog(EditText editText1, EditText editText2, EditText editText3) {
    }

    @Override
    public void setTitleInsertDialog(AlertDialog ad) {
        ad.setTitle("Adicionar um novo Operador");
    }

    @Override
    protected void onResume() {
        super.onResume();

        allGlebas = helper.listarTodasOperadors();

        if (!allGlebas.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            nenhuma_gleba.setVisibility(View.GONE);
            adapter = new AdapterOperadorBox(this, helper, allGlebas, fragmentManager, deletaBotao);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            nenhuma_gleba.setVisibility(View.VISIBLE);
        }
    }
}