package com.kelompok5.openlibrary.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.kelompok5.openlibrary.api.ApiClient;
import com.kelompok5.openlibrary.api.OpenLibraryService;
import com.kelompok5.openlibrary.data.db.AppDatabase;
import com.kelompok5.openlibrary.data.db.BookDao;
import com.kelompok5.openlibrary.data.model.Book;
import com.kelompok5.openlibrary.data.model.BookResponse;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private final OpenLibraryService apiService;
    private final BookDao bookDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface ApiCallback {
        void onSuccess();
        void onError(String msg);
    }

    public BookRepository(Application app) {
        apiService = ApiClient.getClient().create(OpenLibraryService.class);
        AppDatabase db = AppDatabase.getInstance(app);
        bookDao = db.bookDao();
    }

    public LiveData<List<Book>> getBooks() {
        return bookDao.getAllBooks();
    }

    public void fetchBooks(String title, ApiCallback callback) {
        apiService.searchBooks(title).enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body().getDocs();
                    executor.execute(() -> bookDao.insertBooks(books));
                    callback.onSuccess();
                } else {
                    callback.onError("Gagal memuat data dari server");
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
