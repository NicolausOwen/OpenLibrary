package com.kelompok5.openlibrary.ui.setting;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.kelompok5.openlibrary.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Tombol Back
        ImageView btnBack = findViewById(R.id.btn_back_about);
        btnBack.setOnClickListener(v -> finish());
    }
}