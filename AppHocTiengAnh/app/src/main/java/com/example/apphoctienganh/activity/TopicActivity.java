package com.example.apphoctienganh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.apphoctienganh.R;
import com.example.apphoctienganh.adapter.TopicAdapter;
import com.example.apphoctienganh.database.TopicApi;
import com.example.apphoctienganh.model.Topic;
import com.example.apphoctienganh.model.TopicListResponse;
import com.example.apphoctienganh.model.TopicModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicActivity extends AppCompatActivity {
    private ListView listView;
    private TopicAdapter adapter;
    private List<TopicModel> list;
    private TopicApi topicApi;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            navigateToLogin();
            return;
        }

        listView = findViewById(R.id.listview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = new ArrayList<>();
        topicApi = new TopicApi();
        setTopics(token);
        setupListView();
    }

    private void setTopics(String token) {
        topicApi.getTopicList(token, new Callback<TopicListResponse>() {
            @Override
            public void onResponse(Call<TopicListResponse> call, Response<TopicListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                    List<Topic> topics = response.body().getData();
                    if (topics != null && !topics.isEmpty()) {
                        for (Topic topic : topics) {
                            int imageResId = getImageResourceForTopic(topic.getTopic());
                            list.add(new TopicModel(imageResId, topic.getTopic()));
                        }
                        adapter = new TopicAdapter(TopicActivity.this, list);
                        listView.setAdapter(adapter);
                    } else {
                        Toast.makeText(TopicActivity.this, "Không có chủ đề nào.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TopicActivity.this, "Không thể tải danh sách chủ đề.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TopicListResponse> call, Throwable t) {
                Toast.makeText(TopicActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getImageResourceForTopic(String topicName) {
        switch (topicName) {
            case "Thể thao":
                return R.drawable.sport;
            case "Giáo dục":
                return R.drawable.education;
            case "Môi trường":
                return R.drawable.enviroment;
            default:
                return R.drawable.default_topic;
        }
    }

    private void setupListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TopicModel selectedTopic = list.get(i);
                Intent intent = new Intent(TopicActivity.this, VocabularyActivity.class);
                intent.putExtra("topic", selectedTopic.getTopic());
                startActivity(intent);
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(TopicActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(TopicActivity.this, LayoutActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}