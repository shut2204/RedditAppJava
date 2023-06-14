package com.example.redditapp.models;

public class Post {
    private String id; // Добавьте это поле
    private final String author;
    private final String date; // Может быть типа String, если вы хотите хранить дату в формате "X hours ago"
    private final String thumbnailUrl; // URL изображения-миниатюры
    private final int commentCount;
    private final String fullImageUrl; // URL полноразмерного изображения

    public Post(String id, String author, String date, String thumbnailUrl, int commentCount, String fullImageUrl) {
        this.id = id;
        this.author = author;
        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
        this.commentCount = commentCount;
        this.fullImageUrl = fullImageUrl;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getFullImageUrl() {
        return fullImageUrl;
    }
}
