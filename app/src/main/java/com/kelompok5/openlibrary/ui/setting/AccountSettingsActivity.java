package com.kelompok5.openlibrary.ui.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.kelompok5.openlibrary.R;

public class AccountSettingsActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        auth = FirebaseAuth.getInstance();

        ImageView btnBack = findViewById(R.id.btn_back);

        // 1. Inisialisasi ID (Pastikan ID et_name ada di XML)
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        Button btnConfirm = findViewById(R.id.btn_confirm);

        // 2. Tampilkan Data User Saat Ini (Prefill)
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            etEmail.setText(user.getEmail());

            // Tampilkan nama jika ada
            if (user.getDisplayName() != null) {
                etName.setText(user.getDisplayName());
            }
        }

        btnBack.setOnClickListener(v -> finish());
        btnConfirm.setOnClickListener(v -> updateAccount());
    }

    private void updateAccount() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        String newName = etName.getText().toString().trim();
        String newEmail = etEmail.getText().toString().trim();
        String newPassword = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(newName)) {
            etName.setError("Name cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(newEmail)) {
            etEmail.setError("Email cannot be empty");
            return;
        }

        // --- PROSES UPDATE BERTAHAP ---
        // 1. Update Nama Dulu
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Jika update nama sukses, lanjut update Email
                        updateEmailProcess(user, newEmail, newPassword);
                    } else {
                        Toast.makeText(this, "Failed to update name", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateEmailProcess(FirebaseUser user, String newEmail, String newPassword) {
        // Cek apakah email berubah?
        if (!newEmail.equals(user.getEmail())) {
            user.updateEmail(newEmail)
                    .addOnSuccessListener(aVoid -> {
                        // Jika email sukses, lanjut update Password
                        updatePasswordProcess(user, newPassword);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Update Email Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        } else {
            // Email tidak berubah, langsung cek password
            updatePasswordProcess(user, newPassword);
        }
    }

    private void updatePasswordProcess(FirebaseUser user, String newPassword) {
        // Cek apakah password diisi?
        if (!TextUtils.isEmpty(newPassword)) {
            if (newPassword.length() < 6) {
                etPassword.setError("Password min 6 chars");
                return;
            }

            user.updatePassword(newPassword)
                    .addOnSuccessListener(aVoid -> showSuccess())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Update Password Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        } else {
            // Password kosong (artinya user tidak mau ganti password), Selesai.
            showSuccess();
        }
    }

    private void showSuccess() {
        Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
        finish(); // Kembali ke halaman sebelumnya agar data ter-refresh
    }
}