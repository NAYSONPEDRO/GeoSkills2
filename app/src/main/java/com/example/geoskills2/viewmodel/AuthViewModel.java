package com.example.geoskills2.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.geoskills2.model.User;
import com.example.geoskills2.repository.AuthRepository;
import com.example.geoskills2.repository.FirestoreRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AuthViewModel extends AndroidViewModel {
    private FirestoreRepository firestoreRepository = FirestoreRepository.getInstance();
    private AuthRepository authRepository = AuthRepository.getInstance();

    private MutableLiveData<FirebaseUser> currentUserVM = new MutableLiveData<>();

    private MutableLiveData<Boolean> sendedEmail = new MutableLiveData<>(false);

    public AuthViewModel(@NonNull Application application) {
        super(application);
        currentUserVM.setValue(authRepository.getCurrentUser().getValue());
    }

    public void registerUser(User user, String password) {
        firestoreRepository.checkUserName(user.getName(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        Log.i("USERNAME_EXISTS", "O nome de usuário " + user.getName() + " já está em uso.");
                        return;
                    } else {
                        authRepository.registerUser(user.getEmail(), password, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    authRepository.getCurrentUser().setValue(authRepository.getAuth().getCurrentUser());
                                    user.setUiid(authRepository.getCurrentUser().getValue().getUid());
                                    firestoreRepository.registerUserOnDb(user, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                currentUserVM.postValue(authRepository.getCurrentUser().getValue());
                                            } else {
                                                Log.d("ERRO_REGISTER_USER", "Erro ao registrar usuário NO BANCO", task.getException());
                                            }
                                        }
                                    });
                                } else {
                                    Log.d("ERRO_REGISTER_USER", "Erro ao registrar usuário", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.d("ERRO_USERNAME", "Erro ao procurar nome de usuário", task.getException());
                }

            }
        });

    }

    public void loginUser(String email, String password) {
        authRepository.loginUser(email, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUserVM.postValue(authRepository.getAuth().getCurrentUser());
                } else {
                    Log.d("ERRO_LOGIN", "Erro ao fazer login", task.getException());
                }
            }
        });
    }

    public void recoverPassword(String email) {
        authRepository.revoverPassword(email, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    sendedEmail.postValue(true);
                }
                else {
                    Log.d("ERRO_RECOVER_PASSWORD", "Erro ao recuperar senha", task.getException());
                }
            }
        });
    }


    public MutableLiveData<FirebaseUser> getCurrentUser() {
        return currentUserVM;
    }
    public MutableLiveData<Boolean> getSendedEmail(){
        return sendedEmail;
    }

    public void logout() {
        authRepository.logout();
        currentUserVM.postValue(null);
    }
}
