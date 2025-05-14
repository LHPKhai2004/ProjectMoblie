package com.example.apphoctienganh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.apphoctienganh.R;
import com.example.apphoctienganh.adapter.LeaderBoardAdapter;
import com.example.apphoctienganh.database.PointUserApi;
import com.example.apphoctienganh.model.UserPoint;
import com.example.apphoctienganh.model.UserPointListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderBoardActivity extends AppCompatActivity {
    private ListView listView;
    private List<UserPoint> list;
    private LeaderBoardAdapter adapter;
    private PointUserApi pointUserApi;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            navigateToLogin();
            return;
        }

        pointUserApi = new PointUserApi(this);
        mapping();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setLeaderBoard(token);
    }

    public void mapping() {
        list = new ArrayList<>();
        listView = findViewById(R.id.listLeaderBoard);
    }

    public void setLeaderBoard(String token) {
        pointUserApi.getAllUserPoints(new Callback<UserPointListResponse>() {
            @Override
            public void onResponse(Call<UserPointListResponse> call, Response<UserPointListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                    list = response.body().getData();
                    if (list != null && !list.isEmpty()) {
                        adapter = new LeaderBoardAdapter(LeaderBoardActivity.this, list);
                        listView.setAdapter(adapter);
                    } else {
                        Toast.makeText(LeaderBoardActivity.this, "Không có dữ liệu bảng xếp hạng.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LeaderBoardActivity.this, "Không thể tải bảng xếp hạng.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserPointListResponse> call, Throwable t) {
                Toast.makeText(LeaderBoardActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(LeaderBoardActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(LeaderBoardActivity.this, LayoutActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}