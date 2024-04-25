package com.example.geoskills2.view.homeviewpager;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.geoskills2.R;
import com.example.geoskills2.adapter.HomeViewPagerAdapter;
import com.example.geoskills2.databinding.FragmentMainHomeBinding;
import com.example.geoskills2.model.User;
import com.example.geoskills2.ultil.Sounds;
import com.example.geoskills2.view.profile.ProfileBottomSheet;
import com.example.geoskills2.view.profile.ReloadFragment;
import com.example.geoskills2.viewmodel.AuthViewModel;

import com.example.geoskills2.viewmodel.MainHomeFragmentViewModel;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import java.util.ArrayList;
import java.util.List;


public class MainHomeFragment extends Fragment {

    private FragmentMainHomeBinding binding;
    private List<Fragment> list;
    private Sounds sounds;
    private boolean toggleSoundClick = true;
    private boolean toggleMusicClick = true;
    private ReloadFragment reloadFragment;
    private User mUser;
    private AuthViewModel authViewModel;
    private MainHomeFragmentViewModel homeViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        homeViewModel = new ViewModelProvider(this).get(MainHomeFragmentViewModel.class);
        binding = FragmentMainHomeBinding.inflate(inflater, container, false);
        list = new ArrayList<>();
        initViewPager();
        sounds = Sounds.getInstance(requireContext());
        reloadFragment = new ReloadFragment() {
            @Override
            public void reload() {
                onViewCreated(requireView(), null);
            }
            @Override
            public void reloadInforUser(){
                homeViewModel.reloadInforUser();
            }
        };
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initClicks();
        addInfoUser();
    }

    private void initViewPager() {
        list.add(new HomeFragment());
        list.add(new RankFragment());
        binding.homeViewpager.setAdapter(new HomeViewPagerAdapter(requireActivity(), list));
        binding.homeViewpager.setOffscreenPageLimit(list.size());
        binding.homeViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sounds.transitSound();
                if (position == 0) {
                    binding.homePage.setBackgroundResource(R.drawable.bottom_nav_active);
                    binding.rankPage.setBackgroundResource(R.drawable.bottom_nav);
                } else {
                    binding.rankPage.setBackgroundResource(R.drawable.bottom_nav_active);
                    binding.homePage.setBackgroundResource(R.drawable.bottom_nav);
                }
            }
        });
    }

    private void initClicks() {
        binding.homePage.setOnClickListener(v -> {
            v.setBackgroundResource(R.drawable.bottom_nav_active);
            binding.rankPage.setBackgroundResource(R.drawable.bottom_nav);
            binding.homeViewpager.setCurrentItem(0);
        });
        binding.rankPage.setOnClickListener(v -> {
            v.setBackgroundResource(R.drawable.bottom_nav_active);
            binding.homePage.setBackgroundResource(R.drawable.bottom_nav);
            binding.homeViewpager.setCurrentItem(1);
        });
        binding.btnSetting.setOnClickListener(v -> {
            sounds.transitSound();
            AlertDialog settingDialog = initSettingDialog();
            settingDialog.show();
        });

        binding.perfilUser.setOnClickListener(v -> {
            sounds.transitSound();
            if (mUser != null) {
                ProfileBottomSheet profileBottomSheet = createBottomSheetProfile(mUser.getName(), mUser.getProfileSelected());
                profileBottomSheet.show(getChildFragmentManager(), null);
            }
        });

        ClickShrinkEffectKt.applyClickShrink(binding.homePage, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.rankPage, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.perfilUser, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.btnSetting, .95f, 200L);
    }

    private AlertDialog initSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.setting_dialog, null);
        AlertDialog alertDialog = builder.setView(view).create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animdialog;
        alertDialog.setOnDismissListener(dialog -> sounds.transitSound());
        Button btnSingOut = view.findViewById(R.id.btn_singOut);
        ImageView btnSound = view.findViewById(R.id.sound_btn);
        ImageView btnMusic = view.findViewById(R.id.music_btn);

        btnSingOut.setOnClickListener(v -> {
            AlertDialog loadingDialog = authViewModel.makeLoadingAlert(requireContext());
            alertDialog.dismiss();
            loadingDialog.show();
            sounds.clickSound();
            authViewModel.logout();
            authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), firebaseUser -> {
                if (firebaseUser == null) {
                    loadingDialog.dismiss();
                    Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_auth);
                }
            });
        });


        if (!sounds.getIsMusicEnabled()) {
            btnMusic.setImageResource(R.drawable.ic_music_off);
        }
        if (!sounds.getIsClickSoundEnabled()) {
            btnSound.setImageResource(R.drawable.ic_sound_off);
        }

        btnSound.setOnClickListener(v -> {
            sounds.clickSound();
            if (toggleSoundClick) {
                btnSound.setImageResource(R.drawable.ic_sound_off);
                toggleSoundClick = false;
                sounds.setClickSoundEnabled(false);
            } else {
                btnSound.setImageResource(R.drawable.ic_sound);
                toggleSoundClick = true;
                sounds.setClickSoundEnabled(true);
            }
        });

        btnMusic.setOnClickListener(v -> {
            sounds.clickSound();
            if (toggleMusicClick) {
                btnMusic.setImageResource(R.drawable.ic_music_off);
                toggleMusicClick = false;
                sounds.setMusicEnabled(false);
            } else {
                btnMusic.setImageResource(R.drawable.ic_music);
                toggleMusicClick = true;
                sounds.setMusicEnabled(true);
            }
        });

        ClickShrinkEffectKt.applyClickShrink(btnSound, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(btnMusic, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(btnSingOut, .95f, 200L);

        return alertDialog;
    }

    public void addInfoUser() {
        homeViewModel.reloadInforUser();
        homeViewModel.setUserGetted(getViewLifecycleOwner());
        homeViewModel.getUserGetted().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                this.mUser = user;
                binding.textNameUser.setText(user.getName());
                binding.textPoints.setText(String.valueOf(user.getPoints()));
                if(user.getProfileSelected() == 0){
                    binding.profileImg.setImageResource(R.drawable.boy_profile);
                }else{
                    binding.profileImg.setImageResource(R.drawable.girl_profile);
                }
            }
        });
    }

    public ProfileBottomSheet createBottomSheetProfile(String name, int profileIm) {
        return new ProfileBottomSheet(name, profileIm, reloadFragment);
    }
}