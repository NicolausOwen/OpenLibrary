package com.kelompok5.openlibrary.ui.category;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.data.model.Book;
import com.kelompok5.openlibrary.ui.book.BookAdapter;
import com.kelompok5.openlibrary.ui.book.BookViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoryResultActivity extends AppCompatActivity {

    private TextView tvCategoryTitle, tvTotalBooks;
    private RecyclerView rvCategoryBooks;
    private ImageView btnBack;

    // Filter Buttons
    private TextView btnFilterAll, btnFilterPopular, btnFilterNew;

    private BookViewModel viewModel;
    private BookAdapter bookAdapter;

    // Simpan list original untuk keperluan sorting
    private List<Book> originalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_result);

        // 1. Ambil Nama Kategori
        String categoryName = getIntent().getStringExtra("EXTRA_CATEGORY_NAME");
        if (categoryName == null) categoryName = "Unknown Category";

        // 2. Init View
        initViews();

        // 3. Set UI Dasar
        tvCategoryTitle.setText(categoryName);
        btnBack.setOnClickListener(v -> finish());

        // 4. Setup RecyclerView
        rvCategoryBooks.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this);
        rvCategoryBooks.setAdapter(bookAdapter);

        // 5. Init ViewModel & Observe
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // Load Data
        loadBooksByCategory(categoryName);

        // Observe
        viewModel.getBooks().observe(this, books -> {
            if (books != null) {
                // Simpan ke originalList agar bisa di-reset saat klik "All Books"
                originalList = new ArrayList<>(books);

                // Tampilkan data
                bookAdapter.setBooks(books);
                tvTotalBooks.setText(books.size() + " Books Found");
            }
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

        // 6. SETUP FILTER CLICK LISTENERS (INI YANG BARU)
        setupFilterListeners();
    }

    private void initViews() {
        tvCategoryTitle = findViewById(R.id.tv_category_title);
        tvTotalBooks = findViewById(R.id.tv_total_books);
        rvCategoryBooks = findViewById(R.id.rv_category_books);
        btnBack = findViewById(R.id.btn_back_category);

        // Init Tombol Filter
        btnFilterAll = findViewById(R.id.btn_filter_all);
        btnFilterPopular = findViewById(R.id.btn_filter_popular);
        btnFilterNew = findViewById(R.id.btn_filter_new);
    }

    private void setupFilterListeners() {
        // Klik All Books
        btnFilterAll.setOnClickListener(v -> {
            updateFilterUI(btnFilterAll);
            // Kembalikan urutan asli
            bookAdapter.setBooks(originalList);
        });

        // Klik Popular (Urutkan berdasarkan Ebook Count / Edition Count)
        btnFilterPopular.setOnClickListener(v -> {
            updateFilterUI(btnFilterPopular);
            sortBooksByPopularity();
        });

        // Klik New (Urutkan berdasarkan Tahun Terbit)
        btnFilterNew.setOnClickListener(v -> {
            updateFilterUI(btnFilterNew);
            sortBooksByNewest();
        });
    }

    // Logic Mengubah Warna Tombol (Pink vs Abu-abu)
    private void updateFilterUI(TextView selectedBtn) {
        // Reset semua ke gaya "Tidak Aktif" (Abu-abu, Outline)
        resetButtonStyle(btnFilterAll);
        resetButtonStyle(btnFilterPopular);
        resetButtonStyle(btnFilterNew);

        // Set tombol yang dipilih ke gaya "Aktif" (Pink, Filled)
        selectedBtn.setBackgroundResource(R.drawable.bg_button_filled);
        selectedBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E91E63")));
        selectedBtn.setTextColor(Color.WHITE);
    }

    private void resetButtonStyle(TextView btn) {
        btn.setBackgroundResource(R.drawable.bottom_border_edittext); // Atau background outline kamu
        btn.setBackgroundTintList(null); // Hapus tint pink
        btn.setTextColor(Color.parseColor("#757575"));
    }

    private void sortBooksByNewest() {
        if (originalList.isEmpty()) return;
        List<Book> sortedList = new ArrayList<>(originalList);

        // Sort descending by First Publish Year
        Collections.sort(sortedList, (b1, b2) -> {
            int year1 = b1.getFirstPublishYear() != null ? b1.getFirstPublishYear() : 0;
            int year2 = b2.getFirstPublishYear() != null ? b2.getFirstPublishYear() : 0;
            return Integer.compare(year2, year1); // Descending (Terbaru di atas)
        });

        bookAdapter.setBooks(sortedList);
    }

    private void sortBooksByPopularity() {
        if (originalList.isEmpty()) return;
        List<Book> sortedList = new ArrayList<>(originalList);

        // Sort descending by Edition Count (Semakin banyak edisi, biasanya semakin populer)
        Collections.sort(sortedList, (b1, b2) -> {
            int count1 = b1.getEditionCount() != null ? b1.getEditionCount() : 0;
            int count2 = b2.getEditionCount() != null ? b2.getEditionCount() : 0;
            return Integer.compare(count2, count1); // Descending
        });

        bookAdapter.setBooks(sortedList);
    }

    private void loadBooksByCategory(String categoryName) {
        tvTotalBooks.setText("Searching for " + categoryName + "...");
        String query = categoryName;
        if (query.contains("&")) query = query.replace("&", "");
        query = query.trim();
        viewModel.searchBooks(query);
    }
}