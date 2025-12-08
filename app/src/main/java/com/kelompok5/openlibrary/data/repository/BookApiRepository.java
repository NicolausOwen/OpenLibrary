package com.kelompok5.openlibrary.data.repository;

import com.kelompok5.openlibrary.api.ApiClient;
import com.kelompok5.openlibrary.api.OpenLibraryService;
import com.kelompok5.openlibrary.data.model.BookDetail;
import com.kelompok5.openlibrary.data.model.BookResponse;
import com.kelompok5.openlibrary.data.model.EditionResponse;
import com.kelompok5.openlibrary.data.model.SubjectResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookApiRepository {

    private final OpenLibraryService api;

    public interface Error {
        void error(String msg);
    }

    public interface Result<T> extends Error {
        void success(T data);
    }

    public BookApiRepository() {
        api = ApiClient.getClient().create(OpenLibraryService.class);
    }

    public void search(String q, Result<BookResponse> cb) {
        api.searchBooks(q).enqueue(new Callback<>() {
            public void onResponse(Call<BookResponse> call, Response<BookResponse> res) {
                if (res.isSuccessful() && res.body() != null)
                    cb.success(res.body());
                else cb.error("Error search");
            }

            public void onFailure(Call<BookResponse> call, Throwable t) {
                cb.error(t.getMessage());
            }
        });
    }

    public void detail(String workId, Result<BookDetail> cb) {
        api.getBookDetail(workId).enqueue(new Callback<>() {
            public void onResponse(Call<BookDetail> call, Response<BookDetail> res) {
                if (res.isSuccessful() && res.body() != null)
                    cb.success(res.body());
                else cb.error("Error detail");
            }

            public void onFailure(Call<BookDetail> call, Throwable t) {
                cb.error(t.getMessage());
            }
        });
    }

    public void read(String olid, Result<EditionResponse> cb) {
        api.getEditionData(olid).enqueue(new Callback<>() {
            public void onResponse(Call<EditionResponse> call, Response<EditionResponse> res) {
                if (res.isSuccessful() && res.body() != null)
                    cb.success(res.body());
                else cb.error("Error read link");
            }

            public void onFailure(Call<EditionResponse> call, Throwable t) {
                cb.error(t.getMessage());
            }
        });
    }
}
