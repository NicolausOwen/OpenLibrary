package com.kelompok5.openlibrary.ui.book;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.data.model.Book;
import com.kelompok5.openlibrary.data.model.HistoryBook;
import com.kelompok5.openlibrary.ui.search.SearchActivity;
import com.kelompok5.openlibrary.ui.category.CategoryResultActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookFragment extends Fragment {

    private BookViewModel viewModel;
    private RecyclerView rvHorizontalBooks;

    // UI Continue Reading
    private CardView cardContinue;
    private ImageView imgContinueCover;
    private TextView tvContinueTitle, tvContinueDesc;

    private BookAdapterHorizontal horizontalAdapter;
    private List<Book> allBooks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book, container, false);

        // =============================
        // 0. SETUP GREETING USER (BARU)
        // =============================
        TextView tvGreeting = view.findViewById(R.id.tvGreeting);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String name = currentUser.getDisplayName();

            // Jika Display Name kosong, ambil dari Email (misal: emir@gmail.com -> Emir)
            if (name == null || name.isEmpty()) {
                String email = currentUser.getEmail();
                if (email != null) {
                    name = email.split("@")[0]; // Ambil kata sebelum @
                    // Bikin huruf pertama kapital (Opsional)
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                }
            }

            // Set Text ke TextView
            if (name != null) {
                tvGreeting.setText("Hi, " + name);
            }
        }

        // ============================================
        // 1. INIT VIEW COMPONENTS
        // ============================================
        // Continue Reading Components (Sekarang ID-nya sudah ada di XML)
        cardContinue = view.findViewById(R.id.cardContinueReading);
        imgContinueCover = view.findViewById(R.id.imgContinueCover);
        tvContinueTitle = view.findViewById(R.id.tvContinueTitle);
        tvContinueDesc = view.findViewById(R.id.tvContinueDesc);

        // Recycler View Horizontal
        rvHorizontalBooks = view.findViewById(R.id.rvBooksHorizontal);

        // ============================================
        // 2. NAVIGASI SEARCH
        // ============================================
        CardView cvSearchTrigger = view.findViewById(R.id.cvSearchTrigger);
        cvSearchTrigger.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });

        // ============================================
        // 3. NAVIGASI KATEGORI
        // ============================================
        setupCategoryClicks(view);

        // ============================================
        // 4. SETUP RECYCLER VIEW
        // ============================================
        rvHorizontalBooks.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false)
        );
        horizontalAdapter = new BookAdapterHorizontal(getContext());
        rvHorizontalBooks.setAdapter(horizontalAdapter);

        // ============================================
        // 5. VIEWMODEL & OBSERVERS
        // ============================================
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // A. Observe List Buku Horizontal (Recommendation)
        viewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                allBooks = books;
                horizontalAdapter.setBooks(books);
            }
        });

        // B. Observe History untuk Continue Reading
        viewModel.getHistory().observe(getViewLifecycleOwner(), historyList -> {
            if (historyList != null && !historyList.isEmpty()) {

                cardContinue.setVisibility(View.VISIBLE);

                // Sortir history terbaru
                List<HistoryBook> sortedList = new ArrayList<>(historyList);
                Collections.sort(sortedList, (b1, b2) -> Long.compare(b2.getTimestamp(), b1.getTimestamp()));

                HistoryBook latestBook = sortedList.get(0);

                // 1. Set Judul
                tvContinueTitle.setText(latestBook.getTitle());

                // 2. Set Deskripsi (FITUR BARU)
                if (latestBook.getDescription() != null && !latestBook.getDescription().isEmpty()) {
                    tvContinueDesc.setText(latestBook.getDescription());
                } else {
                    // Fallback jika deskripsi kosong, tampilkan author
                    tvContinueDesc.setText("By " + latestBook.getAuthor());
                }

                // 3. Set Cover (FIX GAMBAR TIDAK MUNCUL)
                // Pastikan Glide dipanggil dengan URL yang benar
                if (latestBook.getCoverId() != null && latestBook.getCoverId() > 0) {
                    String coverUrl = "https://covers.openlibrary.org/b/id/" + latestBook.getCoverId() + "-M.jpg";

                    Glide.with(this)
                            .load(coverUrl)
                            .placeholder(R.drawable.ic_upcoming) // Gambar sementara loading
                            .error(R.drawable.ic_upcoming)       // Gambar jika error
                            .into(imgContinueCover);
                } else {
                    imgContinueCover.setImageResource(R.drawable.ic_upcoming);
                }

                // 4. Klik Card
                cardContinue.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), BookDetailActivity.class);
                    intent.putExtra(BookDetailActivity.EXTRA_WORK_ID, latestBook.getId());
                    intent.putExtra(BookDetailActivity.EXTRA_TITLE, latestBook.getTitle());
                    intent.putExtra(BookDetailActivity.EXTRA_COVER_ID, latestBook.getCoverId());
                    startActivity(intent);
                });

            } else {
                cardContinue.setVisibility(View.GONE);
            }
        });

        // C. Observe Error
        viewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) {
                // Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
            }
        });

        // Load data awal untuk list horizontal (misal: programming)
        viewModel.searchBooks("programming");

        return view;
    }

    private void setupCategoryClicks(View view) {
        setCatClick(view, R.id.cat_action, "Actions & Adventure");
        setCatClick(view, R.id.cat_antiques, "Antiques");
        setCatClick(view, R.id.cat_business, "Business & Economics");
        setCatClick(view, R.id.cat_computer, "Computer");

        setCatClick(view, R.id.cat_design, "Design");
        setCatClick(view, R.id.cat_education, "Education");
        setCatClick(view, R.id.cat_fiction, "Fiction");

        setCatClick(view, R.id.cat_see_all, "");
    }

    private void setCatClick(View parent, int id, String categoryName) {
        LinearLayout layout = parent.findViewById(id);
        if (layout != null) {
            layout.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), CategoryResultActivity.class);
                intent.putExtra("EXTRA_CATEGORY_NAME", categoryName);
                startActivity(intent);
            });
        }
    }
}