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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.ui.main.LoginActivity;
// Pastikan import Activity tujuannya benar
import com.kelompok5.openlibrary.ui.setting.AccountSettingsActivity;

public class SettingFragment extends Fragment {

    private SettingViewModel viewModel;
    private TextView tvUserName, tvUserEmail;
    private LinearLayout menuAccountSettings, menuAboutUs, btnLogout;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        auth = FirebaseAuth.getInstance();

        // 1. Inisialisasi ViewModel
        viewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        // 2. Bind Views
        tvUserName = v.findViewById(R.id.tvUserName);
        tvUserEmail = v.findViewById(R.id.tvUserEmail);
        menuAccountSettings = v.findViewById(R.id.menu_account_settings);
        menuAboutUs = v.findViewById(R.id.menu_about_us);
        btnLogout = v.findViewById(R.id.btnLogout);

        // 3. LOGIKA DATA USER (PENTING: Agar tidak kosong)
        // Kita coba ambil langsung dari Firebase agar pasti muncul
        loadUserData();

        // Opsi Tambahan: Jika ViewModel kamu sudah jalan, biarkan observe ini menyala
        // viewModel.getUserName().observe(getViewLifecycleOwner(), name -> {
        //    if (name != null) tvUserName.setText(name);
        // });
        // viewModel.getUserEmail().observe(getViewLifecycleOwner(), email -> {
        //    if (email != null) tvUserEmail.setText(email);
        // });

        // 4. CLICK LISTENER
        menuAccountSettings.setOnClickListener(view -> {
            // PERBAIKAN: Buka halaman Account Settings
            Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
            startActivity(intent);
        });

        menuAboutUs.setOnClickListener(view -> {
            // TODO: Buka halaman About Us (Nanti bisa dibuat Activity baru)
            Toast.makeText(getContext(), "About Us clicked", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(view -> showLogoutDialog());
    }

    // Fungsi helper untuk mengambil data user langsung
    private void loadUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // Set Email
            String email = user.getEmail();
            tvUserEmail.setText(email);

            // Set Nama
            // Jika user punya DisplayName di Firebase, pakai itu.
            // Jika tidak, ambil nama depan dari email.
            if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                tvUserName.setText(user.getDisplayName());
            } else {
                // Contoh: budi@gmail.com -> Budi
                if (email != null) {
                    String nameFromEmail = email.split("@")[0];
                    tvUserName.setText(capitalize(nameFromEmail));
                } else {
                    tvUserName.setText("User");
                }
            }
        }
    }

    // Fungsi kecil untuk huruf besar di awal
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    if (getActivity() != null) getActivity().finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Agar data ter-refresh saat kembali dari halaman Edit Profile
    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }
}