package com.kelompok5.openlibrary.ui.library;

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
import com.kelompok5.openlibrary.data.model.FavoriteBook;
import com.kelompok5.openlibrary.data.model.HistoryBook;

import java.util.ArrayList;
import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    private final Context context;
    private List<Object> books = new ArrayList<>();

    public LibraryAdapter(Context context) {
        this.context = context;
    }

    public void setBooks(List<Object> list) {
        this.books = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LibraryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_library_book, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryAdapter.ViewHolder holder, int position) {
        Object obj = books.get(position);

        String title = "Unknown Title";
        String author = "Unknown";
        Integer cover = null;

        if (obj instanceof FavoriteBook) {
            FavoriteBook b = (FavoriteBook) obj;
            if (b.getTitle() != null) title = b.getTitle();
            if (b.getAuthor() != null) author = b.getAuthor();    // <-- pakai getAuthor()
            cover = b.getCover();                                 // <-- pakai getCover()
        } else if (obj instanceof HistoryBook) {
            HistoryBook b = (HistoryBook) obj;
            if (b.getTitle() != null) title = b.getTitle();
            if (b.getAuthor() != null) author = b.getAuthor();    // <-- pakai getAuthor()
            cover = b.getCover();                                 // <-- pakai getCover()
        }

        holder.title.setText(title);
        holder.author.setText(author);

        if (cover != null && cover != 0) {
            String img = "https://covers.openlibrary.org/b/id/" + cover + "-M.jpg";
            Glide.with(context)
                    .load(img)
                    .placeholder(R.drawable.ic_upcoming)
                    .error(R.drawable.ic_upcoming)
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
            cover = itemView.findViewById(R.id.imgBookCover);
            title = itemView.findViewById(R.id.tvBookTitle);
            author = itemView.findViewById(R.id.tvAuthorName);
        }
    }
}
