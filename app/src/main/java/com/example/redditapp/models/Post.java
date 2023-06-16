package com.example.redditapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Модель данных для представления поста.
 */
public class Post implements Parcelable {
    private final String id;
    private final String author;
    private final String date;
    private final String thumbnailUrl;
    private final int commentCount;
    private final String fullImageUrl;

    /**
     * Конструктор класса Post.
     *
     * @param id            Идентификатор поста.
     * @param author        Автор поста.
     * @param date          Дата создания поста.
     * @param thumbnailUrl  URL-адрес миниатюры поста.
     * @param commentCount  Количество комментариев к посту.
     * @param fullImageUrl  URL-адрес полного изображения поста.
     */
    public Post(String id, String author, String date, String thumbnailUrl, int commentCount, String fullImageUrl) {
        this.id = id;
        this.author = author;
        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
        this.commentCount = commentCount;
        this.fullImageUrl = fullImageUrl;
    }

    protected Post(Parcel in) {
        id = in.readString();
        author = in.readString();
        date = in.readString();
        thumbnailUrl = in.readString();
        commentCount = in.readInt();
        fullImageUrl = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    /**
     * Возвращает идентификатор поста.
     *
     * @return Идентификатор поста.
     */
    public String getId() {
        return id;
    }

    /**
     * Возвращает автора поста.
     *
     * @return Автор поста.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Возвращает дату создания поста.
     *
     * @return Дата создания поста.
     */
    public String getDate() {
        return date;
    }

    /**
     * Возвращает URL-адрес миниатюры поста.
     *
     * @return URL-адрес миниатюры поста.
     */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    /**
     * Возвращает количество комментариев к посту.
     *
     * @return Количество комментариев к посту.
     */
    public int getCommentCount() {
        return commentCount;
    }

    /**
     * Возвращает URL-адрес полного изображения поста.
     *
     * @return URL-адрес полного изображения поста.
     */
    public String getFullImageUrl() {
        return fullImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(date);
        parcel.writeString(thumbnailUrl);
        parcel.writeInt(commentCount);
        parcel.writeString(fullImageUrl);
    }
}
