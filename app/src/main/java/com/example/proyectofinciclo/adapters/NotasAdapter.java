package com.example.proyectofinciclo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinciclo.models.NotasModel;
import com.example.proyectofinciclo.R;

import java.util.ArrayList;

public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.NotasViewHolder> {

    private ArrayList<NotasModel> listaNotas  = new ArrayList<>();

    public NotasAdapter(ArrayList<NotasModel> listaNotas) {
        this.listaNotas = listaNotas;
    }

    @NonNull
    @Override
    public NotasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notas,parent,false);
        NotasViewHolder nvh = new NotasViewHolder(view);
        return  nvh;

    }

    @Override
    public void onBindViewHolder(@NonNull NotasViewHolder holder, int position) {

        holder.titulo.setText(listaNotas.get(position).getTitulo().toString());
        holder.contenido.setText(listaNotas.get(position).getContenido().toString());

    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public class  NotasViewHolder extends RecyclerView.ViewHolder{

        TextView titulo,contenido;

        public NotasViewHolder(View itemView){
            super(itemView);

            titulo = (TextView) itemView.findViewById(R.id.note_title);
            contenido = (TextView) itemView.findViewById(R.id.note_content);

        }

    }
}
