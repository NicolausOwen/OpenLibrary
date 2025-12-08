package com.kelompok5.openlibrary.ui.book;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.data.model.BookDetail;
import com.kelompok5.openlibrary.data.model.HistoryBook;

public class BookDetailActivity extends AppCompatActivity {

    public static final String EXTRA_WORK_ID = "work_id";
    public static final String EXTRA_TITLE = "book_title";
    public static final String EXTRA_COVER_ID = "cover_id";

    private BookViewModel viewModel;

    // UI
    private ImageView imgCover;
    private TextView tvTitle, tvAuthor, tvDescription;
    private ImageButton btnBack, btnFavorite;
    private TextView btnReadNow;
    private ProgressBar progressBar;

    private String workId = "";
    private String passedTitle = "";
    private Integer passedCoverId = 0;

    private boolean isFavorite = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        initViews();
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        getIntentData();
        setupObservers();

        // Load detail dari API
        viewModel.loadBookDetail(workId);

        // Add to history automatically (FIX: Tambahkan parameter kosong untuk description)
        viewModel.addToHistory(
                new HistoryBook(
                        workId,
                        passedTitle,
                        "",             // Author (kosong dulu)
                        "",             // Description (kosong dulu) -> FIX AGAR TIDAK ERROR
                        passedCoverId,
                        System.currentTimeMillis()
                )
        );

        btnBack.setOnClickListener(v -> finish());

        btnFavorite.setOnClickListener(v -> {
            if (isFavorite) {
                viewModel.removeFavorite(workId);
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                // add from detail
                BookDetail d = viewModel.getBookDetail().getValue();
                if (d != null) {
                    viewModel.addToFavorites(d, workId);
                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews() {
        imgCover = findViewById(R.id.imgBookCover);
        tvTitle = findViewById(R.id.tvBookTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvDescription = findViewById(R.id.tvDescription);
        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnReadNow = findViewById(R.id.btnReadNow);
        progressBar = findViewById(R.id.progressBar);
    }

    private void getIntentData() {
        workId = getIntent().getStringExtra(EXTRA_WORK_ID);
        passedTitle = getIntent().getStringExtra(EXTRA_TITLE);
        passedCoverId = getIntent().getIntExtra(EXTRA_COVER_ID, 0);

        if (passedCoverId != 0) {
            String url = "https://covers.openlibrary.org/b/id/" + passedCoverId + "-L.jpg";
            Glide.with(this).load(url).placeholder(R.drawable.ic_upcoming).into(imgCover);
        }

        tvTitle.setText(passedTitle);
    }

    private void setupObservers() {

        // 1. Observe FAVORITES
        viewModel.getFavorites().observe(this, favs -> {
            isFavorite = favs.stream().anyMatch(f -> f.getId().equals(workId));
            btnFavorite.setImageResource(isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
        });

        // 2. Observe BOOK DETAIL
        viewModel.getBookDetail().observe(this, detail -> {
            if (detail == null) return;

            // Title
            tvTitle.setText(detail.getTitle());

            // Author
            String a = detail.getFirstAuthor();
            tvAuthor.setText(a != null ? a : "Unknown Author");

            // Description
            String desc = detail.getDescriptionText();
            tvDescription.setText(desc != null ? desc : "No description available");

            // Cover (prefer detail cover if available)
            Integer finalCoverId = passedCoverId;
            if (detail.getCovers() != null && !detail.getCovers().isEmpty()) {
                finalCoverId = detail.getCovers().get(0);
                String url = "https://covers.openlibrary.org/b/id/" + finalCoverId + "-L.jpg";
                Glide.with(this).load(url).into(imgCover);
            }

            // Update History dengan Data Lengkap (Deskripsi & Author)
            // Ini penting supaya di Home nanti ada deskripsinya
            viewModel.addToHistory(
                    new HistoryBook(
                            workId,
                            detail.getTitle(),
                            a != null ? a : "",
                            desc != null ? desc : "", // Simpan Deskripsi
                            finalCoverId,
                            System.currentTimeMillis()
                    )
            );

            // ============================================
            // 3. LOGIC TOMBOL READ NOW (DIRECT WEB)
            // ============================================
            btnReadNow.setOnClickListener(v -> {
                // Bersihkan ID jika ada prefix "/works/" agar URL tidak double
                String cleanId = workId.replace("/works/", "");

                // URL Web Open Library
                String webUrl = "https://openlibrary.org/works/" + cleanId;

                // Buka Browser
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "No browser found", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // 4. Loading
        viewModel.isLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Note: Observer getReadLink() dihapus karena kita pakai link manual
    }
}