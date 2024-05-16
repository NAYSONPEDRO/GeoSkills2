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
import com.example.geoskills2.repository.AuthRepository;
import com.example.geoskills2.repository.FirestoreRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.checkerframework.checker.units.qual.A;

public class QuizViewModel extends AndroidViewModel {
    private FirestoreRepository firestoreRepository;
    private MutableLiveData<Boolean> isEndedQuiz;
    private AuthRepository authRepository;
    public QuizViewModel(@NonNull Application application) {
        super(application);
        firestoreRepository = FirestoreRepository.getInstance();
        authRepository = AuthRepository.getInstance();
        isEndedQuiz = new MutableLiveData<>(false);
    }

    public void updatePointsUser(int points){
        firestoreRepository.updatePointsUserOnDb(authRepository.getCurrentUserId(), points, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    isEndedQuiz.postValue(true);
                }else{
                    Log.i("ERROR_UPDATE_POINTS", "Error : " + task.getException().getMessage());

                }
            }
        });
    }

    public MutableLiveData<Boolean> getIsEndedQuiz() {
        return isEndedQuiz;
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
