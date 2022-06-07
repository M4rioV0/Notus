package com.example.proyectofinciclo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.proyectofinciclo.R;

public class SignUp extends AppCompatActivity {

    ImageView imageViewUser;
    Button bttRegistrarse;
    Button bttSalir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        imageViewUser = findViewById(R.id.img_user_sp);
        bttRegistrarse = findViewById(R.id.btt_registrarse_sp);
        bttSalir = findViewById(R.id.btt_salir_sp);

        imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bttRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bttSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}