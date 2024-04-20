package com.example.geoskills2.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geoskills2.R;
import com.example.geoskills2.databinding.FragmentSplashBinding;
import com.example.geoskills2.ultil.Sounds;
import com.example.geoskills2.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.Socket;


public class SplashFragment extends Fragment {
    private FragmentSplashBinding binding;
    private AuthViewModel viewModel;
    private Sounds sounds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        sounds = Sounds.getInstance(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared", getContext().MODE_PRIVATE);
        final boolean sliderShown = onSliderFinished();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sounds.setMusicEnabled(false);
                if (!sliderShown) {
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_mainSliderFragment);
                } else if (viewModel.getCurrentUser().getValue() != null) {
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment);
                } else {
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_auth);
                }
            }
        }, 3000);


    }


    private boolean onSliderFinished() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("slider", getContext().MODE_PRIVATE);
        return sharedPreferences.getBoolean("SLIDER_SHOWN", false);
    }

}