package com.kelompok5.openlibrary.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.kelompok5.openlibrary.api.ApiClient;
import com.kelompok5.openlibrary.api.OpenLibraryService;
import com.kelompok5.openlibrary.data.db.AppDatabase;
import com.kelompok5.openlibrary.data.db.BookDao;
import com.kelompok5.openlibrary.data.db.FavoriteDao;
import com.kelompok5.openlibrary.data.db.HistoryDao;
import com.kelompok5.openlibrary.data.model.Book;
import com.kelompok5.openlibrary.data.model.BookDetail;
import com.kelompok5.openlibrary.data.model.BookResponse;
import com.kelompok5.openlibrary.data.model.EditionResponse;
import com.kelompok5.openlibrary.data.model.FavoriteBook;
import com.kelompok5.openlibrary.data.model.HistoryBook;
import com.kelompok5.openlibrary.data.model.SubjectResponse;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {

    private final OpenLibraryService apiService;
    private final BookDao bookDao;
    private final FavoriteDao favoriteDao;
    private final HistoryDao historyDao;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public BookRepository(Application app) {
        apiService = ApiClient.getClient().create(OpenLibraryService.class);

        AppDatabase db = AppDatabase.getInstance(app);
        bookDao = db.bookDao();
        favoriteDao = db.favoriteDao();
        historyDao = db.historyDao();
    }


    // ================================
    // CALLBACK INTERFACE
    // ================================
    public interface CallbackBase {
        void onError(String msg);
    }

    public interface SimpleCallback extends CallbackBase {
        void onSuccess();
    }

    public interface BookListCallback extends CallbackBase {
        void onSuccess(List<Book> list);
    }

    public interface DetailCallback extends CallbackBase {
        void onSuccess(BookDetail detail);
    }

    public interface ReadCallback extends CallbackBase {
        void onSuccess(String readLink);
    }


    // ================================
    // ROOM DATABASE ACCESS
    // ================================
    public LiveData<List<Book>> getBooks() {
        return bookDao.getAllBooks();
    }

    public LiveData<List<FavoriteBook>> getFavorites() {
        return favoriteDao.getFavorites();
    }

    public LiveData<List<HistoryBook>> getHistory() {
        return historyDao.getHistory();
    }


    // Convert Book → FavoriteBook
    public void insertFavorite(Book book) {
        executor.execute(() -> {
            FavoriteBook f = new FavoriteBook(
                    book.getTitle(),
                    book.getAuthorName(),
                    book.getCoverId() != null ? book.getCoverId() : 0,
                    book.getWorkId()
            );
            favoriteDao.insertFavorite(f);
        });
    }

    // Convert Book → FavoriteBook for deletion
    public void deleteFavorite(Book book) {
        executor.execute(() -> {
            FavoriteBook f = new FavoriteBook(
                    book.getTitle(),
                    book.getAuthorName(),
                    book.getCoverId(),
                    book.getWorkId()
            );
            favoriteDao.deleteFavorite(f);
        });
    }

    // Convert Book → HistoryBook
    public void insertHistory(Book book) {
        executor.execute(() -> {
            HistoryBook h = new HistoryBook(
                    book.getTitle(),
                    book.getAuthorName(),
                    book.getCoverId(),
                    book.getWorkId()
            );
            historyDao.insertHistory(h);
        });
    }


    // ================================
    // 1. SEARCH BOOKS /search.json
    // ================================
    public void searchBooks(String query, SimpleCallback callback) {
        apiService.searchBooks(query).enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body().getDocs();

                    executor.execute(() -> bookDao.insertBooks(books));
                    callback.onSuccess();
                } else {
                    callback.onError("Gagal memuat data pencarian");
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }


    // ================================
    // 2. GET CATEGORY BOOKS /subjects/{category}.json
    // ================================
    public void fetchBooksByCategory(String category, BookListCallback callback) {
        apiService.getBooksByCategory(category).enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getWorks());
                } else {
                    callback.onError("Gagal memuat kategori");
                }
            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }


    // ================================
    // 3. BOOK DETAIL /works/{work_id}.json
    // ================================
    public void fetchBookDetail(String workId, DetailCallback callback) {
        apiService.getBookDetail(workId).enqueue(new Callback<BookDetail>() {
            @Override
            public void onResponse(Call<BookDetail> call, Response<BookDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Gagal memuat detail buku");
                }
            }

            @Override
            public void onFailure(Call<BookDetail> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }


    // ================================
    // 4. READ PAGE /books/{OLID}.json
    // ================================
    public void fetchReadLink(String olid, ReadCallback callback) {
        apiService.getEditionData(olid).enqueue(new Callback<EditionResponse>() {
            @Override
            public void onResponse(Call<EditionResponse> call, Response<EditionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String readUrl = response.body().getReadUrl();
                    callback.onSuccess(readUrl);
                } else {
                    callback.onError("Gagal memuat halaman baca");
                }
            }

            @Override
            public void onFailure(Call<EditionResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
