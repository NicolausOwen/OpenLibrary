package com.kelompok5.openlibrary.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.ui.main.LoginActivity;
import com.kelompok5.openlibrary.ui.main.RegisterActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_2);

        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}