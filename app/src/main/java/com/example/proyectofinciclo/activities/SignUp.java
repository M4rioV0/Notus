package com.example.proyectofinciclo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectofinciclo.R;

public class SignUp extends AppCompatActivity {

    ImageView imageViewUser;
    Button bttRegistrarse;
    Button bttSalir;
    EditText editTextEmail;
    EditText editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        imageViewUser = findViewById(R.id.img_user_sp);
        bttRegistrarse = findViewById(R.id.btt_enviar_correo_recuperacion);
        bttSalir = findViewById(R.id.btt_salir_sp);
        editTextEmail = findViewById(R.id.et_correo_reg);
        editTextPassword = findViewById(R.id.et_registra_contraseña_sp);

        imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                
                if (email.isEmpty()||password.isEmpty()){
                    Toast.makeText(SignUp.this, "Introduce tu correo y contraseña para poder registrarte", Toast.LENGTH_SHORT).show();
                }else{
                    
                }
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