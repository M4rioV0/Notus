package com.example.proyectofinciclo.database;

import static android.service.controls.ControlsProviderService.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.proyectofinciclo.activities.MainActivity;
import com.example.proyectofinciclo.fragments.CuentaFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.sql.Struct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadImages {

    public DownloadImages() {
    }

    public static void bitmapToString(Bitmap bitmap){
        int anchoImagen = 150;
        int altoImagen =  bitmap.getHeight() * anchoImagen / bitmap.getWidth();
        Bitmap bitmapPreview = Bitmap.createScaledBitmap(bitmap,anchoImagen,altoImagen,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapPreview.compress(Bitmap.CompressFormat.PNG, 100,byteArrayOutputStream);
        byte[] b =byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(b,  Base64.DEFAULT);
        uploadProfileImage(encodedImage);
    }

    public static String getStringFromBitmap(Bitmap bitmap){
        int anchoImagen = 150;
        int altoImagen =  bitmap.getHeight() * anchoImagen / bitmap.getWidth();
        Bitmap bitmapPreview = Bitmap.createScaledBitmap(bitmap,anchoImagen,altoImagen,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapPreview.compress(Bitmap.CompressFormat.PNG, 100,byteArrayOutputStream);
        byte[] b =byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(b,  Base64.DEFAULT);
        return encodedImage;
    }


    public static void uploadProfileImage(String encodedImage) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

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
                downloadImage(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

    public static void downloadImage(Boolean fragmentCargado){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

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
                            Log.w(TAG,(String) snapshot.get("profileImage"));
                            Bitmap userImageBitmap = stringToBitMap((String) snapshot.get("profileImage"));
                            if (fragmentCargado){
                                CuentaFragment.imageViewUserProfilePic.setImageBitmap(userImageBitmap);
                            }
                            MainActivity.imgUserNav.setImageBitmap(userImageBitmap);
                            MainActivity.imgUser.setImageBitmap(userImageBitmap);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public static Bitmap stringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,encodeByte.length);
            return bitmap;
        }catch (Exception e){
            return null;
        }

    }
}
