package com.example.proyectofinciclo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectofinciclo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {


    ImageView imageViewUser;
    Button bttRegistrarse;
    Button bttSalir;
    EditText editTextEmail;
    EditText editTextPassword;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        bttRegistrarse = findViewById(R.id.btt_enviar_correo_recuperacion);
        bttSalir = findViewById(R.id.btt_salir_sp);
        editTextEmail = findViewById(R.id.et_correo_reg);
        editTextPassword = findViewById(R.id.et_registra_contraseña_sp);


        bttRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty()||password.isEmpty()){
                    Toast.makeText(SignUp.this, "Introduce tu correo y contraseña para poder registrarte", Toast.LENGTH_SHORT).show();
                }else if(password.length()<7){
                    Toast.makeText(SignUp.this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(SignUp.this, "Te has registrado en Notus", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }else{
                                Toast.makeText(SignUp.this, "Registro de usuario fallido", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        bttSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SignUp.this, "Email de verificación enviado", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(),SignIn.class));
                }
            });
        }else{
            Toast.makeText(this, "Envio de email de verificación fallido", Toast.LENGTH_SHORT).show();
        }

    }
}