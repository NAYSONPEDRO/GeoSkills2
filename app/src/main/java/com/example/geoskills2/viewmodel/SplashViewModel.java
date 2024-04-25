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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;



public class SplashViewModel extends AndroidViewModel {
    private FirestoreRepository firestoreRepository;
    private AuthRepository authRepository;
    private MutableLiveData<User> userGetted;
    public SplashViewModel(@NonNull Application application) {
        super(application);
        firestoreRepository = FirestoreRepository.getInstance();
        authRepository = AuthRepository.getInstance();
        userGetted = new MutableLiveData<>();

    }

    public void getUserFromDb(){
        // Mover a fun getUserFromDb pra q, porque se ela ficar no MainSliderViewModel,  n vai dar pra verificar e o motivo é óbvio ( viewmodel é de lá, ou seja só vai chamar a fun lá)
        firestoreRepository.getUserOnDb(authRepository.getCurrentUserId(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult() != null){
                        User user = task.getResult().toObject(User.class);
                        if(user != null) {
                            userGetted.postValue(user);
                            firestoreRepository.getUserGetted().postValue(user);
                        }
                    }else{
                        Log.i("ERROR_USER_NOT_FOUND","O usuario não foi encontrado no db" +  task.getException().getMessage());
                    }
                }else{
                    Log.i("ERROR_GET_USER","Erro ao buscar usuario no db" +  task.getException().getMessage());
                }
            }
        });

    }

    public MutableLiveData<User> getUserGetted() {
        return userGetted;
    }

    public MutableLiveData<FirebaseUser> getCurrentUser(){
        return authRepository.getCurrentUser();
    }

}
