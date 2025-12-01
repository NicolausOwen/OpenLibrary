package com.kelompok5.openlibrary.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.ui.book.BookFragment;
import com.kelompok5.openlibrary.ui.library.LibraryFragment;
import com.kelompok5.openlibrary.ui.setting.SettingFragment;

public class MainActivity extends AppCompatActivity {

    // Deklarasi fragment final: Books, Library, Setting
    final Fragment fragmentBooks = new BookFragment();
    final Fragment fragmentLibrary = new LibraryFragment();
    final Fragment fragmentSetting = new SettingFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragmentBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        // Setup fragment di FragmentManager
        fm.beginTransaction().add(R.id.fragment_container, fragmentSetting, "3").hide(fragmentSetting).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragmentLibrary, "2").hide(fragmentLibrary).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragmentBooks, "1").commit();

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_books) {
                fm.beginTransaction().hide(active).show(fragmentBooks).commit();
                active = fragmentBooks;
                return true;
            } else if (itemId == R.id.nav_library) { // Diubah ke nav_library
                fm.beginTransaction().hide(active).show(fragmentLibrary).commit();
                active = fragmentLibrary;
                return true;
            } else if (itemId == R.id.nav_setting) {
                fm.beginTransaction().hide(active).show(fragmentSetting).commit();
                active = fragmentSetting;
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_books);
        }
    }
}