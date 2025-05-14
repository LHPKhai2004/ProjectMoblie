package com.example.apphoctienganh.model;

class QuestionResponse {
    private boolean result;
    private Question data;
    private String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Question getData() {
        return data;
    }

    public void setData(Question data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}