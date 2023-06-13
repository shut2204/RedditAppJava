package com.example.redditapp.services;

import com.example.redditapp.models.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RedditPostService {
    private static final String TOP_POSTS_URL = "https://oauth.reddit.com/top.json";
    private static final String USER_AGENT = "MyApp/0.1 by jester_2204";

    private final OkHttpClient client;
    private String after = null;

    public RedditPostService() {
        this.client = new OkHttpClient();
    }

    public List<Post> getTopPosts(String accessToken) throws IOException, JSONException {
        Request request = new Request.Builder()
                .url(TOP_POSTS_URL + "?limit=10" + (after != null ? "&after=" + after : ""))
                .get()
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("User-Agent", USER_AGENT)
                .build();

        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();

        JSONObject data = new JSONObject(jsonData).getJSONObject("data");
        after = data.getString("after");

        JSONArray children = data.getJSONArray("children");
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < children.length(); i++) {
            JSONObject postJson = children.getJSONObject(i).getJSONObject("data");
            String author = postJson.getString("author");
            String date = postJson.getString("created_utc"); // Мне нужно будет преобразовать это в формат "X hours ago"
            String thumbnailUrl = postJson.getString("thumbnail");
            int commentCount = postJson.getInt("num_comments");
            String fullImageUrl = postJson.getString("url"); // Это может не быть URL изображения, мне еще нужно будет проверить это
            posts.add(new Post(author, date, thumbnailUrl, commentCount, fullImageUrl));
            System.out.println(postJson);
        }

        return posts;
    }
}
