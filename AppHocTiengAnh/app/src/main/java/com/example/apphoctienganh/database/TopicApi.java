package com.example.apphoctienganh.database;

import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.TopicListResponse;

import retrofit2.Callback;

public class TopicApi {
    private ApiService apiService;

    public TopicApi() {
        apiService = RetrofitClient.getApiService();
    }

    public void getTopicList(String token, Callback<TopicListResponse> callback) {
        apiService.getTopicList(token).enqueue(callback);
    }
}