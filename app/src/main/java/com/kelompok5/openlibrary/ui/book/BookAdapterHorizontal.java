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

public class BookAdapterHorizontal extends RecyclerView.Adapter<BookAdapterHorizontal.ViewHolder> {

    private final Context context;
    private List<Book> books = new ArrayList<>();

    public BookAdapterHorizontal(Context context) {
        this.context = context;
    }

    public void setBooks(List<Book> list) {
        this.books = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_book_horizontal, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Book b = books.get(position);

        holder.title.setText(b.getTitle());

        if (b.getAuthorName() != null && !b.getAuthorName().isEmpty()) {
            holder.author.setText(b.getAuthorName().get(0));
        } else {
            holder.author.setText("Unknown");
        }

        Integer cover = b.getCoverId();

        if (cover != null && cover != 0) {
            String url = "https://covers.openlibrary.org/b/id/" + cover + "-M.jpg";
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.ic_upcoming)
                    .into(holder.cover);
        } else {
            holder.cover.setImageResource(R.drawable.ic_upcoming);
        }

        // ============================
        // OPEN DETAIL PAGE
        // ============================
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, BookDetailActivity.class);
            i.putExtra(BookDetailActivity.EXTRA_WORK_ID, b.getWorkId());
            i.putExtra(BookDetailActivity.EXTRA_TITLE, b.getTitle());
            i.putExtra(BookDetailActivity.EXTRA_COVER_ID, cover != null ? cover : 0);
            context.startActivity(i);
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
