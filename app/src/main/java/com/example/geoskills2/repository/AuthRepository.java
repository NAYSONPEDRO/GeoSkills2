package com.example.geoskills2.repository;

import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.geoskills2.model.User;
import com.example.geoskills2.view.auth.ErrorData;
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
        String[] firebaseAuthErrorMessages = {
                "The email address is badly formatted.",
                "The password is invalid or the user does not have a password.",
                "The email address is already in use by another account.",
                "The given password is invalid. [ Password should be at least 6 characters ]",
                "The email address is badly formatted.",
                "This operation is sensitive and requires recent authentication. Log in again before retrying this request.",
                "This credential is for a different user.",
                "An internal error has occurred. [ WEB_INTERNAL_ERROR ]",
                "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
        };
        String exception = e.getMessage();
        assert exception != null : "Null excepition kk";
        if (exception.contains(firebaseAuthErrorMessages[0])) {
            return new ErrorData("Endereço de e-mail mal formatado", "Por favor, verifique o formato do endereço de e-mail.");
        } else if (exception.contains(firebaseAuthErrorMessages[1])) {
            return new ErrorData("Senha inválida", "A senha é inválida ou o usuário não tem uma senha.");
        } else if (exception.contains(firebaseAuthErrorMessages[2])) {
            return new ErrorData("Endereço de e-mail em uso", "O endereço de e-mail já está em uso por outra conta.");
        } else if (exception.contains(firebaseAuthErrorMessages[3])) {
            return new ErrorData("Senha inválida", "A senha fornecida é inválida. A senha deve ter pelo menos 6 caracteres.");
        } else if (exception.contains(firebaseAuthErrorMessages[4])) {
            return new ErrorData("Endereço de e-mail mal formatado", "Por favor, verifique o formato do endereço de e-mail.");
        } else if (exception.contains(firebaseAuthErrorMessages[5])) {
            return new ErrorData("Operação sensível", "Esta operação requer autenticação recente. Faça login novamente antes de tentar esta solicitação.");
        } else if (exception.contains(firebaseAuthErrorMessages[6])) {
            return new ErrorData("Credencial para usuário diferente", "Esta credencial é para um usuário diferente.");
        } else if (exception.contains(firebaseAuthErrorMessages[7])) {
            return new ErrorData("Erro interno", "Ocorreu um erro interno.");
        } else if (exception.contains(firebaseAuthErrorMessages[8])) {
            return new ErrorData("Erro internet", "Verifique sua conexão com a internet.");
        } else {
            return new ErrorData("Erro desconhecido", "Ocorreu um erro desconhecido.");
        }

    }
}
