package com.kelompok5.openlibrary.ui.category;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.ui.book.BookAdapter;
import com.kelompok5.openlibrary.ui.book.BookViewModel;

public class CategoryResultActivity extends AppCompatActivity {

    private TextView tvCategoryTitle, tvTotalBooks;
    private RecyclerView rvCategoryBooks;
    private ImageView btnBack;

    private BookViewModel viewModel;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_result);

        // 1. Ambil Data Intent
        String categoryRawName = getIntent().getStringExtra("EXTRA_CATEGORY_NAME");
        if (categoryRawName == null) categoryRawName = "General";

        // 2. Init ViewModel
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // 3. Init UI
        tvCategoryTitle = findViewById(R.id.tv_category_title);
        tvTotalBooks = findViewById(R.id.tv_total_books);
        rvCategoryBooks = findViewById(R.id.rv_category_books);
        btnBack = findViewById(R.id.btn_back_category);

        // Set Judul Halaman
        tvCategoryTitle.setText(categoryRawName);

        // 4. Setup RecyclerView
        rvCategoryBooks.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this);
        rvCategoryBooks.setAdapter(bookAdapter);

        // 5. Observe Data
        viewModel.getBooks().observe(this, books -> {
            if (books != null) {
                bookAdapter.setBooks(books);
                tvTotalBooks.setText(books.size() + " Books Found");
            } else {
                tvTotalBooks.setText("No Books Found");
            }
        });

        // Error Handling
        viewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
                tvTotalBooks.setText("Error loading books");
            }
        });

        // 6. Tombol Back
        btnBack.setOnClickListener(v -> finish());

        // 7. LOAD DATA BERDASARKAN KATEGORI
        loadBooksByCategory(categoryRawName);
    }

    private void loadBooksByCategory(String categoryName) {
        // Tampilkan Loading (Opsional)
        tvTotalBooks.setText("Searching for " + categoryName + "...");

        // PEMBERSIHAN QUERY
        // Kita ubah "Actions & Adventure" menjadi "Actions Adventure"
        // agar API lebih mudah menemukannya.
        String query = categoryName;

        // Hapus simbol "&" agar tidak merusak URL query
        if (query.contains("&")) {
            query = query.replace("&", "");
        }

        // Hapus spasi ganda jika ada
        query = query.trim();

        // Debugging: Munculkan toast untuk melihat apa yang sedang dicari
        // Toast.makeText(this, "Query: " + query, Toast.LENGTH_SHORT).show();

        // Panggil ViewModel (Sama seperti saat kamu search manual)
        viewModel.searchBooks(query);
    }
}