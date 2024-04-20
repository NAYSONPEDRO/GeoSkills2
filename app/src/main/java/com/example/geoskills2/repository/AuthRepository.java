package com.example.geoskills2.repository;

import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.geoskills2.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private FirebaseAuth auth;
    private FirestoreRepository firestoreRepository;


    public static AuthRepository authRepository;

    private MutableLiveData<FirebaseUser> currentUser;
    private AuthRepository(){
        auth = FirebaseAuth.getInstance();
        firestoreRepository = FirestoreRepository.getInstance();
        currentUser = new MutableLiveData<>();
        if(auth.getCurrentUser() != null)
            currentUser.setValue(auth.getCurrentUser());
    }

    public FirebaseAuth getAuth(){
        return auth;
    }

    public static AuthRepository getInstance(){
        if(authRepository == null){
            authRepository = new AuthRepository();
        }
        return authRepository;
    }

    public void registerUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }
    public void loginUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }


    public void revoverPassword(String email, OnCompleteListener<Void> onCompleteListener){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(onCompleteListener);
    }

    public MutableLiveData<FirebaseUser> getCurrentUser(){
        return currentUser;
    }

    public void logout() {
        auth.signOut();
    }
}
