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

import com.example.proyectofinciclo.adapters.NotasAdapter;
import com.example.proyectofinciclo.models.NotasModel;
import com.example.proyectofinciclo.R;
import com.example.proyectofinciclo.database.SQLite;
import com.example.proyectofinciclo.database.Utilidades;
import com.example.proyectofinciclo.activities.CrearNotasActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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

        final NotasModel[] notasModelFire = new NotasModel[1];
        firebaseFirestore.collection("notes")
                .document(firebaseUser.getUid())
                .collection("myNotes")
                .whereEqualTo(firebaseUser.getUid(),firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                notasModelFire[0] = new NotasModel();
                                notasModelFire[0].setId(Integer.parseInt(document.getId()));
                                notasModelFire[0].setTitulo((String) document.get("title"));
                                notasModelFire[0].setContenido((String) document.get("content"));

                                listaNotas.add(notasModelFire[0]);
                            }
                        }else{

                        }
                    }
                });

        notasAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        consultarListaNotas();

    }

    private void eliminarNotas(ArrayList<NotasModel> listaNotasEliminadas){



    }

}