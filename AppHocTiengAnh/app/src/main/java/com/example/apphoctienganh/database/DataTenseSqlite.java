package com.example.apphoctienganh.database;

import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.ApiResponse;
import com.example.apphoctienganh.model.Question;
import com.example.apphoctienganh.model.QuestionListResponse;
import com.example.apphoctienganh.model.QuestionResponse;

import retrofit2.Callback;

public class DataTenseSqlite {
    private ApiService apiService;

    public DataTenseSqlite() {
        apiService = RetrofitClient.getApiService();
    }

    public void addQuestion(Question question, Callback<ApiResponse> callback) {
        apiService.createQuestion(question).enqueue(callback);
    }

    public void getQuestion(String id, Callback<QuestionResponse> callback) {
        apiService.getQuestion(id).enqueue(callback);
    }

    public void getAllQuestions(Callback<QuestionListResponse> callback) {
        apiService.getQuestionList().enqueue(callback);
    }

    public void updateQuestion(Question question, Callback<ApiResponse> callback) {
        apiService.updateQuestion(question).enqueue(callback);
    }

    public void deleteQuestion(String id, Callback<ApiResponse> callback) {
        apiService.deleteQuestion(id).enqueue(callback);
    }
}