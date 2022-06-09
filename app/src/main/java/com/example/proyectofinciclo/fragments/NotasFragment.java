package com.example.proyectofinciclo.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectofinciclo.adapters.NotasAdapter;
import com.example.proyectofinciclo.models.NotasModel;
import com.example.proyectofinciclo.R;
import com.example.proyectofinciclo.database.SQLite;
import com.example.proyectofinciclo.database.Utilidades;
import com.example.proyectofinciclo.activities.CrearNotasActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NotasFragment extends Fragment {

    FloatingActionButton floatingActionButton;
    RecyclerView notasRecicler;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FloatingActionButton floatingActionButtonEdit;
    FloatingActionButton floatingActionButtonDelete;
    FloatingActionButton floatingActionButtonSelectAll;

    ArrayList<NotasModel> listaNotas;
    public static NotasAdapter notasAdapter;


    public static NotasModel nota;
    public static boolean editar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;


    //DB
    public static SQLite conn;

    public NotasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notas, container, false);
        // Inflate the layout for this fragment

        nota = new NotasModel("","",0);
        editar = false;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        conn = new SQLite(getActivity().getApplication(),"bd_notas",null,1);

        notasRecicler = view.findViewById(R.id.recicler_notas);
        listaNotas = new ArrayList<>();



        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        notasRecicler.setLayoutManager(staggeredGridLayoutManager);

        notasAdapter = new NotasAdapter(listaNotas);
        notasRecicler.setAdapter(notasAdapter);

        //Buttons
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButtonEdit = view.findViewById(R.id.floatingActionButtonEdit);
        floatingActionButtonDelete = view.findViewById(R.id.floatingActionButtonDelete);
        floatingActionButtonSelectAll = view.findViewById(R.id.floatingActionButtonSelectAll);

        floatingActionButtonEdit.hide();
        floatingActionButtonDelete.hide();
        floatingActionButtonSelectAll.hide();


        if (firebaseUser!=null){
            consultarFirestore();
        }else{
            consultarListaNotas();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity().getApplication(), CrearNotasActivity.class));

            }
        });

        notasAdapter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                nota.setId(listaNotas.get(notasRecicler.getChildAdapterPosition(view)).getId());
                nota.setTitulo(listaNotas.get(notasRecicler.getChildAdapterPosition(view)).getTitulo());
                nota.setContenido(listaNotas.get(notasRecicler.getChildAdapterPosition(view)).getContenido());

                editar = true;
                Toast.makeText(getActivity().getApplication(), String.valueOf(nota.getId()), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity().getApplication(),CrearNotasActivity.class));

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
            notasModel.setId(cursor.getInt(0));
            notasModel.setTitulo(cursor.getString(1));
            notasModel.setContenido(cursor.getString(2));

            listaNotas.add(notasModel);

        }

        notasAdapter.notifyDataSetChanged();

    }

    private void consultarFirestore(){
        listaNotas.clear();

        CollectionReference collectionReference = firebaseFirestore
                .collection("notes")
                .document(firebaseUser.getUid())
                .collection("myNotes");

        collectionReference
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            listaNotas.add(documentSnapshot.toObject(NotasModel.class));
                        }
                    }
                });


        notasAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseUser!=null){
            consultarFirestore();
        }else{
            consultarListaNotas();
        }

    }

    private void eliminarNotas(ArrayList<NotasModel> listaNotasEliminadas){



    }

}