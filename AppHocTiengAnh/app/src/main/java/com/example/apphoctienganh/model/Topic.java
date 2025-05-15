package com.example.apphoctienganh.model;

import java.io.Serializable;

public class Topic implements Serializable {
    private String id;
    private String imageView;
    private String topic;

    // Constructor for API response
    public Topic(String id, String imageView, String topic) {
        this.id = id;
        this.imageView = imageView;
        this.topic = topic;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}