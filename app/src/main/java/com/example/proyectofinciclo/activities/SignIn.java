package com.example.proyectofinciclo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinciclo.R;
import com.example.proyectofinciclo.database.Utilidades;
import com.example.proyectofinciclo.fragments.NotasFragment;
import com.example.proyectofinciclo.models.NotasModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    public static boolean logueado = false;

    Button bttIniciarSesion;
    Button bttRegistrarse;
    Button bttSalir;
    TextView tvForgotPass;
    EditText editTextEmail;
    EditText editTextPass;

    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        bttIniciarSesion = findViewById(R.id.btt_iniciar_sesion);
        bttRegistrarse = findViewById(R.id.btt_registrarse_ln);
        bttSalir = findViewById(R.id.btt_salir_ln);
        tvForgotPass = findViewById(R.id.tv_forgot_pass);
        editTextEmail = findViewById(R.id.et_correo_sn);
        editTextPass = findViewById(R.id.et_contraseña_sn);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        bttSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SignIn.this,MainActivity.class));
            }
        });

        bttRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
            }
        });

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(intent);
            }
        });

        bttIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String pass = editTextPass.getText().toString().trim();

                if (email.isEmpty()||pass.isEmpty()){
                    Toast.makeText(SignIn.this, "Es necesario introducir un correo y una contraseña para inciar sesion", Toast.LENGTH_SHORT).show();
                }else{

                    firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                checkEmailVerification();
                            }else{
                                Toast.makeText(SignIn.this, "La cuenta no existe", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }

    private void loadSQLiteData() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


        SQLiteDatabase db = NotasFragment.conn.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.tablaNotas,null);

        while (cursor.moveToNext()){

            DocumentReference documentReference = firebaseFirestore
                    .collection("notes")
                    .document(firebaseUser.getUid())
                    .collection("myNotes").document(String.valueOf(cursor.getInt(0)));

            Map<String ,Object> note = new HashMap<>();
            String titulo = cursor.getString(1);
            String contenido = cursor.getString(2);
            note.put("title",titulo);
            note.put("content",contenido);

            documentReference
                    .update(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //Toast.makeText(CrearNotasActivity.this, "Titulo actualizado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(CrearNotasActivity.this, "fallo al actualizar el titulo", Toast.LENGTH_SHORT).show();
                        }
                    });
            documentReference
                    .update(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //Toast.makeText(CrearNotasActivity.this, "Contenido actualizado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(CrearNotasActivity.this, "fallo al actualizar el contenido", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser.isEmailVerified()==true){
            Toast.makeText(this, "Logueado", Toast.LENGTH_SHORT).show();
            logueado = true;
            loadSQLiteData();
            finish();
            Intent intent = new Intent(SignIn.this,MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Verifica tu correo para poder loguearte", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }
}