package com.example.redditapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.redditapp.adapters.PostAdapter;
import com.example.redditapp.models.Post;
import com.example.redditapp.services.RedditAuthService;
import com.example.redditapp.services.RedditPostService;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private RedditAuthService authService;
    private RedditPostService postService;
    private PostAdapter postAdapter;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authService = new RedditAuthService();
        postService = new RedditPostService();
        executorService = Executors.newSingleThreadExecutor();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        postAdapter = new PostAdapter(new ArrayList<>());
        recyclerView.setAdapter(postAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 4 >= postAdapter.getItemCount()) {
                    loadPosts();
                }
            }
        });

        loadPosts();
    }

    private void loadPosts() {
        executorService.execute(() -> {
            try {
                String accessToken = authService.getAccessToken();
                List<Post> newPosts = postService.getTopPosts(accessToken);
                runOnUiThread(() -> {
                    List<Post> updatedPosts = new ArrayList<>(postAdapter.getPosts());
                    updatedPosts.addAll(newPosts);
                    postAdapter.setPosts(updatedPosts);
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Ошибка при загрузке постов", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
