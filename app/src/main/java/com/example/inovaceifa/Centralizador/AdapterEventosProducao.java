package com.example.inovaceifa.Centralizador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inovaceifa.R;
import java.util.List;

public class AdapterEventosProducao extends RecyclerView.Adapter<AdapterEventosProducao.ViewHolder> {

    private List<EventoProducao> eventos;

    public AdapterEventosProducao(List<EventoProducao> eventos) {
        this.eventos = eventos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento_producao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventoProducao evento = eventos.get(position);
        
        if ("MARCAR_PE".equals(evento.getTipo())) {
            holder.txtInfo.setText("Pé de Café Marcado #" + evento.getValor());
        } else {
            holder.txtInfo.setText("Evento: " + evento.getTipo() + " (" + evento.getValor() + ")");
        }
        
        String opInfo = "Operador ID: " + (evento.getOperadorID() != null ? evento.getOperadorID() : "N/A");
        holder.txtOperador.setText(opInfo);
        holder.txtData.setText("Data: " + evento.getData());
        holder.txtHora.setText("Hora: " + evento.getHora());
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtInfo, txtOperador, txtData, txtHora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInfo = itemView.findViewById(R.id.txt_evento_info);
            txtOperador = itemView.findViewById(R.id.txt_evento_operador);
            txtData = itemView.findViewById(R.id.txt_evento_data);
            txtHora = itemView.findViewById(R.id.txt_evento_hora);
        }
    }
}
