package com.kelompok5.openlibrary.ui.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.data.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {

    private BookViewModel viewModel;

    private SearchView searchView;
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
        // INIT VIEW
        // =============================
        searchView = view.findViewById(R.id.searchView);
        rvHorizontalBooks = view.findViewById(R.id.rvBooksHorizontal);
        cardContinue = view.findViewById(R.id.cardContinueReading);

        // =============================
        // SETUP RECYCLER HORIZONTAL
        // =============================
        rvHorizontalBooks.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false)
        );
        horizontalAdapter = new BookAdapterHorizontal(getContext());
        rvHorizontalBooks.setAdapter(horizontalAdapter);

        // =============================
        // INIT VIEWMODEL
        // =============================
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // =============================
        // OBSERVE BUKU DARI ROOM
        // =============================
        viewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                allBooks = books;
                horizontalAdapter.setBooks(books);
            }
        });

        // =============================
        // OBSERVE ERROR
        // =============================
        viewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) {
                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });

        // =============================
        // SEARCH VIEW
        // =============================
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

        // =============================
        // LOAD DEFAULT
        // =============================
        viewModel.searchBooks("programming");

        return view;
    }
}
