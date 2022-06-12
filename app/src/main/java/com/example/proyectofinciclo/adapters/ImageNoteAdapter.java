package com.example.proyectofinciclo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinciclo.R;
import com.example.proyectofinciclo.database.DownloadImages;
import com.example.proyectofinciclo.models.NotasModel;

import java.util.ArrayList;

public class ImageNoteAdapter extends RecyclerView.Adapter<ImageNoteAdapter.ImageViewHolder> implements View.OnClickListener {


    private ArrayList<NotasModel> listaNotasAdapter  = new ArrayList<>();
    private View.OnClickListener onClickListener;

    public ImageNoteAdapter(ArrayList listaNotasAdapter) {
        this.listaNotasAdapter = listaNotasAdapter;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_imagen,parent,false);
        view.setOnClickListener(this);
        ImageViewHolder nvh = new ImageViewHolder(view);

        return  nvh;

    }

    @Override
    public void onBindViewHolder(@NonNull ImageNoteAdapter.ImageViewHolder holder, int position) {

        if (!listaNotasAdapter.get(position).getImagen().toString().isEmpty()){
            holder.imagen.setImageBitmap(DownloadImages.stringToBitMap(listaNotasAdapter.get(position).getImagen().toString()));
        }

    }

    @Override
    public int getItemCount() {
        return listaNotasAdapter.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.onClickListener =listener;

    }

    @Override
    public void onClick(View view) {

        if(onClickListener !=null){
            onClickListener.onClick(view);
        }

    }

    public class  ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView imagen;

        public ImageViewHolder(View itemView){
            super(itemView);

            imagen = (ImageView) itemView.findViewById(R.id.iv_image);

        }

    }
}
