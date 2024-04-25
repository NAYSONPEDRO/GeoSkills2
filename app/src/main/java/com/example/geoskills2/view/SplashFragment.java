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
import com.example.geoskills2.model.User;
import com.example.geoskills2.ultil.Sounds;
import com.example.geoskills2.viewmodel.AuthViewModel;
import com.example.geoskills2.viewmodel.MainHomeFragmentViewModel;
import com.example.geoskills2.viewmodel.SplashViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.Socket;


public class SplashFragment extends Fragment {
    private FragmentSplashBinding binding;
    private SplashViewModel viewModel;

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
        viewModel = new ViewModelProvider(requireActivity()).get(SplashViewModel.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared", getContext().MODE_PRIVATE);
        final boolean sliderShown = onSliderFinished();

        new Handler().postDelayed(() -> {
            sounds.setMusicEnabled(false);
            if (!sliderShown) {
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_mainSliderFragment);
            } else if (viewModel.getCurrentUser().getValue() == null) {
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_auth);
            } else {
                viewModel.getUserFromDb();
                viewModel.getUserGetted().observe(getViewLifecycleOwner(), new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if(user != null) {
                            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment);
                        }
                    }
                });
            }
        }, 3000);


    }

    private boolean onSliderFinished() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("slider", getContext().MODE_PRIVATE);
        return sharedPreferences.getBoolean("SLIDER_SHOWN", false);
    }

}