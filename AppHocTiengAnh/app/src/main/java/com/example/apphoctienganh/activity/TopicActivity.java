package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.apphoctienganh.R;
import com.example.apphoctienganh.adapter.TopicAdapter;
import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.Topic;
import com.example.apphoctienganh.model.TopicListResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicActivity extends AppCompatActivity {
    private static final String PREF_NAME = "UserPrefs", KEY_TOKEN = "token";
    private ListView lvTopics;
    private TopicAdapter adapter;
    private List<Topic> topics = new ArrayList<>();
    private ApiService apiService = RetrofitClient.getApiService();
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = prefs.getString(KEY_TOKEN, null);
        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_LONG).show();
            navigateTo(LoginActivity.class);
            return;
        }
        lvTopics = findViewById(R.id.listview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadTopics("Bearer " + token);
        setupListView();
    }

    private void loadTopics(String token) {
        apiService.getTopicList(token).enqueue(new Callback<TopicListResponse>() {
            @Override
            public void onResponse(Call<TopicListResponse> call, Response<TopicListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                    List<Topic> data = response.body().getData();
                    if (data != null && !data.isEmpty()) {
                        topics.clear();
                        topics.addAll(data);
                        adapter = new TopicAdapter(TopicActivity.this, topics);
                        lvTopics.setAdapter(adapter);
                    } else {
                        Toast.makeText(TopicActivity.this, "Không có chủ đề nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TopicActivity.this, "Tải danh sách chủ đề thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TopicListResponse> call, Throwable t) {
                Toast.makeText(TopicActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListView() {
        lvTopics.setOnItemClickListener((parent, view, position, id) -> {
            Topic topic = topics.get(position);
            Intent intent = new Intent(this, VocabularyActivity.class);
            intent.putExtra("topicId", topic.getId());
            intent.putExtra("topicName", topic.getTopic());
            startActivity(intent);
        });
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