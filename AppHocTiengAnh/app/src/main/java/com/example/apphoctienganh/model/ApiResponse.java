package com.example.apphoctienganh.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("result")
    private boolean result;

    @SerializedName("message")
    private String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}