package com.kelompok5.openlibrary.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.kelompok5.openlibrary.data.model.FavoriteBook;
import com.kelompok5.openlibrary.data.model.HistoryBook;

import java.util.*;

public class FirestoreRepository {

    private static final String COLLECTION_USERS = "users";
    private static final String SUB_FAVORITES = "favorites";
    private static final String SUB_HISTORY = "history";

    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    // LiveData untuk Favorites & History
    private final MutableLiveData<List<FavoriteBook>> favorites = new MutableLiveData<>();
    private final MutableLiveData<List<HistoryBook>> history = new MutableLiveData<>();

    public FirestoreRepository() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        listenFavorites();
        listenHistory();
    }

    private String getUid() {
        return (auth.getCurrentUser() != null)
                ? auth.getCurrentUser().getUid()
                : "guest";
    }

    private DocumentReference userDoc() {
        return db.collection(COLLECTION_USERS).document(getUid());
    }

    // ============================================================
    // REALTIME LISTENERS
    // ============================================================

    private void listenFavorites() {
        userDoc().collection(SUB_FAVORITES)
                .orderBy("title", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        Log.e("FS", "Fav listen error: " + error.getMessage());
                        return;
                    }

                    List<FavoriteBook> list = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        FavoriteBook fb = doc.toObject(FavoriteBook.class);
                        list.add(fb);
                    }
                    favorites.setValue(list);
                });
    }

    private void listenHistory() {
        userDoc().collection(SUB_HISTORY)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        Log.e("FS", "History listen error: " + error.getMessage());
                        return;
                    }

                    List<HistoryBook> list = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        HistoryBook hb = doc.toObject(HistoryBook.class);
                        list.add(hb);
                    }
                    history.setValue(list);
                });
    }

    // ============================================================
    // PUBLIC GETTERS
    // ============================================================

    public LiveData<List<FavoriteBook>> getFavorites() {
        return favorites;
    }

    public LiveData<List<HistoryBook>> getHistory() {
        return history;
    }

    // ============================================================
    // FAVORITE ACTIONS
    // ============================================================

    public Task<Void> addFavorite(FavoriteBook book) {
        return userDoc()
                .collection(SUB_FAVORITES)
                .document(book.getId())
                .set(book);
    }

    public Task<Void> removeFavorite(String id) {
        return userDoc()
                .collection(SUB_FAVORITES)
                .document(id)
                .delete();
    }

    // ============================================================
    // HISTORY ACTIONS
    // ============================================================

    public Task<Void> addHistory(HistoryBook book) {
        return userDoc()
                .collection(SUB_HISTORY)
                .document(book.getId())
                .set(book);
    }
}
