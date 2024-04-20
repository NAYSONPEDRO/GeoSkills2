package com.example.geoskills2.repository;

import androidx.annotation.NonNull;

import com.example.geoskills2.enums.FirestoreEnum;
import com.example.geoskills2.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreRepository {
    private FirebaseFirestore firestoreDb;
    private CollectionReference collectionUser;
    private static FirestoreRepository instance;
    public static FirestoreRepository getInstance(){
        if(instance == null){
            instance = new FirestoreRepository();
        }
        return instance;
    }

    private FirestoreRepository(){
        firestoreDb = FirebaseFirestore.getInstance();
        collectionUser = firestoreDb.collection(FirestoreEnum.COLECTION_USERS.getValue());
    }

    public void  checkUserName(String name, OnCompleteListener<QuerySnapshot> onCompleteListener){
        collectionUser.whereEqualTo(FirestoreEnum.NAME.getValue(), name).get().addOnCompleteListener(onCompleteListener);
    }

    public void registerUserOnDb(@NonNull User user, OnCompleteListener<Void> onCompleteListener){
        collectionUser.document(user.getUiid()).set(user).addOnCompleteListener(onCompleteListener);
    }
}
