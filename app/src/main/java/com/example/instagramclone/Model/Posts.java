package com.example.instagramclone.Model;

import androidx.annotation.NonNull;

public class Posts {

    private String description;
    private String imageUrl;
    private String postId;
    private String publisher;


    public Posts() {

    }
    public Posts(String description, String imageUrl, String postId, String publisher) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.postId = postId;
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
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
