package com.example.apphoctienganh.model;

import com.google.gson.annotations.SerializedName;

public class Question {
    @SerializedName("id")
    private String id;

    @SerializedName("question")
    private String question;

    @SerializedName("answer")
    private String answer;

    @SerializedName("allChoice")
    private String allChoice;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAllChoice() {
        return allChoice;
    }

    public void setAllChoice(String allChoice) {
        this.allChoice = allChoice;
    }
}