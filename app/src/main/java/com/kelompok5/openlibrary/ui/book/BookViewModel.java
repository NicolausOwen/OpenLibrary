package com.kelompok5.openlibrary.ui.book;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kelompok5.openlibrary.data.model.Book;
import com.kelompok5.openlibrary.data.model.BookDetail;
import com.kelompok5.openlibrary.data.model.BookResponse;
import com.kelompok5.openlibrary.data.model.EditionResponse;
import com.kelompok5.openlibrary.data.model.FavoriteBook;
import com.kelompok5.openlibrary.data.model.HistoryBook;
import com.kelompok5.openlibrary.data.repository.BookApiRepository;
import com.kelompok5.openlibrary.data.repository.FirestoreRepository;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private final BookApiRepository apiRepo;
    private final FirestoreRepository fireRepo;

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();
    private final MutableLiveData<List<Book>> categoryBooks = new MutableLiveData<>();
    private final MutableLiveData<BookDetail> bookDetail = new MutableLiveData<>();
    private final MutableLiveData<String> readLink = new MutableLiveData<>();

    private final LiveData<List<FavoriteBook>> favorites;
    private final LiveData<List<HistoryBook>> history;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public BookViewModel(@NonNull Application app) {
        super(app);

        apiRepo  = new BookApiRepository();
        fireRepo = new FirestoreRepository();

        favorites = fireRepo.getFavorites();
        history   = fireRepo.getHistory();
    }

    // GETTERS
    public LiveData<List<Book>> getBooks() { return books; }
    public LiveData<List<Book>> getCategoryBooks() { return categoryBooks; }
    public LiveData<BookDetail> getBookDetail() { return bookDetail; }
    public LiveData<String> getReadLink() { return readLink; }
    public LiveData<List<FavoriteBook>> getFavorites() { return favorites; }
    public LiveData<List<HistoryBook>> getHistory() { return history; }
    public LiveData<Boolean> isLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    // SEARCH
    public void searchBooks(String query) {
        loading.setValue(true);

        apiRepo.search(query, new BookApiRepository.Result<BookResponse>() {
            @Override
            public void success(BookResponse data) {

                List<Book> filtered = new ArrayList<>();

                for (Book b : data.getDocs()) {

                    boolean hasPreview = false;

                    // 1. Ada file digital (Internet Archive)
                    if (b.getIa() != null && !b.getIa().isEmpty()) {
                        hasPreview = true;
                    }

                    // 2. Ada ebook
                    if (b.getEbookCount() != null && b.getEbookCount() > 0) {
                        hasPreview = true;
                    }

                    // 3. OpenLibrary availability
                    if (b.getAvailability() != null &&
                            ("open".equals(b.getAvailability().getStatus())
                                    || b.getAvailability().isAvailableToBorrow())) {
                        hasPreview = true;
                    }

                    if (hasPreview) {
                        filtered.add(b);
                    }
                }

                books.postValue(filtered);
                loading.postValue(false);
            }

            @Override
            public void error(String msg) {
                error.postValue(msg);
                loading.postValue(false);
            }
        });
    }


    // CATEGORY
    public void loadCategoryBooks(String category) {
        loading.setValue(true);
        apiRepo.search(category, new BookApiRepository.Result<BookResponse>() {
            @Override
            public void success(BookResponse data) {
                categoryBooks.postValue(data.getDocs());
                loading.postValue(false);
            }

            @Override
            public void error(String msg) {
                error.postValue(msg);
                loading.postValue(false);
            }
        });
    }

    // DETAIL
    public void loadBookDetail(String workId) {
        loading.setValue(true);
        apiRepo.detail(workId, new BookApiRepository.Result<BookDetail>() {
            @Override
            public void success(BookDetail data) {
                bookDetail.postValue(data);
                loading.postValue(false);
            }

            @Override
            public void error(String msg) {
                error.postValue(msg);
                loading.postValue(false);
            }
        });
    }

    // READ
    public void loadReadLink(String olid) {
        loading.setValue(true);
        apiRepo.read(olid, new BookApiRepository.Result<EditionResponse>() {
            @Override
            public void success(EditionResponse data) {
                readLink.postValue(data.getReadUrl());
                loading.postValue(false);
            }

            @Override
            public void error(String msg) {
                error.postValue(msg);
                loading.postValue(false);
            }
        });
    }

    // ADD HISTORY from Book
    public void addToHistory(Book book) {
        String author = "";

        if (book.getAuthorName() != null && !book.getAuthorName().isEmpty()) {
            author = book.getAuthorName().get(0);
        }

        fireRepo.addHistory(new HistoryBook(
                book.getWorkId(),
                book.getTitle(),
                author,
                book.getCoverId(),
                System.currentTimeMillis()
        ));
    }

    // ADD HISTORY from HistoryBook (Detail Page)
    public void addToHistory(HistoryBook hb) {
        fireRepo.addHistory(hb);
    }

    // ADD Favorite from Detail Page
    public void addToFavorites(BookDetail detail, String workId) {

        Integer cover = (detail.getCovers() != null && !detail.getCovers().isEmpty())
                ? detail.getCovers().get(0) : 0;

        fireRepo.addFavorite(new FavoriteBook(
                workId,
                detail.getTitle(),
                detail.getFirstAuthor(),
                cover
        ));
    }

    public void addDetailHistory(BookDetail detail, String workId) {
        Integer cover = (detail.getCovers() != null && !detail.getCovers().isEmpty())
                ? detail.getCovers().get(0) : 0;

        fireRepo.addHistory(new HistoryBook(
                workId,
                detail.getTitle(),
                detail.getFirstAuthor(),
                cover,
                System.currentTimeMillis()
        ));
    }

    // REMOVE FAVORITE
    public void removeFavorite(String workId) {
        fireRepo.removeFavorite(workId);
    }
}
