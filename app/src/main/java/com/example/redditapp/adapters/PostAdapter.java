package com.example.redditapp.adapters;

import static com.example.redditapp.utils.Utils.*;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.redditapp.R;
import com.example.redditapp.models.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private final List<Post> posts;

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
        double timeDouble = Double.parseDouble(post.getDate());
        long timeMillis = (long) timeDouble * 1000;
        holder.date.setText(getTimeAgo(timeMillis));
        holder.commentCount.setText(String.valueOf(post.getCommentCount()));

        if (isValidUrl(post.getThumbnailUrl())) {
            Glide.with(holder.thumbnail.getContext()).load(post.getThumbnailUrl()).into(holder.thumbnail);
            holder.imageBlock.setVisibility(View.VISIBLE);

            holder.thumbnail.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(post.getFullImageUrl()));
                v.getContext().startActivity(intent);
            });
        } else {
            holder.imageBlock.setVisibility(View.GONE);
        }


        holder.itemView.setScaleX(0);
        holder.itemView.setScaleY(0);
        holder.itemView.animate().scaleX(1).scaleY(1).setDuration(500).start();

        holder.thumbnail.setRotation(0);
        holder.thumbnail.animate().rotation(360).setDuration(500).start();

//        holder.itemView.setAlpha(0);
//        holder.itemView.animate().alpha(1).setDuration(500).start();

//        holder.itemView.setRotation(0);
//        holder.itemView.animate().rotation(360).setDuration(500).start();

//        holder.itemView.setTranslationX(-1000);
//        holder.itemView.animate().translationX(0).setDuration(500).start();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<Post> newPosts) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new PostDiffCallback(posts, newPosts));
        this.posts.clear();
        this.posts.addAll(newPosts);
        diffResult.dispatchUpdatesTo(this);
    }

    private static class PostDiffCallback extends DiffUtil.Callback {
        private final List<Post> oldPosts;
        private final List<Post> newPosts;

        public PostDiffCallback(List<Post> oldPosts, List<Post> newPosts) {
            this.oldPosts = oldPosts;
            this.newPosts = newPosts;
        }

        @Override
        public int getOldListSize() {
            return oldPosts.size();
        }

        @Override
        public int getNewListSize() {
            return newPosts.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).getId().equals(newPosts.get(newItemPosition).getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).getCommentCount() == newPosts.get(newItemPosition).getCommentCount();
        }
    }

}
