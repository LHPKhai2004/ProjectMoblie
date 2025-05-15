package com.example.apphoctienganh.model;

import java.util.List;

public class VocabularyListResponse {
    private boolean result;
    private Data data;

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

    public static class Data {
        private List<Vocabulary> content;
        private int totalElements;
        private int totalPages;

        public List<Vocabulary> getContent() {
            return content;
        }

        public void setContent(List<Vocabulary> content) {
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