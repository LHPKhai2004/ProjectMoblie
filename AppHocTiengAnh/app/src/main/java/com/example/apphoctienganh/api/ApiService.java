package com.example.apphoctienganh.api;

import com.example.apphoctienganh.model.LoginRequest;
import com.example.apphoctienganh.model.LoginResponse;
import com.example.apphoctienganh.model.OtpRequest;
import com.example.apphoctienganh.model.OtpResponse;
import com.example.apphoctienganh.model.RegisterRequest;
import com.example.apphoctienganh.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("v1/api/auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("v1/api/auth/verify-otp")
    Call<OtpResponse> verifyOtp(@Body OtpRequest request);

    @POST("v1/api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}