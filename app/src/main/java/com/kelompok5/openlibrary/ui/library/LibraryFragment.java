package com.kelompok5.openlibrary.ui.library;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok5.openlibrary.R;
// Pastikan ini mengarah ke BookViewModel kamu yang ada datanya
import com.kelompok5.openlibrary.ui.book.BookViewModel;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    // Gunakan BookViewModel karena disitulah logic database kamu berada
    private BookViewModel viewModel;

    private LibraryAdapter favAdapter, historyAdapter;

    private RecyclerView rvFavorites, rvHistory;
    private TextView tvFavoritesTab, tvHistoryTab;
    private TextView tvBooksCount;
    private TextView tvEmptyFavorite, tvEmptyHistory;
    private View indicatorFavorites, indicatorHistory;

    private boolean isFavoritesTabActive = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_library, container, false);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        rvHistory = view.findViewById(R.id.rvHistory);

        tvFavoritesTab = view.findViewById(R.id.tvFavoritesTab);
        tvHistoryTab = view.findViewById(R.id.tvHistoryTab);
        tvBooksCount = view.findViewById(R.id.tvBooksCount);

        tvEmptyFavorite = view.findViewById(R.id.tvEmptyFavorite);
        tvEmptyHistory = view.findViewById(R.id.tvEmptyHistory);

        indicatorFavorites = view.findViewById(R.id.indicatorFavorites);
        indicatorHistory = view.findViewById(R.id.indicatorHistory);

        // Setup Layout Manager
        rvFavorites.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        rvHistory.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        // Setup Adapter
        favAdapter = new LibraryAdapter(requireContext());
        historyAdapter = new LibraryAdapter(requireContext());

        rvFavorites.setAdapter(favAdapter);
        rvHistory.setAdapter(historyAdapter);

        setupTabListeners();

        // PENTING: Pakai BookViewModel agar data history/favorit muncul
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        observeData();

        // Default tab
        switchToFavoritesTab();

        return view;
    }

    private void setupTabListeners() {
        tvFavoritesTab.setOnClickListener(v -> switchToFavoritesTab());
        tvHistoryTab.setOnClickListener(v -> switchToHistoryTab());
    }

    // === LOGIKA PINDAH TAB (DIPERBAIKI) ===
    private void switchToFavoritesTab() {
        isFavoritesTabActive = true;

        highlightSelected(tvFavoritesTab, indicatorFavorites);
        unhighlight(tvHistoryTab, indicatorHistory);

        // UI Dasar: Tampilkan Recycler Favorit, Sembunyikan History
        rvHistory.setVisibility(View.GONE);
        tvEmptyHistory.setVisibility(View.GONE);

        // Cek data untuk menentukan apakah List atau Text Kosong yang muncul
        refreshEmptyState();
    }

    private void switchToHistoryTab() {
        isFavoritesTabActive = false;

        highlightSelected(tvHistoryTab, indicatorHistory);
        unhighlight(tvFavoritesTab, indicatorFavorites);

        // UI Dasar: Tampilkan Recycler History, Sembunyikan Favorit
        rvFavorites.setVisibility(View.GONE);
        tvEmptyFavorite.setVisibility(View.GONE);

        // Cek data
        refreshEmptyState();
    }

    // === LOGIKA CEK DATA KOSONG (DIPERBAIKI) ===
    private void refreshEmptyState() {
        if (isFavoritesTabActive) {
            int count = favAdapter.getItemCount();

            if (count > 0) {
                // Ada Data: Munculkan List, HILANGKAN Text Kosong
                rvFavorites.setVisibility(View.VISIBLE);
                tvEmptyFavorite.setVisibility(View.GONE);
            } else {
                // Kosong: HILANGKAN List, Munculkan Text Kosong
                rvFavorites.setVisibility(View.GONE);
                tvEmptyFavorite.setVisibility(View.VISIBLE);
            }
            updateBooksCount(count);

        } else {
            int count = historyAdapter.getItemCount();

            if (count > 0) {
                rvHistory.setVisibility(View.VISIBLE);
                tvEmptyHistory.setVisibility(View.GONE);
            } else {
                rvHistory.setVisibility(View.GONE);
                tvEmptyHistory.setVisibility(View.VISIBLE);
            }
            updateBooksCount(count);
        }
    }

    private void highlightSelected(TextView tab, View indicator) {
        tab.setTextColor(getResources().getColor(R.color.pink));
        tab.setTypeface(null, Typeface.BOLD);
        indicator.setVisibility(View.VISIBLE);
    }

    private void unhighlight(TextView tab, View indicator) {
        tab.setTextColor(getResources().getColor(R.color.gray));
        tab.setTypeface(null, Typeface.NORMAL);
        indicator.setVisibility(View.INVISIBLE);
    }

    private void observeData() {
        // Observe Favorites
        viewModel.getFavorites().observe(getViewLifecycleOwner(), list -> {
            List<Object> temp = new ArrayList<>(list);
            favAdapter.setBooks(temp); // Masukkan data ke adapter

            // Jika sedang di tab favorit, refresh tampilan kosong/isi
            if (isFavoritesTabActive) {
                refreshEmptyState();
            }
        });

        // Observe History
        viewModel.getHistory().observe(getViewLifecycleOwner(), list -> {
            List<Object> temp = new ArrayList<>(list);
            historyAdapter.setBooks(temp); // Masukkan data ke adapter

            // Jika sedang di tab history, refresh tampilan kosong/isi
            if (!isFavoritesTabActive) {
                refreshEmptyState();
            }
        });
    }

    private void updateBooksCount(int count) {
        tvBooksCount.setText(count + (count == 1 ? " Book" : " Books"));
    }
}