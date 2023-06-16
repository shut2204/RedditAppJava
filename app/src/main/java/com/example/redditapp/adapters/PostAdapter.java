package com.example.redditapp.adapters;

import static com.example.redditapp.utils.Utils.getTimeAgo;
import static com.example.redditapp.utils.Utils.isValidUrl;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.redditapp.MainActivity;
import com.example.redditapp.R;
import com.example.redditapp.models.Post;

import java.util.List;

/**
 * Адаптер для списка постов.
 */
public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private final List<Post> posts;

    /**
     * Конструктор адаптера.
     *
     * @param posts Список постов.
     */
    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Создание ViewHolder для элемента списка.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        // Привязка данных к ViewHolder.
        Post post = posts.get(position);
        holder.author.setText(post.getAuthor());
        double timeDouble = Double.parseDouble(post.getDate());
        long timeMillis = (long) timeDouble * 1000;
        holder.date.setText(getTimeAgo(timeMillis));
        holder.commentCount.setText(String.valueOf(post.getCommentCount()));
        String imageUrl = post.getFullImageUrl();
        String thumbnailUrl = post.getThumbnailUrl();

        if (isValidUrl(thumbnailUrl)) {
            // Загрузка изображения с использованием библиотеки Glide
            thumbnailUrl = android.text.Html.fromHtml(thumbnailUrl).toString();
            Glide.with(holder.thumbnail.getContext())
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.placeholder) // Заглушка во время загрузки
                    .error(R.drawable.error) // Заглушка в случае ошибки
                    .into(holder.thumbnail);

            holder.imageBlock.setVisibility(View.VISIBLE);

            listenThumbnail(holder, post);
            if ((imageUrl.endsWith(".jpg") || imageUrl.endsWith(".png"))) {
                listenDownloadButton(holder, post);
            } else {
                holder.downloadButton.setVisibility(View.GONE);
            }

        } else {
            holder.imageBlock.setVisibility(View.GONE);
        }
        animateObjects(holder);
    }

    /**
     * Метод для прослушивания нажатия на кнопку скачивания изображения.
     *
     * @param holder ViewHolder элемента списка.
     * @param post   Пост.
     */
    private static void listenDownloadButton(PostViewHolder holder, Post post) {
        holder.downloadButton.setOnClickListener(v -> {
            // Проверка разрешений и сохранение изображения в галерее
            MainActivity activity = (MainActivity) v.getContext();
            activity.requestPermission(post.getFullImageUrl());
        });
    }

    /**
     * Метод для прослушивания нажатия на миниатюру изображения.
     *
     * @param holder ViewHolder элемента списка.
     * @param post   Пост.
     */
    private static void listenThumbnail(PostViewHolder holder, Post post) {
        holder.thumbnail.setOnClickListener(v -> {
            // Открытие полноразмерного изображения во внешнем приложении
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(post.getFullImageUrl()));
            v.getContext().startActivity(intent);
        });
    }

    /**
     * Метод для анимации элементов списка.
     *
     * @param holder ViewHolder элемента списка.
     */
    private static void animateObjects(PostViewHolder holder) {
        holder.itemView.setScaleX(0);
        holder.itemView.setScaleY(0);
        holder.itemView.animate().scaleX(1).scaleY(1).setDuration(500).start();

        holder.thumbnail.setRotation(0);
        holder.thumbnail.animate().rotation(360).setDuration(500).start();
    }

    @Override
    public int getItemCount() {
        // Возвращает количество элементов в списке.
        return posts.size();
    }

    /**
     * Метод для обновления списка постов.
     *
     * @param newPosts Новый список постов.
     */
    public void setPosts(List<Post> newPosts) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new PostDiffCallback(posts, newPosts));
        this.posts.clear();
        this.posts.addAll(newPosts);
        diffResult.dispatchUpdatesTo(this);
    }

    /**
     * Класс для определения разницы между старым и новым списком постов.
     */
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
