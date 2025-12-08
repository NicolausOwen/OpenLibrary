package com.kelompok5.openlibrary.ui.setting;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.ui.main.LoginActivity;

public class SettingFragment extends Fragment {

    private SettingViewModel viewModel;
    private TextView tvUserName, tvUserEmail;
    private LinearLayout btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        tvUserName = v.findViewById(R.id.tvUserName);
        tvUserEmail = v.findViewById(R.id.tvUserEmail);
        btnLogout = v.findViewById(R.id.btnLogout);

        // Observe user data
        viewModel.getUserName().observe(getViewLifecycleOwner(), tvUserName::setText);
        viewModel.getUserEmail().observe(getViewLifecycleOwner(), tvUserEmail::setText);

        btnLogout.setOnClickListener(v1 -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (d, w) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
