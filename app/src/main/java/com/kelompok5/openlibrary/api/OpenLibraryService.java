package com.kelompok5.openlibrary.api;

import com.kelompok5.openlibrary.data.model.BookResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenLibraryService {
    @GET("search.json")
    Call<BookResponse> searchBooks(@Query("title") String title);
}