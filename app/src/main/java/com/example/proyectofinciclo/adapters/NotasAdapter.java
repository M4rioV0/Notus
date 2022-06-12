package com.example.proyectofinciclo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinciclo.database.DownloadImages;
import com.example.proyectofinciclo.models.NotasModel;
import com.example.proyectofinciclo.R;

import java.util.ArrayList;

public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.NotasViewHolder> implements  View.OnClickListener{

    private ArrayList<NotasModel> listaNotasAdapter  = new ArrayList<>();
    private View.OnClickListener listener;


    public NotasAdapter(ArrayList<NotasModel> listaNotas) {
        this.listaNotasAdapter = listaNotas;
    }

    @NonNull
    @Override
    public NotasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notas,parent,false);
        view.setOnClickListener(this);
        NotasViewHolder nvh = new NotasViewHolder(view);

        return  nvh;

    }

    @Override
    public void onBindViewHolder(@NonNull NotasViewHolder holder, int position) {

        holder.titulo.setText(listaNotasAdapter.get(position).getTitulo().toString());
        holder.contenido.setText(listaNotasAdapter.get(position).getContenido().toString());
        if (!listaNotasAdapter.get(position).getImagen().toString().isEmpty()){
            holder.imagen.setImageBitmap(DownloadImages.stringToBitMap(listaNotasAdapter.get(position).getImagen().toString()));
        }


    }

    @Override
    public int getItemCount() {
        return listaNotasAdapter.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;

    }

    @Override
    public void onClick(View view) {

        if(listener!=null){
            listener.onClick(view);
        }

    }

    public class  NotasViewHolder extends RecyclerView.ViewHolder{

        TextView titulo,contenido;
        ImageView imagen;

        public NotasViewHolder(View itemView){
            super(itemView);

            titulo = (TextView) itemView.findViewById(R.id.note_title);
            contenido = (TextView) itemView.findViewById(R.id.note_content);
            imagen = (ImageView) itemView.findViewById(R.id.note_image);

        }

    }
}
