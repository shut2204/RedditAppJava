package com.example.redditapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.redditapp.adapters.PostAdapter;
import com.example.redditapp.models.Post;
import com.example.redditapp.models.PostViewModel;
import com.example.redditapp.services.RedditAuthService;
import com.example.redditapp.services.RedditPostService;
import com.example.redditapp.utils.ImageUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Главная активность приложения.
 */
public class MainActivity extends AppCompatActivity {
    private static final String RECYCLER_VIEW_POSITION_KEY = "recycler_view_position";
    private PostViewModel postViewModel;
    private RedditAuthService authService;
    private RedditPostService postService;
    private PostAdapter postAdapter;
    private ExecutorService executorService;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ImageUtils imageUtils;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isLoading = false;

    /**
     * Метод вызывается при создании активности.
     *
     * @param savedInstanceState Сохраненное состояние активности.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация ViewModel
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Инициализация сервисов
        authService = new RedditAuthService();
        postService = new RedditPostService();

        // Создание пула потоков
        executorService = Executors.newSingleThreadExecutor();

        // Инициализация ImageUtils
        imageUtils = new ImageUtils(this);

        // Настройка RecyclerView
        setupRecyclerView();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        // Установка слушателя для обработки прокрутки RecyclerView
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 3 >= postAdapter.getItemCount()) {
                    loadPosts();
                }
            }
        });

        // Установка слушателя для обновления постов при свайпе
        swipeRefreshLayout.setOnRefreshListener(() -> {
            postService.resetAfter();
            refreshPosts();
        });

        // Наблюдение за изменениями списка постов и обновление адаптера
        postViewModel.getPosts().observe(this, newPosts -> postAdapter.setPosts(newPosts));

        // Загрузка постов, если список пустой
        if (postViewModel.getPosts().getValue() == null || postViewModel.getPosts().getValue().isEmpty()) {
            loadPosts();
        }
    }

    /**
     * Метод для обновления списка постов.
     */
    private void refreshPosts() {
        executorService.execute(() -> {
            try {
                String accessToken = authService.getAccessToken();
                List<Post> newPosts = postService.getTopPosts(accessToken);

                // Обновление постов на UI-потоке и остановка индикатора обновления
                runOnUiThread(() -> {
                    updatePosts(newPosts);
                    swipeRefreshLayout.setRefreshing(false);
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    // Отображение сообщения об ошибке при загрузке постов и остановка индикатора обновления
                    Toast.makeText(MainActivity.this, "Ошибка при загрузке постов", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        });
    }

    /**
     * Метод для загрузки новых постов.
     */
    private void loadPosts() {
        if (isLoading) {
            return;
        }

        isLoading = true;
        executorService.execute(() -> {
            try {
                String accessToken = authService.getAccessToken();
                List<Post> newPosts = postService.getTopPosts(accessToken);

                // Обновление постов на UI-потоке и сброс флага загрузки
                runOnUiThread(() -> {
                    updatePosts(newPosts);
                    isLoading = false;
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    // Отображение сообщения об ошибке при загрузке постов и сброс флага загрузки
                    Toast.makeText(MainActivity.this, "Ошибка при загрузке постов", Toast.LENGTH_SHORT).show();
                    isLoading = false;
                });
            }
        });
    }

    /**
     * Метод для обновления списка постов и уведомления адаптера об изменениях.
     *
     * @param newPosts Новый список постов.
     */
    private void updatePosts(List<Post> newPosts) {
        List<Post> updatedPosts = postViewModel.getPosts().getValue();
        if (updatedPosts == null) {
            updatedPosts = new ArrayList<>();
            postViewModel.getPosts().postValue(updatedPosts);
        }
        updatedPosts.addAll(newPosts);
        postViewModel.getPosts().postValue(updatedPosts);
    }

    /**
     * Метод для настройки RecyclerView.
     */
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        postAdapter = new PostAdapter(new ArrayList<>());
        recyclerView.setAdapter(postAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
    }

    /**
     * Метод для запроса разрешения и сохранения изображения.
     *
     * @param imageUrl URL изображения.
     */
    public void requestPermission(String imageUrl) {
        imageUtils.requestPermission(imageUrl);
    }

    /**
     * Метод, вызываемый при уничтожении активности.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow();
    }

    /**
     * Метод, вызываемый для сохранения состояния активности при повороте экрана или других событиях.
     *
     * @param outState Объект Bundle для сохранения состояния.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Сохранение состояния RecyclerView
        outState.putParcelable(RECYCLER_VIEW_POSITION_KEY, layoutManager.onSaveInstanceState());
        Log.d("MainActivity", "onSaveInstanceState: saved position");
    }

    /**
     * Метод, вызываемый для восстановления состояния активности после поворота экрана или других событий.
     *
     * @param savedInstanceState Сохраненное состояние активности.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLER_VIEW_POSITION_KEY);
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(savedRecyclerLayoutState);
            Log.d("MainActivity", "onRestoreInstanceState: restored position");
        }
    }
}
