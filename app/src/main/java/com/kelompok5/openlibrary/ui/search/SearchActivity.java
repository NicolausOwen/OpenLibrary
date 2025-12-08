package com.kelompok5.openlibrary.ui.search;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.ui.book.BookAdapter; // Pakai adapter vertical yang baru dibuat
import com.kelompok5.openlibrary.ui.book.BookViewModel;

public class SearchActivity extends AppCompatActivity {

    private EditText etSearchQuery;
    private ImageView btnClose;
    private RecyclerView rvSearchResults;
    private TextView tvLabelCategory;

    private BookViewModel viewModel;
    private BookAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 1. Init ViewModel
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // 2. Init View
        etSearchQuery = findViewById(R.id.et_search_query);
        btnClose = findViewById(R.id.btn_close_search);
        rvSearchResults = findViewById(R.id.rv_search_results);
        tvLabelCategory = findViewById(R.id.tv_label_category);

        // 3. Setup RecyclerView
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new BookAdapter(this);
        rvSearchResults.setAdapter(searchAdapter);

        // 4. Observe Data dari ViewModel
        viewModel.getBooks().observe(this, books -> {
            if (books != null && !books.isEmpty()) {
                // Sembunyikan label kategori, tampilkan hasil
                tvLabelCategory.setVisibility(View.GONE);
                rvSearchResults.setVisibility(View.VISIBLE);
                searchAdapter.setBooks(books);
            } else {
                // Kosong
                // Bisa tambahkan logic "Data Not Found" di sini
            }
        });

        // 5. Action Listener untuk Keyboard (Tombol Search/Enter)
        etSearchQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                String query = etSearchQuery.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                }
                return true;
            }
            return false;
        });

        // 6. Tombol Close (Back)
        btnClose.setOnClickListener(v -> finish());

        // Fokus langsung ke keyboard saat dibuka
        etSearchQuery.requestFocus();
    }

    private void performSearch(String query) {
        Toast.makeText(this, "Searching: " + query, Toast.LENGTH_SHORT).show();
        // Panggil fungsi search di ViewModel
        viewModel.searchBooks(query);
    }
}