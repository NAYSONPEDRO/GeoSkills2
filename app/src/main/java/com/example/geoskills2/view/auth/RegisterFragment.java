package com.example.geoskills2.view.auth;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geoskills2.R;
import com.example.geoskills2.databinding.FragmentRegisterBinding;
import com.example.geoskills2.model.User;
import com.example.geoskills2.ultil.Sounds;
import com.example.geoskills2.viewmodel.AuthViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import org.aviran.cookiebar2.CookieBar;

public class RegisterFragment extends Fragment{
    private AuthViewModel viewModel;
    private Sounds sounds;
    private FragmentRegisterBinding binding;
    private int profileSelected = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        sounds = Sounds.getInstance(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        initClick();
    }

    private void initClick(){
        binding.btnRegister.setOnClickListener(v->{
            sounds.clickSound();
            registerUser(binding.editName.getText().toString(), binding.editEmail.getText().toString(), binding.editPassword.getText().toString());
        });

        binding.boyProfile.setOnClickListener(v -> {
            sounds.clickSound();
            v.setBackgroundResource(R.drawable.profile_selected);
            binding.girlProfile.setBackgroundResource(R.drawable.profile_selection_bg);
            profileSelected = 0;
        });
        binding.girlProfile.setOnClickListener(v -> {
            sounds.clickSound();
            v.setBackgroundResource(R.drawable.profile_selected);
            binding.boyProfile.setBackgroundResource(R.drawable.profile_selection_bg);
            profileSelected = 1;
        });

        binding.btnLogin.setOnClickListener(v->{
            sounds.clickSound();
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
        });
        binding.btnRecover.setOnClickListener(v->{
            sounds.clickSound();
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_recoverFragment);
        });

        ClickShrinkEffectKt.applyClickShrink(binding.btnLogin, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.btnRegister, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.btnRecover, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.boyProfile, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.girlProfile, .95f, 200L);

    }

    private void registerUser(String name, String email, String senha){


            if (name.isEmpty() || email.isEmpty() || senha.isEmpty() || profileSelected == -1) {
                CookieBar.build(requireActivity()).setTitle("Preencha todos os campos").setMessage("Preencha todos os campos do formulário de recuperação para poder continuar")
                        .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.red).setDuration(4000).setIcon(R.drawable.ic_error).show();
                return;
            }
            viewModel.registerUser(new User(name, email, 0, profileSelected), senha);
            AlertDialog alertDialog = viewModel.makeLoadingAlert(requireContext());
            alertDialog.show();
            viewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
                @Override
                public void onChanged(FirebaseUser firebaseUser) {
                    if (firebaseUser != null) {
                        alertDialog.dismiss();
                        Navigation.findNavController(requireView()).navigate(R.id.action_global_homeFragment);
                    }
                }
            });

    }


}