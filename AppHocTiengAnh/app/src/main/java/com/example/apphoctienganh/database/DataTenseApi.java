package com.example.apphoctienganh.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.ApiResponse;
import com.example.apphoctienganh.model.Question;
import com.example.apphoctienganh.model.QuestionListResponse;
import com.example.apphoctienganh.model.QuestionResponse;

import retrofit2.Callback;

public class DataTenseApi {
    private ApiService apiService;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_TOKEN = "token";

    public DataTenseApi(Context context) {
        apiService = RetrofitClient.getApiService();
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // API không yêu cầu token
    public void addQuestion(Question question, Callback<ApiResponse> callback) {
        apiService.createQuestion(question).enqueue(callback);
    }

    // API yêu cầu token
    public void getQuestion(String id, Callback<QuestionResponse> callback) {
        String token = getToken();
        if (token != null) {
            apiService.getQuestion("Bearer " + token, id).enqueue(callback);
        } else {
            // Xử lý lỗi khi không có token
        }
    }

    // API yêu cầu token
    public void getAllQuestions(Callback<QuestionListResponse> callback) {
        String token = getToken();
        if (token != null) {
            apiService.getQuestionList("Bearer " + token).enqueue(callback);
        } else {
            // Xử lý lỗi khi không có token
        }
    }

    // API không yêu cầu token
    public void updateQuestion(Question question, Callback<ApiResponse> callback) {
        apiService.updateQuestion(question).enqueue(callback);
    }

    // API không yêu cầu token
    public void deleteQuestion(String id, Callback<ApiResponse> callback) {
        apiService.deleteQuestion(id).enqueue(callback);
    }
}