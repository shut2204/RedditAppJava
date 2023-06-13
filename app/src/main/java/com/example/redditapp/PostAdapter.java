package com.example.redditapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.redditapp.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.author.setText(post.getAuthor());
        holder.date.setText(post.getDate());
        holder.commentCount.setText(String.valueOf(post.getCommentCount()));
        // Загрузить изображение с помощью Glide или Picasso
        Glide.with(holder.thumbnail.getContext()).load(post.getThumbnailUrl()).into(holder.thumbnail);
        // Другие элементы
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }
}
