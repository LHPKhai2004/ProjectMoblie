package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apphoctienganh.R;
import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.ApiResponse;
import com.example.apphoctienganh.model.UserPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {
    private TextView tvResult;
    private Button btnBack;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "UserPrefs", KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        tvResult = findViewById(R.id.tv_result);
        btnBack = findViewById(R.id.btn_back_to_layout);
        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0);
        int totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0);
        tvResult.setText("Điểm của bạn: " + score + "/" + totalQuestions);
        updateUserPoint(score);
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, LayoutActivity.class));
            finish();
        });
    }

    private void updateUserPoint(int score) {
        String token = prefs.getString(KEY_TOKEN, "");
        if (token.isEmpty()) {
            Toast.makeText(this, "Vui lòng đăng nhập để lưu điểm", Toast.LENGTH_SHORT).show();
            return;
        }
        UserPoint userPoint = new UserPoint();
        userPoint.setPoint(score);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.createUserPoint("Bearer " + token, userPoint).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                    Toast.makeText(ResultActivity.this, "Lưu điểm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResultActivity.this, "Lưu điểm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(ResultActivity.this, "Lỗi khi lưu điểm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LayoutActivity.class));
        finish();
    }
}