package com.example.inovaceifa.Centralizador;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inovaceifa.R;
import com.example.inovaceifa.Utilities.DialogInsert;
import com.example.inovaceifa.Utilities.HelperDatabaseSQL;

/**
 * Classe para mostrar ao usuário uma introdução do Centralizador da Colhedora.
 *
 * @author Gustavo Henrique Tostes
 * @version 1.1
 */
public class CentralizadorPrincipal extends AppCompatActivity implements DialogInsert.DialogInsertListener {

    private HelperDatabaseSQL centPrinHelper;
    private TextView textoPrincipal;
    private Button continuar;
    private Button cadastro;
    private int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Se o app fechar aqui, certifique-se que o namespace no Gradle é com.example.inovaceifa
            setContentView(R.layout.activity_centralizador_principal);
        } catch (Exception e) {
            e.printStackTrace();
        }

        centPrinHelper = HelperDatabaseSQL.getInstance(CentralizadorPrincipal.this);

        iniciarComponentes();
        configSistemaOp();
        configCadastro();
    }

    /**
     * Inicializa os componentes e evita crash caso o ID não seja encontrado.
     */
    private void iniciarComponentes() {
        textoPrincipal = findViewById(R.id.centPrin_explicacao);
        continuar = findViewById(R.id.centPrin_continuar);
        cadastro = findViewById(R.id.centPrin_admin);
    }

    /**
     * Configura o acesso para modo Operador.
     */
    private void configSistemaOp() {
        if (continuar != null) {
            continuar.setOnClickListener(v -> {
                Intent intent = new Intent(this, CentralizadorCadastro.class);
                intent.putExtra("formaAcesso", "operador");
                startActivity(intent);
            });
        }
    }

    /**
     * Configura o acesso para modo Administrador.
     */
    private void configCadastro() {
        if (cadastro != null) {
            cadastro.setOnClickListener(v -> {
                DialogInsert di = new DialogInsert();
                di.setCancelable(false);
                di.show(getSupportFragmentManager(), "password page");
            });
        }
    }

    /**
     * Navegação baseada no estado do banco de dados.
     */
    private void passarPagina() {
        Intent intent;
        // Certifique-se que CentralizadorParametros está no pacote correto
        CentralizadorParametros cent = centPrinHelper.searchAjustes(1);

        if (cent == null) {
            intent = new Intent(CentralizadorPrincipal.this, CentralizadorAjustesPrincipal.class);
        } else {
            intent = new Intent(CentralizadorPrincipal.this, CentralizadorSistema.class);
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        contador = 0;
    }

    // --- MÉTODOS DA INTERFACE DialogInsertListener ---

    @Override
    public void onConfirmClickedInsertDialog(String textDialog1, String textDialog2, String textDialog3) {
        final String senha_acesso = "35961729";

        // Comparação segura de String para evitar NullPointerException
        if (senha_acesso.equals(textDialog1)) {
            Intent intent = new Intent(this, CentralizadorCadastro.class);
            intent.putExtra("formaAcesso", "admin");
            startActivity(intent);
        } else {
            Toast.makeText(this, "Acesso não autorizado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancelClickedInsertDialog() {
        contador = 0;
    }

    @Override
    public void setVisibilitiesInsertDialog(EditText editText1, EditText editText2, EditText editText3) {
        if (editText2 != null) editText2.setVisibility(View.GONE);
        if (editText3 != null) editText3.setVisibility(View.GONE);
    }

    @Override
    public void setHintsInsertDialog(EditText editText1, EditText editText2, EditText editText3) {
        if (editText1 != null) editText1.setHint("Password");
    }

    @Override
    public void setInputTypesInsertDialog(EditText editText1, EditText editText2, EditText editText3) {
        if (editText1 != null) {
            editText1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            // Verifica se o texto não é nulo antes de setar a seleção
            if (editText1.getText() != null) {
                editText1.setSelection(editText1.getText().length());
            }
        }
    }

    @Override
    public void setTitleInsertDialog(AlertDialog ad) {
        if (ad != null) {
            ad.setTitle("aAcesso à área de cadastro do usuário");
        }
    }
}