package com.kelompok5.openlibrary.ui.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.data.model.Book;

// === IMPORT ACTIVITY TUJUAN SUDAH DIAKTIFKAN ===
import com.kelompok5.openlibrary.ui.search.SearchActivity;
import com.kelompok5.openlibrary.ui.category.CategoryResultActivity; // Pastikan package ini benar

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {

    private BookViewModel viewModel;
    private RecyclerView rvHorizontalBooks;
    private CardView cardContinue;
    private BookAdapterHorizontal horizontalAdapter;
    private List<Book> allBooks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book, container, false);

        // =============================
        // 1. NAVIGASI SEARCH
        // =============================
        CardView cvSearchTrigger = view.findViewById(R.id.cvSearchTrigger);
        cvSearchTrigger.setOnClickListener(v -> {
            // === INTENT SEARCH SUDAH AKTIF ===
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });

        // =============================
        // 2. NAVIGASI KATEGORI
        // =============================
        setupCategoryClicks(view);

        // =============================
        // SETUP RECYCLER HORIZONTAL
        // =============================
        rvHorizontalBooks = view.findViewById(R.id.rvBooksHorizontal);
        rvHorizontalBooks.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false)
        );
        horizontalAdapter = new BookAdapterHorizontal(getContext());
        rvHorizontalBooks.setAdapter(horizontalAdapter);

        // =============================
        // VIEWMODEL & DATA
        // =============================
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        viewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                allBooks = books;
                horizontalAdapter.setBooks(books);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) {
                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });

        // Load data awal
        viewModel.searchBooks("programming");

        return view;
    }

    private void setupCategoryClicks(View view) {
        // Action & Adventure
        LinearLayout catAction = view.findViewById(R.id.cat_action);
        if (catAction != null) {
            catAction.setOnClickListener(v -> openCategoryResult("Actions & Adventure"));
        }

        // Antiques
        LinearLayout catAntiques = view.findViewById(R.id.cat_antiques);
        if (catAntiques != null) {
            catAntiques.setOnClickListener(v -> openCategoryResult("Antiques"));
        }

        // Business
        LinearLayout catBusiness = view.findViewById(R.id.cat_business);
        if (catBusiness != null) {
            catBusiness.setOnClickListener(v -> openCategoryResult("Business & Economics"));
        }

        // Computer
        LinearLayout catComputer = view.findViewById(R.id.cat_computer);
        if (catComputer != null) {
            catComputer.setOnClickListener(v -> openCategoryResult("Computer"));
        }
    }

    private void openCategoryResult(String categoryName) {
        // === INTENT CATEGORY SUDAH AKTIF ===
        Intent intent = new Intent(getActivity(), CategoryResultActivity.class);
        intent.putExtra("EXTRA_CATEGORY_NAME", categoryName);
        startActivity(intent);
    }
}