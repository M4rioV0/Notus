package com.example.proyectofinciclo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectofinciclo.fragments.NotasFragment;
import com.example.proyectofinciclo.R;
import com.example.proyectofinciclo.database.Utilidades;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CrearNotasActivity extends AppCompatActivity {

    EditText editTextTitulo;
    EditText editTextContenido;
    FloatingActionButton salirGuardarNota;
    int num = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_notas);



        //UI para crear notas
        editTextTitulo = findViewById(R.id.et_titulo_nota);
        editTextContenido = findViewById(R.id.et_contenido_nota);
        salirGuardarNota = findViewById(R.id.fab_exit_crear_notas_layout);

        rellenarCampos();

        salirGuardarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextContenido.getText().toString().isEmpty()||editTextTitulo.getText().toString().isEmpty()){
                    finish();
                }else{
                   if (NotasFragment.editar==true){
                       actualizarNota();
                   }else {
                       registrarNota();
                   }

                    finish();
                }

            }
        });

    }

    private void rellenarCampos() {

        editTextTitulo.setText(NotasFragment.nota.getTitulo());
        editTextContenido.setText(NotasFragment.nota.getContenido());

    }

    public void registrarNota(){

        Toast.makeText(this,"registrar",Toast.LENGTH_LONG);

        SQLiteDatabase db = NotasFragment.conn.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utilidades.campoTitulo,editTextTitulo.getText().toString());
        values.put(Utilidades.campoContenido,editTextContenido.getText().toString());



        db.insert(Utilidades.tablaNotas,null,values);


    }

    public void actualizarNota(){

        Toast.makeText(this,"actualizar",Toast.LENGTH_LONG);

        SQLiteDatabase db = NotasFragment.conn.getWritableDatabase();

        String[] id = {String.valueOf(NotasFragment.nota.getId())};
        ContentValues values = new ContentValues();
        values.put(Utilidades.campoTitulo,editTextTitulo.getText().toString());
        values.put(Utilidades.campoContenido,editTextContenido.getText().toString());

        db.update(Utilidades.tablaNotas,values,Utilidades.campoId+"=?",id);
        NotasFragment.editar = false;

    }

}