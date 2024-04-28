package com.example.geoskills2.viewmodel;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.geoskills2.R;
import com.example.geoskills2.model.User;
import com.example.geoskills2.repository.AuthRepository;
import com.example.geoskills2.repository.FirestoreRepository;
import com.example.geoskills2.view.auth.ErrorData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class AuthViewModel extends AndroidViewModel {
    private final FirestoreRepository firestoreRepository = FirestoreRepository.getInstance();
    private final AuthRepository authRepository = AuthRepository.getInstance();

    private final MutableLiveData<FirebaseUser> currentUserVM = new MutableLiveData<>();
    private final MutableLiveData<ErrorData> errorInLogin = new MutableLiveData<>(null);

    private final MutableLiveData<Boolean> sendedEmail = new MutableLiveData<>(false);



    public AuthViewModel(@NonNull Application application) {
        super(application);
        currentUserVM.setValue(authRepository.getCurrentUser().getValue());
    }

    public void registerUser(User user, String password) {


        firestoreRepository.checkUserNameExists(user.getName(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot queryDocumentSnapshots = task.getResult();

                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        Log.i("USERNAME_EXISTS", "O nome de usuário " + user.getName() + " já está em uso.");
                        return;
                    }
                    authRepository.registerUser(user.getEmail(), password, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                authRepository.getCurrentUser().setValue(authRepository.getAuth().getCurrentUser());
                                user.setUuid(authRepository.getCurrentUser().getValue().getUid());
                                firestoreRepository.registerUserOnDb(user, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            currentUserVM.postValue(authRepository.getCurrentUser().getValue());
                                        } else {
                                            Log.d("ERRO_REGISTER_USER", "Erro ao registrar usuário NO BANCO", task.getException());
                                            errorInLogin.postValue(authRepository.errorHandling(Objects.requireNonNull(task.getException())));
                                        }

                                    }
                                });
                            } else {
                                Log.d("ERRO_REGISTER_USER", "Erro ao registrar usuário", task.getException());
                            }

                        }
                    });

                } else {
                    Log.d("ERRO_USERNAME", "Erro ao procurar nome de usuário", task.getException());
                }

            }
        });

    }

    public ErrorData loginUser(String email, String password) {
        final ErrorData[] errorData = new ErrorData[1];
        authRepository.loginUser(email, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUserVM.postValue(authRepository.getAuth().getCurrentUser());
                    errorData[0] = null;
                } else {
                    Log.d("ERRO_LOGIN", "Erro ao fazer login", task.getException());
                    errorData[0] = authRepository.errorHandling(Objects.requireNonNull(task.getException()));
                }

            }
        });
        return errorData[0];
    }

    public void recoverPassword(String email) {
        authRepository.revoverPassword(email, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    sendedEmail.postValue(true);
                } else {
                    Log.d("ERRO_RECOVER_PASSWORD", "Erro ao recuperar senha", task.getException());
                }

            }
        });
    }


    public MutableLiveData<FirebaseUser> getCurrentUser() {
        return currentUserVM;
    }

    public MutableLiveData<Boolean> getSendedEmail() {
        return sendedEmail;
    }

    public MutableLiveData<ErrorData> getErrorInLogin() {
        return errorInLogin;
    }

    public String getUuid(){
        return authRepository.getCurrentUserId();
    }

    public void logout() {
        authRepository.logout();
        currentUserVM.postValue(null);
    }

    public AlertDialog makeLoadingAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.loading_alert, null);
        AlertDialog alertDialog = builder.setView(view).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }
}
