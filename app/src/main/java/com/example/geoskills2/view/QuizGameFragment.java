package com.example.geoskills2.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoskills2.R;
import com.example.geoskills2.databinding.FragmentQuizGameBinding;
import com.example.geoskills2.model.Question;
import com.example.geoskills2.repository.GameQuizRepository;
import com.example.geoskills2.ultil.Sounds;
import com.example.geoskills2.viewmodel.QuizViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import org.aviran.cookiebar2.CookieBar;

import java.util.ArrayList;
import java.util.List;


public class QuizGameFragment extends Fragment {
    private QuizViewModel viewModel;
    private FragmentQuizGameBinding binding;
    private Sounds sounds;
    private final List<Question> questions = GameQuizRepository.getListQuestions();
    private NavController navController;
    private int seconds = 10;
    private int min = 0;
    private boolean closehandler = false;
    private Handler handler;
    private Runnable runnable;
    private String currentOptionSelect = "";
    private int currentOptionSelectIndex = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                closehandler = true;
                navController.navigate(R.id.action_quizGameFragment_to_homeFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuizGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);
        navController = Navigation.findNavController(view);
        sounds = Sounds.getInstance(requireContext());
        initGame();
    }

    public void initGame() {
        binding.textCont.setText((currentOptionSelectIndex + 1) + " / " + questions.size());
        initClicks();
        initTimer();

        if (currentOptionSelectIndex == 0) {
            Question currentQuestion = questions.get(currentOptionSelectIndex);
            String[] choices = currentQuestion.getChoices();
            binding.textQuestion.setText(currentQuestion.getStatement());
            binding.bntOption1.setText(choices[0]);
            binding.bntOption2.setText(choices[1]);
            binding.bntOption3.setText(choices[2]);
            binding.bntOption4.setText(choices[3]);
        }
    }

    public void initClicks() {
        binding.bntOption1.setOnClickListener(v -> {
            sounds.clickSound();
            if (currentOptionSelect.isEmpty()) {
                currentOptionSelect = binding.bntOption1.getText().toString();
                v.setBackgroundResource(R.drawable.option_wrong_bg);
                revealCorretOption();

                questions.get(currentOptionSelectIndex).setUserAnswer(currentOptionSelect);
            }
        });
        binding.bntOption2.setOnClickListener(v -> {
            sounds.clickSound();
            if (currentOptionSelect.isEmpty()) {
                currentOptionSelect = binding.bntOption2.getText().toString();
                v.setBackgroundResource(R.drawable.option_wrong_bg);
                revealCorretOption();

                questions.get(currentOptionSelectIndex).setUserAnswer(currentOptionSelect);
            }
        });
        binding.bntOption3.setOnClickListener(v -> {
            sounds.clickSound();
            if (currentOptionSelect.isEmpty()) {
                currentOptionSelect = binding.bntOption3.getText().toString();
                v.setBackgroundResource(R.drawable.option_wrong_bg);
                revealCorretOption();

                questions.get(currentOptionSelectIndex).setUserAnswer(currentOptionSelect);
            }
        });
        binding.bntOption4.setOnClickListener(v -> {
            sounds.clickSound();
            if (currentOptionSelect.isEmpty()) {
                currentOptionSelect = binding.bntOption4.getText().toString();
                v.setBackgroundResource(R.drawable.option_wrong_bg);
                revealCorretOption();

                questions.get(currentOptionSelectIndex).setUserAnswer(currentOptionSelect);
            }
        });

        binding.btnNextQuestion.setOnClickListener(v -> {
            sounds.clickSound();
            if (currentOptionSelect.isEmpty()) {
                Snackbar.make(requireView(), "Selecione uma das alternativas", Snackbar.LENGTH_SHORT);
            } else {
                nextQuestion();
            }
        });
        ClickShrinkEffectKt.applyClickShrink(binding.bntOption1, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.bntOption2, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.bntOption3, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.bntOption4, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.btnBack, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.btnNextQuestion, .95f, 200L);

        binding.btnBack.setOnClickListener(v -> {
            sounds.clickSound();
            closehandler = true;
            navController.navigate(R.id.action_quizGameFragment_to_homeFragment);
        });

    }

    private void nextQuestion() {
        currentOptionSelectIndex++;
        if ((currentOptionSelectIndex + 1) == questions.size()) {
            binding.btnNextQuestion.setText("Finalizar");
            binding.btnNextQuestion.setBackgroundResource(R.drawable.btn_login_bg);
        }

        if (currentOptionSelectIndex < questions.size()) {
            currentOptionSelect = "";

            binding.bntOption1.setBackgroundResource(R.drawable.option_bg);
            binding.bntOption2.setBackgroundResource(R.drawable.option_bg);
            binding.bntOption3.setBackgroundResource(R.drawable.option_bg);
            binding.bntOption4.setBackgroundResource(R.drawable.option_bg);

            Question currentQuestion = questions.get(currentOptionSelectIndex);
            String[] choices = currentQuestion.getChoices();
            binding.textQuestion.setText(currentQuestion.getStatement());
            binding.bntOption1.setText(choices[0]);
            binding.bntOption2.setText(choices[1]);
            binding.bntOption3.setText(choices[2]);
            binding.bntOption4.setText(choices[3]);

            binding.textCont.setText((currentOptionSelectIndex + 1) + " / " + questions.size());
        } else {
            viewModel.updatePointsUser(getResultPoins());
            AlertDialog finhisAlert = makeAlertDialog(requireContext(), getResultPoins());
            AlertDialog loadingAlert = viewModel.makeLoadingAlert(requireContext());
            loadingAlert.show();
            viewModel.getIsEndedQuiz().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        loadingAlert.dismiss();
                        finhisAlert.show();
                    }
                }
            });
            closehandler = true;

        }
    }

    private AlertDialog makeAlertDialog(Context context, int points) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.quiz_end_alert_dialog, null);
        AlertDialog alertDialog = builder.setView(view).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animdialog;
        alertDialog.setOnDismissListener(dialog -> sounds.transitSound());

        ImageView stars = view.findViewById(R.id.stars_user_points);
        TextView textPoints = view.findViewById(R.id.user_points);
        Button btnFinishQuiz = view.findViewById(R.id.btn_finish_quiz);

        textPoints.setText("Pontuação: +" + points);

        switch (points){
            case 0:
                stars.setImageResource(R.drawable.stars_0);
                break;
            case 1:
                stars.setImageResource(R.drawable.stars_1);
                break;
            case 2:
                stars.setImageResource(R.drawable.stars_2);
                break;
            default:
                stars.setImageResource(R.drawable.stars_3);
        }

        btnFinishQuiz.setOnClickListener(v-> {
            sounds.clickSound();
            alertDialog.dismiss();
            navController.navigate(R.id.action_quizGameFragment_to_homeFragment);
        });
        ClickShrinkEffectKt.applyClickShrink(btnFinishQuiz, .95f, 200L);

        return alertDialog;
    }

    private void revealCorretOption() {
        String correctAnswer = questions.get(currentOptionSelectIndex).getAnswer();

        if (binding.bntOption1.getText().toString().equals(correctAnswer)) {
            binding.bntOption1.setBackgroundResource(R.drawable.option_correct_bg);
        } else if (binding.bntOption2.getText().toString().equals(correctAnswer)) {
            binding.bntOption2.setBackgroundResource(R.drawable.option_correct_bg);
        } else if (binding.bntOption3.getText().toString().equals(correctAnswer)) {
            binding.bntOption3.setBackgroundResource(R.drawable.option_correct_bg);
        } else if (binding.bntOption4.getText().toString().equals(correctAnswer)) {
            binding.bntOption4.setBackgroundResource(R.drawable.option_correct_bg);
        }
    }


    private void initTimer() {
        handler = new Handler();
        runnable = () -> {


            binding.textTimer.setText(String.format("%02d:%02d", min, seconds));
            if (min >= 1 && seconds == 0) {
                min--;
                seconds = 59;
            } else if (seconds != 0) {
                seconds--;

            } else if (seconds == 0 && min == 0) {
                //t.makeText(requireContext(), "Acabou o tempo \n Pontuação : " + getResultPoins(), Toast.LENGTH_LONG).show();
                CookieBar.build(requireActivity()).setTitle("Acabou o tempo").setMessage("Tente novamente!")
                        .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.yellow).setDuration(4000).setIcon(R.drawable.ic_error).show();
                navController.navigate(R.id.action_quizGameFragment_to_homeFragment);
                handler.removeCallbacks(runnable);
                closehandler = true;
            }
            if (!closehandler)
                handler.postDelayed(runnable, 1000);
        };


        runnable.run();
    }

    private int getResultPoins() {
        int correctsAnswer = 0;
        for (Question question : questions) {
            if (question.getAnswer().equals(question.getUserAnswer())) {
                correctsAnswer++;
            }
        }
        return correctsAnswer;
    }

}