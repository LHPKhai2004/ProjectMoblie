package com.example.apphoctienganh.model;

import java.util.List;

class VocabularyListResponse {
    private boolean result;
    private List<Vocabulary> data;
    private int totalElements;
    private int totalPages;
    private String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<Vocabulary> getData() {
        return data;
    }

    public void setData(List<Vocabulary> data) {
        this.data = data;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}