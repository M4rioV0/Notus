package com.example.proyectofinciclo.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinciclo.R;
import com.example.proyectofinciclo.activities.ForgotPassword;
import com.example.proyectofinciclo.activities.MainActivity;
import com.example.proyectofinciclo.activities.SignIn;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CuentaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CuentaFragment extends Fragment {

    final int PIC_CROP = 1;

    ImageView imageViewUserProfilePic;
    TextView textViewChangeProfilePic;
    TextView textViewChangePassword;
    TextView textViewChangeAccount;
    TextView textViewCorreo;
    TextView textViewNombre;
    Button buttonSignOut;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicReference;
    private DataSnapshot dataSnapshot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);

        imageViewUserProfilePic = view.findViewById(R.id.user_image);
        textViewChangeProfilePic = view.findViewById(R.id.tv_cambiar_imagen_usuario);
        textViewChangeAccount = view.findViewById(R.id.tv_cambiar_cuenta);
        textViewChangePassword = view.findViewById(R.id.tv_cambiar_contraseña);
        textViewNombre = view.findViewById(R.id.tv_nombre_usuario);
        textViewCorreo = view.findViewById(R.id.tv_correo_usuario);
        buttonSignOut = view.findViewById(R.id.btt_sign_out);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
            storageProfilePicReference = FirebaseStorage.getInstance().getReference().child("Profile pic");
            textViewNombre.setText(firebaseUser.getDisplayName());
            textViewCorreo.setText(firebaseUser.getEmail());
        }


            textViewChangeProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (firebaseUser!=null){
                        cropImageMethod();
                        uploadProfileImage();
                    }
                }
            });

            textViewChangeAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firebaseUser!=null){
                        firebaseAuth.signOut();
                        startActivity(new Intent(getActivity().getApplication(),SignIn.class));
                    }
                }
            });

            textViewChangePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firebaseUser!=null){
                        firebaseAuth.sendPasswordResetEmail(firebaseUser.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getActivity().getApplication(), "Mail de reseteo de contraseña enviado", Toast.LENGTH_SHORT).show();
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(getActivity().getApplication(),SignIn.class));
                                    }else{
                                        Toast.makeText(getActivity().getApplication(), "Error al enviar el email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        });
                    }
                }
            });

            buttonSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firebaseUser!=null){
                        firebaseAuth.signOut();
                        startActivity(new Intent(getActivity().getApplication(),MainActivity.class));
                    }
                }
            });



        if (firebaseUser!=null){
            getUserInfo();
        }

        return view;
    }

    private void selectImage(){

    }

    private void cropImageMethod(){

    }

    private void uploadProfileImage() {

    }

    private void getUserInfo(){

        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    if (dataSnapshot.hasChild("image")){
                        String image = dataSnapshot.child("image").getValue().toString();
                        Bitmap bitmap = stringToBitMap(image);
                        imageViewUserProfilePic.setImageBitmap(bitmap);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private Bitmap stringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}