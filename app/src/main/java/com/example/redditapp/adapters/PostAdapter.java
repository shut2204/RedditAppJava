package com.example.redditapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.redditapp.R;
import com.example.redditapp.models.Post;

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

        // Проверьте, является ли URL миниатюры "default" или "image"
        if (!"default".equals(post.getThumbnailUrl()) && !"image".equals(post.getThumbnailUrl())) {
            // Загрузите изображение с помощью Glide
            Glide.with(holder.thumbnail.getContext()).load(post.getThumbnailUrl()).into(holder.thumbnail);
            // Установите видимость блока изображения на VISIBLE
            holder.imageBlock.setVisibility(View.VISIBLE);
        } else {
            // Установите видимость блока изображения на GONE
            holder.imageBlock.setVisibility(View.GONE);
        }

        // Другие элементы
    }




    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
