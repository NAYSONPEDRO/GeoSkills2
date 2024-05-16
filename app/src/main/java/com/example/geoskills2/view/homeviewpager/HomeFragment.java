package com.example.geoskills2.view.homeviewpager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geoskills2.R;
import com.example.geoskills2.databinding.FragmentHomeBinding;
import com.example.geoskills2.ultil.Sounds;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Sounds sounds;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sounds = Sounds.getInstance(requireContext());
        ClickShrinkEffectKt.applyClickShrink(binding.btnPlayQuiz, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.btnPlayCoordinates, .95f, 200L);
        binding.btnPlayQuiz.setOnClickListener(v->{
            sounds.clickSound();
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_quizGameFragment);
        });
        binding.btnPlayCoordinates.setOnClickListener(v->{
            sounds.clickSound();
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_coordinatesGameFragment);
        });

    }
}