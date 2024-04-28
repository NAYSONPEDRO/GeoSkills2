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
import com.example.geoskills2.databinding.FragmentLoginBinding;
import com.example.geoskills2.ultil.Sounds;
import com.example.geoskills2.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import org.aviran.cookiebar2.CookieBar;


public class LoginFragment extends Fragment{
    private AuthViewModel viewModel;
    private FragmentLoginBinding binding;
    private Sounds sounds;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
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
        binding.btnLogin.setOnClickListener(v->{
            loginUser(binding.editEmail.getText().toString(), binding.editPassword.getText().toString());
            sounds.clickSound();
        });
        binding.btnRegister.setOnClickListener(v->{
            sounds.clickSound();
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment);
        });
        binding.btnRecover.setOnClickListener(v->{
            sounds.clickSound();
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_recoverFragment);
        });

        ClickShrinkEffectKt.applyClickShrink(binding.btnLogin, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.btnRegister, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.btnRecover, .95f, 200L);
    }

    private void loginUser(String email, String password){
        if(email.isEmpty() || password.isEmpty()){
//            Snackbar.make(requireView(), "Preencha todos os campos", Snackbar.LENGTH_SHORT).show();
            CookieBar.build(requireActivity()).setTitle("Preencha todos os campos").setMessage("Preencha todos os campos do formulário de login para poder continuar")
                    .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.red).setDuration(4000).setIcon(R.drawable.ic_error).show();
            return;
        }
        ErrorData errorData = viewModel.loginUser(email, password);
        if(errorData != null){
            CookieBar.build(requireActivity()).setTitle(errorData.getTitle())
                    .setMessage(errorData.getMessage())
                    .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.red).setDuration(4000).setIcon(R.drawable.ic_error).show();
        }
        AlertDialog alertDialog = viewModel.makeLoadingAlert(requireContext());
        alertDialog.show();
        viewModel.getErrorInLogin().observe(getViewLifecycleOwner(), new Observer<ErrorData>() {
            @Override
            public void onChanged(ErrorData errorData) {
                if(errorData != null){
                    CookieBar.build(requireActivity()).setTitle(errorData.getTitle())
                            .setMessage(errorData.getMessage())
                            .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.red).setDuration(4000).setIcon(R.drawable.ic_error).show();
                }
            }
        });
        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    alertDialog.dismiss();
                    Navigation.findNavController(requireView()).navigate(R.id.action_global_homeFragment);
                }
            }
        });
    }


}