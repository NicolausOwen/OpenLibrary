package com.kelompok5.openlibrary.data.model;

import java.util.List;

public class BookResponse {
    private int numFound;
    private List<Book> docs;

    public int getNumFound() { return numFound; }
    public List<Book> getDocs() { return docs; }

    public void setNumFound(int numFound) { this.numFound = numFound; }
    public void setDocs(List<Book> docs) { this.docs = docs; }
}
