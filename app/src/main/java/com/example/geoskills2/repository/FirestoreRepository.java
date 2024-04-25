package com.example.geoskills2.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.geoskills2.enums.FirestoreEnum;
import com.example.geoskills2.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.PortUnreachableException;

public class FirestoreRepository {
    private FirebaseFirestore firestoreDb;
    private CollectionReference collectionUser;
    private static FirestoreRepository instance;
    private MutableLiveData<User> userGetted;


    private FirestoreRepository(){
        firestoreDb = FirebaseFirestore.getInstance();
        collectionUser = firestoreDb.collection(FirestoreEnum.COLECTION_USERS.getValue());
        userGetted = new MutableLiveData<>();
    }

    public static FirestoreRepository getInstance(){
        if(instance == null){
            instance = new FirestoreRepository();
        }
        return instance;
    }


    public MutableLiveData<User> getUserGetted(){
        return userGetted;
    }

    public void  checkUserNameExists(String name, OnCompleteListener<QuerySnapshot> onCompleteListener){
        collectionUser.whereEqualTo(FirestoreEnum.NAME.getValue(), name).get().addOnCompleteListener(onCompleteListener);
    }

    public void registerUserOnDb(@NonNull User user, OnCompleteListener<Void> onCompleteListener){
        collectionUser.document(user.getUuid()).set(user).addOnCompleteListener(onCompleteListener);
    }
    public void updateNameUserOnDb(@NonNull String UuID, String newName, OnCompleteListener<Void> onCompleteListener){
        collectionUser.document(UuID).update(FirestoreEnum.NAME.getValue(), newName).addOnCompleteListener(onCompleteListener);
    }
    public void updateProfileImgUserOnDb(String UuID, int img, OnCompleteListener<Void> onCompleteListener){
        collectionUser.document(UuID).update(FirestoreEnum.PROFILE_SELECTED.getValue(), img).addOnCompleteListener(onCompleteListener);
    }

    public void getUserOnDb(@NonNull String UuID, OnCompleteListener<DocumentSnapshot> onCompleteListener){
        collectionUser.document(UuID).get().addOnCompleteListener(onCompleteListener);
    }
}
