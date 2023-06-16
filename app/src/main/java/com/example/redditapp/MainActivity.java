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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        authService = new RedditAuthService();
        postService = new RedditPostService();
        executorService = Executors.newSingleThreadExecutor();
        imageUtils = new ImageUtils(this);

        recyclerView = findViewById(R.id.recyclerView); // Initialize recyclerView here
        postAdapter = new PostAdapter(new ArrayList<>());
        recyclerView.setAdapter(postAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

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


        swipeRefreshLayout.setOnRefreshListener(() -> {
            postService.resetAfter();
            refreshPosts();
        });


        postViewModel.getPosts().observe(this, newPosts -> postAdapter.setPosts(newPosts));

        if (postViewModel.getPosts().getValue() == null || postViewModel.getPosts().getValue().isEmpty()) {
            loadPosts();
        }
    }

    private void refreshPosts() {
        executorService.execute(() -> {
            try {
                String accessToken = authService.getAccessToken();
                List<Post> newPosts = postService.getTopPosts(accessToken);
                runOnUiThread(() -> {
                    List<Post> updatedPosts = postViewModel.getPosts().getValue();
                    if (updatedPosts == null) {
                        updatedPosts = new ArrayList<>();
                    }

                    updatedPosts.clear();
                    updatedPosts.addAll(newPosts);
                    postViewModel.getPosts().postValue(updatedPosts);
                    swipeRefreshLayout.setRefreshing(false);
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Ошибка при загрузке постов", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        });
    }
    private void loadPosts() {
        if (isLoading) {
            return;
        }

        isLoading = true;
        executorService.execute(() -> {
            try {
                String accessToken = authService.getAccessToken();
                List<Post> newPosts = postService.getTopPosts(accessToken);
                runOnUiThread(() -> {
                    List<Post> updatedPosts = postViewModel.getPosts().getValue();
                    if (updatedPosts == null) {
                        updatedPosts = new ArrayList<>();
                    }
                    updatedPosts.addAll(newPosts);
                    postViewModel.getPosts().postValue(updatedPosts); // Update the LiveData
                    isLoading = false;
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Ошибка при загрузке постов", Toast.LENGTH_SHORT).show();
                    isLoading = false;
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save RecyclerView state
        outState.putParcelable(RECYCLER_VIEW_POSITION_KEY, layoutManager.onSaveInstanceState());
        Log.d("MainActivity", "onSaveInstanceState: saved position");
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLER_VIEW_POSITION_KEY);
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(savedRecyclerLayoutState);
            Log.d("MainActivity", "onRestoreInstanceState: restored position");
        }
    }
    public void requestPermission(String imageUrl) {
        imageUtils.requestPermission(imageUrl);
    }
}
