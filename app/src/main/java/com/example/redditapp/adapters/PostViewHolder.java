package com.example.redditapp.adapters;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.redditapp.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    TextView author;
    TextView date;
    TextView commentCount;
    ImageView thumbnail;
    ImageButton downloadButton;
    FrameLayout imageBlock;

    public PostViewHolder(View itemView) {
        super(itemView);
        author = itemView.findViewById(R.id.author);
        date = itemView.findViewById(R.id.date);
        commentCount = itemView.findViewById(R.id.comment_count);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        downloadButton = itemView.findViewById(R.id.downloadButton);
        imageBlock = itemView.findViewById(R.id.image_block);
    }
}


