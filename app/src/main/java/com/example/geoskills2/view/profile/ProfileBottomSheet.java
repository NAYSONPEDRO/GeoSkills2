package com.example.geoskills2.view.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.geoskills2.R;
import com.example.geoskills2.ultil.Sounds;
import com.example.geoskills2.viewmodel.ProfileBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import org.aviran.cookiebar2.CookieBar;

public class ProfileBottomSheet extends BottomSheetDialogFragment {
    private String name;
    private int profileImage;
    private int newProfileImage;
    private Button saveButton;
    private ImageButton replaceImgButton;
    private EditText editName;
    private LinearLayout profileAllContent;
    private ReloadFragment reloadFragment;
    private ImageView imgProfile;
    private Sounds sounds;
    private ProfileBottomSheetViewModel viewModel;


    public ProfileBottomSheet(String name, int profileImage, ReloadFragment reloadFragment) {
        this.name = name;
        this.profileImage = profileImage;
        this.reloadFragment = reloadFragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ProfileBottomSheetViewModel.class);
        View view = inflater.inflate(R.layout.profile_edit, container, false);
        saveButton = view.findViewById(R.id.save_name);
        replaceImgButton = view.findViewById(R.id.btn_replace_img);
        editName = view.findViewById(R.id.edit_name);
        profileAllContent = view.findViewById(R.id.profile_all_content);
        imgProfile = view.findViewById(R.id.img_profile);
        sounds = Sounds.getInstance(requireContext());
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editName.setText(name);
        if (profileImage == 0) {
            imgProfile.setImageResource(R.drawable.boy_profile);
        } else {
            imgProfile.setImageResource(R.drawable.girl_profile);
        }

        initClicks();
        configKeyBoardEndEdit(view);


    }

    private void initClicks() {

        newProfileImage = profileImage;
        replaceImgButton.setOnClickListener(v -> {
            sounds.clickSound();
            if (newProfileImage == 0) {
                imgProfile.setImageResource(R.drawable.girl_profile);
                newProfileImage = 1;
            } else {
                imgProfile.setImageResource(R.drawable.boy_profile);
                newProfileImage = 0;
            }
        });

        saveButton.setOnClickListener(v -> {
            sounds.clickSound();

            upadateInforUser();
        });

        ClickShrinkEffectKt.applyClickShrink(replaceImgButton, .95f, 150L);
        ClickShrinkEffectKt.applyClickShrink(saveButton, .95f, 150L);
    }

    private void upadateInforUser() {

        AlertDialog alertDialog = viewModel.makeLoadingAlert(requireContext());
        String newName = editName.getText().toString();
        if (profileImage == newProfileImage && newName.equals(name)) {
            CookieBar.build(requireActivity()).setTitle("Mesmas informações").setMessage("Modifique as informações do usuário para poder atualizar")
                    .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.red).setDuration(4000).setIcon(R.drawable.ic_error).show();
        } else if (profileImage != newProfileImage && newName.equals(name)) {
            updateProfileImage(alertDialog);
        } else if (profileImage == newProfileImage && !newName.equals(name)) {
            updateNewName(newName, alertDialog);
        } else {
            updateNewName(newName, null);
            updateProfileImage(alertDialog);
        }
    }


    private void updateNewName(String newName, AlertDialog alertDialog) {
        if (newName.length() > 20) {
            editName.setError("O nome pode ter no máximo 20 caracteres");
            return;
        }
        if (newName.isEmpty()) {
            editName.setError("O nome não pode estar vazio");
            return;
        }
        if (newName.equalsIgnoreCase(name)) {
            editName.setError("O novo nome não pode ser igual ao atual");
            return;
        }

        if (alertDialog != null)
            alertDialog.show();

        viewModel.updateNameUser(newName);
        viewModel.getUserUpdated().observe(getViewLifecycleOwner(), new Observer<Pair<Boolean, Boolean>>() {
            @Override
            public void onChanged(Pair<Boolean, Boolean> booleanBooleanPair) {
                if (booleanBooleanPair.first) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                        dismiss();
                        reloadFragment.reloadInforUser();
                        reloadFragment.reload();
                    }
                }
            }
        });

    }

    private void updateProfileImage(AlertDialog alertDialog) {
//        if (newProfileImage == profileImage) {
//            Log.i("IMAGE_PROFILE_IS_EQUAL", "A imagem à ser atualizada não pode ser a mesma");
//            return;
//        }
        viewModel.updateProfileUser(newProfileImage);
        alertDialog.show();
        viewModel.getUserUpdated().observe(getViewLifecycleOwner(), new Observer<Pair<Boolean, Boolean>>() {
            @Override
            public void onChanged(Pair<Boolean, Boolean> booleanBooleanPair) {
                if (booleanBooleanPair.second) {
                    alertDialog.dismiss();
                    dismiss();
                    reloadFragment.reloadInforUser();
                    reloadFragment.reload();
                }
            }
        });
    }


    private void configKeyBoardEndEdit(View view) {
        final BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) getView().getParent());
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                view.getWindowVisibleDisplayFrame(rect);
                int screenHeight = view.getRootView().getHeight();
                int keypadHeight = screenHeight - rect.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Sounds.getInstance(requireContext()).transitSound();
    }
}
