package com.example.redditapp.models;

import androidx.lifecycle.ViewModel;
import java.util.List;

public class PostViewModel extends ViewModel {
    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
