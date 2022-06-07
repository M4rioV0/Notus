package com.example.proyectofinciclo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.proyectofinciclo.R;

public class SignIn extends AppCompatActivity {

    Button bttIniciarSesion;
    Button bttRegistrarse;
    Button bttSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        bttIniciarSesion = findViewById(R.id.btt_iniciar_sesion);
        bttRegistrarse = findViewById(R.id.btt_registrarse_ln);
        bttSalir = findViewById(R.id.btt_salir_ln);

        bttSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bttRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
            }
        });

    }
}