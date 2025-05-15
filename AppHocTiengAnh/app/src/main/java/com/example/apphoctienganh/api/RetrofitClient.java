package com.example.apphoctienganh.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.80.1:8989/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.80.1:8989/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}