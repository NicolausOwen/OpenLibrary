package com.kelompok5.openlibrary.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.kelompok5.openlibrary.ui.main.MainActivity;
import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.ui.main.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            }

            finish();

        }, 3000);
    }
}