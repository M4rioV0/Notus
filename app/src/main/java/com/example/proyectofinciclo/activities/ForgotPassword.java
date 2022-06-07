package com.example.proyectofinciclo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectofinciclo.R;

public class ForgotPassword extends AppCompatActivity {

    EditText editTextCorreoRecuperacion;
    Button buttonEnviarCorreoRecuperacion;
    Button buttonVolverSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextCorreoRecuperacion = findViewById(R.id.et_correo_recuperacion);
        buttonEnviarCorreoRecuperacion = findViewById(R.id.btt_enviar_correo_recuperacion);
        buttonVolverSignIn = findViewById(R.id.btt_salir_sp);

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

                }

            }
        });
    }
}