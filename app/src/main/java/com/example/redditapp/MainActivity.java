package com.example.redditapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
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

public class MainActivity extends AppCompatActivity {
    private RedditAuthService authService;
    private RedditPostService postService;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authService = new RedditAuthService();
        postService = new RedditPostService();

        // Инициализируйте ваш RecyclerView и PostAdapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        postAdapter = new PostAdapter(new ArrayList<>());
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Получите токен доступа и загрузите посты
        new LoadPostsTask().execute();
    }

    private class LoadPostsTask extends AsyncTask<Void, Void, List<Post>> {
        @Override
        protected List<Post> doInBackground(Void... voids) {
            try {
                String accessToken = authService.getAccessToken();
                return postService.getTopPosts(accessToken);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Post> posts) {
            if (posts != null) {
                postAdapter.setPosts(posts);
                postAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Ошибка при загрузке постов", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
