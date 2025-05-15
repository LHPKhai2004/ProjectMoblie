package com.example.apphoctienganh.database;

import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.ApiResponse;
import com.example.apphoctienganh.model.TopicListResponse;
import com.example.apphoctienganh.model.Vocabulary;
import com.example.apphoctienganh.model.VocabularyListResponse;

import retrofit2.Callback;

public class VocabularyApi {
    private ApiService apiService;

    public VocabularyApi() {
        apiService = RetrofitClient.getApiService();
    }

    public void addVocabulary(Vocabulary vocabulary, Callback<ApiResponse> callback) {
        apiService.createVocabulary(vocabulary).enqueue(callback);
    }

    public void getVocabulariesByTopic(String token, String topicId, Callback<VocabularyListResponse> callback) {
        apiService.getVocabularyList("Bearer " + token, topicId).enqueue(callback);
    }

    public void deleteVocabulary(String id, Callback<ApiResponse> callback) {
        apiService.deleteVocabulary(id).enqueue(callback);
    }
}