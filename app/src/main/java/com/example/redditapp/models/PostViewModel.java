package com.example.redditapp.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends ViewModel {
    private final MutableLiveData<List<Post>> posts = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<List<Post>> getPosts() {
        return posts;
    }
}


