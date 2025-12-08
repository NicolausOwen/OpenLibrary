package com.kelompok5.openlibrary.ui.book;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kelompok5.openlibrary.R;
import com.kelompok5.openlibrary.data.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final Context context;
    private List<Book> books = new ArrayList<>();

    public BookAdapter(Context context) {
        this.context = context;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Book book = books.get(position);

        holder.title.setText(book.getTitle());

        // Author
        if (book.getAuthorName() != null && !book.getAuthorName().isEmpty()) {
            holder.author.setText(book.getAuthorName().get(0));
        } else {
            holder.author.setText("Unknown Author");
        }

        // Cover
        if (book.getCoverId() != null && book.getCoverId() != 0) {
            String img = "https://covers.openlibrary.org/b/id/" + book.getCoverId() + "-M.jpg";

            Glide.with(context)
                    .load(img)
                    .placeholder(R.drawable.ic_upcoming)
                    .into(holder.cover);
        } else {
            holder.cover.setImageResource(R.drawable.ic_upcoming);
        }

        // ============================
        // OPEN BOOK DETAIL
        // ============================
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra(BookDetailActivity.EXTRA_WORK_ID, book.getWorkId());
            intent.putExtra(BookDetailActivity.EXTRA_TITLE, book.getTitle());
            intent.putExtra(BookDetailActivity.EXTRA_COVER_ID, book.getCoverId() != null ? book.getCoverId() : 0);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title, author;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.book_cover);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
        }
    }
}
