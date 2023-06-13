package com.example.redditapp.model;

public class Post {

    private final String author;
    private final String date; // Может быть типа String, если вы хотите хранить дату в формате "X hours ago"
    private final String thumbnailUrl; // URL изображения-миниатюры
    private final int commentCount;
    private final String fullImageUrl; // URL полноразмерного изображения

    public Post(String author, String date, String thumbnailUrl, int commentCount, String fullImageUrl) {
        this.author = author;
        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
        this.commentCount = commentCount;
        this.fullImageUrl = fullImageUrl;
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
