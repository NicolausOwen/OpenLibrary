package com.kelompok5.openlibrary.ui.book;

import android.content.Context;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_horizontal, parent, false);
        return new ViewHolder(view);
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

        Integer coverId = b.getCoverId(); // <-- Integer (bisa null)

        if (coverId != null && coverId != 0) {
            String img = "https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg";
            Glide.with(context)
                    .load(img)
                    .placeholder(R.drawable.ic_upcoming)   // aman
                    .into(holder.cover);
        } else {
            holder.cover.setImageResource(R.drawable.ic_upcoming);
        }
    }


    @Override
    public int getItemCount() {
        return books.size();
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
