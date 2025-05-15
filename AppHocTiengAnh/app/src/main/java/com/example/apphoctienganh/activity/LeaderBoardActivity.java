package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.apphoctienganh.R;
import com.example.apphoctienganh.adapter.LeaderBoardAdapter;
import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.ApiResponse;
import com.example.apphoctienganh.model.UserPoint;
import com.example.apphoctienganh.model.UserPointListResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderBoardActivity extends AppCompatActivity {
    private static final String PREF_NAME = "UserPrefs", KEY_TOKEN = "token";
    private ListView lvLeaderBoard;
    private List<UserPoint> userPoints = new ArrayList<>();
    private LeaderBoardAdapter adapter;
    private ApiService apiService = RetrofitClient.getApiService();
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = prefs.getString(KEY_TOKEN, null);
        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_LONG).show();
            navigateTo(LoginActivity.class);
            return;
        }
        lvLeaderBoard = findViewById(R.id.listLeaderBoard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadLeaderBoard("Bearer " + token);
    }

    private void loadLeaderBoard(String token) {
        apiService.getUserPointList(token).enqueue(new Callback<UserPointListResponse>() {
            @Override
            public void onResponse(Call<UserPointListResponse> call, Response<UserPointListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                    List<UserPoint> data = response.body().getData();
                    if (data != null && !data.isEmpty()) {
                        userPoints = data;
                        adapter = new LeaderBoardAdapter(LeaderBoardActivity.this, userPoints);
                        lvLeaderBoard.setAdapter(adapter);
                    } else {
                        Toast.makeText(LeaderBoardActivity.this, "Không có dữ liệu bảng xếp hạng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LeaderBoardActivity.this, "Tải bảng xếp hạng thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserPointListResponse> call, Throwable t) {
                Toast.makeText(LeaderBoardActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addPoints(String token, UserPoint userPoint, Callback<ApiResponse> callback) {
        apiService.createUserPoint(token, userPoint).enqueue(callback);
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(this, targetActivity));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigateTo(LayoutActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}