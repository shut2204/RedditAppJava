package com.example.redditapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView author;
    public TextView date;
    public ImageView thumbnail;
    public TextView commentCount;
    // Другие элементы

    public PostViewHolder(View itemView) {
        super(itemView);
        author = itemView.findViewById(R.id.author);
        date = itemView.findViewById(R.id.date);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        commentCount = itemView.findViewById(R.id.comment_count);
        // Другие элементы
    }
}

