package project.interdisciplinary.inclusesapp.data.firebase;

import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import project.interdisciplinary.inclusesapp.data.models.Arquivo;

public class DatabaseFirebase {
    public void uploadFileToFirebase(Uri fileUri, Arquivo arquivo) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference fileRef = storageRef.child( "uploads/" + arquivo.getId() );

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                })
                .addOnFailureListener(e -> {
                });
    }
    public void getFileUriFromFirebase(String fileId, OnSuccessListener<Uri> onSuccessListener, OnFailureListener onFailureListener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference fileRef = storageRef.child("uploads/" + fileId);

        fileRef.getDownloadUrl()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }



}
