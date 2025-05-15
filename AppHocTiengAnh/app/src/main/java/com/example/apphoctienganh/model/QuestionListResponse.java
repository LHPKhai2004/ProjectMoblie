package com.example.apphoctienganh.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuestionListResponse {
    @SerializedName("result")
    private boolean result;

    @SerializedName("data")
    private Data data;

    @SerializedName("message")
    private String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Data {
        @SerializedName("content")
        private List<Question> content;

        @SerializedName("totalElements")
        private int totalElements;

        @SerializedName("totalPages")
        private int totalPages;

        public List<Question> getContent() {
            return content;
        }

        public void setContent(List<Question> content) {
            this.content = content;
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
    }
}