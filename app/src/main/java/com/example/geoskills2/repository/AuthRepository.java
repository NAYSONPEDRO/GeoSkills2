package com.example.geoskills2.repository;

import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.geoskills2.model.User;
import com.example.geoskills2.view.auth.ErrorData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseAuthWebException;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private FirebaseAuth auth;
    private FirestoreRepository firestoreRepository;
    public static AuthRepository authRepository;


    private MutableLiveData<FirebaseUser> currentUser;

    private AuthRepository() {
        auth = FirebaseAuth.getInstance();
        firestoreRepository = FirestoreRepository.getInstance();
        currentUser = new MutableLiveData<>();
        if (auth.getCurrentUser() != null)
            currentUser.setValue(auth.getCurrentUser());
    }

    @NonNull
    public String getCurrentUserId() {
        return auth.getCurrentUser().getUid();
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public static AuthRepository getInstance() {
        if (authRepository == null) {
            authRepository = new AuthRepository();
        }
        return authRepository;
    }

    public void registerUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

    public void loginUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }


    public void revoverPassword(String email, OnCompleteListener<Void> onCompleteListener) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(onCompleteListener);
    }

    public MutableLiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        auth.signOut();
    }

    public ErrorData errorHandling(Exception e) {





        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            return new ErrorData("Endereço de e-mail mal formatado", "Por favor, verifique o formato do endereço de e-mail.");
        } else if (e instanceof FirebaseAuthInvalidUserException) {
            return new ErrorData("Usuário inválido", "O usuário não existe ou foi desativado.");
        } else if (e instanceof FirebaseAuthEmailException) {
            return new ErrorData("Erro de e-mail", "Erro relacionado ao endereço de e-mail.");
        } else if (e instanceof FirebaseAuthUserCollisionException) {
            return new ErrorData("Conflito de usuário", "O endereço de e-mail já está em uso.");
        } else if (e instanceof FirebaseAuthWeakPasswordException) {
            return new ErrorData("Senha fraca", "A senha é muito fraca.");
        } else if (e instanceof FirebaseAuthRecentLoginRequiredException) {
            return new ErrorData("Login recente necessário", "É necessário fazer login novamente.");
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            return new ErrorData("Credenciais inválidas", "Credenciais inválidas fornecidas.");
        } else if (e instanceof FirebaseAuthWebException) {
            return new ErrorData("Erro interno da Web", "Erro interno relacionado à Web.");
        } else if (e instanceof FirebaseNetworkException) {
            return new ErrorData("Erro de rede", "Erro de rede, verifique sua conexão.");
        } else {
            return new ErrorData("Erro desconhecido", "Ocorreu um erro desconhecido.");
        }
    }
}
