package com.kelompok5.openlibrary.ui.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    // Set or update the list of books
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

        if (book.getAuthor_name() != null && !book.getAuthor_name().isEmpty()) {
            holder.author.setText(book.getAuthor_name().get(0)); // or join authors
        } else {
            holder.author.setText("Unknown Author");
        }

        // OpenLibrary cover API example:
        // https://covers.openlibrary.org/b/id/{cover_i}-M.jpg
        if (book.getCover_i() != 0) {
            String imageUrl = "https://covers.openlibrary.org/b/id/" + book.getCover_i() + "-M.jpg";
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_nowplaying)
                    .into(holder.cover);
        } else {
            holder.cover.setImageResource(R.drawable.ic_upcoming);
        }

        holder.itemView.setOnClickListener(v -> {
            // Example: show a Toast for now
            Toast.makeText(context, "Selected: " + book.getTitle(), Toast.LENGTH_SHORT).show();

            // Later you can replace this with an Intent:
            // Intent intent = new Intent(context, BookDetailActivity.class);
            // intent.putExtra("book_title", book.getTitle());
            // context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    // ViewHolder class holds references to UI components
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
