package com.example.proyectofinciclo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectofinciclo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText editTextCorreoRecuperacion;
    Button buttonEnviarCorreoRecuperacion;
    Button buttonVolverSignIn;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextCorreoRecuperacion = findViewById(R.id.et_correo_recuperacion);
        buttonEnviarCorreoRecuperacion = findViewById(R.id.btt_enviar_correo_recuperacion);
        buttonVolverSignIn = findViewById(R.id.btt_salir_sp);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonVolverSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonEnviarCorreoRecuperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextCorreoRecuperacion.getText().toString().trim();
                if (email.isEmpty()){
                    Toast.makeText(ForgotPassword.this, "Introduzca un correo para poder enviarle el email", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ForgotPassword.this, "Mail de recuperacion enviado", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this,SignIn.class));
                            }else{
                                Toast.makeText(ForgotPassword.this, "El correo introducido no es correcto o no existe", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}