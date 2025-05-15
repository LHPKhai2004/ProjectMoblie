package com.example.apphoctienganh.api;

import com.example.apphoctienganh.model.ApiResponse;
import com.example.apphoctienganh.model.Question;
import com.example.apphoctienganh.model.QuestionListResponse;
import com.example.apphoctienganh.model.QuestionResponse;
import com.example.apphoctienganh.model.Topic;
import com.example.apphoctienganh.model.TopicListResponse;
import com.example.apphoctienganh.model.UserPoint;
import com.example.apphoctienganh.model.UserPointListResponse;
import com.example.apphoctienganh.model.UserResponse;
import com.example.apphoctienganh.model.Vocabulary;
import com.example.apphoctienganh.model.VocabularyListResponse;
import com.example.apphoctienganh.model.LoginRequest;
import com.example.apphoctienganh.model.LoginResponse;
import com.example.apphoctienganh.model.OtpRequest;
import com.example.apphoctienganh.model.OtpResponse;
import com.example.apphoctienganh.model.RegisterRequest;
import com.example.apphoctienganh.model.RegisterResponse;
import com.example.apphoctienganh.model.ForgetPasswordRequest;
import com.example.apphoctienganh.model.ResetPasswordRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("v1/api/auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("v1/api/auth/verify")
    Call<OtpResponse> verifyOtp(@Body OtpRequest request);

    @POST("v1/api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @PUT("v1/api/question/update")
    Call<ApiResponse> updateQuestion(@Body Question question);

    @POST("v1/api/auth/forget-password")
    Call<ApiResponse> forgetPassword(@Body ForgetPasswordRequest request);

    @DELETE("v1/api/question/delete/{id}")
    Call<ApiResponse> deleteQuestion(@Path("id") String id);

    @POST("v1/api/question/create")
    Call<ApiResponse> createQuestion(@Body Question question);

    @DELETE("v1/api/vocabulary/delete/{id}")
    Call<ApiResponse> deleteVocabulary(@Path("id") String id);

    @POST("v1/api/vocabulary/create")
    Call<ApiResponse> createVocabulary(@Body Vocabulary vocabulary);

    @PUT("v1/api/vocabulary/update")
    Call<ApiResponse> updateVocabulary(@Body Vocabulary vocabulary);

    @POST("v1/api/topic/create")
    Call<ApiResponse> createTopic(@Body Topic topic);

    // API yêu cầu token
    @POST("v1/api/user-point/create")
    Call<ApiResponse> createUserPoint(@Header("Authorization") String token, @Body UserPoint userPoint);

    @POST("v1/api/auth/reset-password")
    Call<ApiResponse> resetPassword(@Header("Authorization") String token, @Body ResetPasswordRequest request);

    @GET("v1/api/question/get/{id}")
    Call<QuestionResponse> getQuestion(@Header("Authorization") String token, @Path("id") String id);

    @GET("v1/api/user-point/list")
    Call<UserPointListResponse> getUserPointList(@Header("Authorization") String token);

    @GET("v1/api/user/get")
    Call<UserResponse> getUser(@Header("Authorization") String token);

    @GET("v1/api/question/list")
    Call<QuestionListResponse> getQuestionList(@Header("Authorization") String token);

    @GET("v1/api/vocabulary/list/{topicId}")
    Call<VocabularyListResponse> getVocabularyList(@Header("Authorization") String token, @Path("topicId") String topicId);

    @GET("v1/api/topic/list")
    Call<TopicListResponse> getTopicList(@Header("Authorization") String token);
}