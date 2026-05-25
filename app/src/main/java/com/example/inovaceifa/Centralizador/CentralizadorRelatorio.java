package com.example.inovaceifa.Centralizador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.inovaceifa.R;
import com.example.inovaceifa.Utilities.HelperDatabaseSQL;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity para exibir o histórico de produção com filtros por data e operador.
 */
public class CentralizadorRelatorio extends AppCompatActivity {

    private RecyclerView rvEventos;
    private AdapterEventosProducao adapter;
    private HelperDatabaseSQL helper;
    private FloatingActionButton voltar;
    private Button finalizar, btnLimpar;
    private TextView txtSemDados;
    private PowerSpinnerView spinnerData, spinnerOperador;

    private String filtroData = null;
    private String filtroOperador = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centralizador_relatorio);

        helper = HelperDatabaseSQL.getInstance(this);

        iniciaComponentes();
        configFiltros();
        configLista();
        configBotoes();
    }

    private void iniciaComponentes() {
        rvEventos = findViewById(R.id.rv_eventos_producao);
        voltar = findViewById(R.id.centRel_voltar);
        finalizar = findViewById(R.id.centRel_finalizar);
        txtSemDados = findViewById(R.id.txt_sem_dados);
        btnLimpar = findViewById(R.id.btn_limpar_filtros);
        spinnerData = findViewById(R.id.spinner_filtro_data);
        spinnerOperador = findViewById(R.id.spinner_filtro_operador);
    }

    private void configFiltros() {
        // Configurar Spinner de Datas
        List<String> datas = helper.listarDatasUnicas();
        spinnerData.setItems(datas);
        spinnerData.setOnSpinnerItemSelectedListener((oldIndex, oldItem, newIndex, newItem) -> {
            filtroData = (String) newItem;
            atualizarLista();
        });

        // Configurar Spinner de Operadores
        ArrayList<Operador> operadores = helper.listarTodasOperadors();
        List<String> nomesOperadores = new ArrayList<>();
        for (Operador op : operadores) {
            nomesOperadores.add(op.getNumero() + " - " + op.getLocal());
        }
        spinnerOperador.setItems(nomesOperadores);
        spinnerOperador.setOnSpinnerItemSelectedListener((oldIndex, oldItem, newIndex, newItem) -> {
            String selecionado = (String) newItem;
            filtroOperador = selecionado.split(" - ")[0]; // Pega apenas o número (ID)
            atualizarLista();
        });
    }

    private void configLista() {
        rvEventos.setLayoutManager(new LinearLayoutManager(this));
        atualizarLista();
    }

    private void atualizarLista() {
        ArrayList<EventoProducao> lista = helper.listarEventosFiltrados(filtroData, filtroOperador);
        
        if (lista.isEmpty()) {
            txtSemDados.setVisibility(View.VISIBLE);
            rvEventos.setVisibility(View.GONE);
        } else {
            txtSemDados.setVisibility(View.GONE);
            rvEventos.setVisibility(View.VISIBLE);
            
            adapter = new AdapterEventosProducao(lista);
            rvEventos.setAdapter(adapter);
        }
    }

    private void configBotoes() {
        voltar.setOnClickListener(v -> finish());
        finalizar.setOnClickListener(v -> finish());
        btnLimpar.setOnClickListener(v -> {
            filtroData = null;
            filtroOperador = null;
            spinnerData.clearSelectedItem();
            spinnerOperador.clearSelectedItem();
            spinnerData.setHint("Data");
            spinnerOperador.setHint("Operador");
            atualizarLista();
        });
    }
}
