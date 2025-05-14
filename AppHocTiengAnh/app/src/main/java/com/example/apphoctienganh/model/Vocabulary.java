package com.example.apphoctienganh.model;
public class Vocabulary {
    private String id;
    private String image;
    private String word;
    private String answer;
    private String topicId;

    public Vocabulary(String id, String image, String word, String answer, String topicId) {
        this.id = id;
        this.image = image;
        this.word = word;
        this.answer = answer;
        this.topicId = topicId;
    }

    public Vocabulary(String words, String image, String answer, String topic) {

        this.image = image;
        this.word = words;
        this.answer = answer;
        this.topicId = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}