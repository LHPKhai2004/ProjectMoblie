package com.example.apphoctienganh.model;

import java.util.List;

public class UserPointListResponse {
    private boolean result;
    private List<UserPoint> data;
    private String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<UserPoint> getData() {
        return data;
    }

    public void setData(List<UserPoint> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}