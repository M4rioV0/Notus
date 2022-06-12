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

public class NotasAdapter extends RecyclerView.Adapter implements  View.OnClickListener{

    private ArrayList<NotasModel> listaNotasAdapter = new ArrayList<>();
    private View.OnClickListener listener;


    public NotasAdapter(ArrayList<NotasModel> listaNotas) {
        this.listaNotasAdapter = listaNotas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0){
            view = layoutInflater.inflate(R.layout.layout_imagen, parent, false);
            view.setOnClickListener(this);
            ImageViewHolder imh = new ImageViewHolder(view);
            return  imh;
        }else{
            view = layoutInflater.inflate(R.layout.layout_notas, parent, false);
            view.setOnClickListener(this);
            NotasViewHolder nvh = new NotasViewHolder(view);
            return  nvh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (listaNotasAdapter.get(position).getTitulo().toString().isEmpty()&&listaNotasAdapter.get(position).getContenido().toString().isEmpty()){
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            imageViewHolder.imagen.setImageBitmap(DownloadImages.stringToBitMap(listaNotasAdapter.get(position).getImagen().toString()));
        }else {
            NotasViewHolder notasViewHolder = (NotasViewHolder) holder;
            notasViewHolder.titulo.setText(listaNotasAdapter.get(position).getTitulo().toString());
            notasViewHolder.contenido.setText(listaNotasAdapter.get(position).getContenido().toString());
            notasViewHolder.imagen.setImageBitmap(DownloadImages.stringToBitMap(listaNotasAdapter.get(position).getImagen().toString()));
        }

    }

    @Override
    public int getItemCount() {
        return listaNotasAdapter.size();
    }

    @Override
    public int getItemViewType(int position){
        if (listaNotasAdapter.get(position).getTitulo().toString().isEmpty()&&listaNotasAdapter.get(position).getContenido().toString().isEmpty()){
            return 0;
        }else{
            return 1;
        }
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

    class  NotasViewHolder extends RecyclerView.ViewHolder{

        TextView titulo,contenido;
        ImageView imagen;

        public NotasViewHolder(View itemView){
            super(itemView);

            titulo = (TextView) itemView.findViewById(R.id.note_title);
            contenido = (TextView) itemView.findViewById(R.id.note_content);
            imagen = (ImageView) itemView.findViewById(R.id.note_image);

        }

    }

    class ImageViewHolder extends  RecyclerView.ViewHolder{

        ImageView imagen;

        public  ImageViewHolder(View itemView){
            super(itemView);

            imagen = (ImageView) itemView.findViewById(R.id.iv_image);

        }
    }
}
