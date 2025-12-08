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

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    private LibraryViewModel viewModel;
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

        rvFavorites.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        rvHistory.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        favAdapter = new LibraryAdapter(requireContext());
        historyAdapter = new LibraryAdapter(requireContext());

        rvFavorites.setAdapter(favAdapter);
        rvHistory.setAdapter(historyAdapter);

        setupTabListeners();

        viewModel = new ViewModelProvider(this).get(LibraryViewModel.class);

        observeData();

        switchToFavoritesTab();

        return view;
    }

    private void setupTabListeners() {
        tvFavoritesTab.setOnClickListener(v -> switchToFavoritesTab());
        tvHistoryTab.setOnClickListener(v -> switchToHistoryTab());
    }

    private void switchToFavoritesTab() {
        isFavoritesTabActive = true;

        highlightSelected(tvFavoritesTab, indicatorFavorites);
        unhighlight(tvHistoryTab, indicatorHistory);

        rvFavorites.setVisibility(View.VISIBLE);
        rvHistory.setVisibility(View.GONE);

        tvEmptyFavorite.setVisibility(favAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

        updateBooksCount(favAdapter.getItemCount());
    }

    private void switchToHistoryTab() {
        isFavoritesTabActive = false;

        highlightSelected(tvHistoryTab, indicatorHistory);
        unhighlight(tvFavoritesTab, indicatorFavorites);

        rvHistory.setVisibility(View.VISIBLE);
        rvFavorites.setVisibility(View.GONE);

        tvEmptyHistory.setVisibility(historyAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

        updateBooksCount(historyAdapter.getItemCount());
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

        viewModel.getFavorites().observe(getViewLifecycleOwner(), list -> {
            List<Object> temp = new ArrayList<>(list);
            favAdapter.setBooks(temp);

            if (isFavoritesTabActive) {
                tvEmptyFavorite.setVisibility(temp.isEmpty() ? View.VISIBLE : View.GONE);
                updateBooksCount(temp.size());
            }
        });

        viewModel.getHistory().observe(getViewLifecycleOwner(), list -> {
            List<Object> temp = new ArrayList<>(list);
            historyAdapter.setBooks(temp);

            if (!isFavoritesTabActive) {
                tvEmptyHistory.setVisibility(temp.isEmpty() ? View.VISIBLE : View.GONE);
                updateBooksCount(temp.size());
            }
        });
    }

    private void updateBooksCount(int count) {
        tvBooksCount.setText(count + (count == 1 ? " Book" : " Books"));
    }
}
