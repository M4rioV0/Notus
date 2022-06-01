package com.example.proyectofinciclo.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinciclo.adapters.NotasAdapter;
import com.example.proyectofinciclo.models.NotasModel;
import com.example.proyectofinciclo.R;
import com.example.proyectofinciclo.database.SQLite;
import com.example.proyectofinciclo.database.Utilidades;
import com.example.proyectofinciclo.activities.CrearNotasActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NotasFragment extends Fragment {

    FloatingActionButton floatingActionButton;
    RecyclerView notasRecicler;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    ArrayList<NotasModel> listaNotas;
    NotasAdapter notasAdapter;

    //DB
    public static SQLite conn;

    public NotasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notas, container, false);
        // Inflate the layout for this fragment

        conn = new SQLite(getActivity().getApplication(),"bd_notas",null,1);

        notasRecicler = view.findViewById(R.id.recicler_notas);
        listaNotas = new ArrayList<>();

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        notasRecicler.setLayoutManager(staggeredGridLayoutManager);

        notasAdapter = new NotasAdapter(listaNotas);
        notasRecicler.setAdapter(notasAdapter);

        floatingActionButton = view.findViewById(R.id.floatingActionButton);

        consultarListaNotas();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity().getApplication(), CrearNotasActivity.class));

            }
        });

        return view;
    }

    private void consultarListaNotas() {

        listaNotas.clear();

        SQLiteDatabase db = conn.getWritableDatabase();

        NotasModel notasModel;

        Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.tablaNotas,null);

        while (cursor.moveToNext()){

            notasModel = new NotasModel();
            notasModel.setTitulo(cursor.getString(0));
            notasModel.setContenido(cursor.getString(1));

            listaNotas.add(notasModel);

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        consultarListaNotas();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void eliminarNota(){

    }

    private void editarNota(){

    }


}