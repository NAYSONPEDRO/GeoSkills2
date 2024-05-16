package com.example.geoskills2.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.geoskills2.enums.AuthEnum;
import com.example.geoskills2.enums.QuestionEnum;
import com.example.geoskills2.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreRepository {
    private FirebaseFirestore firestoreDb;
    private CollectionReference collectionUser;
    private CollectionReference collectionQuestions;
    private static FirestoreRepository instance;
    private MutableLiveData<User> userGetted;


    private FirestoreRepository() {
        firestoreDb = FirebaseFirestore.getInstance();
        collectionUser = firestoreDb.collection(AuthEnum.COLLECTION_USERS.getValue());
        collectionUser = firestoreDb.collection(QuestionEnum.COLLECTION_QUESTION.getValue());
        userGetted = new MutableLiveData<>();
    }

    public static FirestoreRepository getInstance() {
        if (instance == null) {
            instance = new FirestoreRepository();
        }
        return instance;
    }


    public MutableLiveData<User> getUserGetted() {
        return userGetted;
    }

    public void checkUserNameExists(String name, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        collectionUser.whereEqualTo(AuthEnum.NAME.getValue(), name).get().addOnCompleteListener(onCompleteListener);
    }

    public void registerUserOnDb(@NonNull User user, OnCompleteListener<Void> onCompleteListener) {
        collectionUser.document(user.getUuid()).set(user).addOnCompleteListener(onCompleteListener);
    }

    public void updateNameUserOnDb(@NonNull String UuID, String newName, OnCompleteListener<Void> onCompleteListener) {
        collectionUser.document(UuID).update(AuthEnum.NAME.getValue(), newName).addOnCompleteListener(onCompleteListener);
    }

    public void updateProfileImgUserOnDb(String UuID, int img, OnCompleteListener<Void> onCompleteListener) {
        collectionUser.document(UuID).update(AuthEnum.PROFILE_SELECTED.getValue(), img).addOnCompleteListener(onCompleteListener);
    }

    public void getUserOnDb(@NonNull String UuID, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        collectionUser.document(UuID).get().addOnCompleteListener(onCompleteListener);
    }

    public void updatePointsUserOnDb(@NonNull String UuID, int points, OnCompleteListener<Void> onCompleteListener) {
        collectionUser.document(UuID).update(AuthEnum.POINTS.getValue(), points).addOnCompleteListener(onCompleteListener);
    }



}
