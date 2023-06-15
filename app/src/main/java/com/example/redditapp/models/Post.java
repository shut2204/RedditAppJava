package com.example.redditapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private final String id;
    private final String author;
    private final String date;
    private final String thumbnailUrl;
    private final int commentCount;
    private final String fullImageUrl;

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
