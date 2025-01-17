package com.example.geoskills2.viewmodel;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.geoskills2.R;
import com.example.geoskills2.repository.AuthRepository;
import com.example.geoskills2.repository.FirestoreRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileBottomSheetViewModel extends AndroidViewModel {
    private FirestoreRepository firestoreRepository;
    private AuthRepository authRepository;
    private MutableLiveData<Pair<Boolean, Boolean>> userUpdated;


    public ProfileBottomSheetViewModel(@NonNull Application application) {
        super(application);
        this.authRepository = AuthRepository.getInstance();
        this.firestoreRepository = FirestoreRepository.getInstance();
        this.userUpdated = new MutableLiveData<>(new Pair<Boolean,Boolean>(false, false));

    }

    public void updateNameUser(@NonNull String newName) {
        if(checkNameUserExist(newName)){
            Log.i("USERNAME_EXISTS", "O nome de usuário " + newName + " já está em uso.");
            return;
        }

        firestoreRepository.updateNameUserOnDb(authRepository.getAuth().getUid(), newName, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Pair<Boolean, Boolean> booleanPair = userUpdated.getValue();
                    if( booleanPair != null){
                    userUpdated.postValue(new Pair<>(true, booleanPair.second));
                    }
                }else {
                    // tratar depois essa exceção
                    Log.i("UPDATE_NAME_USER", "Erro ao atualizar o nome de usuário." + task.getException().getMessage());
                }

            }
        });
    }

    public void updateProfileUser(@NonNull int profileImage) {
        if(profileImage != 1 && profileImage != 0){
            Log.i("USERNAME_EXISTS", "Mudança não foi feita pois a nova img é igual a antiga");
            return;
        }

        firestoreRepository.updateProfileImgUserOnDb(authRepository.getAuth().getUid(), profileImage, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Pair<Boolean,Boolean> booleanPair = userUpdated.getValue();
                    if(booleanPair != null){
                        userUpdated.postValue(new Pair<>(booleanPair.first, true));
                    }
                }else{
                    Log.i("UPDATE_PROFILE_IMG_USER", "Erro ao atualizar o a img do usuário." + task.getException().getMessage());
                }
            }
        });
    }

    public boolean checkNameUserExist(String newName) {
        boolean[] exists = new boolean[]{false};
        firestoreRepository.checkUserNameExists(newName, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        exists[0] = true;
                    }
                }else{
                    // tratar depois essa exceção
                    Log.i("USERNAME_EXISTS", "Erro ao verificar se o nome de usuário já existe." + task.getException().getMessage());
                }
            }
        });
        return exists[0];
    }

    public MutableLiveData<Pair<Boolean,Boolean>> getUserUpdated() {
        return userUpdated;
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
