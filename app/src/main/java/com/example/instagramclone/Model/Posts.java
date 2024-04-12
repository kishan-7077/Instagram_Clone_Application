package com.example.instagramclone.Model;

import androidx.annotation.NonNull;

public class Posts {

    private String description;
    private String imageUrl;
    private String postid;
    private String publisher;

    public Posts() {}
    public Posts(String description, String imageUrl, String postid, String publisher) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.postid = postid;
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostId() {
        return postid;
    }

    public void setPostId(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
