package com.kelompok5.openlibrary.ui.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.data.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {

    private BookViewModel viewModel;
    private BookAdapter adapter;
    private ProgressBar progressBar;
    private SearchView searchView;
    private Button btnViewMore;

    private List<Book> allBooks = new ArrayList<>();
    private int currentLimit = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvBooks);
        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.searchView);
        btnViewMore = view.findViewById(R.id.btnViewMore);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new BookAdapter(getContext());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // Observe data from ViewModel
        viewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                allBooks = books;
                showLimitedBooks();
            }
        });

        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        // SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    viewModel.searchBooks(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // View More button
        btnViewMore.setOnClickListener(v -> {
            currentLimit += 10;
            showLimitedBooks();
        });

        // Default load for dashboard
        viewModel.searchBooks("trending");

        return view;
    }

    private void showLimitedBooks() {
        if (allBooks == null || allBooks.isEmpty()) {
            adapter.setBooks(new ArrayList<>());
            btnViewMore.setVisibility(View.GONE);
            return;
        }

        int end = Math.min(currentLimit, allBooks.size());
        adapter.setBooks(allBooks.subList(0, end));

        // Show button only if more books are available
        if (end < allBooks.size()) {
            btnViewMore.setVisibility(View.VISIBLE);
        } else {
            btnViewMore.setVisibility(View.GONE);
        }
    }
}
