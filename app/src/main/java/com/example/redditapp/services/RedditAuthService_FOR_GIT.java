package com.example.redditapp.services;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RedditAuthService_FOR_GIT {
    private static final String TOKEN_URL = "https://www.reddit.com/api/v1/access_token";
    private static final String CLIENT_ID = "YOUR_CLIENT_ID";
    private static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
    private static final String REDDIT_USERNAME = "YOUR_USERNAME";
    private static final String REDDIT_PASSWORD = "YOUR_PASSWORD";

    private final OkHttpClient client;

    public RedditAuthService_FOR_GIT() {
        this.client = new OkHttpClient();
    }

    public String getAccessToken() throws IOException, JSONException {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "password")
                .add("username", REDDIT_USERNAME)
                .add("password", REDDIT_PASSWORD)
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(body)
                .addHeader("Authorization", "Basic " + Base64.encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes(), Base64.NO_WRAP))
                .build();

        Response response = client.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }
}
