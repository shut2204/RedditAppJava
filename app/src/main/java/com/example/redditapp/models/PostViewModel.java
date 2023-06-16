package com.example.redditapp.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel для хранения и управления данными постов.
 */
public class PostViewModel extends ViewModel {
    private final MutableLiveData<List<Post>> posts = new MutableLiveData<>(new ArrayList<>());

    /**
     * Возвращает LiveData со списком постов.
     *
     * @return LiveData со списком постов.
     */
    public MutableLiveData<List<Post>> getPosts() {
        return posts;
    }
}
