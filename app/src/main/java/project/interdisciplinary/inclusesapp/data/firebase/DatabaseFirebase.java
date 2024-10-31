package project.interdisciplinary.inclusesapp.data.firebase;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.Error;

public class DatabaseFirebase {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage dbStorage = FirebaseStorage.getInstance();
    private Map<String, java.lang.Object> error_id = new HashMap<String, java.lang.Object>();
    private static final String COLLECTION_NAME = "Errors";
    private static final String COUNTERS_COLLECTION = "counters";
    private static final String ID_DOCUMENT = "error_id";
    private static final String KEY_FIELD = "key";

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

    public void saveError(Error error) {
        if (error.getId() == 0) {
            db.collection(COUNTERS_COLLECTION).document(ID_DOCUMENT).get().addOnCompleteListener(
                    new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            int id = 1;
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    error_id = document.getData();
                                    id = Integer.parseInt(error_id.get(KEY_FIELD).toString()) + 1;
                                }
                                error_id.put(KEY_FIELD, id);
                                db.collection(COUNTERS_COLLECTION).document(ID_DOCUMENT).set(error_id);

                                // Gravar o object
                                error.setId(id);
                                db.collection(COLLECTION_NAME).document(String.valueOf(id)).set(error);
                            }
                        }
                    }
            );
        } else {
            db.collection(COLLECTION_NAME).document(String.valueOf(error.getId())).set(error);
        }
    }

}
