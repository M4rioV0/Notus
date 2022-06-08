package com.example.proyectofinciclo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinciclo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    public static boolean logueado = false;

    Button bttIniciarSesion;
    Button bttRegistrarse;
    Button bttSalir;
    TextView tvForgotPass;
    EditText editTextEmail;
    EditText editTextPass;

    private FirebaseAuth firebaseAuth;

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
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


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

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser.isEmailVerified()==true){
            Toast.makeText(this, "Logueado", Toast.LENGTH_SHORT).show();
            logueado = true;
            finish();
            Intent intent = new Intent(SignIn.this,MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Verifica tu correo para poder loguearte", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }
}