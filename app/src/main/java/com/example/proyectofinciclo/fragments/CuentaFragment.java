package com.example.proyectofinciclo.fragments;

import static android.app.Activity.RESULT_OK;
import static android.service.controls.ControlsProviderService.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinciclo.R;
import com.example.proyectofinciclo.activities.CrearNotasActivity;
import com.example.proyectofinciclo.activities.ForgotPassword;
import com.example.proyectofinciclo.activities.MainActivity;
import com.example.proyectofinciclo.activities.SignIn;
import com.example.proyectofinciclo.models.NotasModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CuentaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CuentaFragment extends Fragment {

    final int PIC_CROP = 1;
    final int PICK_IMAGE = 95;

    ImageView imageViewUserProfilePic;
    TextView textViewChangeProfilePic;
    TextView textViewChangePassword;
    TextView textViewChangeAccount;
    TextView textViewCorreo;
    TextView textViewNombre;
    Button buttonSignOut;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;


    public static Bitmap userImageBitmap;
    private String encodedImage;
    public static Boolean userimage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);

        imageViewUserProfilePic = view.findViewById(R.id.user_image);
        textViewChangeProfilePic = view.findViewById(R.id.tv_cambiar_imagen_usuario);
        textViewChangeAccount = view.findViewById(R.id.tv_cambiar_cuenta);
        textViewChangePassword = view.findViewById(R.id.tv_cambiar_contraseña);
        textViewCorreo = view.findViewById(R.id.tv_correo_usuario);
        buttonSignOut = view.findViewById(R.id.btt_sign_out);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser!=null){
            textViewCorreo.setText(firebaseUser.getEmail());
            if (userimage){
                imageViewUserProfilePic.setImageBitmap(userImageBitmap);
            }
        }


            textViewChangeProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (firebaseUser!=null){
                        pickImage();
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





        return view;
    }

    private void pickImage(){
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
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getApplication().getContentResolver().query(imageUri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
            bitmapToString(bitmap);

        }
    }

    private void bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100,byteArrayOutputStream);
        byte[] b =byteArrayOutputStream.toByteArray();
        encodedImage = Base64.encodeToString(b,  Base64.DEFAULT);
        uploadProfileImage();
    }

    private Bitmap stringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,encodeByte.length);
            return bitmap;
        }catch (Exception e){
            e.getMessage();
            return null;
        }

    }

    private void uploadProfileImage() {
        DocumentReference documentReference = firebaseFirestore
                .collection("notes")
                .document(firebaseUser.getUid())
                .collection("profilePic")
                .document("pm");
        Map<String ,Object> imagenPerfil = new HashMap<>();
        imagenPerfil.put("profileImage",encodedImage);
        documentReference.set(imagenPerfil).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity().getApplication(), "Imagen subida", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplication(), "error al subir la imagen", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void downloadImage(){
        CollectionReference collectionReference = firebaseFirestore
                .collection("notes")
                .document(firebaseUser.getUid())
                .collection("profilePic");

        collectionReference
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot: snapshotList){
                            userImageBitmap = stringToBitMap((String) snapshot.get("profileImage"));
                            userimage = true;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplication(), "Fallo al cargar la imagen", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}