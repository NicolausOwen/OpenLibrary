package com.kelompok5.openlibrary.api;

import com.kelompok5.openlibrary.data.model.BookDetail;
import com.kelompok5.openlibrary.data.model.BookResponse;
import com.kelompok5.openlibrary.data.model.EditionResponse;
import com.kelompok5.openlibrary.data.model.SubjectResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenLibraryService {

    // ======================================
    // 1. SEARCH BOOKS
    // GET /search.json?title=harry+potter
    // ======================================
    @GET("search.json")
    Call<BookResponse> searchBooks(@Query("title") String title);


    // ======================================
    // 2. CATEGORY BOOKS
    // GET /subjects/{category}.json
    // ======================================
    @GET("subjects/{category}.json")
    Call<SubjectResponse> getBooksByCategory(
            @Path("category") String category
    );


    // ======================================
    // 3. BOOK DETAIL
    // GET /works/{workId}.json
    // Example: /works/OL82563W.json
    // ======================================
    @GET("works/{workId}.json")
    Call<BookDetail> getBookDetail(
            @Path("workId") String workId
    );


    // ======================================
    // 4. READ PAGE / Edition
    // GET /books/{OLID}.json
    // Example: /books/OL7353617M.json
    // ======================================
    @GET("books/{olid}.json")
    Call<EditionResponse> getEditionData(
            @Path("olid") String olid
    );
}
