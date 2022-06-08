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

    private void cropImageMethod(){
            try {
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                // indicate image type and Uri
                cropIntent.setDataAndType(imageUri, "image/*");
                // set crop properties here
                cropIntent.putExtra("crop", true);
                // indicate aspect of desired crop
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                // indicate output X and Y
                cropIntent.putExtra("outputX", 128);
                cropIntent.putExtra("outputY", 128);
                // retrieve data on return
                cropIntent.putExtra("return-data", true);
                // start the activity - we handle returning in onActivityResult
                startActivityForResult(cropIntent, PIC_CROP);
            }
            // respond to users whose devices do not support the crop action
            catch (ActivityNotFoundException anfe) {
                // display an error message
                String errorMessage = "Error al recortar la imagen";
                Toast toast = Toast.makeText(getActivity().getApplication(), errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");

                imageViewUserProfilePic.setImageBitmap(selectedBitmap);
                imageViewUserProfilePic.setImageURI(imageUri);
            }
        }
    }

    private void uploadProfileImage() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity().getApplication());
        progressDialog.setMessage("Subiendo imagen de perfil");
        progressDialog.show();

        if (imageUri!=null){
            final StorageReference fileRef = storageProfilePicReference
                    .child(firebaseAuth.getCurrentUser().getUid()+".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = (Uri) task.getResult();
                        myUri = downloadUri.toString();
                        HashMap<String,Object> userMap = new HashMap<>();
                        userMap.put("image",myUri);

                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(userMap);

                        progressDialog.dismiss();
                    }
                }
            });
        }else{
            Toast.makeText(getActivity().getApplication(), "imagen no seleccionada", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
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