package com.example.proyectofinciclo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectofinciclo.adapters.NotasAdapter;
import com.example.proyectofinciclo.database.DownloadImages;
import com.example.proyectofinciclo.fragments.NotasFragment;
import com.example.proyectofinciclo.R;
import com.example.proyectofinciclo.database.Utilidades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CrearNotasActivity extends AppCompatActivity {

    EditText editTextTitulo;
    EditText editTextContenido;
    ImageView imageViewNote;
    FloatingActionButton salirGuardarNota;
    Button buttonDelete;
    Button buttonAddImage;
    Button buttonRemoveImage;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    String notaIdFirebase;
    final int PICK_IMAGE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_notas);

        //UI para crear notas
        editTextTitulo = findViewById(R.id.et_titulo_nota);
        editTextContenido = findViewById(R.id.et_contenido_nota);
        salirGuardarNota = findViewById(R.id.fab_exit_crear_notas_layout);
        buttonDelete = findViewById(R.id.btt_eliminar_nota);
        imageViewNote = findViewById(R.id.iv_imagen_nota);
        buttonAddImage = findViewById(R.id.btt_añadir_imagen);
        buttonRemoveImage = findViewById(R.id.btt_quitar_imagen);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (NotasFragment.editar==true){
            rellenarCampos();
        }


        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        buttonRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewNote.setImageBitmap(null);
            }
        });

        salirGuardarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    imageViewNote.invalidate();
                    BitmapDrawable drawable = (BitmapDrawable) imageViewNote.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    Boolean imagenVacia = DownloadImages.getStringFromBitmap(bitmap).isEmpty();
                    Boolean contenidoVacio = editTextContenido.getText().toString().isEmpty();
                    Boolean tituloVacio = editTextTitulo.getText().toString().isEmpty();
                    if (tituloVacio&&contenidoVacio&&imagenVacia){
                        finish();
                    }else if (tituloVacio&&!contenidoVacio){
                        finish();
                    }else{

                        if (NotasFragment.editar==true){
                            actualizarNota();
                            if (firebaseUser!=null){
                                actualizarNotaFirebase();
                            }
                        }else {
                            registrarNota();
                            if (firebaseUser!=null){
                                registrarNotaFireBase();
                            }
                        }
                        finish();
                    }
                }catch (Exception e){
                    Boolean contenidoVacio = editTextContenido.getText().toString().isEmpty();
                    Boolean tituloVacio = editTextTitulo.getText().toString().isEmpty();
                    if (tituloVacio&&contenidoVacio){
                        finish();
                    }else if (tituloVacio&&!contenidoVacio){
                        finish();
                    }else if (!tituloVacio&&contenidoVacio){

                        if (NotasFragment.editar==true){
                            actualizarNota();
                            if (firebaseUser!=null){
                                actualizarNotaFirebase();
                            }
                        }else {
                            registrarNota();
                            if (firebaseUser!=null){
                                registrarNotaFireBase();
                            }
                        }
                        finish();
                    }else{
                        if (NotasFragment.editar==true){
                            actualizarNota();
                            if (firebaseUser!=null){
                                actualizarNotaFirebase();
                            }
                        }else {
                            registrarNota();
                            if (firebaseUser!=null){
                                registrarNotaFireBase();
                            }
                        }
                        finish();
                    }
                }


                }


        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                if (NotasFragment.editar==true){
                        eliminarNota();
                        if (firebaseUser!=null){
                            eliminarNotaFirebase();
                        }
                        editTextTitulo.setText("");
                        editTextContenido.setText("");
                        imageViewNote.setImageBitmap(null);
                        finish();
                }else {
                    finish();
                }

            }
        });

    }

    private void pickImage() {
        Intent galeria = new Intent();
        galeria.setType("image/*");
        galeria.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(galeria,"selecciona una imagen"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){

            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewNote.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){

            }
        }
    }

    private void eliminarNotaFirebase() {
        DocumentReference documentReference = firebaseFirestore.collection("notes")
                .document(firebaseUser.getUid())
                .collection("myNotes").document(String.valueOf(NotasFragment.nota.getId()));

        documentReference
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        NotasFragment.editar = false;
                        Toast.makeText(CrearNotasActivity.this, "Nota eliminada", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CrearNotasActivity.this, "Error al intentar eliminar la nota", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void actualizarNotaFirebase() {
        DocumentReference documentReference = firebaseFirestore
                .collection("notes")
                .document(firebaseUser.getUid())
                .collection("myNotes").document(String.valueOf(NotasFragment.nota.getId()));

        Map<String ,Object> note = new HashMap<>();
        String titulo = editTextTitulo.getText().toString();
        String contenido = editTextContenido.getText().toString();
        note.put("title",titulo);
        note.put("content",contenido);
        try {
            imageViewNote.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) imageViewNote.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            String imagen = DownloadImages.getStringFromBitmap(bitmap);
            note.put("image",imagen);
        }catch (Exception e){
            String imagen = "";
            note.put("image",imagen);
        }

        documentReference
                .update(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        NotasFragment.editar = false;
                        //Toast.makeText(CrearNotasActivity.this, "Titulo actualizado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(CrearNotasActivity.this, "fallo al actualizar el titulo", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registrarNotaFireBase() {
        DocumentReference documentReference = firebaseFirestore
                .collection("notes")
                .document(firebaseUser.getUid())
                .collection("myNotes").document(notaIdFirebase);
        Map<String ,Object> note = new HashMap<>();
        String titulo = editTextTitulo.getText().toString();
        String contenido = editTextContenido.getText().toString();
        try {
            imageViewNote.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) imageViewNote.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            String imagen = DownloadImages.getStringFromBitmap(bitmap);
            note.put("title",titulo);
            note.put("content",contenido);
            note.put("image",imagen);
            documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CrearNotasActivity.this, "Nota guardada", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CrearNotasActivity.this, "error al guardar la nota", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            note.put("title",titulo);
            note.put("content",contenido);
            documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CrearNotasActivity.this, "Nota guardada", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CrearNotasActivity.this, "error al guardar la nota", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void rellenarCampos() {

        editTextTitulo.setText(NotasFragment.nota.getTitulo());
        editTextContenido.setText(NotasFragment.nota.getContenido());
        imageViewNote.setImageBitmap(DownloadImages.stringToBitMap(NotasFragment.nota.getImagen()));

    }

    public void registrarNota(){

        Toast.makeText(this,"registrar",Toast.LENGTH_LONG);

        SQLiteDatabase db = NotasFragment.conn.getWritableDatabase();

        String titulo = editTextTitulo.getText().toString();
        String contenido = editTextContenido.getText().toString();
        ContentValues values = new ContentValues();
        values.put(Utilidades.campoTitulo,titulo);
        values.put(Utilidades.campoContenido,contenido);
        try {
            imageViewNote.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) imageViewNote.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            values.put(Utilidades.campoImagen,DownloadImages.getStringFromBitmap(bitmap));
            db.insert(Utilidades.tablaNotas,null,values);
        }catch (Exception e){
            values.put(Utilidades.campoImagen,"");
            db.insert(Utilidades.tablaNotas,null,values);
        }

        String select = "SELECT "+Utilidades.campoId+" FROM "+Utilidades.tablaNotas;

        Cursor cursor = db.rawQuery(select,null);

        do {
            cursor.moveToNext();
        }while (cursor.isLast()!=true);

        notaIdFirebase = String.valueOf(cursor.getInt(0));


    }

    public void actualizarNota(){

        SQLiteDatabase db = NotasFragment.conn.getWritableDatabase();

        String[] id = {String.valueOf(NotasFragment.nota.getId())};
        ContentValues values = new ContentValues();
        values.put(Utilidades.campoTitulo,editTextTitulo.getText().toString());
        values.put(Utilidades.campoContenido,editTextContenido.getText().toString());
        try {
            imageViewNote.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) imageViewNote.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            values.put(Utilidades.campoImagen,DownloadImages.getStringFromBitmap(bitmap));
            db.update(Utilidades.tablaNotas,values,Utilidades.campoId+"=?",id);
        }catch (Exception e){
            db.update(Utilidades.tablaNotas,values,Utilidades.campoId+"=?",id);
        }
        NotasFragment.editar = false;

        String select = "SELECT "+Utilidades.campoId+" FROM "+Utilidades.tablaNotas;

        Cursor cursor = db.rawQuery(select,null);

        do {
            cursor.moveToNext();
        }while (cursor.isLast()!=true);

        notaIdFirebase = String.valueOf(cursor.getInt(0));

    }

    public void eliminarNota(){

        SQLiteDatabase db = NotasFragment.conn.getWritableDatabase();
        String[] id = {String.valueOf(NotasFragment.nota.getId())};

        db.delete(Utilidades.tablaNotas,Utilidades.campoId+"=?",id);
        NotasFragment.editar = false;
    }

}