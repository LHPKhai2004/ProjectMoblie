package com.example.apphoctienganh.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.ApiResponse;
import com.example.apphoctienganh.model.UserPoint;
import com.example.apphoctienganh.model.UserPointListResponse;

import retrofit2.Callback;

public class DataBasePointUser {
    private ApiService apiService;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_TOKEN = "token";

    public DataBasePointUser(Context context) {
        apiService = RetrofitClient.getApiService();
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void addPoints(UserPoint userPoint, Callback<ApiResponse> callback) {
        String token = getToken();
        if (token == null) {
            // Xử lý khi không tìm thấy token
            return;
        }
        apiService.createUserPoint("Bearer " + token, userPoint).enqueue(callback);
    }

    public void getAllUserPoints(Callback<UserPointListResponse> callback) {
        String token = getToken();
        if (token == null) {
            // Xử lý khi không tìm thấy token
            return;
        }
        apiService.getUserPointList("Bearer " + token).enqueue(callback);
    }
}