package com.example.geoskills2.view.slider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.geoskills2.R;
import com.example.geoskills2.adapter.SlideViewPagerAdapter;
import com.example.geoskills2.databinding.FragmentMainSliderBinding;
import com.example.geoskills2.ultil.Sounds;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import java.util.ArrayList;
import java.util.List;

public class MainSliderFragment extends Fragment {
    private List<Fragment> list = new ArrayList<>();
    private FragmentMainSliderBinding binding;
    private Sounds sounds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainSliderBinding.inflate(inflater, container, false);
        sounds = Sounds.getInstance(requireContext());
        initViewPager();
        return binding.getRoot();

    }

    public void onSliderFinished() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("slider", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SLIDER_SHOWN", true);
        editor.commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void initViewPager() {
        list.add(new SlideFirstFragment());
        list.add(new SlideSecondFragment());
        list.add(new SlideThirdFragment());

        binding.viewpager2.setAdapter(new SlideViewPagerAdapter(requireActivity(), list));
        binding.viewpager2.setOffscreenPageLimit(2);

        binding.dotsIndicator.attachTo(binding.viewpager2);

        binding.btnSkipSlide.setOnClickListener(v -> {
            binding.viewpager2.setCurrentItem(2);
        });
        binding.btnInit.setOnClickListener(v -> {
            onSliderFinished();
            sounds.clickSound();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_mainSliderFragment_to_loginFragment);
        });

        binding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0: {
                        binding.btnInit.setVisibility(View.INVISIBLE);
                        binding.arrowBackSlide.setVisibility(View.INVISIBLE);

                        binding.arrowNextSlide.setVisibility(View.VISIBLE);
                        binding.arrowNextSlide.setOnClickListener(v -> {
                            binding.viewpager2.setCurrentItem(1);
                        });
                    }
                    break;
                    case 1: {
                        binding.btnInit.setVisibility(View.INVISIBLE);
                        binding.arrowBackSlide.setVisibility(View.VISIBLE);
                        binding.arrowNextSlide.setVisibility(View.VISIBLE);
                        binding.arrowNextSlide.setOnClickListener(v -> {
                            binding.viewpager2.setCurrentItem(2);
                        });
                        binding.arrowBackSlide.setOnClickListener(v -> {
                            binding.viewpager2.setCurrentItem(0);
                        });
                    }
                    break;
                    case 2: {
                        binding.arrowBackSlide.setOnClickListener(v -> {
                            binding.viewpager2.setCurrentItem(1);
                        });
                        binding.arrowNextSlide.setVisibility(View.INVISIBLE);
                        binding.btnInit.setVisibility(View.VISIBLE);

                    }
                    break;
                    default: {
                        Toast.makeText(requireContext(), "Erro sei la o q", Toast.LENGTH_SHORT).show();
                    }
                }
                super.onPageSelected(position);
            }
        });

        ClickShrinkEffectKt.applyClickShrink(binding.btnInit, .95f, 150L);
        ClickShrinkEffectKt.applyClickShrink(binding.btnSkipSlide, .95f, 150L);
        ClickShrinkEffectKt.applyClickShrink(binding.arrowNextSlide, .95f, 150L);
        ClickShrinkEffectKt.applyClickShrink(binding.arrowBackSlide, .95f, 150L);

    }
}